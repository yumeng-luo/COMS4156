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
        0, 0, emptyList, emptyItem, 0, 0);

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
   * Search Item by string. creates both search items and alternative items
   * search items are items with exact match alternative items are items with
   * some but not all match
   * 
   * @param item name of item
   * @param lat  of user
   * @param lon  of user
   * @throws SQLException Exception
   */
  @PostMapping("/search")
  @ResponseBody
  public List<Item> searchItem(
      @RequestParam(value = "item", defaultValue = "whole milk") String item,
      @AuthenticationPrincipal OAuth2User principal,
      @RequestParam(value = "lat", defaultValue = "37.7510") double lat,
      @RequestParam(value = "lon", defaultValue = "-97.8220") double lon) {
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
    // TODO implement this part after rapid api
    List<List<Item>> list = WegmanApi.getItems(database, "Store", item, lat,
        lon);
    // TODO test 2

    // save to ongoing task
    currentTask.setSearchItems(list.get(0));
    currentTask.setAlternativeItem(list.get(1));
    try {
      DatabaseJdbc.removeSearch(database, "Search", id + "1");
      DatabaseJdbc.removeSearch(database, "Search", id + "2");
      DatabaseJdbc.addSearch(database, "Search", "Item", list.get(0), id + "1");
      DatabaseJdbc.addSearch(database, "Search", "Item", list.get(1), id + "2");
      DatabaseJdbc.addTask(database, "Task", currentTask);
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    return list.get(0);
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
    task.setInitialLat(Double.valueOf(task.getInitialItem().getLat()));
    task.setInitialLon(Double.valueOf(task.getInitialItem().getLon()));

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
   * Search Item by string. creates both search items and alternative items
   * search items are items with exact match alternative items are items with
   * some but not all match
   * 
   * @param lat of user
   * @param lon of user
   * @throws SQLException Exception
   */
  @PostMapping("/alternatives")
  @ResponseBody
  public List<Item> searchAlternativeItem(
      @AuthenticationPrincipal OAuth2User principal,
      @RequestParam(value = "lat", defaultValue = "37.7510") double lat,
      @RequestParam(value = "lon", defaultValue = "-97.8220") double lon) {
    String id;
    if (principal != null) {
      id = principal.getAttribute("sub");
    } else {
      id = "105222900313734280075";
    }

    List<Item> result = new ArrayList<Item>();
    System.out.print(id);
    // get task info from table
    OngoingTask currentTask = new OngoingTask();
    try {
      currentTask = DatabaseJdbc.getTask(database, "Task", "Search", "Item",
          id);
    } catch (SQLException e1) {
      e1.printStackTrace();
    }

    // save to on going task
    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
    currentTask.setTaskStartTime(timestamp);

    // check if there are alternatives
    if (currentTask.getAlternativeItem().size() == 0) {
      // search for more item
      // TODO implement this part after rapid api
      result = WegmanApi.getAlternativeItems(database, "Store",
          currentTask.getSearchString(), lat, lon);

    } else {
      result = currentTask.getAlternativeItem();
    }

    // save to ongoing task
    currentTask.setAlternativeItem(result);
    try {
      DatabaseJdbc.removeSearch(database, "Search", id + "2");
      DatabaseJdbc.addSearch(database, "Search", "Item", result, id + "2");
      DatabaseJdbc.addTask(database, "Task", currentTask);
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    return result;
  }

  /**
   * Select Final Item.
   * 
   * @param barcode unique barcode/upc of item chosen
   * @param lat     of store loaction
   * @param lon     of store location
   * @throws SQLException Exception
   */
  @PostMapping("/select_purchase")
  @ResponseBody
  public Message selectPurchase(
      @RequestParam(value = "upc", defaultValue = "0") String barcode,
      @RequestParam(value = "lat", defaultValue = "37.7510") String lat,
      @RequestParam(value = "lon", defaultValue = "-97.8220") String lon,
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
      finalItem = DatabaseJdbc.getItem(database, "Item", barcode,
          Double.valueOf(lat), Double.valueOf(lon));
    } catch (SQLException e1) {
      e1.printStackTrace();
    }

    // ongoing task
    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
    task.setTaskStartTime(timestamp);
    task.setFinalItem(finalItem);
    task.setFinalLat(Double.valueOf(lat));
    task.setFinalLon(Double.valueOf(lon));

    // save to ongoing task
    try {
      DatabaseJdbc.addTask(database, "Task", task);
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return new Message(200, task.getFinalItem().getName());
  }

}
