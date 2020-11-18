package savings.tracker;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import kong.unirest.UnirestException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

@TestMethodOrder(OrderAnnotation.class)
public class TargetApiTest {
  
  @Test
  @Order(1)
  public static void emptyZip() throws UnirestException, InvalUserInputException {
    String res;
    String emptyZip = "05001";
    
    res = TargetApi.target_getStoreId(emptyZip);
    
    assertEquals(res, null);
  }
  
  @Test
  @Order(2)
  public static void invalZip() throws UnirestException, InvalUserInputException {
    String invalZip = "05";

    Assertions.assertThrows(InvalUserInputException.class, () -> {
      TargetApi.target_getStoreId(invalZip);
    });
  }
  
  @Test
  @Order(3)
  public static void validZip() throws UnirestException, InvalUserInputException {
    String res;
    String validZip = "10025";
    String locationId = "1263";
    
    res = TargetApi.target_getStoreId(validZip);
    
    assertEquals(res, locationId);
  }

  @Test
  @Order(4)
  public static void invalidTcin() throws UnirestException, InvalUserInputException {
    String locationId = "1263";
    String invalidTcin = "06";

    Assertions.assertThrows(InvalUserInputException.class, () -> {
      TargetApi.target_getPrice(invalidTcin, locationId);
    });
  }
  
  @Test
  @Order(5)
  public static void validTcin() throws UnirestException, InvalUserInputException {
    String locationId = "1263";
    String validTcin = "54191097";
    String price = "129.99";
    String res;

    res = TargetApi.target_getPrice(validTcin, locationId);
    
    assertEquals(res, price);
  }
  
  

}
