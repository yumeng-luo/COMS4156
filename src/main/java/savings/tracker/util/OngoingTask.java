package savings.tracker.util;

public class OngoingTask {

  private int userId;
  private String taskStartTime;
  private String initialItem;
  private String alternativeItem;

  public int getUser_id() {
    return userId;
  }

  public void setUser_id(int userId) {
    this.userId = userId;
  }

  public String getTask_start_time() {
    return taskStartTime;
  }

  public void setTask_start_time(String taskStartTime) {
    this.taskStartTime = taskStartTime;
  }

  public String getInitial_item() {
    return initialItem;
  }

  public void setInitial_item(String initialItem) {
    this.initialItem = initialItem;
  }

  public String getAlternative_item() {
    return alternativeItem;
  }

  public void setAlternative_item(String alternativeItem) {
    this.alternativeItem = alternativeItem;
  }

}
