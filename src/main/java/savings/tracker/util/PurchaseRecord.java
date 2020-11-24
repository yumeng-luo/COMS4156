package savings.tracker.util;

public class PurchaseRecord {

  private String userId;
  private String date;
  private String item;
  private double price;
  private double saving;

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public String getDate() {
    return date;
  }

  public void setDate(String date) {
    this.date = date;
  }

  public String getItem() {
    return item;
  }

  public void setItem(String item) {
    this.item = item;
  }

  public double getPrice() {
    return price;
  }

  public void setPrice(double price) {
    this.price = price;
  }

  public double getSaving() {
    return saving;
  }

  public void setSaving(double saving) {
    this.saving = saving;
  }
}
