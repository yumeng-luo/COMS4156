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
      /*
       * // only when needed to clear item db DatabaseJdbc.deleteTable(database,
       * "Task"); DatabaseJdbc.deleteTable(database, "Search");
       * DatabaseJdbc.deleteTable(database, "Item"); // end here
       * 
       */

      DatabaseJdbc.createLoginTable(database, "User");
      DatabaseJdbc.createItemTable(database, "Item");
      DatabaseJdbc.createSearchTable(database, "Search", "Item");
      DatabaseJdbc.createTaskTable(database, "Task", "User", "Search", "Item");
      // DatabaseJdbc.deleteTable(database, "Store");
      DatabaseJdbc.createStoreTable(database, "Store");

      /*
       * // can skip this to save time List<Store> stores =
       * WegmanApi.getStores();
       * 
       * for (int i = 0; i < stores.size(); i++) {
       * DatabaseJdbc.addStore(database, "Store", stores.get(i)); }
       */
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    SpringApplication.run(Application.class, args);

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
                "/select_item", "/select_purchase", "/no_alternative",
                "/alternatives", "/confirm")
            // if there are any authentication problems, first try uncommenting either of the two and using the other
            .permitAll().anyRequest().authenticated())
            //.authenticated())
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

}
