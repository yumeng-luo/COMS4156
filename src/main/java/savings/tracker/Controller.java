package savings.tracker;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import savings.tracker.util.DatabaseJdbc;
import savings.tracker.util.Item;
import savings.tracker.util.Message;
import savings.tracker.util.OngoingTask;
import savings.tracker.util.User;

@RestController
public class Controller {

  private static DatabaseJdbc database = new DatabaseJdbc();

  @RequestMapping("/frontend")
  public String index() {
    return "Placeholder for frontend";
  }

  /**
   * Create user object and register into DB.
   * 
   * @param principal OAuth2User
   * @throws SQLException Exception
   */
  @GetMapping("/user")
  public Map<String, Object> user(
      @AuthenticationPrincipal OAuth2User principal) {
    try {
      System.out.print(principal);
      String email = principal.getAttribute("email");
      String name = principal.getAttribute("name");
      String id = principal.getAttribute("sub");

      User currentUser = new User(id, email, name, 0, 0, 0);

      if (DatabaseJdbc.alreadyExists(database, "User", currentUser.getUserId(),
          "user_id")) {
        System.out.print(name + " Already exists");
        DatabaseJdbc.updatesLoginData(database, "User", id);
      } else {
        System.out.print("Adding " + name);
        DatabaseJdbc.addLoginData(database, "User", currentUser);
      }

    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    System.out.print(principal);

    return Collections.singletonMap("name", principal.getAttribute("name"));
  }

  /**
   * No Alternative Found.
   * 
   * @throws SQLException Exception
   */
  @GetMapping("/no_alternative")
  public Message noAlternative(@AuthenticationPrincipal OAuth2User principal) {
    String id;
    if (principal != null) {
      id = principal.getAttribute("sub");
    } else {
      id = "105222900313734280075";
    }
    System.out.print(principal);
    // delete ongoing task
    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
    List<Item> emptyList = new ArrayList<Item>();
    Item emptyItem = new Item();
    OngoingTask task = new OngoingTask(id, "", timestamp, emptyList, emptyItem,
        emptyList, emptyItem);
    try {
      DatabaseJdbc.addTask(database, "Task", task);
      DatabaseJdbc.removeSearch(database, "Search", id + "1");
      DatabaseJdbc.removeSearch(database, "Search", id + "2");
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return new Message(200, "No Alternative");
  }

  /**
   * Search Item by string.
   * 
   * @param item name of item
   * @throws SQLException Exception
   */
  @PostMapping("/search")
  @ResponseBody
  public List<Item> searchItem(
      @RequestParam(value = "item", defaultValue = "coffee") String item,
      // @RequestBody String item,
      @AuthenticationPrincipal OAuth2User principal) {
    String id;
    if (principal != null) {
      id = principal.getAttribute("sub");
    } else {
      id = "105222900313734280075";
    }

    System.out.print(id);
    System.out.print(item);
    // save to on going task
    OngoingTask currentTask = new OngoingTask();
    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
    currentTask.setTaskStartTime(timestamp);
    currentTask.setUserId(id);
    currentTask.setSearchString(item);
    try {
      DatabaseJdbc.addTask(database, "Task", currentTask);
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    // search for item
    double lat1 = 45;
    double lon1 = 23;
    // TODO implement this part after rapid api
    List<Item> list = WegmanApi.getItems(database, "Store", item, lat1, lon1);
    /*
     * double price1 = 5.39; double lat1 = 45; double lon1 = 23; double price2 =
     * 3.29; double lat2 = 54; double lon2 = 13; double price3 = 1.39; double
     * lat3 = 4; double lon3 = 17; Item item1 = new Item("Good Coffee",
     * "123456789", price1, "McDonalds", lat1, lon1); Item item2 = new
     * Item("Okay Coffee", "111111111", price2, "Tim Hortons", lat2, lon2); Item
     * item3 = new Item("Bad Coffee", "555555555", price3, "Walmart", lat3,
     * lon3);
     * 
     * 
     * List<Item> list = new ArrayList<Item>(); list.add(item1);
     * list.add(item2); list.add(item3);
     */
    // save to ongoing task
    currentTask.setSearchItems(list);
    try {
      DatabaseJdbc.removeSearch(database, "Search", id + "1");
      DatabaseJdbc.removeSearch(database, "Search", id + "2");
      DatabaseJdbc.addSearch(database, "Search", "Item", list, id + "1");
      DatabaseJdbc.addTask(database, "Task", currentTask);
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    return list;
  }

  /**
   * Select Item.
   * 
   * @param itemNumber number of item chosen
   * @throws SQLException Exception
   */
  @PostMapping("/select_item")
  @ResponseBody
  public Message selectItem(
      @RequestParam(value = "item_number", defaultValue = "0") int itemNumber,
      @AuthenticationPrincipal OAuth2User principal) {
    String id;
    if (principal != null) {
      id = principal.getAttribute("sub");
    } else {
      id = "105222900313734280075";
    }
    System.out.print(id);
    OngoingTask task = new OngoingTask();
    try {
      task = DatabaseJdbc.getTask(database, "Task", "Search", "Item", id);
    } catch (SQLException e1) {
      e1.printStackTrace();
    }

    // ongoing task
    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
    task.setTaskStartTime(timestamp);
    List<Item> itemList = task.getSearchItems();
    task.setInitialItem(itemList.get(itemNumber));

    // save to ongoing task
    try {
      DatabaseJdbc.removeSearch(database, "Search", id + "2");
      DatabaseJdbc.addTask(database, "Task", task);
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return new Message(200, task.getInitialItem().getName());
  }

  /**
   * Select Final Item.
   * 
   * @param barcode unique barcode/upc of item chosen
   * @throws SQLException Exception
   */
  @PostMapping("/select_purchase")
  @ResponseBody
  public Message selectPurchase(
      @RequestParam(value = "upc", defaultValue = "0") String barcode,
      @AuthenticationPrincipal OAuth2User principal) {
    String id;
    if (principal != null) {
      id = principal.getAttribute("sub");
    } else {
      id = "105222900313734280075";
    }
    System.out.print(id);
    OngoingTask task = new OngoingTask();
    Item finalItem = new Item();
    try {
      task = DatabaseJdbc.getTask(database, "Task", "Search", "Item", id);
      finalItem = DatabaseJdbc.getItem(database, "Item", barcode);
    } catch (SQLException e1) {
      e1.printStackTrace();
    }

    // ongoing task
    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
    task.setTaskStartTime(timestamp);
    // in case we use row number to access items
    // List<Item> itemList = task.getSearchItems();
    // List<Item> itemList2 = task.getAlternativeItem();
    task.setFinalItem(finalItem);

    // save to ongoing task
    try {
      DatabaseJdbc.addTask(database, "Task", task);
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return new Message(200, task.getFinalItem().getName());
  }

}
