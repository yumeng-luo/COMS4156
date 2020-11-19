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
  private String image;

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
  
  /**
   * Construct from existing item. make a copy
   * 
   */
  public Item(Item item__) {
    super();
    this.name = item__.getName();
    this.barcode = item__.getBarcode();
    this.price = item__.getPrice();
    this.store = item__.getStore();
    this.lat = item__.getLat();
    this.lon = item__.getLon();
    this.tcin = item__.getTcin();
    this.sku = item__.getSku();
    this.image = item__.getImage();
  }

  public Item() {

  }

  /**
   * Construct from input.
   * 
   */
  public Item(String name, String barcode, double price, String store, double lat,
      double lon, String tcin, String sku, String image) {
    this.name = name;
    this.barcode = barcode;
    this.price = price;
    this.store = store;
    this.lat = lat;
    this.lon = lon;
    this.tcin = tcin;
    this.sku = sku;
    this.image = image;
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

  public String getImage() {
    return image;
  }

  public void setImage(String image) {
    this.image = image;
  }
  
}
