package savings.tracker;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.ArrayList;
import java.util.List;
import kong.unirest.UnirestException;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import savings.tracker.util.TargetApi;

@TestMethodOrder(OrderAnnotation.class)
public class TargetApiTest {

  @Test
  @Order(1)
  public void nonEmptyZip() {

    int nonEmptyZip = 05001;

    List<Store> storeList = TargetApi.getStoreIdList(nonEmptyZip);

    assertNotEquals(storeList, null);
  }

  @Test
  @Order(2)
  public void invalZip() throws UnirestException {
    int invalZip = -5;

    List<Store> storeList = TargetApi.getStoreIdList(invalZip);

    assertEquals(storeList, null);
  }

  @Test
  @Order(3)
  public void validZipList() throws UnirestException {

    int validZip = 10025;
    int locationId = 1263;

    List<Store> storeList = TargetApi.getStoreIdList(validZip);

    assertEquals(storeList.get(0).getNumber(), locationId);

  }

  @Test
  @Order(4)
  public void validZipSecList() throws UnirestException {

    int validZip = 10025;

    List<Store> storeList = TargetApi.getSecStoreIdList(validZip);

    assert (storeList.size() > 0);

  }

  @Test
  @Order(5)
  public void invalidTcin() throws UnirestException {
    int locationId = -1263;
    String invalidTcin = "-06";

    Item item = TargetApi.getItem(locationId, invalidTcin);

    assertEquals(item, null);
  }

  @Test
  @Order(6)
  public void validTcin() throws UnirestException {
    int locationId = 1263;
    String validTcin = "54191097";
    double price = 129.99;

    Item item = TargetApi.getItem(locationId, validTcin);

    assertEquals(item.getPrice(), price);
  }

  @Test
  @Order(7)
  public void sleepToAvoidlimit() {
    try {
      Thread.sleep(10000);
    } catch (InterruptedException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  @Test
  @Order(8)
  public void getItemList() throws UnirestException {

    Item item1 = new Item();
    item1.setTcin("54191097");
    Store store1 = new Store();
    store1.setNumber(911);

    Store store2 = new Store();
    store2.setNumber(912);

    Item item3 = new Item();
    item3.setTcin("5417");
    Store store3 = new Store();
    store3.setNumber(911);

    Item item4 = new Item();
    item4.setTcin("54191097");

    List<Item> itemList = new ArrayList<Item>();
    itemList.add(item1);
    itemList.add(item3);
    itemList.add(item4);
    
    List<Store> storeList = new ArrayList<Store>();
    storeList.add(store1);
    storeList.add(store2);
    storeList.add(store3);

    // List<Item> alterItems = TargetApi.getItemList(storeList, itemList);

    // assertNotEquals(alterItems.size(), 0);
  }

  @Test
  @Order(9)
  public void validCoords() throws UnirestException {

    System.out.println("\n starting valid coords test \n");

    double lat = 37.42158889770508;
    double lon = -122.08370208740234;

    int zip = TargetApi.getZip(lat, lon);

    assertEquals(zip, 94043);
  }

  @Test
  @Order(10)
  public void invalidCoords() throws UnirestException {
    double lat = 0;
    double lon = -181;

    int zip = TargetApi.getZip(lat, lon);

    assertEquals(zip, -1);
  }

  @Test
  @Order(11)
  public void invalidZipSecList() throws UnirestException {

    int validZip = -10;

    List<Store> storeList = TargetApi.getSecStoreIdList(validZip);

    assert (storeList == null);

  }

  @Test
  @Order(12)
  public void getEmptyItemList() throws UnirestException {

    List<Item> itemList = new ArrayList<Item>();
    List<Store> storeList = new ArrayList<Store>();

    List<Item> alterItems = TargetApi.getItemList(storeList, itemList);

    assertNotEquals(alterItems, null);
  }

  @Test
  @Order(13)
  public void getInvalidAlternativeList() throws UnirestException {

    int validZip = -10;
    List<Item> itemList = new ArrayList<Item>();

    List<Item> alterItems = TargetApi.getTargetAlternatives(validZip, itemList);

    assertEquals(alterItems, null);
  }

  @Test
  @Order(14)
  public void getValidAlternativeList() throws UnirestException {

    int validZip = 10025;
    Item item1 = new Item();
    item1.setTcin("54191097");
    List<Item> itemList = new ArrayList<Item>();
    itemList.add(item1);

    List<Item> alterItems = TargetApi.getTargetAlternatives(validZip, itemList);

    assertNotEquals(alterItems, null);
  }

}
