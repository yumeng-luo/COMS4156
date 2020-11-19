package savings.tracker;

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
import savings.tracker.util.DatabaseJdbc;
import savings.tracker.util.Item;
import savings.tracker.util.Store;

public class WegmanApi {

  private static final int STORESIZE = 10;
  private static final int SEARCHSIZE = 60;
  private static final int ALTSIZE = 60;

  /**
   * gets products by name.
   * 
   * @param jdbc      Database
   * @param tableName name of store table
   * @param name      product name
   * @param lat       user location
   * @param lon       user location
   * @return price as string
   */
  public static List<List<Item>> getItems(DatabaseJdbc jdbc, String tableName,
      String name, double lat, double lon) {
    HttpResponse<String> response = Unirest
        .get("https://api.wegmans.io/products/search?query=" + name
            + "&api-version=2018-10-18")
        .header("Subscription-Key", "c455d00cb0f64e238a5282d75921f27e")
        .asString();

    String body = response.getBody();
    JsonObject jsonObject = new JsonParser().parse(body).getAsJsonObject();
    JsonArray results = jsonObject.get("results").getAsJsonArray();
    List<Item> exactList = new ArrayList<Item>();
    List<Item> altList = new ArrayList<Item>();
    for (int i = 0; i < SEARCHSIZE; i++) {
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
      store.setNumber(Integer.valueOf(storeNumber.toString()));
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
            if (getDistance(lat, lon, s1.getLat(), s1.getLon()) < getDistance(
                lat, lon, s2.getLat(), s2.getLon())) {
              return -1;
            } else if (getDistance(lat, lon, s1.getLat(), s1
                .getLon()) > getDistance(lat, lon, s2.getLat(), s2.getLon())) {
              return 1;
            }
            return 0;
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
            if (getDistance(lat, lon, s1.getLat(), s1.getLon()) < getDistance(
                lat, lon, s2.getLat(), s2.getLon())) {
              return -1;
            } else if (getDistance(lat, lon, s1.getLat(), s1
                .getLon()) > getDistance(lat, lon, s2.getLat(), s2.getLon())) {
              return 1;
            }
            return 0;
          }
        });
    for (int i = 0; i < allList.size(); i++) {
      pq.add(allList.get(i));
    }
    for (int i = 0; i < STORESIZE; i++) {
      pq.poll();
    }
    for (int i = 0; i < STORESIZE; i++) {
      shortList.add(pq.poll());
    }

    return shortList;

  }

  /**
   * gets additional alternatives by distance.
   * 
   * @param jdbc      Database
   * @param tableName name of store table
   * @param name      product name
   * @param lat       user location
   * @param lon       user location
   * @return price as string
   */
  public static List<Item> getAlternativeItems(DatabaseJdbc jdbc,
      String tableName, String name, double lat, double lon) {
    HttpResponse<String> response = Unirest
        .get("https://api.wegmans.io/products/search?query=" + name
            + "&api-version=2018-10-18")
        .header("Subscription-Key", "c455d00cb0f64e238a5282d75921f27e")
        .asString();

    String body = response.getBody();
    JsonObject jsonObject = new JsonParser().parse(body).getAsJsonObject();
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

      // get nearest 11-20 stores
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
