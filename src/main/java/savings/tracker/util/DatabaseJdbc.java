package savings.tracker.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.sqlite.JDBC;

public class DatabaseJdbc {

  public static void main(String[] args) {
     
  }
  
  /**
   * Creates a connection with database.
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
   * @param jdbc database
   * @param tableName the table name
   * @return returns boolean 
   * @throws SQLException Exception
   */
  public static boolean createLoginTable(DatabaseJdbc jdbc, String tableName) throws SQLException {
    PreparedStatement stmt = null;
    Connection c = jdbc.createConnection();
    
    try {
      String sql = String.format("CREATE TABLE IF NOT EXISTS %s (%s, %s, %s, %s, %s, %s)", tableName, 
                                  "USER_ID text PRIMARY KEY", "EMAIL TEXT","NAME TEXT","SAVINGS FLOAT", "LOCATION POINT", "ONLINE BOOLEAN DEFAULT FALSE");
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
   * @param jdbc the database
   * @param tableName the table name
   * @param player the player object
   * @return returns boolean
   * @throws SQLException exception
   */
  public static boolean addLoginData(DatabaseJdbc jdbc, String tableName, User user) //****************************************update
                                throws SQLException {
    PreparedStatement stmt = null;
    Connection c = jdbc.createConnection();
    
    try {
      c.setAutoCommit(false);
      System.out.println("Opened database successfully for addPlayerData");
      
      stmt = c.prepareStatement("INSERT INTO " + tableName + " values(?,?,?,?,?,?)");
      stmt.setString(1, user.getUser_id());  
      stmt.setString(2, user.getEmail()); 
      stmt.setString(3, user.getName()); 
      stmt.setString(4, String.valueOf(user.getSavings())); 
      stmt.setString(5, "(" + String.valueOf(user.getLat())+","+String.valueOf(user.getLon())+")"); 
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
   * updates login data to data table.
   * @param jdbc the database
   * @param tableName the table name
   * @param player the player object
   * @return returns boolean
   * @throws SQLException exception
   */
  public static boolean updatesLoginData(DatabaseJdbc jdbc, String tableName, User user) //****************************************update
                                throws SQLException {
    PreparedStatement stmt = null;
    Connection c = jdbc.createConnection();
    
    try {
      c.setAutoCommit(false);
      System.out.println("Opened database successfully for User");
      
      stmt = c.prepareStatement("UPDATE " + tableName + " SET ONLINE=1 WHERE USER_ID=?");
      stmt.setString(1, user.getUser_id());  
      
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
   * Creates an arraylist of player objects.
   * @param jdbc the database
   * @param tableName the table name
   * @return returns the arraylist
   * @throws SQLException exception
   */
  public static List<User> getLoginData(DatabaseJdbc jdbc, String tableName) throws SQLException { //****************************************update
    Statement stmt = null;
    ResultSet rs = null;
    Connection c = jdbc.createConnection();
    List<User> loggedInList = new ArrayList<User>();
    
    try {
      c.setAutoCommit(false);
      System.out.println("Opened database successfully for getPlayerData");

      stmt = c.createStatement();
      rs = stmt.executeQuery("SELECT * FROM " + tableName + " WHERE ONLINE = 1;");
     
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

        
        User newUser = new User(id, email,name,savings,lat,lon);
        
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
   * Check if a user already exists
   * @param jdbc the database
   * @param tableName the table name
   * @param user id
   * @return True or False
   * @throws SQLException exception
   */
  public static boolean AlreadyExists(DatabaseJdbc jdbc, String tableName, String user_id) throws SQLException { //****************************************update
    Statement stmt = null;
    ResultSet rs = null;
    Connection c = jdbc.createConnection();
    boolean result =false;
    try {
      c.setAutoCommit(false);
      System.out.println("Opened database successfully for User");

      stmt = c.createStatement();
      rs = stmt.executeQuery("SELECT * FROM " + tableName + " WHERE USER_ID = \""+ user_id +"\";");
     
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
   * deletes the entries in the player table.
   * @param jdbc the database
   * @param tableName the table name
   * @return returns boolean
   * @throws SQLException Exception
   */
  public static boolean deleteLoginTable(DatabaseJdbc jdbc, String tableName) throws SQLException {
    Statement stmt = null;
    Connection c = jdbc.createConnection();
    
    try {
      c.setAutoCommit(false);
      System.out.println("Opened database successfully for deletePlayerTable");
      
      stmt = c.createStatement();
      String sql = "DELETE from " + tableName;
      
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
    
    System.out.println("Player table deleted successfully");
    return true;
  }
}
