package savings.tracker;

public class Store {
  private String name; // "Milpitas Square"
  private String type; // "Safeway"
  private int number; // 1,2,3 unique to type
  private double lat;
  private double lon;

  public Store() {

  }

  /**
   * Construct from input.
   * 
   */
  public Store(String name, String type, int number, double lat, double lon) {
    super();
    this.name = name;
    this.type = type;
    this.number = number;
    this.lat = lat;
    this.lon = lon;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public int getNumber() {
    return number;
  }

  public void setNumber(int number) {
    this.number = number;
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
