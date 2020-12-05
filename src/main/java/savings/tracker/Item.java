package savings.tracker;

public class Item {
  private String name;
  private String barcode;
  private double price;
  private String store;
  private double lat;
  private double lon;
  private String sku;
  @SuppressWarnings("unused")
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
  public Item(Item item2) {
    super();
    this.name = item2.getName();
    this.barcode = item2.getBarcode();
    this.price = item2.getPrice();
    this.store = item2.getStore();
    this.lat = item2.getLat();
    this.lon = item2.getLon();
    this.tcin = item2.getTcin();
    this.sku = item2.getSku();
    this.image = item2.getImage();
  }

  public Item() {

  }

  /**
   * Construct from input.
   * 
   */
  public Item(String name, String barcode, double price, String store,
      double lat, double lon, String tcin, String sku, String image) {
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
