package savings.tracker;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.util.ArrayList;
import java.util.List;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import savings.tracker.util.Item;
import savings.tracker.util.Store;

public class WegmanApi {
  
  /**
   * gets products by name.
   * 
   * @param name product name
   * @return price as string
   */
  public static List<Item> getItems(String name) {
    HttpResponse<String> response = Unirest
        .get("https://api.wegmans.io/products/search?query=" + name
            + "&api-version=2018-10-18")
        .header("Subscription-Key", "c455d00cb0f64e238a5282d75921f27e")
        .asString();

    String body = response.getBody();
    JsonObject jsonObject = new JsonParser().parse(body).getAsJsonObject();
    JsonArray results = jsonObject.get("results").getAsJsonArray();
    List<Item> list = new ArrayList<Item>();
    for (int i = 0; i < 40; i++) {
      // get item name store and sku
      JsonElement itemJson = results.get(i);
      JsonElement itemName = itemJson.getAsJsonObject().get("name");
      JsonElement itemSku = itemJson.getAsJsonObject().get("sku");
      Item item = new Item();
      item.setName(itemName.toString().replace("\"", ""));
      item.setSku(itemSku.toString().replace("\"", ""));
      item.setStore("Wegmans");

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

      // get item locatin
      /*
       * HttpResponse<String> response3 = Unirest.get(String.format(
       * "https://api.wegmans.io/products/%s/locations?api-ver" +
       * "sion=2018-10-18&subscription-key=c455d00cb0f64e238a5282d75921f27e",
       * item.getSku()).replace("\"", "")).asString(); JsonObject jsonObject3 =
       * new JsonParser().parse(response3.getBody()) .getAsJsonObject(); if
       * (jsonObject3.get("error") != null) { continue; }
       */
      // https://api.wegmans.io/products/484208/prices?api-version=2018-10-18&subscription-key=c455d00cb0f64e238a5282d75921f27e

      list.add(item);
    }

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

}
