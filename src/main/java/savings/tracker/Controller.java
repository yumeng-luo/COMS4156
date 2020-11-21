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
    /**
     * TODO: test if multiple /search calls can be called with changing item name.
     * Currently, the endpoint is reusing the first search item for all subsequent /search calls.
     * Also, reduce database operations (taking too long on the frontend).
     */
    String id;
    if (principal != null) {
      id = principal.getAttribute("sub");
    } else {
      id = "105222900313734280075";
    }
    
    System.out.println("/search called->item: " + item);

    // updates user location data
    try {
      User user = DatabaseJdbc.getUser(database, "User", id);
      user.setLat(lat);
      user.setLon(lon);
      DatabaseJdbc.updatesUser(database, "User", user);
    } catch (SQLException e) {
      e.printStackTrace();
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

    // save to ongoing task
    currentTask.setInitialItem(new Item());
    currentTask.setInitialLat(0);
    currentTask.setInitialLon(0);
    currentTask.setFinalItem(new Item());
    currentTask.setFinalLat(0);
    currentTask.setFinalLon(0);
    List<List<Item>> list = WegmanApi.getItems(database, "Store", item, lat,
        lon);
    if (list.size() == 0) {
      currentTask.setSearchItems(new ArrayList<Item>());
      currentTask.setAlternativeItem(new ArrayList<Item>());
    } else {
      currentTask.setSearchItems(list.get(0));
      currentTask.setAlternativeItem(list.get(1));
    }
    try {
      DatabaseJdbc.removeSearch(database, "Search", id + "1");
      DatabaseJdbc.removeSearch(database, "Search", id + "2");
      DatabaseJdbc.addSearch(database, "Search", "Item",
          currentTask.getSearchItems(), id + "1");
      DatabaseJdbc.addSearch(database, "Search", "Item",
          currentTask.getAlternativeItem(), id + "2");
      DatabaseJdbc.addTask(database, "Task", currentTask);
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    return currentTask.getSearchItems();
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
      // DatabaseJdbc.removeSearch(database, "Search", id + "2");
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
      @RequestParam(value = "lon", defaultValue = "-97.8220") double lon,
      @RequestParam(value = "CHEAPER", defaultValue = "false") boolean cheaper,
      @RequestParam(value = "CLOSER", defaultValue = "false") boolean closer,
      @RequestParam(value = "SAME", defaultValue = "false") boolean same) {

    // chaneg switches accordingly
    WegmanApi.setMustbecheaper(cheaper);
    WegmanApi.setMustbecloser(closer);
    WegmanApi.setMustbesameitem(same);

    String id;
    if (principal != null) {
      id = principal.getAttribute("sub");
    } else {
      id = "105222900313734280075";
    }

    List<Item> result = new ArrayList<Item>();
    
    // temporary dummy return value
    List<Item> dummy_result = new ArrayList<Item>();
    Item item1 = new Item();
    item1.setName("name1");
    item1.setBarcode("barcode1");
    item1.setPrice(99.99);
    item1.setStore("store1");
    item1.setLat(100);
    item1.setLon(100);
    
    Item item2 = new Item();
    item2.setName("name2");
    item2.setBarcode("barcode2");
    item2.setPrice(299.99);
    item2.setStore("store2");
    item2.setLat(200);
    item2.setLon(200);
    dummy_result.add(item1);
    dummy_result.add(item2);
    
    System.out.print(id);
    // get task info from table
    OngoingTask currentTask = new OngoingTask();
    try {
      currentTask = DatabaseJdbc.getTask(database, "Task", "Search", "Item",
          id);
    } catch (SQLException e1) {
      e1.printStackTrace();
    }
//    dummy_result = currentTask.getAlternativeItem();

    // save to on going task
    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
    currentTask.setTaskStartTime(timestamp);
    if (currentTask.getInitialItem().getBarcode() == null) {
      return result;
    }
    currentTask.setFinalItem(new Item());
    currentTask.setFinalLat(0);
    currentTask.setFinalLon(0);

    result = filterAlternativeItem(lat, lon, currentTask.getAlternativeItem(),
        currentTask.getInitialItem());
    if (result.size() <= 10) {
      // search for more item
      // TODO implement this part after rapid api
      result = WegmanApi.getAlternativeItems(database, "Store",
          currentTask.getSearchString(), lat, lon,
          currentTask.getInitialItem().getPrice());

    }
    result = filterAlternativeItem(lat, lon, result,
        currentTask.getInitialItem());

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
    return dummy_result;
//    return result;
  }

  /**
   * Filter Alternatives based on toggle switch.
   * 
   * @param lat          of user
   * @param lon          of user
   * @param alternatives list of alternatives
   * @param initialItem  initial chosen item
   * @throws SQLException Exception
   */
  public List<Item> filterAlternativeItem(double lat, double lon,
      List<Item> alternatives, Item initialItem) {

    List<Item> result = new ArrayList<Item>();

    // check if there are alternatives
    if (alternatives.size() != 0) {
      // deep copy
      // non empty alternatives, check if there is alt requirement
      for (int i = 0; i < alternatives.size(); i++) {
        if (WegmanApi.isMustbecheaper() == false
            || alternatives.get(i).getPrice() <= initialItem.getPrice()) {
          // no cheaper requirement or yes cheaper requirement but cheaper

          // check distance reuirement
          double lat1 = alternatives.get(i).getLat(); // alt
          double lon1 = alternatives.get(i).getLon();
          double lat2 = initialItem.getLat(); // initial
          double lon2 = initialItem.getLon();
          // lat lon user
          double distance1 = WegmanApi.getDistance(lat, lon, lat1, lon1);
          double distance2 = WegmanApi.getDistance(lat, lon, lat2, lon2);
          if (WegmanApi.isMustbecloser() == false || distance1 <= distance2) {
            // no distance requirement or yes but closer

            String barcode1 = initialItem.getBarcode();
            String barcode2 = alternatives.get(i).getBarcode();
            boolean locationNotMatch = ((lat1 != lat2) || (lon1 != lon2));
            // check same item requirement
            if (WegmanApi.isMustbesameitem() == false
                || (barcode2.equals(barcode1) && locationNotMatch)) {
              // no same item requirement or yes but same item but different
              // location
              result.add(new Item(alternatives.get(i)));
            }
          }
        }
      }
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

  /**
   * Confirm Final puchasing Item.
   * 
   * @throws SQLException Exception
   */
  @PostMapping("/confirm")
  @ResponseBody
  public Message confirmPurchase(
      @AuthenticationPrincipal OAuth2User principal) {
    String id;
    if (principal != null) {
      id = principal.getAttribute("sub");
    } else {
      id = "105222900313734280075";
    }
    System.out.print(id);
    User user = new User();
    OngoingTask task = new OngoingTask();
    try {
      user = DatabaseJdbc.getUser(database, "User", id);
      task = DatabaseJdbc.getTask(database, "Task", "Search", "Item", id);
    } catch (SQLException e1) {
      e1.printStackTrace();
    }

    // ongoing task
    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
    task.setTaskStartTime(timestamp);
    Item finalItem = task.getFinalItem();
    Item initialItem = task.getInitialItem();
    if (initialItem.getBarcode() == null) {
      return new Message(201, user.getName() + " haven't chose any items yet!");
    }

    if (finalItem.getBarcode() == null) {
      return new Message(202,
          user.getName() + " haven't added any items to cart yet!");
    }

    // after using clear task to avoid multiple purchase
    task.setInitialItem(new Item());
    task.setFinalItem(new Item());

    // calculate savings
    double saving = 0;
    saving = initialItem.getPrice() - finalItem.getPrice();
    saving = Math.max(saving, 0);
    // save to user info
    try {
      DatabaseJdbc.addTask(database, "Task", task);
      user.setSavings(user.getSavings() + saving);
      DatabaseJdbc.updatesUser(database, "User", user);
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return new Message(200,
        user.getName() + " purchased " + finalItem.getName() + " with $"
            + String.valueOf(finalItem.getPrice()) + " instead of "
            + initialItem.getName() + "with $" + initialItem.getPrice()
            + ". \n Good Job! You saved $" + String.valueOf(saving)
            + " on this purchase! \n You have accumulated $" + user.getSavings()
            + " so far!");
  }

  /**
   * For testing purposes only.
   * 
   * @return database instance
   */
  public static DatabaseJdbc getDb() {
    return database;
  }
}
