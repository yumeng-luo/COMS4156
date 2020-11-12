package savings.tracker;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import savings.tracker.util.DatabaseJdbc;
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
    DatabaseJdbc.createLoginTable(jdbc,"UserTest");

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
      DatabaseJdbc.createLoginTable(jdbc,"UserTest");
      DatabaseJdbc.deleteLoginTable(jdbc,"UserTest");
    } catch (SQLException e1) {
      // TODO Auto-generated catch block
      e1.printStackTrace();
    }
    Connection c = jdbc.createConnection();
    Statement stmt = null;

    try {
      c.setAutoCommit(false);
      stmt = c.createStatement();
      String sql = "CREATE TABLE User (USER_ID text PRIMARY KEY, EMAIL TEXT, NAME TEXT,SAVINGS FLOAT,LOCATION POINT,ONLINE BOOLEAN DEFAULT FALSE)";
      stmt.executeUpdate(sql);
      stmt.close();
      c.commit();
      c.close();
      assertEquals(1, 0);
    } catch (Exception e) {
      // correct
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
    User user = new User("123","123@123.com","ABC DEF",145.6,1.361, 51.3267);
    try {
      DatabaseJdbc.deleteLoginTable(jdbc,"UserTest");
      DatabaseJdbc.createLoginTable(jdbc,"UserTest");
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
      String sql = "SELECT * FROM User WHERE USER_ID = 123;";
      ResultSet rs = stmt.executeQuery(sql);
      while (rs.next()) {
        String user_id = rs.getString("USER_ID");
        assertEquals(user_id, "123");
        
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
    User user1 = new User("345","345@123.com","qwe FKH",1000,79.3803, 51.3267);
    User user2 = new User("789","789@123.com","hij kl",5,-0.4632, 51.3552);
    List<User> users;
    try {
      DatabaseJdbc.deleteLoginTable(jdbc,"UserTest");
      DatabaseJdbc.createLoginTable(jdbc,"UserTest");
      DatabaseJdbc.addLoginData(jdbc, "UserTest", user1);
      DatabaseJdbc.addLoginData(jdbc, "UserTest", user2);
      users = DatabaseJdbc.getLoginData(jdbc,"UserTest");
      assertEquals(users.size(), 2);
      assertEquals(users.get(0).getEmail(), user1.getEmail());
      assertEquals(users.get(0).getUser_id(), user1.getUser_id());
      assertEquals(users.get(0).getName(), user1.getName());
      assertEquals(users.get(0).getSavings(), user1.getSavings());
      assertEquals(users.get(0).getLat(), user1.getLat());
      assertEquals(users.get(0).getLon(), user1.getLon());
      assertEquals(users.get(1).getEmail(), user2.getEmail());
      assertEquals(users.get(1).getUser_id(), user2.getUser_id());
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
  public void TestAlreadyExists() {
    System.out.println("========TESTING AlreadyExists ========");
    User user1 = new User("56789","345@123.com","qwe FKH",1000,79.3803, 51.3267);
    try {
      DatabaseJdbc.deleteLoginTable(jdbc,"UserTest");
      DatabaseJdbc.createLoginTable(jdbc,"UserTest");
      boolean result = DatabaseJdbc.AlreadyExists(jdbc, "UserTest", "56789");
      assertEquals(result, false);
      
      DatabaseJdbc.addLoginData(jdbc, "UserTest", user1);
      result = DatabaseJdbc.AlreadyExists(jdbc, "UserTest", "56789");
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
  public void TestUpdateLogin() {
    System.out.println("========TESTING Update login ========");
    User user1 = new User("56789","345@123.com","qwe FKH",1000,79.3803, 51.3267);
    try {
      DatabaseJdbc.deleteLoginTable(jdbc,"UserTest");
      DatabaseJdbc.createLoginTable(jdbc,"UserTest");
      DatabaseJdbc.addLoginData(jdbc, "UserTest", user1);
      DatabaseJdbc.updatesLoginData(jdbc, "UserTest", user1);
      
      
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

  }
}


