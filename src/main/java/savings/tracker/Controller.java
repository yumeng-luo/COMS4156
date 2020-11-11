package savings.tracker;

import org.springframework.web.bind.annotation.RestController;

import savings.tracker.util.DatabaseJdbc;
import savings.tracker.util.User;

import java.sql.SQLException;
import java.util.Collections;
import java.util.Map;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
public class Controller {
  private static DatabaseJdbc database = new DatabaseJdbc();

  
  @RequestMapping("/frontend")
  public String index() {
    return "Placeholder for frontend";
  }

  @GetMapping("/user")
  public Map<String, Object> user(@AuthenticationPrincipal OAuth2User principal) {
    try {
      DatabaseJdbc.createLoginTable(database,"User");
      User newUser = new User(principal.getAttribute("id"),"andg@123.com",principal.getAttribute("login"),0,13,56);
      DatabaseJdbc.addLoginData(database, "User", newUser);
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    
    System.out.print(principal);
    
    return Collections.singletonMap("name", principal.getAttribute("login"));
  }
}
