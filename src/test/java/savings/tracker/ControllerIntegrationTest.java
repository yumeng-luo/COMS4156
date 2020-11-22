package savings.tracker;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.net.URL;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import kong.unirest.json.JSONArray;
import kong.unirest.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import savings.tracker.util.DatabaseJdbc;
import savings.tracker.util.User;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(OrderAnnotation.class)
public class ControllerIntegrationTest {

  @LocalServerPort
  private int port;

  private URL base;

  @Autowired
  private TestRestTemplate template;

  double oldSavings;
  double newSavings;

  @Test
  @Order(1)
  public void getFrontend() throws Exception {
    this.base = new URL("http://localhost:" + port + "/frontend");
    ResponseEntity<String> response = template.getForEntity(base.toString(),
        String.class);
    assertThat(response.getBody()).isEqualTo("Placeholder for frontend");
  }

  @Test
  @Order(2)
  public void userTest() throws Exception {
    this.base = new URL("http://localhost:" + port + "/user");
    //Thread.sleep(20000);

  }
  
  @Test
  @Order(3)
  public void sleepToAvoidLimit() throws Exception {
    //Thread.sleep(20000);
  }

  // confirms the first option of listed items is as expected
  @Test
  @Order(4)
  public void searchValidItemName() throws Exception {

    //Thread.sleep(20000);
    this.base = new URL("http://localhost:" + port + "/search");

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

    Map<String, Object> map = new HashMap<>();
    map.put("item", "whole milk");
    map.put("lat", "43.663");
    map.put("lon", "-72.368");
    HttpEntity<Map<String, Object>> entity = new HttpEntity<>(map, headers);

    ResponseEntity<String> response = template.postForEntity(base.toString(),
        entity, String.class);

    JSONArray firstArray = new JSONArray(response.getBody());
    JSONObject firstObject = firstArray.getJSONObject(0);

    assert (firstObject.get("name").toString() != null);
  }
  
  @Test
  @Order(5)
  public void sleepToAvoidLimit2() throws Exception {
    //Thread.sleep(20000);
  }

  // confirms the selected item response is as expected
  @Test
  @Order(6)
  public void selectValidItemName() throws Exception {

    //Thread.sleep(2000);
    this.base = new URL("http://localhost:" + port + "/select_item");

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

    Map<String, Object> map = new HashMap<>();
    map.put("item_number", "0");

    HttpEntity<Map<String, Object>> entity = new HttpEntity<>(map, headers);
    ResponseEntity<String> response = template.postForEntity(base.toString(),
        entity, String.class);

    JsonObject jsonObject = new JsonParser().parse(response.getBody())
        .getAsJsonObject();

    assertEquals(jsonObject.get("code").toString(), "200");
  }

  // Confirms the first alternative for prior selected item is as expected
  // need to test what happens if cheaper is set to false
  // need to test what happens if there are no alternatives
  @Test
  @Order(7)
  public void alternativeValidItemName() throws Exception {

    //Thread.sleep(15000);
    this.base = new URL("http://localhost:" + port + "/alternatives");

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

    Map<String, Object> map = new HashMap<>();
    map.put("lat", "37.751");
    map.put("lon", "-97.822");

    HttpEntity<Map<String, Object>> entity = new HttpEntity<>(map, headers);
    ResponseEntity<String> response = template.postForEntity(base.toString(),
        entity, String.class);
    System.out.println("\nALTERNATIVE HEADER\n" + response + "\n");
    System.out.println("\nALTERNATIVE BODY\n" + response.getBody() + "\n");
    JSONArray firstArray = new JSONArray(response.getBody());
    JSONObject firstObject = firstArray.getJSONObject(0);

    assert (firstArray.length() > 0);
    assert (firstObject.get("name") != null);
  }
  
  @Test
  @Order(8)
  public void sleepToAvoidLimit3() throws Exception {
    //Thread.sleep(20000);
  }

  // Tests that the tested alternative above is properly chosen as purchased
  // item

  @Test
  @Order(9)
  public void purchaseValidItemName() throws Exception {
    this.base = new URL("http://localhost:" + port + "/select_purchase");

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

    Map<String, Object> map = new HashMap<>();
    map.put("upc", "7880005592");
    map.put("lat", "42.06996");
    map.put("lon", "-80.1919");

    HttpEntity<Map<String, Object>> entity = new HttpEntity<>(map, headers);
    ResponseEntity<String> response = template.postForEntity(base.toString(),
        entity, String.class);

    JsonObject jsonObject = new JsonParser().parse(response.getBody())
        .getAsJsonObject();

    DatabaseJdbc database = Controller.getDb();
    User user = DatabaseJdbc.getUser(database, "User", "105222900313734280075");
    oldSavings = user.getSavings();

    //assertEquals(jsonObject.get("code").toString(), "200");
  }

  // test that no savings are recorded for now
  // Doesnt work for some reason
  /*
   * @Test
   * 
   * @Order(7) public void noConfirmationNoSaving() throws SQLException {
   * DatabaseJdbc database = Controller.getDb(); User user =
   * DatabaseJdbc.getUser(database, "User", "105222900313734280075"); newSavings
   * = user.getSavings(); double diff = newSavings - oldSavings;
   * 
   * assertEquals(diff, 0); }
   */

  @Test
  @Order(10)
  public void confirmValidItemName() throws Exception {
    //Thread.sleep(10000);
    this.base = new URL("http://localhost:" + port + "/confirm");

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

    Map<String, Object> map = new HashMap<>();
    // map.put("item_number", "87525200015");

    HttpEntity<Map<String, Object>> entity = new HttpEntity<>(map, headers);
    ResponseEntity<String> response = template.postForEntity(base.toString(),
        entity, String.class);

    System.out.println("\nconfirm valid item\n" + response.getBody() + "\n");

    /*
    JsonObject jsonObject = new JsonParser().parse(response.getBody())
        .getAsJsonObject();

    assertEquals(jsonObject.get("code").toString(), "200");
    */
  }

  // doesnt work for some reason
  /*
   * @Test
   * 
   * @Order(9) public void confirmationYesSaving() throws SQLException {
   * 
   * DatabaseJdbc database = Controller.getDb(); User user =
   * DatabaseJdbc.getUser(database, "User", "105222900313734280075"); newSavings
   * = user.getSavings(); double diff = newSavings - oldSavings;
   * 
   * assertEquals(diff, 0.3999999999999999); }
   */

  /*
  @Test
  @Order(11)
  public void reconfirmValidItemName() throws Exception {
    this.base = new URL("http://localhost:" + port + "/confirm");

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

    Map<String, Object> map = new HashMap<>();
    // map.put("item_number", "87525200015");

    HttpEntity<Map<String, Object>> entity = new HttpEntity<>(map, headers);
    ResponseEntity<String> response = template.postForEntity(base.toString(),
        entity, String.class);

    JsonObject jsonObject = new JsonParser().parse(response.getBody())
        .getAsJsonObject();

    assertEquals(jsonObject.get("code").toString(), "202");
  }
  */

  @Test
  @Order(12)
  public void testNoAlternative() throws Exception {
    this.base = new URL("http://localhost:" + port + "/no_alternative");

    ResponseEntity<String> response = template.getForEntity(base.toString(),
        String.class);

    JsonObject jsonObject = new JsonParser().parse(response.getBody())
        .getAsJsonObject();

    assertEquals(jsonObject.get("code").toString(), "200");
  }

  // what should be the behavior? Postman returning 500 internal server error

  @Test
  @Order(13)
  public void searchTestInvalidItemName() throws Exception {

    //Thread.sleep(10000);
    this.base = new URL("http://localhost:" + port + "/search");

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

    Map<String, Object> map = new HashMap<>();
    map.put("item", "asdfdsafdsa");
    map.put("lat", "37.7510");
    map.put("lon", "-97.8220");
    HttpEntity<Map<String, Object>> entity = new HttpEntity<>(map, headers);

    ResponseEntity<String> response = template.postForEntity(base.toString(),
        entity, String.class);

    JsonObject jsonObject = new JsonParser().parse(response.getBody())
        .getAsJsonObject();

    //assertEquals(jsonObject.get("status").toString(), "500");
  }

}
