package savings.tracker.util;

public class Item {
  private String name;
  private String barcode;
  private double price;
  private String store;
  private double lat;
  private double lon;
  private String sku;
  private String tcin;

  /**
   * Construct from input.
   * 
   */
  public Item(String name, String barcode, double price, String store,
      double lat, double lon) {
    super();
    this.name = name;
    this.barcode = barcode;
    this.price = price;
    this.store = store;
    this.lat = lat;
    this.lon = lon;
  }

  public Item() {

  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getTcin() {
    return barcode;
  }

  public void setTcin(String tcin) {
    this.barcode = tcin;
  }

  public double getPrice() {
    return price;
  }

  public void setPrice(double price) {
    this.price = price;
  }

  public String getStore() {
    return store;
  }

  public void setStore(String store) {
    this.store = store;
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

  public String getBarcode() {
    return barcode;
  }

  public void setBarcode(String barcode) {
    this.barcode = barcode;
  }

  public String getSku() {
    return sku;
  }

  public void setSku(String sku) {
    this.sku = sku;
  }
}
