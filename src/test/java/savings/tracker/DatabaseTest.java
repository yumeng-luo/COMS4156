package savings.tracker;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import savings.tracker.util.DatabaseJdbc;
import savings.tracker.util.Item;
import savings.tracker.util.OngoingTask;
import savings.tracker.util.PurchaseRecord;
import savings.tracker.util.Store;
import savings.tracker.util.User;

public class DatabaseTest {

  DatabaseJdbc jdbc = new DatabaseJdbc();

  /*
   * Test createConnection()
   * 
   */
  @Test
  @Order(1)
  public void testCreateConnection() {
    System.out.println("========TESTING CREATE CONNECTION ========");
    Connection c = jdbc.createConnection();
    assertNotEquals(c, null);
    try {
      c.close();
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
      assertEquals(1, 0);
    }
  }

  /*
   * Test createTable
   * 
   */
  @Test
  @Order(2)
  public void testCreateTable() throws SQLException {
    System.out.println("========TESTING CREATE TABLE ========");
    DatabaseJdbc.createLoginTable(jdbc, "UserTest");
    DatabaseJdbc.createItemTable(jdbc, "ItemTest");
    DatabaseJdbc.createSearchTable(jdbc, "SearchTest", "ItemTest");
    DatabaseJdbc.createTaskTable(jdbc, "TaskTest", "UserTest", "SearchTest",
        "ItemTest");
    DatabaseJdbc.createStoreTable(jdbc, "StoreTest");
    DatabaseJdbc.createPurchaseTable(jdbc, "RecordTest");

  }

  /*
   * Test cleanTable
   * 
   */
  @Test
  @Order(3)
  public void testCleanTable() {
    System.out.println("========TESTING CLEAN TABLE ========");
    try {
      DatabaseJdbc.createLoginTable(jdbc, "UserTest");
      DatabaseJdbc.createItemTable(jdbc, "ItemTest");
      DatabaseJdbc.createSearchTable(jdbc, "SearchTest", "ItemTest");
      DatabaseJdbc.createTaskTable(jdbc, "TaskTest", "UserTest", "SearchTest",
          "ItemTest");
      DatabaseJdbc.createStoreTable(jdbc, "StoreTest");
      DatabaseJdbc.createPurchaseTable(jdbc, "RecordTest");

      DatabaseJdbc.deleteTable(jdbc, "TaskTest");
      DatabaseJdbc.deleteTable(jdbc, "SearchTest");
      DatabaseJdbc.deleteTable(jdbc, "ItemTest");
      DatabaseJdbc.deleteTable(jdbc, "UserTest");
      DatabaseJdbc.deleteTable(jdbc, "StoreTest");
      DatabaseJdbc.deleteTable(jdbc, "RecordTest");
    } catch (SQLException e1) {
      // TODO Auto-generated catch block
      e1.printStackTrace();
    }
    Connection c = jdbc.createConnection();
    Statement stmt = null;

    try {
      c.setAutoCommit(false);
      stmt = c.createStatement();
      String sql = "CREATE TABLE UserTest (USER_ID text PRIMARY KEY, EMAIL"
          + " TEXT, NAME TEXT,SAVINGS FLOAT,LOCATION POINT,ONLINE BOOLEAN DEFAULT FALSE)";
      stmt.executeUpdate(sql);
      stmt.close();
      c.commit();
      c.close();
    } catch (Exception e) {
      assertEquals(1, 0);
    }

  }

  /*
   * Test insert User
   * 
   */
  @Test
  @Order(4)
  public void testInsertUser() {
    System.out.println("========TESTING INSERT User ========");
    User user = new User("123", "123@123.com", "ABC DEF", 145.6, 1.361,
        51.3267);
    try {
      DatabaseJdbc.deleteTable(jdbc, "UserTest");
      DatabaseJdbc.createLoginTable(jdbc, "UserTest");
      DatabaseJdbc.addLoginData(jdbc, "UserTest", user);
    } catch (SQLException e1) {
      // TODO Auto-generated catch block
      e1.printStackTrace();
    }
    Statement stmt = null;
    Connection c = jdbc.createConnection();

    try {
      c.setAutoCommit(false);
      stmt = c.createStatement();
      String sql = "SELECT * FROM UserTest WHERE USER_ID = 123;";
      ResultSet rs = stmt.executeQuery(sql);
      while (rs.next()) {
        String userId = rs.getString("USER_ID");
        assertEquals(userId, "123");

        String email = rs.getString("email");
        assertEquals(email, "123@123.com");

        String name = rs.getString("name");
        assertEquals(name, "ABC DEF");

        double savings = rs.getDouble("savings");
        assertEquals(savings, 145.6);

        String location = rs.getString("location");
        assertEquals(location, "(1.361,51.3267)");

        boolean online = rs.getBoolean("ONLINE");
        assertEquals(online, true);

      }

      stmt.close();
      c.commit();
      c.close();
    } catch (Exception e) {
      System.err.println(e.getClass().getName() + ": " + e.getMessage());
      assertEquals(1, 0);
    }

  }

  /*
   * Test getLoginData
   * 
   */
  @Test
  @Order(5)
  public void testgetLoginData() {
    System.out.println("========TESTING RETREIVING TABLE ========");
    User user1 = new User("345", "345@123.com", "qwe FKH", 1000, 79.3803,
        51.3267);
    User user2 = new User("789", "789@123.com", "hij kl", 5, -0.4632, 51.3552);
    List<User> users;
    try {
      DatabaseJdbc.deleteTable(jdbc, "UserTest");
      DatabaseJdbc.createLoginTable(jdbc, "UserTest");
      DatabaseJdbc.addLoginData(jdbc, "UserTest", user1);
      DatabaseJdbc.addLoginData(jdbc, "UserTest", user2);
      users = DatabaseJdbc.getLoginData(jdbc, "UserTest");
      assertEquals(users.size(), 2);
      assertEquals(users.get(0).getEmail(), user1.getEmail());
      assertEquals(users.get(0).getUserId(), user1.getUserId());
      assertEquals(users.get(0).getName(), user1.getName());
      assertEquals(users.get(0).getSavings(), user1.getSavings());
      assertEquals(users.get(0).getLat(), user1.getLat());
      assertEquals(users.get(0).getLon(), user1.getLon());
      assertEquals(users.get(1).getEmail(), user2.getEmail());
      assertEquals(users.get(1).getUserId(), user2.getUserId());
      assertEquals(users.get(1).getName(), user2.getName());
      assertEquals(users.get(1).getSavings(), user2.getSavings());
      assertEquals(users.get(1).getLat(), user2.getLat());
      assertEquals(users.get(1).getLon(), user2.getLon());

    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

  }

  /*
   * Test AlreadyExists
   * 
   */
  @Test
  @Order(6)
  public void testAlreadyExists() {
    System.out.println("========TESTING AlreadyExists ========");
    User user1 = new User("56789", "345@123.com", "qwe FKH", 1000, 79.3803,
        51.3267);
    try {
      DatabaseJdbc.deleteTable(jdbc, "UserTest");
      DatabaseJdbc.createLoginTable(jdbc, "UserTest");
      boolean result = DatabaseJdbc.alreadyExists(jdbc, "UserTest", "56789",
          "user_id");
      assertEquals(result, false);

      DatabaseJdbc.addLoginData(jdbc, "UserTest", user1);
      result = DatabaseJdbc.alreadyExists(jdbc, "UserTest", "56789", "user_id");
      assertEquals(result, true);

    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

  }

  /*
   * Test Update login data
   * 
   */
  @Test
  @Order(7)
  public void testUpdateLogin() {
    System.out.println("========TESTING Update login ========");
    User user1 = new User("56789", "345@123.com", "qwe FKH", 1000, 79.3803,
        51.3267);
    try {
      DatabaseJdbc.deleteTable(jdbc, "UserTest");
      DatabaseJdbc.createLoginTable(jdbc, "UserTest");
      DatabaseJdbc.addLoginData(jdbc, "UserTest", user1);
      DatabaseJdbc.logoutUser(jdbc, "UserTest", user1.getUserId());
      DatabaseJdbc.updatesLoginData(jdbc, "UserTest", user1.getUserId());

    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

  }

  /*
   * Test Update logout data
   * 
   */
  @Test
  @Order(8)
  public void testLogoutUser() {
    System.out.println("========TESTING logout user ========");
    User user1 = new User("34535667", "345@123.com", "qwe FKH", 1000, 79.3803,
        51.3267);
    try {
      DatabaseJdbc.deleteTable(jdbc, "UserTest");
      DatabaseJdbc.createLoginTable(jdbc, "UserTest");
      DatabaseJdbc.addLoginData(jdbc, "UserTest", user1);
      DatabaseJdbc.logoutUser(jdbc, "UserTest", user1.getUserId());

      Statement stmt = null;
      Connection c = jdbc.createConnection();

      try {
        c.setAutoCommit(false);
        stmt = c.createStatement();
        String sql = "SELECT * FROM UserTest WHERE USER_ID = "
            + user1.getUserId() + ";";
        ResultSet rs = stmt.executeQuery(sql);
        while (rs.next()) {
          String userId = rs.getString("USER_ID");
          assertEquals(userId, "34535667");

          String email = rs.getString("email");
          assertEquals(email, "345@123.com");

          String name = rs.getString("name");
          assertEquals(name, "qwe FKH");

          double savings = rs.getDouble("savings");
          assertEquals(savings, 1000);

          String location = rs.getString("location");
          assertEquals(location, "(79.3803,51.3267)");

          boolean online = rs.getBoolean("ONLINE");
          assertEquals(online, false);

        }

        stmt.close();
        c.commit();
        c.close();
      } catch (Exception e) {
        System.err.println(e.getClass().getName() + ": " + e.getMessage());
        assertEquals(1, 0);
      }

    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

  }

  /*
   * Test insert Item
   * 
   */
  @Test
  @Order(9)
  public void testInsertItem() {
    System.out.println("========TESTING INSERT ITEM ========");
    Item item = new Item("FUJI APPLE", "001", 7.1, "COSTCO", 41.5, 34.1);
    try {
      DatabaseJdbc.deleteTable(jdbc, "ItemTest");
      DatabaseJdbc.createItemTable(jdbc, "ItemTest");
      DatabaseJdbc.addItem(jdbc, "ItemTest", item);
    } catch (SQLException e1) {
      // TODO Auto-generated catch block
      e1.printStackTrace();
    }
    Statement stmt = null;
    Connection c = jdbc.createConnection();

    try {
      c.setAutoCommit(false);
      stmt = c.createStatement();
      String sql = "SELECT * FROM ItemTest WHERE ID = \"001\";";
      ResultSet rs = stmt.executeQuery(sql);
      while (rs.next()) {
        String id = rs.getString("ID");
        assertEquals(id, "001");

        String name = rs.getString("name");
        assertEquals(name, "FUJI APPLE");

        double price = rs.getDouble("price");
        assertEquals(price, 7.1);

        String store = rs.getString("store");
        assertEquals(store, "COSTCO");

        double lat = rs.getDouble("lat");
        assertEquals(lat, 41.5);

        double lon = rs.getDouble("lon");
        assertEquals(lon, 34.1);

      }

      stmt.close();
      c.commit();
      c.close();
    } catch (Exception e) {
      System.err.println(e.getClass().getName() + ": " + e.getMessage());
      assertEquals(1, 0);
    }

  }

  /*
   * Test insert Item that already exist
   * 
   */
  @Test
  @Order(10)
  public void testInsertItem2() {
    System.out.println("========TESTING INSERT ITEM 2 ========");
    Item item1 = new Item("FUJI APPLE", "001", 7.1, "COSTCO", 41.5, 34.1);
    Item item2 = new Item("FUJI APPLE", "001", 8.9, "COSTCO", 14.5, 13.4);
    try {
      DatabaseJdbc.deleteTable(jdbc, "ItemTest");
      DatabaseJdbc.createItemTable(jdbc, "ItemTest");
      DatabaseJdbc.addItem(jdbc, "ItemTest", item1);
      DatabaseJdbc.addItem(jdbc, "ItemTest", item2);
    } catch (SQLException e1) {
      // TODO Auto-generated catch block
      e1.printStackTrace();
    }
    Statement stmt = null;
    Connection c = jdbc.createConnection();

    try {
      c.setAutoCommit(false);
      stmt = c.createStatement();
      String sql = "SELECT * FROM ItemTest WHERE ID = \"001\" and lat = 14.5;";
      ResultSet rs = stmt.executeQuery(sql);
      while (rs.next()) {
        String id = rs.getString("ID");
        assertEquals(id, "001");

        String name = rs.getString("name");
        assertEquals(name, "FUJI APPLE");

        double price = rs.getDouble("price");
        assertEquals(price, 8.9);

        String store = rs.getString("store");
        assertEquals(store, "COSTCO");

        double lat = rs.getDouble("lat");
        assertEquals(lat, 14.5);

        double lon = rs.getDouble("lon");
        assertEquals(lon, 13.4);

      }

      stmt.close();
      c.commit();
      c.close();
    } catch (Exception e) {
      System.err.println(e.getClass().getName() + ": " + e.getMessage());
      assertEquals(1, 0);
    }

  }

  /*
   * Test insert search record
   * 
   */
  @Test
  @Order(11)
  public void testInsertSearch() {
    System.out.println("========TESTING INSERT SEARCH ========");
    Item item1 = new Item("HONEYCRISP APPLE", "002", 9.3, "COSTCO", 41.5, 34.1);
    Item item2 = new Item("AVACADO", "002", 6, "COSTCO", 41.6, 34.1);
    Item item3 = new Item("PEAR", "004", 5, "COSTCO", 41.5, 34.1);

    List<Item> list = new ArrayList<Item>();
    list.add(item1);
    list.add(item2);
    list.add(item3);

    try {
      DatabaseJdbc.deleteTable(jdbc, "SearchTest");
      DatabaseJdbc.createSearchTable(jdbc, "SearchTest", "ItemTest");
      DatabaseJdbc.addSearch(jdbc, "SearchTest", "ItemTest", list, "123" + "1");

    } catch (SQLException e1) {
      // TODO Auto-generated catch block
      e1.printStackTrace();
    }
    Statement stmt = null;
    Connection c = jdbc.createConnection();

    try {
      c.setAutoCommit(false);
      stmt = c.createStatement();
      String sql = "SELECT * FROM ItemTest WHERE ID = \"002\" and lat=41.5 and lon=34.1;";
      ResultSet rs = stmt.executeQuery(sql);
      while (rs.next()) {
        String id = rs.getString("ID");
        assertEquals(id, "002");

        String name = rs.getString("name");
        assertEquals(name, "HONEYCRISP APPLE");

        double price = rs.getDouble("price");
        assertEquals(price, 9.3);

        String store = rs.getString("store");
        assertEquals(store, "COSTCO");

        double lat = rs.getDouble("lat");
        assertEquals(lat, 41.5);

        double lon = rs.getDouble("lon");
        assertEquals(lon, 34.1);

      }

      stmt.close();
      c.commit();
      c.close();
    } catch (Exception e) {
      System.err.println(e.getClass().getName() + ": " + e.getMessage());
      assertEquals(1, 0);
    }

  }

  /*
   * Test insert task record
   * 
   */
  @Test
  @Order(15)
  public void testInsertTask() {
    System.out.println("========TESTING INSERT TASK ========");
    User user = new User("34535667", "345@123.com", "qwe FKH", 1000, 79.3803,
        51.3267);
    Item item1 = new Item("HONEYCRISP APPLE", "002", 9.3, "COSTCO", 41.5, 34.1);
    Item item2 = new Item("AVACADO", "003", 6, "COSTCO", 41.5, 34.1);
    Item item3 = new Item("PEAR", "004", 5, "COSTCO", 41.5, 34.1);

    List<Item> list = new ArrayList<Item>();
    list.add(item1);
    list.add(item2);
    list.add(item3);

    OngoingTask task = new OngoingTask();
    task.setUserId("34535667");
    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
    task.setTaskStartTime(timestamp);
    task.setSearchString("FRUIT");
    task.setSearchItems(list);
    task.setInitialItem(item3);
    task.setAlternativeItem(new ArrayList<Item>());
    task.setFinalItem(new Item());

    try {
      DatabaseJdbc.deleteTable(jdbc, "TaskTest");
      DatabaseJdbc.deleteTable(jdbc, "SearchTest");
      DatabaseJdbc.deleteTable(jdbc, "ItemTest");
      DatabaseJdbc.deleteTable(jdbc, "UserTest");
      DatabaseJdbc.createLoginTable(jdbc, "UserTest");
      DatabaseJdbc.createItemTable(jdbc, "ItemTest");
      DatabaseJdbc.createSearchTable(jdbc, "SearchTest", "ItemTest");
      DatabaseJdbc.createTaskTable(jdbc, "TaskTest", "UserTest", "SearchTest",
          "ItemTest");

      DatabaseJdbc.addLoginData(jdbc, "UserTest", user);
      DatabaseJdbc.addTask(jdbc, "TaskTest", task);

    } catch (SQLException e1) {
      // TODO Auto-generated catch block
      e1.printStackTrace();
    }
    Statement stmt = null;
    Connection c = jdbc.createConnection();

    try {
      c.setAutoCommit(false);
      stmt = c.createStatement();
      String sql = "SELECT * FROM TaskTest WHERE USER_ID = \"34535667\";";
      ResultSet rs = stmt.executeQuery(sql);
      if (rs.next()) {
        String id = rs.getString("USER_ID");
        assertEquals(id, "34535667");

        // Timestamp time = rs.getTimestamp("STARTTIME");
        // assertEquals(time, timestamp);

        String searchString = rs.getString("SEARCHSTRING");
        assertEquals(searchString, "FRUIT");

        String searchId = rs.getString("SEARCH_ID");
        assertEquals(id + "1", searchId);

        String initial = rs.getString("INITIAL");
        assertEquals("004", initial);

      }

      stmt.close();
      c.commit();
      c.close();
    } catch (Exception e) {
      System.err.println(e.getClass().getName() + ": " + e.getMessage());
      assertEquals(1, 0);
    }

  }

  /*
   * Test AlreadyExists on item table
   * 
   */
  @Test
  @Order(12)
  public void testAlreadyExists2() {
    System.out.println("========TESTING AlreadyExists ========");
    Item item = new Item("FUJI APPLE", "001", 7.1, "COSTCO", 41.5, 34.1);
    try {
      DatabaseJdbc.deleteTable(jdbc, "ItemTest");
      DatabaseJdbc.createItemTable(jdbc, "ItemTest");
      boolean result = DatabaseJdbc.alreadyExists(jdbc, "ItemTest", "001",
          "id");
      assertEquals(result, false);

      DatabaseJdbc.addItem(jdbc, "ItemTest", item);
      result = DatabaseJdbc.alreadyExists(jdbc, "ItemTest", "001", "id");
      assertEquals(result, true);

    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

  }

  /*
   * Test get item
   * 
   */
  @Test
  @Order(13)
  public void testGetItem() {
    System.out.println("========TESTING get item ========");
    Item item1 = new Item("HONEYCRISP APPLE", "002", 9.3, "COSTCO", 41.5, 34.1);
    Item item2 = new Item("AVACADO", "003", 6, "COSTCO", 41.5, 34.1);
    Item item3 = new Item("PEAR", "004", 5, "COSTCO", 17.9, 15.8);

    List<Item> list = new ArrayList<Item>();
    list.add(item1);
    list.add(item2);
    list.add(item3);

    try {
      DatabaseJdbc.deleteTable(jdbc, "SearchTest");
      DatabaseJdbc.createSearchTable(jdbc, "SearchTest", "ItemTest");
      DatabaseJdbc.addSearch(jdbc, "SearchTest", "ItemTest", list, "123" + "1");
      Item item4 = DatabaseJdbc.getItem(jdbc, "ItemTest", "004", 17.9, 15.8);
      assertEquals(item4.getBarcode(), "004");
      assertEquals(item4.getName(), "PEAR");
      assertEquals(item4.getPrice(), 5);
      assertEquals(item4.getStore(), "COSTCO");
      assertEquals(item4.getLat(), 17.9);
      assertEquals(item4.getLon(), 15.8);

    } catch (SQLException e1) {
      // TODO Auto-generated catch block
      e1.printStackTrace();
    }

  }

  /*
   * Test get search record
   * 
   */
  @Test
  @Order(14)
  public void testGetSearch() {
    System.out.println("========TESTING GET SEARCH ========");
    Item item1 = new Item("HONEYCRISP APPLE", "002", 9.3, "COSTCO", 41.5, 34.1);
    Item item2 = new Item("AVACADO", "003", 6, "COSTCO", 41.5, 34.1);
    Item item3 = new Item("PEAR", "004", 5, "COSTCO", 41.5, 34.1);

    List<Item> list1 = new ArrayList<Item>();
    list1.add(item1);
    list1.add(item2);
    list1.add(item3);

    Item item4 = new Item("MILK", "005", 3, "7-11", 41.5, 34.1);
    Item item5 = new Item("TEA", "006", 4.5, "TARGET", 41.5, 34.1);
    List<Item> list2 = new ArrayList<Item>();
    list2.add(item4);
    list2.add(item5);

    try {
      DatabaseJdbc.deleteTable(jdbc, "SearchTest");
      DatabaseJdbc.createSearchTable(jdbc, "SearchTest", "ItemTest");
      DatabaseJdbc.addSearch(jdbc, "SearchTest", "ItemTest", list1,
          "123" + "2");
      DatabaseJdbc.addSearch(jdbc, "SearchTest", "ItemTest", list2,
          "123" + "1");
      List<Item> result = DatabaseJdbc.getSearch(jdbc, "SearchTest", "ItemTest",
          "1232");
      assertEquals(3, result.size());

    } catch (SQLException e1) {
      // TODO Auto-generated catch block
      e1.printStackTrace();
    }

  }

  /*
   * Test get task
   * 
   */
  @Test
  @Order(16)
  public void testGetTask() {
    System.out.println("========TESTING GET TASK ========");
    User user = new User("34535667", "345@123.com", "qwe FKH", 1000, 79.3803,
        51.3267);
    Item item1 = new Item("HONEYCRISP APPLE", "002", 9.3, "COSTCO", 41.5, 34.1);
    Item item2 = new Item("AVACADO", "003", 6, "COSTCO", 41.5, 34.1);
    Item item3 = new Item("PEAR", "004", 5, "COSTCO", 41.5, 34.1);

    List<Item> list = new ArrayList<Item>();
    list.add(item1);
    list.add(item2);
    list.add(item3);

    OngoingTask task = new OngoingTask();
    task.setUserId("34535667");
    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
    task.setTaskStartTime(timestamp);
    task.setSearchString("FRUIT");
    task.setSearchItems(list);
    task.setInitialItem(item3);
    task.setInitialLat(item3.getLat());
    task.setInitialLon(item3.getLon());
    task.setAlternativeItem(new ArrayList<Item>());
    task.setFinalItem(new Item());

    OngoingTask result = new OngoingTask();

    try {
      DatabaseJdbc.deleteTable(jdbc, "TaskTest");
      DatabaseJdbc.deleteTable(jdbc, "SearchTest");
      DatabaseJdbc.deleteTable(jdbc, "ItemTest");
      DatabaseJdbc.deleteTable(jdbc, "UserTest");
      DatabaseJdbc.createLoginTable(jdbc, "UserTest");
      DatabaseJdbc.createItemTable(jdbc, "ItemTest");
      DatabaseJdbc.createSearchTable(jdbc, "SearchTest", "ItemTest");
      DatabaseJdbc.createTaskTable(jdbc, "TaskTest", "UserTest", "SearchTest",
          "ItemTest");

      DatabaseJdbc.addLoginData(jdbc, "UserTest", user);
      DatabaseJdbc.addSearch(jdbc, "SearchTest", "ItemTest", list,
          "34535667" + "1");
      DatabaseJdbc.addTask(jdbc, "TaskTest", task);
      result = DatabaseJdbc.getTask(jdbc, "TaskTest", "SearchTest", "ItemTest",
          "34535667");
      assertEquals("34535667", result.getUserId());
      // assertEquals(timestamp, result.getTaskStartTime());
      assertEquals("FRUIT", result.getSearchString());
      assertEquals(3, result.getSearchItems().size());
      assertEquals("004", result.getInitialItem().getBarcode());

    } catch (SQLException e1) {
      // TODO Auto-generated catch block
      e1.printStackTrace();
    }
  }

  /*
   * Test REMOVE search record
   * 
   */
  @Test
  @Order(16)
  public void testRemoveSearch() {
    System.out.println("========TESTING REMOVE SEARCH ========");
    Item item1 = new Item("HONEYCRISP APPLE", "002", 9.3, "COSTCO", 41.5, 34.1);
    Item item2 = new Item("AVACADO", "003", 6, "COSTCO", 41.5, 34.1);
    Item item3 = new Item("PEAR", "004", 5, "COSTCO", 41.5, 34.1);

    List<Item> list1 = new ArrayList<Item>();
    list1.add(item1);
    list1.add(item2);
    list1.add(item3);

    Item item4 = new Item("MILK", "005", 3, "7-11", 41.5, 34.1);
    Item item5 = new Item("TEA", "006", 4.5, "TARGET", 41.5, 34.1);
    List<Item> list2 = new ArrayList<Item>();
    list2.add(item4);
    list2.add(item5);

    try {
      DatabaseJdbc.deleteTable(jdbc, "SearchTest");
      DatabaseJdbc.createSearchTable(jdbc, "SearchTest", "ItemTest");
      DatabaseJdbc.addSearch(jdbc, "SearchTest", "ItemTest", list1,
          "123" + "2");
      DatabaseJdbc.addSearch(jdbc, "SearchTest", "ItemTest", list2,
          "123" + "1");
      DatabaseJdbc.removeSearch(jdbc, "SearchTest", "123" + "2");
      List<Item> result = DatabaseJdbc.getSearch(jdbc, "SearchTest", "ItemTest",
          "1232");
      assertEquals(0, result.size());

    } catch (SQLException e1) {
      // TODO Auto-generated catch block
      e1.printStackTrace();
    }

  }

  /*
   * Test AlreadyExists on Store table
   * 
   */
  @Test
  @Order(17)
  public void testAlreadyExists3() {
    System.out.println("========TESTING AlreadyExists ========");
    Store store = new Store("RIDGEMONT", "Wegmans Store", 12, 43.20785,
        -77.70403);
    try {
      DatabaseJdbc.deleteTable(jdbc, "StoreTest");
      DatabaseJdbc.createStoreTable(jdbc, "StoreTest");
      boolean result = DatabaseJdbc.alreadyExistsStore(jdbc, "StoreTest",
          store);
      assertEquals(result, false);

      DatabaseJdbc.addStore(jdbc, "StoreTest", store);
      result = DatabaseJdbc.alreadyExistsStore(jdbc, "StoreTest", store);
      assertEquals(result, true);

    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

  }

  /*
   * Test get store on Store table
   * 
   */
  @Test
  @Order(18)
  public void testGetStore() {
    System.out.println("========TESTING GetStore ========");
    Store store = new Store("RIDGEMONT", "Wegmans Store", 12, 43.20785,
        -77.70403);
    Store store2 = new Store("MT. LAUREL", "Wegmans Store", 8, 39.96771,
        -74.91507);
    try {
      DatabaseJdbc.deleteTable(jdbc, "StoreTest");
      DatabaseJdbc.createStoreTable(jdbc, "StoreTest");
      DatabaseJdbc.addStore(jdbc, "StoreTest", store);
      DatabaseJdbc.addStore(jdbc, "StoreTest", store2);
      List<Store> stores = DatabaseJdbc.getStore(jdbc, "StoreTest",
          "Wegmans Store");
      assertEquals(2, stores.size());

    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

  }

  /*
   * Test AlreadyExists on Item table
   * 
   */
  @Test
  @Order(19)
  public void testAlreadyExists4() {
    System.out.println("========TESTING AlreadyExists ========");
    Item item = new Item("CV Ella Coffee-2Maria Med", "73038517792", 9.99,
        "Wegmans Store", 43.03306, -76.05325, "73038517792", "766617",
        "https://wfmproducts.azureedge.net/images-500/00730385177928.jpg");
    try {
      DatabaseJdbc.deleteTable(jdbc, "ItemTest");
      DatabaseJdbc.createItemTable(jdbc, "ItemTest");
      boolean result = DatabaseJdbc.alreadyExistsItem(jdbc, "ItemTest", item);
      assertEquals(result, false);

      DatabaseJdbc.addItem(jdbc, "ItemTest", item);
      result = DatabaseJdbc.alreadyExistsItem(jdbc, "ItemTest", item);
      assertEquals(result, true);

    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

  }
  
  /*
   * Test insert Item
   * 
   */
  @Test
  @Order(20)
  public void testInsertPurchase() {
    System.out.println("========TESTING INSERT PURCHASE ========");
    Item item = new Item("FUJI APPLE", "001", 7.1, "COSTCO", 41.5, 34.1);

    try {
      DatabaseJdbc.createPurchaseTable(jdbc, "PurchaseTest");
      //Thread.sleep(1000);

      DatabaseJdbc.addPurchaseData(jdbc, "PurchaseTest", item, "100", 1.00, "2020-11-11");

    } catch (SQLException e1) {
      // TODO Auto-generated catch block
      e1.printStackTrace();
    }
    Statement stmt = null;
    Connection c = jdbc.createConnection();

    try {
      c.setAutoCommit(false);
      stmt = c.createStatement();
      String sql = "SELECT * FROM PurchaseTest WHERE USER_ID = 100;";

      ResultSet rs = stmt.executeQuery(sql);
      while (rs.next()) {
        String id = rs.getString("user_id");
        assertEquals(id, "100");

        String name = rs.getString("item_name");
        assertEquals(name, "FUJI APPLE");

        double price = rs.getDouble("price");
        assertEquals(price, 7.1);
        
        double saving = rs.getDouble("savings");
        assertEquals(saving, 1.00);

        String date = rs.getString("date");
        assertEquals(date, "2020-11-11");

      }
      
      stmt.close();
      c.commit();
      c.close();
      DatabaseJdbc.deleteTable(jdbc, "PurchaseTest");
    } catch (Exception e) {
      System.err.println(e.getClass().getName() + ": " + e.getMessage());
      assertEquals(1, 0);
    }

  }
  
  @Test
  @Order(21)
  public void testGetPurchase() {
    System.out.println("========TESTING GET PURCHASE ========");

    Item item = new Item("FUJI APPLE", "001", 7.1, "COSTCO", 41.5, 34.1);
    List<PurchaseRecord> testList;

    try {
      DatabaseJdbc.createPurchaseTable(jdbc, "PurchaseTest");
      DatabaseJdbc.addPurchaseData(jdbc, "PurchaseTest", item, "300", 1.00, "2020-11-11");
      
      testList = DatabaseJdbc.getPurchaseData(jdbc, "PurchaseTest", "300");
      
      assertEquals(testList.size(), 1);
      
      PurchaseRecord testRecord = testList.get(0);
      
      assertEquals(testRecord.getDate(), "2020-11-11");
      assertEquals(testRecord.getPrice(), 7.1);
      assertEquals(testRecord.getItem(), "FUJI APPLE");
      assertEquals(testRecord.getSaving(), 1.00);
      
      DatabaseJdbc.deleteTable(jdbc, "PurchaseTest");

    } catch (SQLException e1) {
      // TODO Auto-generated catch block
      e1.printStackTrace();
    }
  }
  
  @Test
  @Order(22)
  public void testGetTooOldWeekSaving() {
    System.out.println("========TESTING GET TOO OLD WEEK SAVING ========");

    Item item = new Item("FUJI APPLE", "001", 7.1, "COSTCO", 41.5, 34.1);
    List<PurchaseRecord> testList;

    try {
      DatabaseJdbc.createPurchaseTable(jdbc, "PurchaseTest");
      DatabaseJdbc.addPurchaseData(jdbc, "PurchaseTest", item, "300", 1.00, "2019-11-11");
      
      testList = DatabaseJdbc.getPurchaseData(jdbc, "PurchaseTest", "300");
      
      double saving = DatabaseJdbc.getWeekSavings(testList);
      
      assertEquals(saving, 0);
      
      DatabaseJdbc.deleteTable(jdbc, "PurchaseTest");
      

    } catch (SQLException e1) {
      // TODO Auto-generated catch block
      e1.printStackTrace();
    }
  }
  
  @Test
  @Order(23)
  public void testGetWeekSaving() {
    System.out.println("========TESTING GET WEEK SAVING ========");

    Item item = new Item("FUJI APPLE", "001", 7.1, "COSTCO", 41.5, 34.1);
    List<PurchaseRecord> testList;
    
    LocalDate currentDate = LocalDate.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    String testDate = currentDate.format(formatter);
    
//    SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
//    
//    Calendar recordCal = Calendar.getInstance();
//    recordCal.add(Calendar.DATE, -1); 
//    Date dateObj = recordCal.getTime();
//    String testDate = format1.format(dateObj);

    System.out.println("Input date: " + testDate);
    
    try {
      DatabaseJdbc.createPurchaseTable(jdbc, "PurchaseTest");
      DatabaseJdbc.addPurchaseData(jdbc, "PurchaseTest", item, "300", 7.00, testDate);
      
      testList = DatabaseJdbc.getPurchaseData(jdbc, "PurchaseTest", "300");
      
      double saving = DatabaseJdbc.getWeekSavings(testList);
      
      assertEquals(saving, 7);
      
      DatabaseJdbc.deleteTable(jdbc, "PurchaseTest");
      

    } catch (SQLException e1) {
      // TODO Auto-generated catch block
      e1.printStackTrace();
    }

  }
}
