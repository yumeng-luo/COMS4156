package savings.tracker.util;

public class PurchaseRecord {

  private int user_id;
  private String date;
  private String item;
  private float price;
  private float saving;
  public int getUser_id() {
    return user_id;
  }
  public void setUser_id(int user_id) {
    this.user_id = user_id;
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
