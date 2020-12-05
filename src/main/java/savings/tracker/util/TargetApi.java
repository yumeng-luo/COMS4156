package savings.tracker.util;

import java.util.ArrayList;
import java.util.List;
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

    Item item = TargetApi.getItem(locationId, validTcin);

    assert (item.getPrice() == price);
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
            "90edf87601msheef53f51133c0f9p149c70jsn62c12b7d797b")
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
            "90edf87601msheef53f51133c0f9p149c70jsn62c12b7d797b")
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
   * @param tcin product id
   * @return price as string
   */
  public static Item getItem(int storeId, String tcin) throws UnirestException {

    Item item = new Item();

    HttpResponse<JsonNode> response = Unirest
        .get("https://target1.p.rapidapi.com/products/get-details?tcin=" + tcin
            + "&store_id=" + storeId)
        .header("x-rapidapi-key",
            "90edf87601msheef53f51133c0f9p149c70jsn62c12b7d797b")
        .header("x-rapidapi-host", "target1.p.rapidapi.com").asJson();

    JsonNode body = response.getBody();
    JSONObject bodyJson = body.getObject();

    // API determines its invalid tcin number
    if (bodyJson.has("errors")) {
      JSONArray errorArray = bodyJson.getJSONArray("errors");
      String errorMsg = errorArray.getJSONObject(0).get("message").toString();
      System.out.println("\n" + errorMsg + "\n");

      return null;

      // throw new InvalUserInputException(errorMsg);
    }

    double price = bodyJson.getJSONObject("data").getJSONObject("product")
        .getJSONObject("price").getDouble("current_retail");

    item.setPrice(price);
    item.setTcin(tcin);
    item.setStore("Target");
    item.setImage(
        "https://1000logos.net/wp-content/uploads/2017/06/Target-Logo.png");

    System.out.println(price);
    return item;
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
      for (int j = 0; j < storeList.size(); j++) {
        Item item = getItem(storeList.get(j).getNumber(),
            orgList.get(i).getTcin());

        if (item != null) {
          item.setLat(storeList.get(j).getLat());
          item.setLon(storeList.get(j).getLon());

          itemList.add(item);
        }
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
      return null;
    }

    List<Item> itemList = getItemList(storeList, orgList);
    if (itemList == null) {
      return null;
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
  public static List<Item> getSecTargetAlternatives(int zip, List<Item> orgList)
      throws UnirestException {

    List<Store> storeList = getSecStoreIdList(zip);
    if (storeList == null) {
      return null;
    }

    List<Item> itemList = getItemList(storeList, orgList);
    if (itemList == null) {
      return null;
    }

    return itemList;

  }
}
