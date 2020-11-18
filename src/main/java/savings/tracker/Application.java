package savings.tracker;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.sql.SQLException;
import java.text.Format;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.json.JSONArray;
import kong.unirest.json.JSONObject;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import savings.tracker.util.DatabaseJdbc;
import savings.tracker.util.Item;
import savings.tracker.util.Store;

@SpringBootApplication
public class Application extends WebSecurityConfigurerAdapter {

  private static DatabaseJdbc database = new DatabaseJdbc();

  /**
   * Main function to run the application.
   * 
   * @param args args
   * @throws SQLException Exception
   */
  public static void main(String[] args) {

    try {
      DatabaseJdbc.createLoginTable(database, "User");
      DatabaseJdbc.createItemTable(database, "Item");
      DatabaseJdbc.createSearchTable(database, "Search", "Item");
      DatabaseJdbc.createTaskTable(database, "Task", "User", "Search", "Item");
      DatabaseJdbc.createStoreTable(database, "Store");
      List<Store> wegmans = wegman_getStores();

      for (int i = 0; i < wegmans.size(); i++) {
        DatabaseJdbc.addStore(database, "Store", wegmans.get(i));
      }
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    SpringApplication.run(Application.class, args);

    // String tcin = "54191097";
    // String zipcode = "10025";
    // String locationId = target_getStoreId(zipcode);
    // tring price = target_getPrice(tcin, locationId);
    // List<Item> items = wegman_getItems("Milk");

  }

  /**
   * System log of all beans provided by Spring Boot.
   * 
   * @return CommandLineRunner log
   */
  @Bean
  public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
    return args -> {

      System.out.println("Let's inspect the beans provided by Spring Boot:");

      String[] beanNames = ctx.getBeanDefinitionNames();
      Arrays.sort(beanNames);
      for (String beanName : beanNames) {
        System.out.println(beanName);
      }

    };
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    // @formatter:off
    http.authorizeRequests(
        // this is very dangerous, i only did this so i can test without using
        // too many google
        // oauth login quota
        a -> a
            .antMatchers("/frontend", "/", "/error", "/webjars/**", "/search",
                "/select_item", "/select_purchase", "/no_alternative")
            .permitAll().anyRequest().authenticated())
        .exceptionHandling(e -> e.authenticationEntryPoint(
            new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
        .csrf(c -> c
            .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))
        .logout(l -> l.logoutSuccessUrl("/").permitAll())
        .oauth2Login(o -> o.failureHandler((request, response, exception) -> {
          request.getSession().setAttribute("error.message",
              exception.getMessage());
          // handler.onAuthenticationFailure(request, response, exception);
        }));
    http.cors().and().csrf().disable();
  }

  @Bean
  CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowedOrigins(Arrays.asList("*"));
    configuration.setAllowedMethods(Arrays.asList("*"));
    configuration.setAllowedHeaders(Arrays.asList("*"));
    configuration.setAllowCredentials(true);
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }

  /**
   * returns the first store ID according to a zipcode.
   * 
   * @param zipcode zipcode
   * @return location id
   */
  public static String target_getStoreId(String zipcode) {
    String response = Unirest
        .get("https://target1.p.rapidapi.com/stores/list?zipcode=" + zipcode)
        .header("x-rapidapi-key",
            "d742520193mshd93d24a0e93b9adp152d18jsncdffefd389e9")
        .header("x-rapidapi-host", "target1.p.rapidapi.com").asString()
        .getBody();

    JSONArray firstArray = new JSONArray(response);
    JSONObject firstObject = firstArray.getJSONObject(0);
    JSONArray secondArray = firstObject.getJSONArray("locations");
    JSONObject location = secondArray.getJSONObject(0);
    String locationId = location.get("location_id").toString();

    System.out.println("\n" + locationId + "\n");
    return locationId;
  }

  /**
   * gets price of an object.
   * 
   * @param tcin    product id
   * @param storeID store id
   * @return list of items
   */
  public static String target_getPrice(String tcin, String storeID) {
    HttpResponse<JsonNode> response = Unirest
        .get("https://target1.p.rapidapi.com/products/get-details?tcin=" + tcin
            + "&store_id=" + storeID)
        .header("x-rapidapi-key",
            "d742520193mshd93d24a0e93b9adp152d18jsncdffefd389e9")
        .header("x-rapidapi-host", "target1.p.rapidapi.com").asJson();

    JsonNode body = response.getBody();
    JSONObject data = body.getObject();
    String test = data.getJSONObject("data").getJSONObject("product")
        .getJSONObject("price").get("current_retail").toString();

    System.out.println(test);
    return test;
  }

  /**
   * gets products by name.
   * 
   * @param name product name
   * @return price as string
   */
  public static List<Item> wegman_getItems(String name) {
    HttpResponse<String> response = Unirest
        .get("https://api.wegmans.io/products/search?query=" + name
            + "&api-version=2018-10-18")
        .header("Subscription-Key", "c455d00cb0f64e238a5282d75921f27e")
        .asString();

    String body = response.getBody();
    JsonObject jsonObject = new JsonParser().parse(body).getAsJsonObject();
    JsonArray results = jsonObject.get("results").getAsJsonArray();
    List<Item> list = new ArrayList<Item>();
    for (int i = 0; i < results.size(); i++) {
      // get item name store and sku
      JsonElement itemJson = results.get(i);
      JsonElement itemName = itemJson.getAsJsonObject().get("name");
      JsonElement itemSku = itemJson.getAsJsonObject().get("sku");
      Item item = new Item();
      item.setName(itemName.toString());
      item.setSku(itemSku.toString());
      item.setStore("Wegmans");

      // get item barcode
      HttpResponse<String> response2 = Unirest.get(String.format(
          "https://api.wegmans.io/products/%s?api-version=2018-10-18&Subscri"
              + "ption-Key=c455d00cb0f64e238a5282d75921f27e",
          item.getSku()).replace("\"", "")).asString();
      JsonObject jsonObject2 = new JsonParser().parse(response2.getBody())
          .getAsJsonObject();
      if (jsonObject2.get("error") != null) {
        continue;
      }
      JsonArray tradeIdentifiers = jsonObject2.getAsJsonObject()
          .get("tradeIdentifiers").getAsJsonArray();
      if (tradeIdentifiers.size() == 0) {
        continue;
      }
      JsonArray barcodes = tradeIdentifiers.get(0).getAsJsonObject()
          .get("barcodes").getAsJsonArray();
      if (barcodes.size() == 0) {
        continue;
      }
      JsonElement barcode = barcodes.get(0).getAsJsonObject().get("barcode");
      item.setBarcode(barcode.toString());

      // get item locatin
      /*
       * HttpResponse<String> response3 = Unirest.get(String.format(
       * "https://api.wegmans.io/products/%s/locations?api-ver" +
       * "sion=2018-10-18&subscription-key=c455d00cb0f64e238a5282d75921f27e",
       * item.getSku()).replace("\"", "")).asString(); JsonObject jsonObject3 =
       * new JsonParser().parse(response3.getBody()) .getAsJsonObject(); if
       * (jsonObject3.get("error") != null) { continue; }
       */
      // https://api.wegmans.io/products/484208/prices?api-version=2018-10-18&subscription-key=c455d00cb0f64e238a5282d75921f27e

      list.add(item);
    }

    return list;
  }

  /**
   * get all store locations.
   * 
   * @return all stores
   */
  public static List<Store> wegman_getStores() {
    HttpResponse<String> response = Unirest
        .get("https://api.wegmans.io/stores?Subsc"
            + "ription-Key=c455d00cb0f64e238a5282d75921f27e&api-version=2018-10-18")
        .asString();

    String body = response.getBody();
    JsonObject jsonObject = new JsonParser().parse(body).getAsJsonObject();
    JsonArray results = jsonObject.get("stores").getAsJsonArray();
    List<Store> list = new ArrayList<Store>();
    for (int i = 0; i < results.size(); i++) {
      // get store name and type location and
      JsonElement storeJson = results.get(i);
      JsonElement storeName = storeJson.getAsJsonObject().get("name");
      JsonElement storeNumber = storeJson.getAsJsonObject().get("number");
      JsonElement storeType = storeJson.getAsJsonObject().get("type");
      JsonElement storeLat = storeJson.getAsJsonObject().get("latitude");
      JsonElement storeLon = storeJson.getAsJsonObject().get("longitude");
      Store store = new Store();
      store.setName(storeName.toString());
      store.setNumber(Integer.valueOf(storeNumber.toString()));
      store.setType(storeType.toString());
      store.setLat(storeLat.getAsDouble());
      store.setLon(storeLon.getAsDouble());
      list.add(store);
    }

    return list;
  }
}
