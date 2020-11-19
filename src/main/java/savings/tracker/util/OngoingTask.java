package savings.tracker.util;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class OngoingTask {

  private String userId;
  private String searchString;
  private Timestamp taskStartTime;
  private List<Item> searchItems;
  private Item initialItem;
  private double initialLat;
  private double initialLon;
  private List<Item> alternativeItem;
  private Item finalItem;
  private double finalLat;
  private double finalLon;

  /**
   * Construct from input.
   * 
   */
  public OngoingTask(String userId, String searchString,
      Timestamp taskStartTime, List<Item> searchItems, Item initialItem,
      double initialLat, double initialLon, List<Item> alternativeItem,
      Item finalItem, double finalLat, double finalLon) {
    super();
    this.userId = userId;
    this.searchString = searchString;
    this.taskStartTime = taskStartTime;
    this.searchItems = searchItems;
    this.initialItem = initialItem;
    this.initialLat = initialLat;
    this.initialLon = initialLon;
    this.alternativeItem = alternativeItem;
    this.finalItem = finalItem;
    this.finalLat = finalLat;
    this.finalLon = finalLon;
  }


  /**
   * Construct from blank.
   * 
   */
  public OngoingTask() {
    this.userId = "";
    this.searchString = "";
    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
    this.taskStartTime = timestamp;
    this.searchItems = new ArrayList<Item>();
    this.initialItem = new Item();
    this.initialLat = 0;
    this.initialLon = 0;
    this.alternativeItem = new ArrayList<Item>();
    this.finalItem = new Item();
    this.finalLat = 0;
    this.finalLon = 0;
  }

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public Timestamp getTaskStartTime() {
    return taskStartTime;
  }

  public void setTaskStartTime(Timestamp taskStartTime) {
    this.taskStartTime = taskStartTime;
  }

  public String getSearchString() {
    return searchString;
  }

  public void setSearchString(String searchString) {
    this.searchString = searchString;
  }

  public List<Item> getSearchItems() {
    return searchItems;
  }

  public void setSearchItems(List<Item> searchItems) {
    this.searchItems = searchItems;
  }

  public Item getInitialItem() {
    return initialItem;
  }

  public void setInitialItem(Item initialItem) {
    this.initialItem = initialItem;
  }

  public List<Item> getAlternativeItem() {
    return alternativeItem;
  }

  public void setAlternativeItem(List<Item> alternativeItem) {
    this.alternativeItem = alternativeItem;
  }

  public Item getFinalItem() {
    return finalItem;
  }

  public void setFinalItem(Item finalItem) {
    this.finalItem = finalItem;
  }

  public double getInitialLat() {
    return initialLat;
  }

  public void setInitialLat(double initialLat) {
    this.initialLat = initialLat;
  }

  public double getInitialLon() {
    return initialLon;
  }

  public void setInitialLon(double initialLon) {
    this.initialLon = initialLon;
  }

  public double getFinalLat() {
    return finalLat;
  }

  public void setFinalLat(double finalLat) {
    this.finalLat = finalLat;
  }

  public double getFinalLon() {
    return finalLon;
  }

  public void setFinalLon(double finalLon) {
    this.finalLon = finalLon;
  }

}
