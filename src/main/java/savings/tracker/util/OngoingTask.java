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
  private List<Item> alternativeItem;
  private Item finalItem;

  /**
   * Construct from input.
   * 
   */
  public OngoingTask(String userId, String searchString,
      Timestamp taskStartTime, List<Item> searchItems, Item initialItem,
      List<Item> alternativeItem, Item finalItem) {
    super();
    this.userId = userId;
    this.searchString = searchString;
    this.taskStartTime = taskStartTime;
    this.searchItems = searchItems;
    this.initialItem = initialItem;
    this.alternativeItem = alternativeItem;
    this.finalItem = finalItem;
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
    this.alternativeItem = new ArrayList<Item>();
    this.finalItem = new Item();
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

}
