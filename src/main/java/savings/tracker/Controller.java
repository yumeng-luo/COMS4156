package savings.tracker;

import java.sql.SQLException;
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
import savings.tracker.util.EndPointHelper;
import savings.tracker.util.Walmart;
import savings.tracker.util.WegmanApi;

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
    return EndPointHelper.noAlternativeHelper(database, id);
  }

  /**
   * Search Item by string. creates both search items and alternative items
   * search items are items with exact match alternative items are items with
   * some but not all match
   * 
   * @param item name of item
   * @param lat  of user
   * @param lon  of user
   * @throws InterruptedException Exception
   */
  @PostMapping("/search")
  @ResponseBody
  public List<Item> searchItem(
      @RequestParam(value = "item", defaultValue = "whole milk") String item,
      @AuthenticationPrincipal OAuth2User principal,
      @RequestParam(value = "lat", defaultValue = "37.7510") double lat,
      @RequestParam(value = "lon", defaultValue = "-97.8220") double lon)
      throws InterruptedException {

    if (item.length() > 50) {
      List<Item> itemList = new ArrayList<Item>();
      return itemList;
    }
    String id;
    if (principal != null) {
      id = principal.getAttribute("sub");
    } else {
      id = "105222900313734280075";
    }

    System.out.println("/search called->item: " + item);

    return EndPointHelper.searchItemHelper(database, item, id, lat, lon);
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
    if (itemNumber < 0) {
      return new Message(400, "Negative index numbers are not allowed.");
    }
    if (principal != null) {
      id = principal.getAttribute("sub");
    } else {
      id = "105222900313734280075";
    }
    System.out.print(id);
    return EndPointHelper.selectItemHelper(database, itemNumber, id);
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

    System.out.println("\nstarting alter\n");
    System.out.flush();


    // chaneg switches accordingly
    // WegmanApi.setMustbecheaper(cheaper);
    // WegmanApi.setMustbecloser(closer);
    // WegmanApi.setMustbesameitem(same);

    try {
      Thread.sleep(3000);
    } catch (InterruptedException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    String id;
    if (principal != null) {
      id = principal.getAttribute("sub");
    } else {
      id = "105222900313734280075";
    }

    List<Item> alt = EndPointHelper.searchAlternativeItemHelper(database, id, lat, lon);
    return Walmart.sortItemByDistance(lat, lon, alt);
  }

  /**
   * Filter alternatives by swicthes
   * 
   * @param lat of user
   * @param lon of user
   * @throws SQLException Exception
   */
  @PostMapping("/filter")
  @ResponseBody
  public List<Item> filterAlternatives(
      @AuthenticationPrincipal OAuth2User principal,
      @RequestParam(value = "lat", defaultValue = "37.7510") double lat,
      @RequestParam(value = "lon", defaultValue = "-97.8220") double lon,
      @RequestParam(value = "CHEAPER", defaultValue = "false") boolean cheaper,
      @RequestParam(value = "CLOSER", defaultValue = "false") boolean closer,
      @RequestParam(value = "SAME", defaultValue = "false") boolean same) {

    System.out.println("\nstarting filter\n");
    System.out.flush();

    if ((cheaper != false && cheaper != true)
        || (closer != false && closer != true)
        || (same != false && same != true)) {
      List<Item> itemList = new ArrayList<Item>();
      return itemList;
    }

    // change switches accordingly
    WegmanApi.setMustbecheaper(cheaper);
    WegmanApi.setMustbecloser(closer);
    WegmanApi.setMustbesameitem(same);

    String id;
    if (principal != null) {
      id = principal.getAttribute("sub");
    } else {
      id = "105222900313734280075";
    }

    // get task info from table
    OngoingTask currentTask = new OngoingTask();
    try {

      currentTask = DatabaseJdbc.getTask(database, "Task", "Search", "Item",
          id);
    } catch (SQLException e1) {
      e1.printStackTrace();
    }

    return EndPointHelper.filterAlternativeItem(lat, lon,
        currentTask.getAlternativeItem(), currentTask.getInitialItem());
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
    if (Double.parseDouble(lat) < -90 || Double.parseDouble(lat) > 90
        || Double.parseDouble(lon) > 180 || Double.parseDouble(lon) < -180) {
      return new Message(400, "Invalid longitude/latitude input");
    }
    String id;
    if (principal != null) {
      id = principal.getAttribute("sub");
    } else {
      id = "105222900313734280075";
    }
    System.out.print(id);
    return EndPointHelper.selectPurchaseHelper(database, barcode, lat, lon, id);
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
    return EndPointHelper.confirmPurchaseHelper(database, id);
  }

  /**
   * For testing purposes only.
   * 
   * @return database instance
   */
  public static DatabaseJdbc getDb() {
    return database;
  }
  
  /**
   * Sends email.
   * @param principal
   * @return a message object
   */
  @GetMapping("/send_email")
  public Message sendEmail(@AuthenticationPrincipal OAuth2User principal) {
    String email;
    String id;

    
    if (principal == null) {
      email = "jch2169@columbia.edu"; //ASE.email.api
      id = "105222900313734280075";
    } else {
      email = principal.getAttribute("email");
      id = principal.getAttribute("sub");
    }
    
    return EndPointHelper.sendEmailHelper(database, id, email);
  }
  
  @GetMapping("/history")
  public List<PurchaseRecord> getHistory(@AuthenticationPrincipal OAuth2User principal) {
    String id;
    List<PurchaseRecord> history = new ArrayList<PurchaseRecord>();
    
    if (principal == null) {
      id = "105222900313734280075";
    } else {
      id = principal.getAttribute("sub");
    }
    
    try {
      history = database.getPurchaseData(database, "Purchase", id);
    } catch (SQLException e) {
      e.printStackTrace();
    }
    
    return history;
  }
}
