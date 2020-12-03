package savings.tracker;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.net.URL;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import kong.unirest.json.JSONArray;
import kong.unirest.json.JSONObject;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
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
import savings.tracker.util.Item;
import savings.tracker.util.OngoingTask;
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
    System.out.println("\nTesting seach valid item\n");
    //Thread.sleep(20000);
    this.base = new URL("http://localhost:" + port + "/search");

//    HttpHeaders headers = new HttpHeaders();
//    headers.setContentType(MediaType.APPLICATION_JSON);
//    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
//
//    Map<String, Object> map = new HashMap<>();
//    map.put("item", "whole milk");
//    map.put("lat", "43.663");
//    map.put("lon", "-72.368");
//    HttpEntity<Map<String, Object>> entity = new HttpEntity<>(map, headers);
//
//    ResponseEntity<String> response = template.postForEntity(base.toString(),
//        entity, String.class);
//
//    JSONArray firstArray = new JSONArray(response.getBody());
//    JSONObject firstObject = firstArray.getJSONObject(0);
//
//    assert (firstObject.get("name").toString() != null);
    
    HttpClient httpclient = HttpClients.createDefault();
    HttpPost httppost = new HttpPost("http://localhost:" + port + "/search");
    
    List<NameValuePair> params = new ArrayList<NameValuePair>(2);
    params.add(new BasicNameValuePair("item", "orange"));
    params.add(new BasicNameValuePair("lat", "43.663"));
    params.add(new BasicNameValuePair("lon", "-72.368"));
    httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));

    //Execute and get the response.
    HttpResponse response = httpclient.execute(httppost);
    org.apache.http.HttpEntity entity = response.getEntity();

    if (entity != null) {
      String result = EntityUtils.toString(entity);
      JSONArray jsonArray = new JSONArray(result);
      JSONObject jsonObject = jsonArray.getJSONObject(0);
      assert (jsonObject.get("name").toString() != null);
      
      System.out.println(result);
    }
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
    System.out.println("\nTesting select valid item\n");
    //Thread.sleep(2000);
//    this.base = new URL("http://localhost:" + port + "/select_item");
//
//    HttpHeaders headers = new HttpHeaders();
//    headers.setContentType(MediaType.APPLICATION_JSON);
//    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
//
//    Map<String, Object> map = new HashMap<>();
//    map.put("item_number", "1");
//
//    HttpEntity<Map<String, Object>> entity = new HttpEntity<>(map, headers);
//    ResponseEntity<String> response = template.postForEntity(base.toString(),
//        entity, String.class);
//
//    JsonObject jsonObject = new JsonParser().parse(response.getBody())
//        .getAsJsonObject();
//
//    assertEquals(jsonObject.get("code").toString(), "200");
    
    HttpClient httpclient = HttpClients.createDefault();
    HttpPost httppost = new HttpPost("http://localhost:" + port + "/select_item");
    
    List<NameValuePair> params = new ArrayList<NameValuePair>(2);
    params.add(new BasicNameValuePair("item_number", "4"));
    httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));

    //Execute and get the response.
    HttpResponse response = httpclient.execute(httppost);
    org.apache.http.HttpEntity entity = response.getEntity();

    if (entity != null) {
      String result = EntityUtils.toString(entity);
      JSONObject jsonObject = new JSONObject(result);
      assertEquals(jsonObject.get("code").toString(), "200");
      
      System.out.println("\nresult: " + result);
    }
  }

  // Confirms the first alternative for prior selected item is as expected
  // need to test what happens if cheaper is set to false
  // need to test what happens if there are no alternatives
  @Test
  @Order(7)
  public void alternativeValidItemNameAndNoSavings() throws Exception {
    System.out.println("\nTesting alternative item\n");
    DatabaseJdbc database = Controller.getDb();
    User user = DatabaseJdbc.getUser(database, "User", "105222900313734280075");
    oldSavings = user.getSavings();

//    this.base = new URL("http://localhost:" + port + "/alternatives");

//    HttpHeaders headers = new HttpHeaders();
//    headers.setContentType(MediaType.APPLICATION_JSON);
//    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
//
//    Map<String, Object> map = new HashMap<>();
//    map.put("lat", "37.7510");
//    map.put("lon", "-97.8220");
//
//    HttpEntity<Map<String, Object>> entity = new HttpEntity<>(map, headers);
//    ResponseEntity<String> response = template.postForEntity(base.toString(),
//        entity, String.class);
    
//    System.out.println("\nALTERNATIVE HEADER\n" + response + "\n");
//    System.out.println("\nALTERNATIVE BODY\n" + response.getBody() + "\n");
//    JSONArray firstArray = new JSONArray(response.getBody());
//    JSONObject firstObject = firstArray.getJSONObject(0);
    
    HttpClient httpclient = HttpClients.createDefault();
    HttpPost httppost = new HttpPost("http://localhost:" + port + "/alternatives");
    
    List<NameValuePair> params = new ArrayList<NameValuePair>(2);
    params.add(new BasicNameValuePair("lat", "37.7510"));
    params.add(new BasicNameValuePair("lon", "-97.8220"));
    httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));

    //Execute and get the response.
    HttpResponse response = httpclient.execute(httppost);
    org.apache.http.HttpEntity entity = response.getEntity();
    System.out.println ("\nAlternative response1: " + response);
    if (entity != null) {
      String result = EntityUtils.toString(entity);
      System.out.println ("\nAlternative response2: " + result);
      JSONArray jsonArray = new JSONArray(result);
      JSONObject jsonObject = jsonArray.getJSONObject(0);
      assert (jsonObject.get("name").toString() != null);
      
      DatabaseJdbc updatedDatabase = Controller.getDb(); 
      User updatedUser = DatabaseJdbc.getUser(updatedDatabase, "User", "105222900313734280075"); 
      newSavings = updatedUser.getSavings(); 
      
      System.out.println("Old savings :" + oldSavings);
      System.out.println("New savings :" + newSavings);
      
      double diff = newSavings - oldSavings;
      assertEquals(diff, 0); 
      assert (jsonArray.length() > 0);
      assert (jsonObject.get("name") != null);
    }

  }
  
  
  // Tests that the tested alternative above is properly chosen as purchased
  // item
  @Test
  @Order(9)
  public void purchaseValidItemNameAndStillNoSavings() throws Exception {
    System.out.println("\nTesting purchase item\n");
//    HttpHeaders headers = new HttpHeaders();
//    headers.setContentType(MediaType.APPLICATION_JSON);
//    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
//
//    Map<String, Object> map = new HashMap<>();
//    map.put("upc", "7880005592");
//    map.put("lat", "42.06996");
//    map.put("lon", "-80.1919");
//
//    HttpEntity<Map<String, Object>> entity = new HttpEntity<>(map, headers);
//    ResponseEntity<String> response = template.postForEntity(base.toString(),
//        entity, String.class);

//    JsonObject jsonObject = new JsonParser().parse(entity.getBody())
//        .getAsJsonObject();
//
//    System.out.println("\n purchase\n" + response.getBody() + "\n");
//    
//    DatabaseJdbc updatedDatabase = Controller.getDb(); 
//    User updatedUser = DatabaseJdbc.getUser(updatedDatabase, "User", "123"); 
//    newSavings = updatedUser.getSavings(); 
//    
//    System.out.println("Old savings :" + oldSavings);
//    System.out.println("New savings :" + newSavings);
//    
//    double diff = newSavings - oldSavings;
//    assertEquals (diff, 0); 
//    assertEquals(jsonObject.get("code").toString(), "200");
//    
//    Item chosenItem = DatabaseJdbc.getItem(updatedDatabase, "Item", "7880005592",
//        42.06996, -80.1919);
//    OngoingTask task = DatabaseJdbc.getTask(updatedDatabase, "Task", "Search", "Item",
//        "123");
//    
//    System.out.println("\nchosen item: " + chosenItem.getName() + "\n");
//    System.out.println("\nFinal Chosen Item : " + task.getFinalItem().getName() + "\n");
    
    Thread.sleep(3000);
    DatabaseJdbc database = Controller.getDb();
    User user = DatabaseJdbc.getUser(database, "User", "105222900313734280075");
    oldSavings = user.getSavings();
    
    HttpClient httpclient = HttpClients.createDefault();
    HttpPost httppost = new HttpPost("http://localhost:" + port + "/select_purchase");

    // Request parameters and other properties.
    List<NameValuePair> params = new ArrayList<NameValuePair>(2);
    params.add(new BasicNameValuePair("upc", "2500004483"));
    params.add(new BasicNameValuePair("lat", "42.06996"));
    params.add(new BasicNameValuePair("lon", "-80.1919"));
    httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));

    //Execute and get the response.
    HttpResponse response = httpclient.execute(httppost);
    org.apache.http.HttpEntity entity = response.getEntity();

    if (entity != null) {
      String result = EntityUtils.toString(entity);
      System.out.println("\npurchase result: " + result);
      
      JSONObject jsonObject = new JSONObject(result);
      
      DatabaseJdbc updatedDatabase = Controller.getDb(); 
      User updatedUser = DatabaseJdbc.getUser(updatedDatabase, "User", "105222900313734280075"); 
      newSavings = updatedUser.getSavings(); 
      
      System.out.println("Old savings :" + oldSavings);
      System.out.println("New savings :" + newSavings);
      
      double diff = newSavings - oldSavings;
      assertEquals (diff, 0); 
      assertEquals(jsonObject.get("code").toString(), "200");
      
    }
  }


  @Test
  @Order(10)
  public void confirmValidItemName() throws Exception {
    System.out.println("\nTesting confirm item\n");
    DatabaseJdbc database = Controller.getDb();
    User user = DatabaseJdbc.getUser(database, "User", "105222900313734280075");
    oldSavings = user.getSavings();
    
//    this.base = new URL("http://localhost:" + port + "/confirm");
//
//    HttpHeaders headers = new HttpHeaders();
//    headers.setContentType(MediaType.APPLICATION_JSON);
//    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
//
//    Map<String, Object> map = new HashMap<>();
//    // map.put("item_number", "87525200015");
//
//    HttpEntity<Map<String, Object>> entity = new HttpEntity<>(map, headers);
//    ResponseEntity<String> response = template.postForEntity(base.toString(),
//        entity, String.class);
    
    HttpClient httpclient = HttpClients.createDefault();
    HttpPost httppost = new HttpPost("http://localhost:" + port + "/confirm");

    // Request parameters and other properties.
    List<NameValuePair> params = new ArrayList<NameValuePair>(2);
//    params.add(new BasicNameValuePair("upc", "1397100385"));
//    params.add(new BasicNameValuePair("lat", "42.06996"));
//    params.add(new BasicNameValuePair("lon", "-80.1919"));
//    httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));

    //Execute and get the response.
    HttpResponse response = httpclient.execute(httppost);
    org.apache.http.HttpEntity entity = response.getEntity();

    if (entity != null) {
      String result = EntityUtils.toString(entity);
      System.out.println("\nconfirm result: " + result);
      JSONObject jsonObject = new JSONObject(result);
      
      DatabaseJdbc updatedDatabase = Controller.getDb(); 
      User updatedUser = DatabaseJdbc.getUser(updatedDatabase, "User", "105222900313734280075"); 
      newSavings = updatedUser.getSavings(); 
      
      System.out.println("Old savings :" + oldSavings);
      System.out.println("New savings :" + newSavings);
      
      double diff = newSavings - oldSavings;
      assert (diff > 0); 
      assertEquals(jsonObject.get("code").toString(), "200");
      
    }
    
  }

//
//  /*
//  @Test
//  @Order(11)
//  public void reconfirmValidItemName() throws Exception {
//    this.base = new URL("http://localhost:" + port + "/confirm");
//
//    HttpHeaders headers = new HttpHeaders();
//    headers.setContentType(MediaType.APPLICATION_JSON);
//    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
//
//    Map<String, Object> map = new HashMap<>();
//    // map.put("item_number", "87525200015");
//
//    HttpEntity<Map<String, Object>> entity = new HttpEntity<>(map, headers);
//    ResponseEntity<String> response = template.postForEntity(base.toString(),
//        entity, String.class);
//
//    JsonObject jsonObject = new JsonParser().parse(response.getBody())
//        .getAsJsonObject();
//
//    assertEquals(jsonObject.get("code").toString(), "202");
//  }
//  */
//
//  @Test
//  @Order(12)
//  public void testNoAlternative() throws Exception {
//    this.base = new URL("http://localhost:" + port + "/no_alternative");
//
//    ResponseEntity<String> response = template.getForEntity(base.toString(),
//        String.class);
//
//    JsonObject jsonObject = new JsonParser().parse(response.getBody())
//        .getAsJsonObject();
//
//    assertEquals(jsonObject.get("code").toString(), "200");
//  }
//
//  // what should be the behavior? Postman returning 500 internal server error
//
//  @Test
//  @Order(13)
//  public void searchTestInvalidItemName() throws Exception {
//
//    //Thread.sleep(10000);
//    this.base = new URL("http://localhost:" + port + "/search");
//
//    HttpHeaders headers = new HttpHeaders();
//    headers.setContentType(MediaType.APPLICATION_JSON);
//    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
//
//    Map<String, Object> map = new HashMap<>();
//    map.put("item", "asdfdsafdsa");
//    map.put("lat", "37.7510");
//    map.put("lon", "-97.8220");
//    HttpEntity<Map<String, Object>> entity = new HttpEntity<>(map, headers);
//
//    ResponseEntity<String> response = template.postForEntity(base.toString(),
//        entity, String.class);
//
//    //JsonObject jsonObject = new JsonParser().parse(response.getBody())
//        //.getAsJsonObject();
//
//    //assertEquals(jsonObject.get("status").toString(), "500");
//  }

}
