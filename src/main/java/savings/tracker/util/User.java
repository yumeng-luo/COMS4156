package savings.tracker.util;

public class User {
  private int user_id;
  private String email;
  private String name;
  private double savings = 0;
  // location
  private double lat;
  private double lon;
  
  public User(int id, String email2, String name2, double savings2, double lat2, double lon2) {
    user_id = id;
    email = email2;
    name = name2;
    savings = savings2;
    lat = lat2;
    lon = lon2;
  }
  public int getUser_id() {
    return user_id;
  }
  public void setUser_id(int user_id) {
    this.user_id = user_id;
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
