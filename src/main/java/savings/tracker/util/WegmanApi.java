package savings.tracker.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import savings.tracker.Item;
import savings.tracker.Store;

public class WegmanApi {

  private static final int STORESIZE = 3;
  private static final int SEARCHSIZE = 10;
  private static final int ALTSIZE = 20;
  private static boolean MUSTBECHEAPER = true;
  private static boolean MUSTBECLOSER = true;
  private static boolean MUSTBESAMEITEM = false;

  public static boolean isMustbecloser() {
    return MUSTBECLOSER;
  }

  public static boolean isMustbesameitem() {
    return MUSTBESAMEITEM;
  }

  public static boolean isMustbecheaper() {
    return MUSTBECHEAPER;
  }

  public static void setMustbecloser(boolean mustbecloser) {
    MUSTBECLOSER = mustbecloser;
  }

  public static void setMustbesameitem(boolean mustbesameitem) {
    MUSTBESAMEITEM = mustbesameitem;
  }

  public static void setMustbecheaper(boolean mustbecheaper) {
    MUSTBECHEAPER = mustbecheaper;
  }

  /**
   * gets products by name.
   * 
   * @param jdbc      Database
   * @param tableName name of store table
   * @param name      product name
   * @param lat       user location
   * @param lon       user location
   * @return price as string
   * @throws InterruptedException exception
   */
  public static List<List<Item>> getItems(DatabaseJdbc jdbc, String tableName,
      String name, double lat, double lon) throws InterruptedException {

    if (lat > 90 || lat < -90 || lon > 180 || lon < -180) {
      return new ArrayList<List<Item>>();
    }
    boolean found = false;
    for (char ch : name.toCharArray()) {
      if (Character.isDigit(ch) || Character.isLetter(ch)) {
        found = true;
        break;
      }
    }
    if (found == false) {
      return new ArrayList<List<Item>>();
    }

    System.out.println("\n starting wegmans get items call\n");
    System.out.flush();

    HttpResponse<String> response = Unirest
        .get("https://api.wegmans.io/products/search?query=" + name
            + "&api-version=2018-10-18")
        .header("Subscription-Key", "c455d00cb0f64e238a5282d75921f27e")
        .asString();

    String body = response.getBody();
    System.out.println("\n wegmen get item response" + body + "\n");
    System.out.flush();

    JsonObject jsonObject = new JsonParser().parse(body).getAsJsonObject();
    JsonObject errorObject;
    if ((errorObject = jsonObject.getAsJsonObject("error")) != null) {
      System.out.println("\n wegmans inside error\n");
      System.out.println("\n code is: " + errorObject.get("code") + "\n");
      System.out.flush();
      if (errorObject.get("code").toString().contains("TooManyRequests")) {
        System.out.println(
            "\n wegmans get items call failed, sleeping and redoing\n");
        System.out.flush();
        Thread.sleep(10000);
        return getItems(jdbc, tableName, name, lat, lon);
      }
    }

    if (jsonObject.get("results") == null) {
      return new ArrayList<List<Item>>();
    }
    JsonArray results = jsonObject.get("results").getAsJsonArray();
    
    if (results.size() == 0) {
      return new ArrayList<List<Item>>();
    }
    
    List<Item> exactList = new ArrayList<Item>();
    List<Item> altList = new ArrayList<Item>();
    int count = 0;
    for (int i = 0; i < results.size(); i++) {
      count++;
      // get item name store and sku
      JsonElement itemJson = results.get(i);
      JsonElement itemName = itemJson.getAsJsonObject().get("name");
      JsonElement itemSku = itemJson.getAsJsonObject().get("sku");
      Item item = new Item();
      item.setName(itemName.toString().replace("\"", ""));
      item.setSku(itemSku.toString().replace("\"", ""));

      // get item barcode
      HttpResponse<String> response2 = Unirest.get(String.format(
          "https://api.wegmans.io/products/%s?api-version=2018-10-18&Subscri"
              + "ption-Key=c455d00cb0f64e238a5282d75921f27e",
          item.getSku()).replace("\"", "")).asString();
      JsonObject jsonObject2 = new JsonParser().parse(response2.getBody())
          .getAsJsonObject();
      if (jsonObject2.get("error") != null) {
        continue;
      }
      JsonArray tradeIdentifiers = jsonObject2.getAsJsonObject()
          .get("tradeIdentifiers").getAsJsonArray();
      if (tradeIdentifiers.size() == 0) {
        continue;
      }
      JsonArray barcodes = tradeIdentifiers.get(0).getAsJsonObject()
          .get("barcodes").getAsJsonArray();
      if (barcodes.size() == 0) {
        continue;
      }
      JsonElement barcode = barcodes.get(0).getAsJsonObject().get("barcode");
      item.setBarcode(barcode.toString().replace("\"", ""));

      // get item image
      JsonArray images = tradeIdentifiers.get(0).getAsJsonObject().get("images")
          .getAsJsonArray();
      if (images.size() == 0) {
        continue;
      }
      JsonElement image = images.get(0);
      item.setImage(image.toString().replace("\"", ""));

      // get item locatin

      // get nearest 10 stores
      List<Store> nearestStores = getNearestStores(jdbc, tableName, lat, lon,
          "Wegmans Store");
      for (int j = 0; j < STORESIZE; j++) {
        HttpResponse<String> response4 = Unirest.get(String.format(
            "https://api.wegmans.io/products/%s/prices/%d?api-version=2018-10-18&subscription-key=c455d00cb0f64e238a5282d75921f27e",
            item.getSku(), nearestStores.get(j).getNumber()).replace("\"", ""))
            .asString();

        JsonObject jsonObject4 = new JsonParser().parse(response4.getBody())
            .getAsJsonObject();
        if (jsonObject4.get("error") != null) {
          continue;
        }
        JsonElement price = jsonObject4.getAsJsonObject().get("price");
        item.setStore(nearestStores.get(j).getType());
        item.setPrice(Double.valueOf(price.toString()));
        item.setLat(nearestStores.get(j).getLat());
        item.setLon(nearestStores.get(j).getLon());

        // check if its exact or close item
        if (item.getName().toLowerCase().contains(name.toLowerCase())) {
          exactList.add(new Item(item));
        } else {
          altList.add(new Item(item));
        }

      }
      
      if (count == SEARCHSIZE) {
        break;
      }

    }

    List<List<Item>> list = new ArrayList<List<Item>>();
    list.add(exactList);
    list.add(altList);
    return list;
  }

  /**
   * get all store locations.
   * 
   * @return all stores
   */
  public static List<Store> getStores() {
    HttpResponse<String> response = Unirest
        .get("https://api.wegmans.io/stores?Subsc"
            + "ription-Key=c455d00cb0f64e238a5282d75921f27e&api-version=2018-10-18")
        .asString();

    String body = response.getBody();
    JsonObject jsonObject = new JsonParser().parse(body).getAsJsonObject();
    JsonObject errorObject;
    if ((errorObject = jsonObject.getAsJsonObject("error")) != null) {
      System.out.println("\n wegmans inside error\n");
      System.out.println("\n code is: " + errorObject.get("code") + "\n");
      System.out.flush();
      if (errorObject.get("code").toString().contains("TooManyRequests")) {
        System.out.println(
            "\n wegmans get stores call failed, sleeping and redoing\n");
        System.out.flush();
        try {
          Thread.sleep(10000);
        } catch (InterruptedException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
        return getStores();
      }
    }
    JsonArray results = jsonObject.get("stores").getAsJsonArray();
    List<Store> list = new ArrayList<Store>();
    for (int i = 0; i < results.size(); i++) {
      // get store name and type location and
      JsonElement storeJson = results.get(i);
      JsonElement storeName = storeJson.getAsJsonObject().get("name");
      JsonElement storeNumber = storeJson.getAsJsonObject().get("number");
      JsonElement storeType = storeJson.getAsJsonObject().get("type");
      JsonElement storeLat = storeJson.getAsJsonObject().get("latitude");
      JsonElement storeLon = storeJson.getAsJsonObject().get("longitude");
      Store store = new Store();
      store.setName(storeName.toString().replace("\"", ""));
      store.setNumber(Integer.parseInt(storeNumber.toString()));
      store.setType(storeType.toString().replace("\"", ""));
      store.setLat(storeLat.getAsDouble());
      store.setLon(storeLon.getAsDouble());
      list.add(store);
    }

    return list;
  }

  /**
   * calculate distance between 2 points.
   * 
   * @return distance in km
   */
  public static double getDistance(double lat1, double lon1, double lat2,
      double lon2) {
    if (lat1 > 90 || lat1 < -90 || lon1 > 180 || lon1 < -180) {
      return -1;
    }
    if (lat2 > 90 || lat2 < -90 || lon2 > 180 || lon2 < -180) {
      return -1;
    }

    if ((lat1 == lat2) && (lon1 == lon2)) {
      return 0;
    } else {
      double theta = lon1 - lon2;
      double dist = Math.sin(Math.toRadians(lat1))
          * Math.sin(Math.toRadians(lat2))
          + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
              * Math.cos(Math.toRadians(theta));
      dist = Math.acos(dist);
      dist = Math.toDegrees(dist);
      dist = dist * 60 * 1.1515;
      dist = dist * 1.609344;
      return (dist);
    }
  }

  /**
   * Find 10 nearest store.
   * 
   * @param jdbc      Database
   * @param tableName name of store table
   * @param lat       latitude of user
   * @param lon       longitude of user
   * @param type      type of store looking for
   * @return list of store
   */
  public static List<Store> getNearestStores(DatabaseJdbc jdbc,
      String tableName, double lat, double lon, String type) {

    if (lat > 90 || lat < -90 || lon > 180 || lon < -180) {
      return new ArrayList<Store>();
    }
    boolean found = false;
    for (char ch : type.toCharArray()) {
      if (Character.isDigit(ch) || Character.isLetter(ch)) {
        found = true;
        break;
      }
    }
    if (found == false) {
      return new ArrayList<Store>();
    }

    List<Store> allList = new ArrayList<Store>();
    List<Store> shortList = new ArrayList<Store>();
    // get all stores
    try {
      allList = DatabaseJdbc.getStore(jdbc, tableName, type);
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    // check distance
    PriorityQueue<Store> pq = new PriorityQueue<Store>(STORESIZE,
        new Comparator<Store>() {
          public int compare(Store s1, Store s2) {
            return Double.compare(
                getDistance(lat, lon, s1.getLat(), s1.getLon()),
                getDistance(lat, lon, s2.getLat(), s2.getLon()));
          }
        });
    for (int i = 0; i < allList.size(); i++) {
      pq.add(allList.get(i));
    }
    for (int i = 0; i < STORESIZE; i++) {
      shortList.add(pq.poll());
    }

    return shortList;

  }

  /**
   * Find 11th - 20th nearest store.
   * 
   * @param jdbc      Database
   * @param tableName name of store table
   * @param lat       latitude of user
   * @param lon       longitude of user
   * @param type      type of store looking for
   * @return list of store
   */
  public static List<Store> getSecNearestStores(DatabaseJdbc jdbc,
      String tableName, double lat, double lon, String type) {

    if (lat > 90 || lat < -90 || lon > 180 || lon < -180) {
      return new ArrayList<Store>();
    }
    boolean found = false;
    for (char ch : type.toCharArray()) {
      if (Character.isDigit(ch) || Character.isLetter(ch)) {
        found = true;
        break;
      }
    }
    if (found == false) {
      return new ArrayList<Store>();
    }

    List<Store> allList = new ArrayList<Store>();
    List<Store> shortList = new ArrayList<Store>();
    // get all stores
    try {
      allList = DatabaseJdbc.getStore(jdbc, tableName, type);
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    // check distance
    PriorityQueue<Store> pq = new PriorityQueue<Store>(2 * STORESIZE,
        new Comparator<Store>() {
          public int compare(Store s1, Store s2) {
            return Double.compare(
                getDistance(lat, lon, s1.getLat(), s1.getLon()),
                getDistance(lat, lon, s2.getLat(), s2.getLon()));
          }
        });
    for (int i = 0; i < allList.size(); i++) {
      pq.add(allList.get(i));
    }

    for (int i = 0; i < 2 * STORESIZE; i++) {
      shortList.add(pq.poll());
    }

    return shortList;

  }

  /**
   * gets additional alternatives by distance. and filter based on toggle
   * switches
   * 
   * @param jdbc         Database
   * @param tableName    name of store table
   * @param name         product name
   * @param lat          user location
   * @param lon          user location
   * @param initialPrice initial item price
   * @return price as string
   */
  public static List<Item> getAlternativeItems(DatabaseJdbc jdbc,
      String tableName, String name, double lat, double lon,
      double initialPrice) {

    if (lat > 90 || lat < -90 || lon > 180 || lon < -180 || initialPrice < 0) {
      return new ArrayList<Item>();
    }
    boolean found = false;
    for (char ch : name.toCharArray()) {
      if (Character.isDigit(ch) || Character.isLetter(ch)) {
        found = true;
        break;
      }
    }
    if (found == false) {
      return new ArrayList<Item>();
    }

    HttpResponse<String> response = Unirest
        .get("https://api.wegmans.io/products/search?query=" + name
            + "&api-version=2018-10-18")
        .header("Subscription-Key", "c455d00cb0f64e238a5282d75921f27e")
        .asString();

    String body = response.getBody();
    JsonObject jsonObject = new JsonParser().parse(body).getAsJsonObject();
    JsonObject errorObject;
    if ((errorObject = jsonObject.getAsJsonObject("error")) != null) {
      System.out.println("\n wegmans inside error\n");
      System.out.println("\n code is: " + errorObject.get("code") + "\n");
      System.out.flush();
      if (errorObject.get("code").toString().contains("TooManyRequests")) {
        System.out.println(
            "\n wegmans get alternative items call failed, sleeping and redoing\n");
        System.out.flush();
        try {
          Thread.sleep(10000);
        } catch (InterruptedException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
        return getAlternativeItems(jdbc, tableName, name, lat, lon,
            initialPrice);
      }
    }
    if (jsonObject.get("results") == null) {
      return new ArrayList<Item>();
    }
    JsonArray results = jsonObject.get("results").getAsJsonArray();
    List<Item> list = new ArrayList<Item>();
    for (int i = 0; i < ALTSIZE; i++) {
      // get item name store and sku
      JsonElement itemJson = results.get(i);
      JsonElement itemName = itemJson.getAsJsonObject().get("name");
      JsonElement itemSku = itemJson.getAsJsonObject().get("sku");
      Item item = new Item();
      item.setName(itemName.toString().replace("\"", ""));
      item.setSku(itemSku.toString().replace("\"", ""));

      // get item barcode
      HttpResponse<String> response2 = Unirest.get(String.format(
          "https://api.wegmans.io/products/%s?api-version=2018-10-18&Subscri"
              + "ption-Key=c455d00cb0f64e238a5282d75921f27e",
          item.getSku()).replace("\"", "")).asString();
      JsonObject jsonObject2 = new JsonParser().parse(response2.getBody())
          .getAsJsonObject();
      if (jsonObject2.get("error") != null) {
        continue;
      }
      JsonArray tradeIdentifiers = jsonObject2.getAsJsonObject()
          .get("tradeIdentifiers").getAsJsonArray();
      if (tradeIdentifiers.size() == 0) {
        continue;
      }
      JsonArray barcodes = tradeIdentifiers.get(0).getAsJsonObject()
          .get("barcodes").getAsJsonArray();
      if (barcodes.size() == 0) {
        continue;
      }
      JsonElement barcode = barcodes.get(0).getAsJsonObject().get("barcode");
      item.setBarcode(barcode.toString().replace("\"", ""));

      // get item image
      JsonArray images = tradeIdentifiers.get(0).getAsJsonObject().get("images")
          .getAsJsonArray();
      if (images.size() == 0) {
        continue;
      }
      JsonElement image = images.get(0);
      item.setImage(image.toString().replace("\"", ""));

      // get item locatin

      // get nearest 1-20 stores
      List<Store> secNearestStores = getSecNearestStores(jdbc, tableName, lat,
          lon, "Wegmans Store");
      for (int j = 0; j < STORESIZE; j++) {
        HttpResponse<String> response4 = Unirest.get(String.format(
            "https://api.wegmans.io/products/%s/prices/%d?api-version=2018-10-18&subscription-key=c455d00cb0f64e238a5282d75921f27e",
            item.getSku(), secNearestStores.get(j).getNumber())
            .replace("\"", "")).asString();

        JsonObject jsonObject4 = new JsonParser().parse(response4.getBody())
            .getAsJsonObject();
        if (jsonObject4.get("error") != null) {
          continue;
        }
        JsonElement price = jsonObject4.getAsJsonObject().get("price");
        if (isMustbecheaper()) {
          if (Double.valueOf(price.toString()) > initialPrice) {
            break;
          }
        }
        item.setStore(secNearestStores.get(j).getType());
        item.setPrice(Double.valueOf(price.toString()));
        item.setLat(secNearestStores.get(j).getLat());
        item.setLon(secNearestStores.get(j).getLon());

        list.add(new Item(item));

      }

    }

    return list;
  }

}
