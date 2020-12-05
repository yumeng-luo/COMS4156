package savings.tracker.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
//import org.sqlite.JDBC;
import savings.tracker.Item;
import savings.tracker.OngoingTask;
import savings.tracker.PurchaseRecord;
import savings.tracker.Store;
import savings.tracker.User;

public class DatabaseJdbc {

  public static void main(String[] args) {

  }

  /**
   * Creates a connection with database.
   * 
   * @return returns type Connection
   */
  public Connection createConnection() {
    Connection c = null;

    try {
      Class.forName("org.sqlite.JDBC");
      c = DriverManager.getConnection("jdbc:sqlite:ase.db");
    } catch (Exception e) {
      System.err.println(e.getClass().getName() + ": " + e.getMessage());
      return null;
    }
    System.out.println("Created connection to database successfully");
    return c;
  }

  /**
   * Creates a table for login info.
   * 
   * @param jdbc      database
   * @param tableName the table name
   * @return returns boolean
   * @throws SQLException Exception
   */
  public static boolean createLoginTable(DatabaseJdbc jdbc, String tableName)
      throws SQLException {
    PreparedStatement stmt = null;
    Connection c = jdbc.createConnection();

    try {
      String sql = String.format(
          "CREATE TABLE IF NOT EXISTS %s (%s, %s, %s, %s, %s, %s)", tableName,
          "USER_ID text PRIMARY KEY", "EMAIL TEXT", "NAME TEXT",
          "SAVINGS FLOAT", "LOCATION POINT", "ONLINE BOOLEAN DEFAULT FALSE");
      stmt = c.prepareStatement(sql);

      stmt.executeUpdate();
      stmt.close();
    } catch (Exception e) {
      if (stmt != null) {
        stmt.close();
      }
      System.err.println(e.getClass().getName() + ": " + e.getMessage());
      return false;
    }

    try {
      c.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }

    System.out.println("Table created successfully");
    return true;
  }

  /**
   * Create table for confirmed purchases.
   * 
   * @param jdbc      database
   * @param tableName table name
   * @throws SQLException exception
   */
  public static boolean createPurchaseTable(DatabaseJdbc jdbc, String tableName)
      throws SQLException {
    PreparedStatement stmt = null;
    Connection c = jdbc.createConnection();

    try {
      String sql = String.format(
          "CREATE TABLE IF NOT EXISTS %s (%s, %s, %s, %s, %s)", tableName,
          "USER_ID text", "DATE TEXT", "ITEM_NAME TEXT", "PRICE DOUBLE",
          "SAVINGS DOUBLE");
      stmt = c.prepareStatement(sql);

      stmt.executeUpdate();
      stmt.close();
    } catch (Exception e) {
      if (stmt != null) {
        stmt.close();
      }
      System.err.println(e.getClass().getName() + ": " + e.getMessage());
      return false;
    }

    try {
      c.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }

    System.out.println("Table created successfully");
    return true;
  }

  /**
   * Creates a table for ongoing task.
   * 
   * @param jdbc      database
   * @param tableName the table name
   * @return returns boolean
   * @throws SQLException Exception
   */
  public static boolean createTaskTable(DatabaseJdbc jdbc, String tableName,
      String userTable, String searchtable, String itemTable)
      throws SQLException {
    PreparedStatement stmt = null;
    Connection c = jdbc.createConnection();

    try {
      String sql = "CREATE TABLE IF NOT EXISTS " + tableName
          + "(USER_ID TEXT REFERENCES " + userTable
          + "(USER_ID) PRIMARY KEY, SEARCHSTRING TEXT,"
          + " STARTTIME TIMESTAMP,SEARCH_ID TEXT REFERENCES " + searchtable
          + "(ID)," + " INITIAL TEXT REFERENCES " + itemTable
          + "(ID), INITIAL_LAT FLOAT REFERENCES " + itemTable
          + "(LAT), INITIAL_LON FLOAT REFERENCES " + itemTable + "(LON),"
          + " ALTERNATIVE_SEARCH TEXT REFERENCES " + searchtable + "(ID),"
          + " FINAL TEXT REFERENCES " + itemTable
          + "(ID), FINAL_LAT FLOAT REFERENCES " + itemTable
          + "(LAT), FINAL_LON FLOAT REFERENCES " + itemTable + "(LON) )";
      // CREATE TABLE IF NOT EXISTS TASK (USER_ID TEXT REFERENCES User(USER_ID)
      // PRIMARY KEY, SEARCHSTRING TEXT, STARTTIME TIMESTAMP,SEARCH_ID TEXT
      // REFERENCES SEARCHRESULT(ID), INITIAL TEXT REFERENCES ITEM(ID),
      // ALTERNATIVE_SEARCH TEXT REFERENCES SEARCHRESULT(ID), FINAL TEXT
      // REFERENCES ITEM(ID) )
      stmt = c.prepareStatement(sql);

      stmt.executeUpdate();
      stmt.close();
    } catch (Exception e) {
      if (stmt != null) {
        stmt.close();
      }
      System.err.println(e.getClass().getName() + ": " + e.getMessage());
      return false;
    }

    try {
      c.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }

    System.out.println("Table created successfully");
    return true;
  }

  /**
   * Creates a table for ITEMS.
   * 
   * @param jdbc      database
   * @param tableName the table name
   * @return returns boolean
   * @throws SQLException Exception
   */
  public static boolean createItemTable(DatabaseJdbc jdbc, String tableName)
      throws SQLException {
    PreparedStatement stmt = null;
    Connection c = jdbc.createConnection();

    try {
      String sql = "CREATE TABLE IF NOT EXISTS " + tableName
          + "(NAME TEXT, ID TEXT, " + "PRICE FLOAT, STORE TEXT, "
          + "LAT FLOAT, LON FLOAT, TCIN TEXT, SKU TEXT, IMAGE TEXT,"
          + " CONSTRAINT pk_item primary key (ID, LAT, LON) )";
      // CREATE TABLE IF NOT EXISTS item (NAME TEXT,
      // ID TEXT PRIMARY KEY, PRICE FLOAT, STORE TEXT, LAT FLOAT, LON FLOAT )
      stmt = c.prepareStatement(sql);

      stmt.executeUpdate();
      stmt.close();
    } catch (Exception e) {
      if (stmt != null) {
        stmt.close();
      }
      System.err.println(e.getClass().getName() + ": " + e.getMessage());
      return false;
    }

    try {
      c.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }

    System.out.println("Table created successfully");
    return true;
  }

  /**
   * Creates a table for searches.
   * 
   * @param jdbc      database
   * @param tableName the table name
   * @return returns boolean
   * @throws SQLException Exception
   */
  public static boolean createSearchTable(DatabaseJdbc jdbc, String tableName,
      String itemTable) throws SQLException {
    PreparedStatement stmt = null;
    Connection c = jdbc.createConnection();

    try {
      String sql = "CREATE TABLE IF NOT EXISTS " + tableName + " ( ID TEXT, "
          + "ITEM_ID TEXT REFERENCES " + itemTable
          + "(ID) , LAT FLOAT, LON FLOAT )";
      // CREATE TABLE IF NOT EXISTS SEARCHRESULT ( ID TEXT PRIMARY KEY, ITEM_ID
      // TEXT REFERENCES ITEM(ID) )
      stmt = c.prepareStatement(sql);

      stmt.executeUpdate();
      stmt.close();
    } catch (Exception e) {
      if (stmt != null) {
        stmt.close();
      }
      System.err.println(e.getClass().getName() + ": " + e.getMessage());
      return false;
    }

    try {
      c.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }

    System.out.println("Table created successfully");
    return true;
  }

  /**
   * Creates a table for store info.
   * 
   * @param jdbc      database
   * @param tableName the table name
   * @return returns boolean
   * @throws SQLException Exception
   */
  public static boolean createStoreTable(DatabaseJdbc jdbc, String tableName)
      throws SQLException {
    PreparedStatement stmt = null;
    Connection c = jdbc.createConnection();

    try {
      String sql = String.format("create table if not exists %s "
          + "(name text, number integer, type text,"
          + " lat float, lon float,CONSTRAINT pk_store"
          + " primary key (number,type) )", tableName);
      stmt = c.prepareStatement(sql);

      stmt.executeUpdate();
      stmt.close();
    } catch (Exception e) {
      if (stmt != null) {
        stmt.close();
      }
      System.err.println(e.getClass().getName() + ": " + e.getMessage());
      return false;
    }

    try {
      c.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }

    System.out.println("Table created successfully");
    return true;
  }

  /**
   * Adds login data to data table.
   * 
   * @param jdbc      the database
   * @param tableName the table name
   * @param user      the user object
   * @return returns boolean
   * @throws SQLException exception
   */
  public static boolean addLoginData(DatabaseJdbc jdbc, String tableName,
      User user) throws SQLException {
    PreparedStatement stmt = null;
    Connection c = jdbc.createConnection();

    try {
      c.setAutoCommit(false);
      System.out.println("Opened database successfully for addPlayerData");

      stmt = c.prepareStatement(
          "INSERT INTO " + tableName + " values(?,?,?,?,?,?)");
      stmt.setString(1, user.getUserId());
      stmt.setString(2, user.getEmail());
      stmt.setString(3, user.getName());
      stmt.setString(4, String.valueOf(user.getSavings()));
      stmt.setString(5, "(" + String.valueOf(user.getLat()) + ","
          + String.valueOf(user.getLon()) + ")");
      stmt.setString(6, "1");

      stmt.executeUpdate();
      stmt.close();
      c.commit();
    } catch (Exception e) {
      if (stmt != null) {
        stmt.close();
      }
      System.err.println(e.getClass().getName() + ": " + e.getMessage());
      return false;
    }

    try {
      c.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }

    System.out.println("Record created successfully");
    return true;
  }

  /**
   * Adds purchase record to data table.
   * 
   * @param jdbc      database
   * @param tableName table name
   * @param item      item
   * @throws SQLException exception
   */
  public static boolean addPurchaseData(DatabaseJdbc jdbc, String tableName,
      Item item, String userId, Double savings, String date)
      throws SQLException {
    PreparedStatement stmt = null;
    Connection c = jdbc.createConnection();

    try {
      c.setAutoCommit(false);
      System.out.println("Opened database successfully for addPurchaseData");

      stmt = c
          .prepareStatement("INSERT INTO " + tableName + " values(?,?,?,?,?)");
      stmt.setString(1, userId);
      stmt.setString(2, date);
      stmt.setString(3, item.getName());
      stmt.setDouble(4, item.getPrice());
      stmt.setDouble(5, savings);

      stmt.executeUpdate();
      stmt.close();
      c.commit();
    } catch (Exception e) {
      if (stmt != null) {
        stmt.close();
      }
      System.err.println(e.getClass().getName() + ": " + e.getMessage());
      return false;
    }

    try {
      c.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }

    System.out.println("id: " + userId);
    System.out.println("Record created successfully");
    return true;
  }

  /**
   * Adds item data to item table. If already exist, update item.
   * 
   * @param jdbc      the database
   * @param tableName the table name
   * @param item      the item object
   * @return returns boolean
   * @throws SQLException exception
   */
  public static boolean addItem(DatabaseJdbc jdbc, String tableName, Item item)
      throws SQLException {

    boolean exist = DatabaseJdbc.alreadyExistsItem(jdbc, tableName, item);
    PreparedStatement stmt = null;
    Connection c = jdbc.createConnection();

    try {
      c.setAutoCommit(false);
      System.out.println("Opened database successfully for addPlayerData");

      if (!exist) {
        stmt = c.prepareStatement(
            "INSERT INTO " + tableName + " values(?,?,?,?,?,?,?,?,?)");
        stmt.setString(1, item.getName());
        stmt.setString(2, item.getBarcode());
        stmt.setString(3, String.valueOf(item.getPrice()));
        stmt.setString(4, item.getStore());
        stmt.setString(5, String.valueOf(item.getLat()));
        stmt.setString(6, String.valueOf(item.getLon()));
        stmt.setString(7, item.getTcin());
        stmt.setString(8, item.getSku());
        stmt.setString(9, item.getImage());

      } else {
        stmt = c
            .prepareStatement("UPDATE " + tableName + " SET NAME=?, PRICE=?,"
                + "STORE=?, TCIN=?, SKU=?, IMAGE=? where ID =\""
                + item.getBarcode() + "\" and lat = ? and lon = ?");
        stmt.setString(1, item.getName());
        stmt.setString(2, String.valueOf(item.getPrice()));
        stmt.setString(3, item.getStore());
        stmt.setString(4, item.getTcin());
        stmt.setString(5, item.getSku());
        stmt.setString(6, item.getImage());
        stmt.setString(7, String.valueOf(item.getLat()));
        stmt.setString(8, String.valueOf(item.getLon()));
      }

      stmt.executeUpdate();
      stmt.close();
      c.commit();
    } catch (Exception e) {
      if (stmt != null) {
        stmt.close();
      }
      System.err.println(e.getClass().getName() + ": " + e.getMessage());
      return false;
    }

    try {
      c.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }

    System.out.println("Record created successfully");
    return true;
  }

  /**
   * Adds store data to store table. If already exist, update store.
   * 
   * @param jdbc      the database
   * @param tableName the table name
   * @param store     the store object
   * @return returns boolean
   * @throws SQLException exception
   */
  public static boolean addStore(DatabaseJdbc jdbc, String tableName,
      Store store) throws SQLException {

    boolean exist = DatabaseJdbc.alreadyExistsStore(jdbc, tableName, store);
    PreparedStatement stmt = null;
    Connection c = jdbc.createConnection();

    try {
      c.setAutoCommit(false);
      System.out.println("Opened database successfully for addStoreData");

      if (!exist) {
        stmt = c.prepareStatement("INSERT INTO " + tableName + " values(\""
            + store.getName() + "\"," + String.valueOf(store.getNumber())
            + ", \"" + store.getType() + "\",?,?)");
        stmt.setString(1, String.valueOf(store.getLat()));
        stmt.setString(2, String.valueOf(store.getLon()));

      } else {
        /*
         * System.out.println("UPDATE " + tableName + " SET NAME= \"" +
         * store.getName() + "\",LAT=" + String.valueOf(store.getLat()) +
         * ",LON=" + String.valueOf(store.getLon()) + " where NUMBER =" +
         * store.getNumber() + " AND TYPE= \"" + store.getType() + "\"");
         */
        stmt = c.prepareStatement("UPDATE " + tableName + " SET NAME= \""
            + store.getName() + "\", LAT=" + String.valueOf(store.getLat())
            + ",LON=" + String.valueOf(store.getLon()) + " where NUMBER ="
            + store.getNumber() + " AND TYPE= \"" + store.getType() + "\"");
      }

      stmt.executeUpdate();
      stmt.close();
      c.commit();
    } catch (Exception e) {
      if (stmt != null) {
        stmt.close();
      }
      System.err.println(e.getClass().getName() + ": " + e.getMessage());
      return false;
    }

    try {
      c.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }

    System.out.println("Record created successfully");
    return true;
  }

  /**
   * Get item data from item table.
   * 
   * @param jdbc      the database
   * @param tableName the table name
   * @param itemId    the item id
   * @param lat       the store lat
   * @param lon       the store lon
   * @return item with same id
   * @throws SQLException exception
   */
  public static Item getItem(DatabaseJdbc jdbc, String tableName, String itemId,
      double lat, double lon) throws SQLException {
    Statement stmt = null;
    ResultSet rs = null;
    Connection c = jdbc.createConnection();
    Item result = new Item();
    try {
      c.setAutoCommit(false);
      System.out.println("SELECT * FROM " + tableName + " WHERE ID " + " = \""
          + itemId + "\" and lat  = " + lat + " and lon = " + lon + ";");

      stmt = c.createStatement();
      rs = stmt
          .executeQuery("SELECT * FROM " + tableName + " WHERE ID " + " = \""
              + itemId + "\" and lat  = " + lat + " and lon = " + lon + ";");

      if (rs.next()) {
        result.setBarcode(rs.getString("ID"));
        result.setName(rs.getString("name"));
        result.setPrice(rs.getDouble("price"));
        result.setStore(rs.getString("store"));
        result.setLat(rs.getDouble("lat"));
        result.setLon(rs.getDouble("lon"));
        result.setTcin(rs.getString("TCIN"));
        result.setSku(rs.getString("SKU"));
        result.setImage(rs.getString("IMAGE"));
      }

      rs.close();
      stmt.close();

    } catch (Exception e) {
      if (stmt != null) {
        stmt.close();
      }
      if (rs != null) {
        rs.close();
      }
      System.err.println(e.getClass().getName() + ": " + e.getMessage());
      return result;
    }

    try {
      c.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return result;
  }

  /**
   * Adds task data to task table. If already exist, update task. must add
   * search first
   * 
   * @param jdbc      the database
   * @param tableName the table name
   * @param task      the task object
   * @return returns boolean
   * @throws SQLException exception
   */
  public static boolean addTask(DatabaseJdbc jdbc, String tableName,
      OngoingTask task) throws SQLException {

    boolean exist = DatabaseJdbc.alreadyExists(jdbc, tableName,
        task.getUserId(), "user_id");
    PreparedStatement stmt = null;
    Connection c = jdbc.createConnection();

    try {
      c.setAutoCommit(false);
      System.out.println("Opened database successfully for add task");

      if (!exist) {
        stmt = c.prepareStatement(
            "INSERT INTO " + tableName + " values(?,?,?,?,?,?,?,?,?,?,?)");
        stmt.setString(1, task.getUserId());
        stmt.setString(2, task.getSearchString());
        stmt.setString(3, String.valueOf(task.getTaskStartTime()));
        stmt.setString(4, task.getUserId() + "1");
        stmt.setString(5, task.getInitialItem().getBarcode());
        stmt.setString(6, String.valueOf(task.getInitialLat()));
        stmt.setString(7, String.valueOf(task.getInitialLon()));
        stmt.setString(8, task.getUserId() + "2");
        stmt.setString(9, task.getFinalItem().getBarcode());
        stmt.setString(10, String.valueOf(task.getFinalLat()));
        stmt.setString(11, String.valueOf(task.getFinalLon()));

      } else {
        stmt = c.prepareStatement(
            "UPDATE " + tableName + " SET SEARCHSTRING=?, STARTTIME=?,"
                + "SEARCH_ID=?,INITIAL=?,ALTERNATIVE_SEARCH=?, "
                + "FINAL=?, INITIAL_LAT = ?, INITIAL_LON=?,"
                + " FINAL_LAT=?, FINAL_LON = ?  where USER_ID =\""
                + task.getUserId() + "\"");
        stmt.setString(1, task.getSearchString());
        stmt.setString(2, String.valueOf(task.getTaskStartTime()));
        stmt.setString(3, task.getUserId() + "1");
        stmt.setString(4, task.getInitialItem().getBarcode());
        stmt.setString(5, task.getUserId() + "2");
        stmt.setString(6, task.getFinalItem().getBarcode());
        stmt.setString(7, String.valueOf(task.getInitialLat()));
        stmt.setString(8, String.valueOf(task.getInitialLon()));
        stmt.setString(9, String.valueOf(task.getFinalLat()));
        stmt.setString(10, String.valueOf(task.getFinalLon()));
      }

      stmt.executeUpdate();
      stmt.close();
      c.commit();
    } catch (Exception e) {
      if (stmt != null) {
        stmt.close();
      }
      System.err.println(e.getClass().getName() + ": " + e.getMessage());
      return false;
    }

    try {
      c.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }

    System.out.println("Record created successfully");
    return true;
  }

  /**
   * Get task data from task table.
   * 
   * @param jdbc        the database
   * @param tableName   the table name
   * @param searchTable search table name
   * @param itemTable   item table name
   * @param userId      the user id
   * @return returns boolean
   * @throws SQLException exception
   */
  public static OngoingTask getTask(DatabaseJdbc jdbc, String tableName,
      String searchTable, String itemTable, String userId) throws SQLException {
    Statement stmt = null;
    ResultSet rs = null;
    Connection c = jdbc.createConnection();
    OngoingTask result = new OngoingTask();
    try {
      c.setAutoCommit(false);
      System.out.println("Opened database successfully for get");

      stmt = c.createStatement();
      rs = stmt.executeQuery("SELECT * FROM " + tableName + " WHERE USER_ID "
          + " = \"" + userId + "\";");

      if (rs.next()) {
        result.setUserId(rs.getString("USER_ID"));
        result.setSearchString(rs.getString("SEARCHSTRING"));
        result.setTaskStartTime(rs.getTimestamp("STARTTIME"));
        result.setSearchItems(DatabaseJdbc.getSearch(jdbc, searchTable,
            itemTable, rs.getString("SEARCH_ID")));
        result.setInitialLat(rs.getDouble("INITIAL_LAT"));
        result.setInitialLon(rs.getDouble("INITIAL_LON"));
        result.setFinalLat(rs.getDouble("FINAL_LAT"));
        result.setFinalLon(rs.getDouble("FINAL_LON"));
        result.setInitialItem(
            DatabaseJdbc.getItem(jdbc, itemTable, rs.getString("INITIAL"),
                result.getInitialLat(), result.getInitialLon()));
        result.setAlternativeItem(DatabaseJdbc.getSearch(jdbc, searchTable,
            itemTable, rs.getString("ALTERNATIVE_SEARCH")));
        result.setFinalItem(DatabaseJdbc.getItem(jdbc, itemTable,
            rs.getString("FINAL"), result.getFinalLat(), result.getFinalLon()));
      }

      rs.close();
      stmt.close();
      c.close();

    } catch (Exception e) {
      if (stmt != null) {
        stmt.close();
      }
      if (rs != null) {
        rs.close();
      }
      c.close();
      System.err.println(e.getClass().getName() + ": " + e.getMessage());
      return result;
    }

    return result;
  }

  /**
   * Adds search results data to search table.
   * 
   * @param jdbc      the database
   * @param tableName the table name
   * @param itemTable name of item table
   * @param items     the list of searched results
   * @param searchId  user id +1 for search +2 for alternative
   * @return returns boolean
   * @throws SQLException exception
   */
  public static boolean addSearch(DatabaseJdbc jdbc, String tableName,
      String itemTable, List<Item> items, String searchId) throws SQLException {

    DatabaseJdbc.createItemTable(jdbc, itemTable);
    for (int i = 0; i < items.size(); i++) {
      DatabaseJdbc.addItem(jdbc, itemTable, items.get(i));

      PreparedStatement stmt = null;
      Connection c = jdbc.createConnection();

      try {
        c.setAutoCommit(false);
        System.out.println("Opened database successfully");
        stmt = c
            .prepareStatement("INSERT INTO " + tableName + " values(?,?,?,?)");
        stmt.setString(1, searchId);
        stmt.setString(2, items.get(i).getBarcode());
        stmt.setString(3, String.valueOf(items.get(i).getLat()));
        stmt.setString(4, String.valueOf(items.get(i).getLon()));

        stmt.executeUpdate();
        stmt.close();
        c.commit();
      } catch (Exception e) {
        if (stmt != null) {
          stmt.close();
        }
        System.err.println(e.getClass().getName() + ": " + e.getMessage());
        return false;
      }

      try {
        c.close();
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }

    System.out.println("Record created successfully");
    return true;
  }

  /**
   * Remove search results data in search table.
   * 
   * @param jdbc      the database
   * @param tableName the table name
   * @param searchId  user id +1 for search +2 for alternative
   * @return returns boolean
   * @throws SQLException exception
   */
  public static boolean removeSearch(DatabaseJdbc jdbc, String tableName,
      String searchId) throws SQLException {

    PreparedStatement stmt = null;
    Connection c = jdbc.createConnection();

    try {
      c.setAutoCommit(false);
      System.out.println("Opened database successfully");
      stmt = c.prepareStatement("PRAGMA foreign_keys = OFF;");

      stmt.executeUpdate();
      stmt.close();
      c.commit();
      c.close();
    } catch (Exception e) {
      if (stmt != null) {
        stmt.close();
      }
      c.close();
      System.err.println(e.getClass().getName() + ": " + e.getMessage());
      return false;
    }

    stmt = null;
    c = jdbc.createConnection();
    try {
      c.setAutoCommit(false);
      System.out.println("Opened database successfully");
      stmt = c.prepareStatement(
          "DELETE FROM " + tableName + " WHERE ID = \"" + searchId + "\";");

      stmt.executeUpdate();
      stmt.close();
      c.commit();
      c.close();
    } catch (Exception e) {
      if (stmt != null) {
        stmt.close();
      }
      c.close();
      System.err.println(e.getClass().getName() + ": " + e.getMessage());
      return false;
    }
    stmt = null;
    c = jdbc.createConnection();
    try {
      c.setAutoCommit(false);
      System.out.println("Opened database successfully");
      stmt = c.prepareStatement("PRAGMA foreign_keys = ON;");

      stmt.executeUpdate();
      stmt.close();
      c.commit();
      c.close();
    } catch (Exception e) {
      if (stmt != null) {
        stmt.close();
      }
      c.close();
      System.err.println(e.getClass().getName() + ": " + e.getMessage());
      return false;
    }

    System.out.println("Record created successfully");
    return true;
  }

  /**
   * Get search result from search table.
   * 
   * @param jdbc      the database
   * @param tableName the table name
   * @param itemTable item table name
   * @param id        the search id
   * @return items in the same search
   * @throws SQLException exception
   */
  public static List<Item> getSearch(DatabaseJdbc jdbc, String tableName,
      String itemTable, String id) throws SQLException {
    Statement stmt = null;
    ResultSet rs = null;
    Connection c = jdbc.createConnection();
    List<String> items = new ArrayList<String>();
    List<Double> lats = new ArrayList<Double>();
    List<Double> lons = new ArrayList<Double>();
    List<Item> result = new ArrayList<Item>();
    try {
      c.setAutoCommit(false);
      System.out.println("Opened database successfully for get");

      stmt = c.createStatement();
      rs = stmt.executeQuery(
          "SELECT * FROM " + tableName + " WHERE ID " + " = \"" + id + "\";");

      while (rs.next()) {
        items.add(rs.getString("ITEM_ID"));
        lats.add(rs.getDouble("LAT"));
        lons.add(rs.getDouble("LON"));
      }

      rs.close();
      stmt.close();
      c.close();

    } catch (Exception e) {
      if (stmt != null) {
        stmt.close();
      }
      if (rs != null) {
        rs.close();
      }
      c.close();
      System.err.println(e.getClass().getName() + ": " + e.getMessage());
      return result;
    }

    for (int i = 0; i < items.size(); i++) {
      stmt = null;
      rs = null;
      c = jdbc.createConnection();
      Item item = new Item();
      try {
        c.setAutoCommit(false);
        System.out.println("Opened database successfully for get");

        stmt = c.createStatement();
        rs = stmt.executeQuery("SELECT * FROM " + itemTable + " WHERE ID "
            + " = \"" + items.get(i) + "\" AND LAT = " + lats.get(i)
            + " AND LON = " + lons.get(i) + ";");

        if (rs.next()) {
          item.setBarcode(rs.getString("ID"));
          item.setName(rs.getString("name"));
          item.setPrice(rs.getDouble("price"));
          item.setStore(rs.getString("store"));
          item.setLat(rs.getDouble("lat"));
          item.setLon(rs.getDouble("lon"));
          item.setSku(rs.getString("SKU"));
          item.setImage(rs.getString("IMAGE"));
          item.setTcin(rs.getString("TCIN"));
          result.add(item);
        }

        rs.close();
        stmt.close();
        c.close();

      } catch (Exception e) {
        if (stmt != null) {
          stmt.close();
        }
        if (rs != null) {
          rs.close();
        }
        c.close();
        System.err.println(e.getClass().getName() + ": " + e.getMessage());
        return result;
      }
    }

    return result;
  }

  /**
   * updates login data to data table.
   * 
   * @param jdbc      the database
   * @param tableName the table name
   * @return returns boolean
   * @throws SQLException exception
   */
  public static boolean updatesLoginData(DatabaseJdbc jdbc, String tableName,
      String id) throws SQLException {
    PreparedStatement stmt = null;
    Connection c = jdbc.createConnection();

    try {
      c.setAutoCommit(false);
      System.out.println("Opened database successfully for User");

      stmt = c.prepareStatement(
          "UPDATE " + tableName + " SET ONLINE=1 WHERE USER_ID=?");
      stmt.setString(1, id);

      stmt.executeUpdate();
      stmt.close();
      c.commit();
    } catch (Exception e) {
      if (stmt != null) {
        stmt.close();
      }
      System.err.println(e.getClass().getName() + ": " + e.getMessage());
      return false;
    }

    try {
      c.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }

    System.out.println("Record created successfully");
    return true;
  }

  /**
   * updates logout user to data table.
   * 
   * @param jdbc      the database
   * @param tableName the table name
   * @param id        the user id
   * @return returns boolean
   * @throws SQLException exception
   */
  public static boolean logoutUser(DatabaseJdbc jdbc, String tableName,
      String id) throws SQLException {
    PreparedStatement stmt = null;
    Connection c = jdbc.createConnection();

    try {
      c.setAutoCommit(false);
      System.out.println("Opened database successfully for User");

      stmt = c.prepareStatement(
          "UPDATE " + tableName + " SET ONLINE=0 WHERE USER_ID=?");
      stmt.setString(1, id);

      stmt.executeUpdate();
      stmt.close();
      c.commit();
    } catch (Exception e) {
      if (stmt != null) {
        stmt.close();
      }
      System.err.println(e.getClass().getName() + ": " + e.getMessage());
      return false;
    }

    try {
      c.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return true;
  }

  /**
   * Creates an arraylist of player objects.
   * 
   * @param jdbc      the database
   * @param tableName the table name
   * @return returns the arraylist
   * @throws SQLException exception
   */
  public static List<User> getLoginData(DatabaseJdbc jdbc, String tableName)
      throws SQLException {
    Statement stmt = null;
    ResultSet rs = null;
    Connection c = jdbc.createConnection();
    List<User> loggedInList = new ArrayList<User>();

    try {
      c.setAutoCommit(false);
      System.out.println("Opened database successfully for getPlayerData");

      stmt = c.createStatement();
      rs = stmt
          .executeQuery("SELECT * FROM " + tableName + " WHERE ONLINE = 1;");

      while (rs.next()) {
        String id = rs.getString("user_id");
        String email = rs.getString("email");
        String name = rs.getString("name");
        double savings = rs.getDouble("savings");
        String location = rs.getString("location");

        String delims = "[ (),]+";
        String[] tokens = location.split(delims);
        double lat = Double.parseDouble(tokens[1]);
        double lon = Double.parseDouble(tokens[2]);

        User newUser = new User(id, email, name, savings, lat, lon);

        loggedInList.add(newUser);
      }

      rs.close();
      stmt.close();

    } catch (Exception e) {
      if (stmt != null) {
        stmt.close();
      }
      if (rs != null) {
        rs.close();
      }
      System.err.println(e.getClass().getName() + ": " + e.getMessage());
      return null;
    }

    try {
      c.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }

    System.out.println("Got logged in user successfully");
    return loggedInList;
  }

  /**
   * Gets purchase record for a user.
   * 
   * @param jdbc      database
   * @param tableName name of table
   * @throws SQLException exception
   */
  public static List<PurchaseRecord> getPurchaseData(DatabaseJdbc jdbc,
      String tableName, String userId) throws SQLException {
    Statement stmt = null;
    ResultSet rs = null;
    Connection c = jdbc.createConnection();
    List<PurchaseRecord> purchaseList = new ArrayList<PurchaseRecord>();

    try {
      c.setAutoCommit(false);
      System.out.println("Opened database successfully for getPurchaseData");

      stmt = c.createStatement();
      String sql = "SELECT * FROM " + tableName + " WHERE USER_ID = " + userId
          + ";";
      rs = stmt.executeQuery(sql);

      while (rs.next()) {
        PurchaseRecord record = new PurchaseRecord();
        record.setUserId(rs.getString("user_id"));
        record.setDate(rs.getString("date"));
        record.setItem(rs.getString("item_name"));
        record.setPrice(rs.getDouble("price"));
        record.setSaving(rs.getDouble("savings"));

        purchaseList.add(record);
      }

      rs.close();
      stmt.close();

    } catch (Exception e) {
      if (stmt != null) {
        stmt.close();
      }
      if (rs != null) {
        rs.close();
      }
      System.err.println(e.getClass().getName() + ": " + e.getMessage());
      return null;
    }

    try {
      c.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }

    System.out.println("Got purchase records");
    return purchaseList;
  }

  /**
   * Check if an entry already exists.
   * 
   * @param jdbc      the database
   * @param tableName the table name
   * @param id        id
   * @param key       check where key=id
   * @return True or False
   * @throws SQLException exception
   */
  public static boolean alreadyExists(DatabaseJdbc jdbc, String tableName,
      String id, String key) throws SQLException {
    Statement stmt = null;
    ResultSet rs = null;
    Connection c = jdbc.createConnection();
    boolean result = false;
    try {
      c.setAutoCommit(false);
      System.out.println("Opened database successfully for User");

      stmt = c.createStatement();
      rs = stmt.executeQuery("SELECT * FROM " + tableName + " WHERE " + key
          + " = \"" + id + "\";");

      if (rs.next()) {
        result = true;
      }

      rs.close();
      stmt.close();

    } catch (Exception e) {
      if (stmt != null) {
        stmt.close();
      }
      if (rs != null) {
        rs.close();
      }
      System.err.println(e.getClass().getName() + ": " + e.getMessage());
      return false;
    }

    try {
      c.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return result;
  }

  /**
   * Check if a store already exists. must do separately because pk is 2 columns
   * rows
   * 
   * @param jdbc      the database
   * @param tableName the table name
   * @param store     store object
   * @return True or False
   * @throws SQLException exception
   */
  public static boolean alreadyExistsStore(DatabaseJdbc jdbc, String tableName,
      Store store) throws SQLException {
    Statement stmt = null;
    ResultSet rs = null;
    Connection c = jdbc.createConnection();
    boolean result = false;
    try {
      c.setAutoCommit(false);
      System.out.println("Opened database successfully for User");

      stmt = c.createStatement();
      System.out.println("SELECT * FROM " + tableName + " WHERE NUMBER = "
          + store.getNumber() + " and TYPE = \"" + store.getType() + "\";");
      rs = stmt.executeQuery("SELECT * FROM " + tableName + " WHERE NUMBER = "
          + store.getNumber() + " and TYPE = \"" + store.getType() + "\";");

      if (rs.next()) {
        result = true;
      }

      rs.close();
      stmt.close();

    } catch (Exception e) {
      if (stmt != null) {
        stmt.close();
      }
      if (rs != null) {
        rs.close();
      }
      System.err.println(e.getClass().getName() + ": " + e.getMessage());
      return false;
    }

    try {
      c.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return result;
  }

  /**
   * Check if an item already exists. must do separately because pk is 3 columns
   * rows
   * 
   * @param jdbc      the database
   * @param tableName the table name
   * @param item      item object
   * @return True or False
   * @throws SQLException exception
   */
  public static boolean alreadyExistsItem(DatabaseJdbc jdbc, String tableName,
      Item item) throws SQLException {
    Statement stmt = null;
    ResultSet rs = null;
    Connection c = jdbc.createConnection();
    boolean result = false;
    try {
      c.setAutoCommit(false);
      System.out.println("Opened database successfully for User");

      stmt = c.createStatement();
      /*
       * System.out.println("SELECT * FROM " + tableName + " WHERE LAT = " +
       * item.getLat() + " and LON = " + item.getLon() + " and ID = \"" +
       * item.getBarcode() + "\";");
       */
      rs = stmt.executeQuery("SELECT * FROM " + tableName + " WHERE LAT = "
          + item.getLat() + " and LON = " + item.getLon() + " and ID = \""
          + item.getBarcode() + "\";");

      if (rs.next()) {
        result = true;
      }

      rs.close();
      stmt.close();

    } catch (Exception e) {
      if (stmt != null) {
        stmt.close();
      }
      if (rs != null) {
        rs.close();
      }
      System.err.println(e.getClass().getName() + ": " + e.getMessage());
      return false;
    }

    try {
      c.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return result;
  }

  /**
   * Return all stores given type.
   * 
   * @param jdbc      the database
   * @param tableName the table name
   * @param type      store type "Wegmans"
   * @return True or False
   * @throws SQLException exception
   */
  public static List<Store> getStore(DatabaseJdbc jdbc, String tableName,
      String type) throws SQLException {
    Statement stmt = null;
    ResultSet rs = null;
    Connection c = jdbc.createConnection();
    List<Store> result = new ArrayList<Store>();
    try {
      c.setAutoCommit(false);
      System.out.println("Opened database successfully for User");

      stmt = c.createStatement();
      // System.out.println("SELECT * FROM " + tableName + " WHERE TYPE = \"" +
      // type + "\";");
      rs = stmt.executeQuery(
          "SELECT * FROM " + tableName + " WHERE TYPE = \"" + type + "\";");

      while (rs.next()) {
        String name = rs.getString("NAME");
        String number = rs.getString("NUMBER");
        String lat = rs.getString("LAT");
        String lon = rs.getString("LON");

        Store current = new Store();
        current.setLat(Double.valueOf(lat));
        current.setLon(Double.valueOf(lon));
        current.setNumber(Integer.valueOf(number));
        current.setName(name);
        current.setType(type);
        result.add(current);
      }

      rs.close();
      stmt.close();

    } catch (Exception e) {
      if (stmt != null) {
        stmt.close();
      }
      if (rs != null) {
        rs.close();
      }
      System.err.println(e.getClass().getName() + ": " + e.getMessage());
      return result;
    }

    try {
      c.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return result;
  }

  /**
   * deletes the entries in the given table.
   * 
   * @param jdbc      the database
   * @param tableName the table name
   * @return returns boolean
   * @throws SQLException Exception
   */
  public static boolean deleteTable(DatabaseJdbc jdbc, String tableName)
      throws SQLException {
    Statement stmt = null;
    Connection c = jdbc.createConnection();

    try {
      c.setAutoCommit(false);
      System.out.println("Opened database successfully for deleteTable");

      stmt = c.createStatement();
      String sql = "DROP table " + tableName;

      stmt.executeUpdate(sql);
      c.commit();
      stmt.close();
    } catch (Exception e) {
      if (stmt != null) {
        stmt.close();
      }
      System.err.println(e.getClass().getName() + ": " + e.getMessage());
      return false;
    }

    try {
      c.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }

    System.out.println("Table deleted successfully");
    return true;
  }

  /**
   * Creates an arraylist of player objects.
   * 
   * @param jdbc      the database
   * @param tableName the table name
   * @param id        user id
   * @return returns the arraylist
   * @throws SQLException exception
   */
  public static User getUser(DatabaseJdbc jdbc, String tableName, String id)
      throws SQLException {
    Statement stmt = null;
    ResultSet rs = null;
    Connection c = jdbc.createConnection();
    User user = new User();
    try {
      c.setAutoCommit(false);

      stmt = c.createStatement();
      rs = stmt.executeQuery(
          "SELECT * FROM " + tableName + " WHERE USER_ID = \"" + id + "\";");

      if (rs.next()) {
        String email = rs.getString("email");
        String name = rs.getString("name");
        double savings = rs.getDouble("savings");
        String location = rs.getString("location");

        String delims = "[ (),]+";
        String[] tokens = location.split(delims);
        double lat = Double.parseDouble(tokens[1]);
        double lon = Double.parseDouble(tokens[2]);

        user = new User(id, email, name, savings, lat, lon);

      }

      rs.close();
      stmt.close();

    } catch (Exception e) {
      if (stmt != null) {
        stmt.close();
      }
      if (rs != null) {
        rs.close();
      }
      System.err.println(e.getClass().getName() + ": " + e.getMessage());
      return null;
    }

    try {
      c.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return user;
  }

  /**
   * updates user data. assumes exists.
   * 
   * @param jdbc      the database
   * @param tableName the table name
   * @param user      user object
   * @return returns boolean
   * @throws SQLException exception
   */
  public static boolean updatesUser(DatabaseJdbc jdbc, String tableName,
      User user) throws SQLException {
    PreparedStatement stmt = null;
    Connection c = jdbc.createConnection();

    try {
      c.setAutoCommit(false);

      stmt = c.prepareStatement("UPDATE " + tableName
          + " SET EMAIL=?, NAME=?,SAVINGS=?,LOCATION=? WHERE USER_ID=?");
      stmt.setString(1, user.getEmail());
      stmt.setString(2, user.getName());
      stmt.setString(3, String.valueOf(user.getSavings()));
      stmt.setString(4, "(" + String.valueOf(user.getLat()) + ","
          + String.valueOf(user.getLon()) + ")");
      stmt.setString(5, user.getUserId());

      stmt.executeUpdate();
      stmt.close();
      c.commit();
    } catch (Exception e) {
      if (stmt != null) {
        stmt.close();
      }
      System.err.println(e.getClass().getName() + ": " + e.getMessage());
      return false;
    }

    try {
      c.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return true;
  }

  /**
   * Returns the savings of a user within the last 7 days.
   * 
   * @param purchaseList purchase list
   * @return savings
   */
  public static double getWeekSavings(List<PurchaseRecord> purchaseList) {

    // Calendar currentCal = Calendar.getInstance();
    // Calendar weekCal = Calendar.getInstance();
    // weekCal.add(Calendar.DATE, -7);
    // SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
    double savings = 0;

    LocalDate currentDate = LocalDate.now();
    LocalDate weekDate = currentDate.minusDays(7);

    for (int i = 0; i < purchaseList.size(); i++) {
      PurchaseRecord record = purchaseList.get(i);
      // Date dateObj;

      LocalDate recordDate = LocalDate.parse(record.getDate());

      System.out.println("Current time: " + currentDate);
      System.out.println("recorded time: " + recordDate);
      System.out.println("week ago time: " + weekDate);
      //
      // System.out.println("before" + recordCal.before(currentCal.getTime()));
      // System.out.println("after" + recordCal.after(weekCal.getTime()));

      if (!recordDate.isAfter(currentDate) && recordDate.isAfter(weekDate)) {

        savings += record.getSaving();
        System.out.println("SAVINGS: " + savings);
      }

    }

    return savings;
  }

  /**
   * Return all stores given type with selection.
   * 
   * @param jdbc      the database
   * @param tableName the table name
   * @param type      store type "Walmart" "Trader Joes"
   * @param lat       user lat
   * @param lon       user lon
   * @return True or False
   * @throws SQLException exception
   */
  public static List<Store> getFilterStore(DatabaseJdbc jdbc, String tableName,
      String type, double lat, double lon) throws SQLException {
    Statement stmt = null;
    ResultSet rs = null;
    Connection c = jdbc.createConnection();
    List<Store> result = new ArrayList<Store>();
    try {
      c.setAutoCommit(false);
      System.out.println("Opened database successfully for User");

      stmt = c.createStatement();
      System.out.println("SELECT * FROM " + tableName + " WHERE TYPE = \""
          + type + "\" and lat < " + String.valueOf(lat + 1) + " and lat > "
          + String.valueOf(lat - 1) + " and lon < " + String.valueOf(lon + 1)
          + " and lon > " + String.valueOf(lon - 1) + ";");
      rs = stmt.executeQuery("SELECT * FROM " + tableName + " WHERE TYPE = \""
          + type + "\" and lat < " + String.valueOf(lat + 1) + " and lat > "
          + String.valueOf(lat - 1) + " and lon < " + String.valueOf(lon + 1)
          + " and lon > " + String.valueOf(lon - 1) + ";");

      while (rs.next()) {
        String name = rs.getString("NAME");
        String number = rs.getString("NUMBER");
        String lat1 = rs.getString("LAT");
        String lon1 = rs.getString("LON");

        Store current = new Store();
        current.setLat(Double.valueOf(lat1));
        current.setLon(Double.valueOf(lon1));
        current.setNumber(Integer.valueOf(number));
        current.setName(name);
        current.setType(type);
        result.add(current);
      }

      rs.close();
      stmt.close();

    } catch (Exception e) {
      if (stmt != null) {
        stmt.close();
      }
      if (rs != null) {
        rs.close();
      }
      System.err.println(e.getClass().getName() + ": " + e.getMessage());
      return result;
    }

    try {
      c.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return result;
  }

}
