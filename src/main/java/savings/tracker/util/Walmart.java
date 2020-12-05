package savings.tracker.util;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import savings.tracker.Item;
import savings.tracker.Store;

public class Walmart {
  /**
   * gets products by name.
   * 
   * @param jdbc      Database
   * @param tableName name of store table
   * @param itemList  list of items
   * @param lat       user location
   * @param lon       user location
   * @param storeName name of store table
   * @return price as string
   * @throws InterruptedException exception
   */
  public static List<Item> getItems(DatabaseJdbc jdbc, String tableName,
      List<Item> itemList, double lat, double lon, String storeName)
      throws InterruptedException {

    if (lat > 90 || lat < -90 || lon > 180 || lon < -180) {
      return new ArrayList<Item>();
    }
    List<Item> list = new ArrayList<Item>();
    List<Store> nearestStores = getNearestStores(jdbc, tableName, lat, lon,
        storeName);
    for (int i = 0; i < itemList.size() - 1; i++) {
      Item item = new Item(itemList.get(i));
      if (item.getBarcode() == itemList.get(i + 1).getBarcode()) {
        // same item, skip
        continue;
      }
      // get nearest 10 stores

      for (int j = 0; j < nearestStores.size(); j++) {
        item.setStore(storeName);
        item.setLat(nearestStores.get(j).getLat());
        item.setLon(nearestStores.get(j).getLon());
        list.add(new Item(item));
      }

    }

    return list;
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
      allList = DatabaseJdbc.getFilterStore(jdbc, tableName, type, lat, lon);
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    // check distance
    PriorityQueue<Store> pq = new PriorityQueue<Store>(10,
        new Comparator<Store>() {
          public int compare(Store s1, Store s2) {
            return Double.compare(
                WegmanApi.getDistance(lat, lon, s1.getLat(), s1.getLon()),
                WegmanApi.getDistance(lat, lon, s2.getLat(), s2.getLon()));
          }
        });
    if (allList.size() <= 10) {
      return allList;
    }
    for (int i = 0; i < allList.size(); i++) {
      pq.add(allList.get(i));
    }
    for (int i = 0; i < 10; i++) {
      shortList.add(pq.poll());
    }

    return shortList;

  }

  /**
   * Sort items by distance.
   * 
   * @param lat  latitude of user
   * @param lon  longitude of user
   * @param list list of items
   * @return list of items
   */
  public static List<Item> sortItemByDistance(double lat,
      double lon, List<Item> list) {

    if (lat > 90 || lat < -90 || lon > 180 || lon < -180) {
      return new ArrayList<Item>();
    }
    List<Item> sortList = new ArrayList<Item>();

    // check distance
    PriorityQueue<Item> pq = new PriorityQueue<Item>(10,
        new Comparator<Item>() {
          public int compare(Item s1, Item s2) {
            return Double.compare(
                WegmanApi.getDistance(lat, lon, s1.getLat(), s1.getLon()),
                WegmanApi.getDistance(lat, lon, s2.getLat(), s2.getLon()));
          }
        });

    for (int i = 0; i < list.size(); i++) {
      pq.add(list.get(i));
    }
    for (int i = 0; i < list.size(); i++) {
      sortList.add(pq.poll());
    }

    return sortList;

  }

}
