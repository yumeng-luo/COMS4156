package savings.tracker.util;

public class PurchaseRecord {

  private int userId;
  private String date;
  private String item;
  private float price;
  private float saving;

  public int getUserId() {
    return userId;
  }

  public void setUserId(int userId) {
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

  public float getPrice() {
    return price;
  }

  public void setPrice(float price) {
    this.price = price;
  }

  public float getSaving() {
    return saving;
  }

  public void setSaving(float saving) {
    this.saving = saving;
  }
}
