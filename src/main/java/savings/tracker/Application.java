package savings.tracker;

import java.sql.SQLException;
import java.util.Arrays;
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
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    SpringApplication.run(Application.class, args);
    
    String tcin = "54191097";
    String zipcode = "10025";
    String locationId = target_getStoreId(zipcode);
    String price = target_getPrice(tcin, locationId);
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
                "/select", "/no_alternative")
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
   * @param zipcode zipcode
   * @return location id
   */
  public static String target_getStoreId(String zipcode) {
    String response = Unirest.get("https://target1.p.rapidapi.com/stores/list?zipcode=" + zipcode)
        .header("x-rapidapi-key", System.getenv("RAPID_API_KEY"))
        .header("x-rapidapi-host", "target1.p.rapidapi.com")
        .asString().getBody();
    
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
   * @param tcin product id
   * @param storeID store id
   * @return price as string
   */
  public static String target_getPrice(String tcin, String storeID) {
    HttpResponse<JsonNode> response = Unirest.get
        ("https://target1.p.rapidapi.com/products/get-details?tcin="
        + tcin + "&store_id=" + storeID)
        .header("x-rapidapi-key", System.getenv("RAPID_API_KEY"))
        .header("x-rapidapi-host", "target1.p.rapidapi.com")
        .asJson();
    
    JsonNode body = response.getBody();
    JSONObject data = body.getObject();
    String test = data.getJSONObject("data").getJSONObject("product")
        .getJSONObject("price").get("current_retail").toString();
   
    System.out.println(test);
    return test;
  }
}
