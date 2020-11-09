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
  public boolean createLoginTable(DatabaseJdbc jdbc, String tableName) throws SQLException {
    PreparedStatement stmt = null;
    Connection c = jdbc.createConnection();
    
    try {
      String sql = String.format("CREATE TABLE IF NOT EXISTS %s (%s, %s)", tableName, 
                                  "USER_ID TEXT", "USER_PASS TEXT");
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
  public boolean addLoginData(DatabaseJdbc jdbc, String tableName, Player player) //****************************************update
                                throws SQLException {
    PreparedStatement stmt = null;
    Connection c = jdbc.createConnection();
    
    try {
      c.setAutoCommit(false);
      System.out.println("Opened database successfully for addPlayerData");
      
      stmt = c.prepareStatement("INSERT INTO " + tableName + " values(?,?)");
      stmt.setString(1, player.getId());  
      stmt.setString(2, String.valueOf(player.getType())); 

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
  public List<Player> getLoginData(DatabaseJdbc jdbc, String tableName) throws SQLException { //****************************************update
    Statement stmt = null;
    ResultSet rs = null;
    Connection c = jdbc.createConnection();
    List<Player> moveList = new ArrayList<Player>();
    
    try {
      c.setAutoCommit(false);
      System.out.println("Opened database successfully for getPlayerData");

      stmt = c.createStatement();
      rs = stmt.executeQuery("SELECT * FROM " + tableName + ";");
     
      while (rs.next()) { 
        int id = rs.getInt("player_id");
        char type = rs.getString("player_type").charAt(0);

        
        Player newPlayer = new Player(type, id);
        
        moveList.add(newPlayer);
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
    
    System.out.println("Got move data successfully");
    return moveList;
  }
  
  /**
   * deletes the entries in the player table.
   * @param jdbc the database
   * @param tableName the table name
   * @return returns boolean
   * @throws SQLException Exception
   */
  public boolean deleteLoginTable(DatabaseJdbc jdbc, String tableName) throws SQLException {
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
