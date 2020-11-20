package savings.tracker;

import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.UnirestException;
import kong.unirest.json.JSONArray;
import kong.unirest.json.JSONObject;

public class TargetApi {
  
  /**
   * example main.
   * @param args args
   */
  public static void main(String[] args) {
    String tcin = "54191097"; //54191097
    String zipcode = "05001";
    String locationId;
    String price;
    
    
    //Catches invalid user response / no options (maybe do basic user input checking before)
    try {
      locationId = target_getStoreId(zipcode);
      
      if (locationId == null) {
        System.out.println("no available stores in zipcode");
        return;
      }
      
    } catch (InvalUserInputException e) {
      System.out.println(e.toString());
      return;
    }
    
    try {
      price = target_getPrice(tcin, locationId);
    } catch (InvalUserInputException e) {
      System.out.println(e.toString());
      return;
    }
  }
  
  
  
  /**
   * returns the first store ID according to a zipcode.
   * @param zipcode zipcode
   * @return location id
   */
  public static String target_getStoreId(String zipcode) throws 
      UnirestException, InvalUserInputException {
    
    String response = Unirest.get("https://target1.p.rapidapi.com/stores/list?zipcode=" + zipcode)
        .header("x-rapidapi-key", System.getenv("RAPID_API_KEY"))
        .header("x-rapidapi-host", "target1.p.rapidapi.com")
        .asString().getBody();
    
    JSONArray firstArray = new JSONArray(response);
    JSONObject firstObject = firstArray.getJSONObject(0);
    
    //API determines its not a valid zipcode
    if (firstObject.has("errors")) {
      JSONArray errorArray = firstObject.getJSONArray("errors");
      String errorMsg = errorArray.getJSONObject(0).get("error").toString();
      System.out.println("\n" + errorMsg + "\n");
      throw new InvalUserInputException(errorMsg);
    }
    
    JSONArray secondArray = firstObject.getJSONArray("locations");
    
    //API determines no stores nearby
    if (secondArray.length() == 0) {
      return null;
    }
    
    JSONObject location = secondArray.getJSONObject(0);    
    String locationId = location.get("location_id").toString();

    System.out.println("\n" + locationId + "\n");
    return locationId;
  }
  
  /**
   * gets price of an object.
   * @param tcin product id
   * @return price as string
   */
  public static String target_getPrice(String tcin, String storeId) throws 
      UnirestException, InvalUserInputException {
    
    HttpResponse<JsonNode> response = Unirest.get(
        "https://target1.p.rapidapi.com/products/get-details?tcin="
        + tcin + "&store_id=" + storeId)
        .header("x-rapidapi-key", System.getenv("RAPID_API_KEY"))
        .header("x-rapidapi-host", "target1.p.rapidapi.com")
        .asJson();
    
    JsonNode body = response.getBody();
    JSONObject bodyJson = body.getObject();

    // API determines its invalid tcin number
    if (bodyJson.has("errors")) {
      JSONArray errorArray = bodyJson.getJSONArray("errors");
      String errorMsg = errorArray.getJSONObject(0).get("message").toString();
      System.out.println("\n" + errorMsg + "\n");
      throw new InvalUserInputException(errorMsg);
    }
    
    String test = bodyJson.getJSONObject("data").getJSONObject("product")
        .getJSONObject("price").get("current_retail").toString();
   
    System.out.println(test);
    return test;
  }
}
