package savings.tracker.util;

import java.sql.SQLException;

public class User {
  private String userId;
  private String email;
  private String name;
  private double savings = 0;
  // location
  private double lat;
  private double lon;

  public User() {

  }

  /**
   * Construct user object from input.
   * 
   * @param id       the user id
   * @param email2   the user email
   * @param name2    the user name
   * @param savings2 the user saving
   * @param lat2     the user latitude
   * @param lon2     the user lontitude
   * @throws SQLException exception
   */
  public User(String id, String email2, String name2, double savings2,
      double lat2, double lon2) {
    userId = id;
    email = email2;
    name = name2;
    savings = savings2;
    lat = lat2;
    lon = lon2;
  }

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public double getSavings() {
    return savings;
  }

  public void setSavings(double savings) {
    this.savings = savings;
  }

  public double getLat() {
    return lat;
  }

  public void setLat(double lat) {
    this.lat = lat;
  }

  public double getLon() {
    return lon;
  }

  public void setLon(double lon) {
    this.lon = lon;
  }

}
