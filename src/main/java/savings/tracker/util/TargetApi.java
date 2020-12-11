package savings.tracker.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.UnirestException;
import kong.unirest.json.JSONArray;
import kong.unirest.json.JSONObject;
import savings.tracker.Item;
import savings.tracker.Store;

public class TargetApi {

  private static final int INITALSTORESIZE = 10;
  private static final int SECONDSTORESIZE = 20;

  /**
   * example main.
   * 
   * @param args args
   */
  public static void main(String[] args) {

    int locationId = 1263;
    String validTcin = "54191097";
    double price = 129.99;

    // Item item = TargetApi.getItem(locationId, validTcin);

    // assert (item.getPrice() == price);
  }

  /**
   * returns the zipcode using lat and lon.
   * 
   * @param lat latitude
   * @param lon longitute
   * @return zipcode
   */
  public static int getZip(double lat, double lon) {
    if (lat > 90 || lat < -90 || lon > 180 || lon < -180) {
      return -1;
    }

    HttpResponse<JsonNode> response = Unirest
        .get("https://api.bigdatacloud.net/data/reverse-geocode-client?"
            + "latitude=" + lat + "&longitude=" + lon + "&localityLanguage=en")
        .asJson();

    JsonNode body = response.getBody();

    System.out.println("\nGet zip api response body\n");
    System.out.println(body);

    JSONObject bodyJson = body.getObject();

    // Work around for travis ci
    if (bodyJson.has("status") && bodyJson.getInt("status") == 402) {
      return 94043;
    }

    if (!bodyJson.has("postcode")) {
      return -1;
    } else {
      int zip = bodyJson.getInt("postcode");
      System.out.println(zip);
      return zip;
    }

  }

  /**
   * returns a list of first 10 storeIds according to a zipcode. USEREXCEPTION
   * not currently used.
   * 
   * @param zipcode zipcode
   * @return location id
   */
  public static List<Store> getStoreIdList(int zipcode)
      throws UnirestException {

    List<Store> list = new ArrayList<Store>();
    int count = 0;

    String response = Unirest
        .get("https://target1.p.rapidapi.com/stores/list?zipcode=" + zipcode)
        .header("x-rapidapi-key",
            "DIFFERENT KEY")
        .header("x-rapidapi-host", "target1.p.rapidapi.com").asString()
        .getBody();

    System.out.println("\n starting target getstoreid\n");
    System.out.flush();
    System.out.println("\n getstoreid response " + response + "\n");
    System.out.flush();

    JSONArray firstArray = new JSONArray(response);
    JSONObject firstObject = firstArray.getJSONObject(0);

    // API determines its not a valid zipcode
    if (firstObject.has("errors")) {
      JSONArray errorArray = firstObject.getJSONArray("errors");
      String errorMsg = errorArray.getJSONObject(0).get("error").toString();
      System.out.println("\n" + errorMsg + "\n");

      return null;
    }

    JSONArray secondArray = firstObject.getJSONArray("locations");

    // API determines no stores nearby
    if (secondArray.length() == 0) {
      return null;
    }

    for (int i = 0; i < secondArray.length(); i++) {
      // get store name and type location and

      if (count == INITALSTORESIZE) {
        break;
      }

      Store store = new Store();

      JSONObject location = secondArray.getJSONObject(i);
      store.setName("Target");
      store.setNumber(location.getInt("location_id"));

      JSONObject geographic = location
          .getJSONObject("geographic_specifications");
      store.setLon(geographic.getDouble("longitude"));
      store.setLat(geographic.getDouble("latitude"));

      list.add(store);
      count++;
    }

    // System.out.println("\n" + locationId + "\n");
    return list;
  }

  /**
   * returns a list of second 10 storeIds according to a zipcode. USEREXCEPTION
   * not currently used.
   * 
   * @param zipcode zipcode
   * @return location id
   */
  public static List<Store> getSecStoreIdList(int zipcode)
      throws UnirestException {

    List<Store> list = new ArrayList<Store>();
    int count = 0;

    String response = Unirest
        .get("https://target1.p.rapidapi.com/stores/list?zipcode=" + zipcode)
        .header("x-rapidapi-key",
            "DIFFERENT KEY")
        .header("x-rapidapi-host", "target1.p.rapidapi.com").asString()
        .getBody();

    JSONArray firstArray = new JSONArray(response);
    JSONObject firstObject = firstArray.getJSONObject(0);
    System.out.println("\n" + response + "\n");
    System.out.flush();
    // API determines its not a valid zipcode
    if (firstObject.has("errors")) {
      JSONArray errorArray = firstObject.getJSONArray("errors");
      String errorMsg = errorArray.getJSONObject(0).get("error").toString();
      System.out.println("\n" + errorMsg + "\n");

      return null;
    }

    JSONArray secondArray = firstObject.getJSONArray("locations");

    // API determines no stores nearby
    if (secondArray.length() == 0 || secondArray.length() < 10) {
      return null;
    }

    for (int i = 10; i < secondArray.length(); i++) {
      // get store name and type location and

      if (count == SECONDSTORESIZE) {
        break;
      }

      Store store = new Store();

      JSONObject location = secondArray.getJSONObject(i);
      store.setName("Target");
      store.setNumber(location.getInt("location_id"));

      JSONObject geographic = location
          .getJSONObject("geographic_specifications");
      store.setLon(geographic.getDouble("longitude"));
      store.setLat(geographic.getDouble("latitude"));

      list.add(store);
      count++;
    }

    // System.out.println("\n" + locationId + "\n");
    return list;
  }

  /**
   * gets price of an object.
   * 
   * @param name product name
   * @return price as string
   */
  public static List<List<Item>> getItem(int zip, String name)
      throws UnirestException {

    List<Store> storeList = getStoreIdList(zip);
    if (storeList == null) {
      System.out.println("\n null store list --- Target\n");
      return null;
    }

    List<Item> exactList = new ArrayList<Item>();
    List<Item> altList = new ArrayList<Item>();

    for (int i = 0; i < Math.min(storeList.size(), 3); i++) {
      System.out.println(storeList.get(i).getName());
      HttpResponse<String> response = Unirest
          // https://target-com-store-product-reviews-locations-data.p.rapidapi.com/product/search?store_id=3991&keyword=lamp&sponsored=1&limit=50&offset=0"
          .get(
              "https://target-com-store-product-reviews-locations-data.p.rapidapi.com/product/search?store_id="
                  + String.valueOf(storeList.get(i).getNumber()) + "&keyword="
                  + name + "&sponsored=1&limit=50&offset=0")
          .header("x-rapidapi-key",
              "KEEEEEEEY")
          .header("x-rapidapi-host",
              "target-com-store-product-reviews-locations-data.p.rapidapi.com")
          .asString();

      String body = response.getBody();
      System.out.println(body);
      JsonObject jsonObject = new JsonParser().parse(body).getAsJsonObject();

      // API determines its invalid name
      JsonObject errorObject;
      if ((errorObject = jsonObject.getAsJsonObject("error")) != null) {
        System.out.println("\n Target inside error\n");
        System.out.println("\n code is: " + errorObject.get("code") + "\n");
        System.out.flush();
        break;
      }

      if (jsonObject.get("products") == null) {
        continue;
      }

      JsonArray results = jsonObject.get("products").getAsJsonArray();

      if (results.size() == 0) {
        continue;
      }

      for (int j = 0; j < Math.min(results.size(), 10); j++) {
        // get item name store and sku
        JsonElement itemJson = results.get(j);
        JsonElement itemName = itemJson.getAsJsonObject().get("title");
        JsonElement itemTcin = itemJson.getAsJsonObject().get("tcin");
        Item item1 = new Item();
        item1.setName(itemName.toString().replace("\"", ""));
        item1.setTcin(itemTcin.toString().replace("\"", ""));
        item1.setBarcode(itemTcin.toString().replace("\"", ""));

        // get item image
        JsonArray images = itemJson.getAsJsonObject().get("images")
            .getAsJsonArray();
        JsonElement image = images.get(0);
        JsonElement primary = image.getAsJsonObject().get("primary");
        item1.setImage("https://target.scene7.com/is/image/Target/"
            + primary.toString().replace("\"", ""));

        // get item location

        JsonElement priceArray = itemJson.getAsJsonObject().get("price");
        String price = priceArray.getAsJsonObject()
            .get("formatted_current_price").toString();
        item1.setStore("Target");
        String temp = price.replace("\"", "");
        temp = temp.substring(1);
        Double priceD = Double.valueOf(temp);
        item1.setPrice(Double.valueOf(priceD));
        item1.setLat(storeList.get(i).getLat());
        item1.setLon(storeList.get(i).getLon());

        // check if its exact or close item
        if (item1.getName().toLowerCase().contains(name.toLowerCase())) {
          exactList.add(new Item(item1));
        } else {
          altList.add(new Item(item1));
        }
      }

    }

    List<List<Item>> list = new ArrayList<List<Item>>();
    list.add(exactList);
    list.add(altList);
    return list;
  }

  /**
   * Gets a list of items from the orgList if present in the storeList.
   * 
   * @param storeList list of stores
   * @param orgList   list of org items
   * @return list of items
   * @throws UnirestException        exception
   * @throws InvalUserInputException exception
   */
  public static List<Item> getItemList(List<Store> storeList,
      List<Item> orgList) throws UnirestException {

    List<Item> itemList = new ArrayList<Item>();

    for (int i = 0; i < orgList.size(); i++) {
      System.out.println(orgList.get(i).getName());
      System.out.flush();
    }

    Set<String> itemSet = new HashSet<>();
    for (int i = 0; i < orgList.size() - 1; i++) {

      Item temp = orgList.get(i);
      if (itemSet.contains(temp.getBarcode())) {
        continue;
      }

      itemSet.add(temp.getBarcode());
      for (int j = 0; j < storeList.size(); j++) {
        // System.out.println("In adding a store");
        // System.out.flush();

        Item item = new Item();

        item.setName(temp.getName());
        item.setStore("Target");
        item.setPrice(temp.getPrice());
        item.setTcin(temp.getTcin());
        item.setImage(temp.getImage());
        item.setLat(storeList.get(j).getLat());
        item.setLon(storeList.get(j).getLon());

        itemList.add(item);
      }
    }

    return itemList;

  }

  /**
   * Gets alternatives of first 10 stores. Relies on
   * Controller.filterAlternative to filter items not suitable for specific
   * conditions.
   * 
   * @param zip     zipcode
   * @param orgList original alternative list
   * @return list of items
   * @throws UnirestException        exception
   * @throws InvalUserInputException exception
   */
  public static List<Item> getTargetAlternatives(int zip, List<Item> orgList)
      throws UnirestException {

    List<Store> storeList = getStoreIdList(zip);
    if (storeList == null) {
      System.out.println("\n null store list\n");
      return null;
    }

    for (int i = 0; i < storeList.size(); i++) {
      System.out.println(storeList.get(i).getName());
    }

    List<Item> itemList = getItemList(storeList, orgList);
    if (itemList == null) {
      System.out.println("\n Null item list\n");
      return null;
    }

    if (itemList.size() == 0) {
      System.out.println("\n Empty item list\n");
    }

    for (int i = 0; i < itemList.size(); i++) {
      System.out.println(itemList.get(i).getName());
    }

    return itemList;
  }

  /**
   * Gets alternatives of next 10 stores. Relies on Controller.filterAlternative
   * to filter items not suitable for specific conditions.
   * 
   * @param zip     zipcode
   * @param orgList original alternative list
   * @return list of items
   * @throws UnirestException        exception
   * @throws InvalUserInputException exception
   */
  public static List<Item> getSecTargetAlternatives(int zip, String name)
      throws UnirestException {
    List<Store> storeList = getSecStoreIdList(zip);
    if (storeList == null) {
      System.out.println("\n null store list --- Target\n");
      return null;
    }

    List<Item> list = new ArrayList<Item>();

    for (int i = 0; i < Math.min(storeList.size(), 3); i++) {
      System.out.println(storeList.get(i).getName());
      HttpResponse<String> response = Unirest
          // https://target-com-store-product-reviews-locations-data.p.rapidapi.com/product/search?store_id=3991&keyword=lamp&sponsored=1&limit=50&offset=0"
          .get(
              "https://target-com-store-product-reviews-locations-data.p.rapidapi.com/product/search?store_id="
                  + String.valueOf(storeList.get(i).getNumber()) + "&keyword="
                  + name + "&sponsored=1&limit=50&offset=0")
          .header("x-rapidapi-key",
              "KEEEEEEEY")
          .header("x-rapidapi-host",
              "target-com-store-product-reviews-locations-data.p.rapidapi.com")
          .asString();

      String body = response.getBody();
      System.out.println(body);
      JsonObject jsonObject = new JsonParser().parse(body).getAsJsonObject();

      // API determines its invalid name
      JsonObject errorObject;
      if ((errorObject = jsonObject.getAsJsonObject("error")) != null) {
        System.out.println("\n Target inside error\n");
        System.out.println("\n code is: " + errorObject.get("code") + "\n");
        System.out.flush();
        break;
      }

      if (jsonObject.get("products") == null) {
        continue;
      }

      JsonArray results = jsonObject.get("products").getAsJsonArray();

      if (results.size() == 0) {
        continue;
      }

      for (int j = 0; j < Math.min(results.size(), 10); j++) {
        // get item name store and sku
        JsonElement itemJson = results.get(j);
        JsonElement itemName = itemJson.getAsJsonObject().get("title");
        JsonElement itemTcin = itemJson.getAsJsonObject().get("tcin");
        Item item1 = new Item();
        item1.setName(itemName.toString().replace("\"", ""));
        item1.setTcin(itemTcin.toString().replace("\"", ""));
        item1.setBarcode(itemTcin.toString().replace("\"", ""));

        // get item image
        JsonArray images = itemJson.getAsJsonObject().get("images")
            .getAsJsonArray();
        JsonElement image = images.get(0);
        JsonElement primary = image.getAsJsonObject().get("primary");
        item1.setImage("https://target.scene7.com/is/image/Target/"
            + primary.toString().replace("\"", ""));

        // get item location

        JsonElement priceArray = itemJson.getAsJsonObject().get("price");
        String price = priceArray.getAsJsonObject()
            .get("formatted_current_price").toString();
        item1.setStore("Target");
        String temp = price.replace("\"", "");
        temp = temp.substring(1);
        Double priceD = Double.valueOf(temp);
        item1.setPrice(Double.valueOf(priceD));
        item1.setLat(storeList.get(i).getLat());
        item1.setLon(storeList.get(i).getLon());

        list.add(new Item(item1));
      }

    }

    return list;
  }
}
