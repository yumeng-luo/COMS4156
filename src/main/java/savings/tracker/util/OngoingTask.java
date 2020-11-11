package savings.tracker.util;

public class OngoingTask {

  private int user_id;
  private String task_start_time;
  private String initial_item;
  private String alternative_item;
  public int getUser_id() {
    return user_id;
  }
  public void setUser_id(int user_id) {
    this.user_id = user_id;
  }
  public String getTask_start_time() {
    return task_start_time;
  }
  public void setTask_start_time(String task_start_time) {
    this.task_start_time = task_start_time;
  }
  public String getInitial_item() {
    return initial_item;
  }
  public void setInitial_item(String initial_item) {
    this.initial_item = initial_item;
  }
  public String getAlternative_item() {
    return alternative_item;
  }
  public void setAlternative_item(String alternative_item) {
    this.alternative_item = alternative_item;
  }
  
}
