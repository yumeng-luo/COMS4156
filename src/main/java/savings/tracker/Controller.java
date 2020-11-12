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
      System.out.print(principal);
      String email = principal.getAttribute("email");
      String name = principal.getAttribute("name");
      String id = principal.getAttribute("sub");
      DatabaseJdbc.createLoginTable(database,"User");
      
      User newUser = new User(id,email,name,0,13,56);
      
      if( DatabaseJdbc.AlreadyExists(database, "User", newUser.getUser_id())) {
        System.out.print(name+" Already exists");
        DatabaseJdbc.updatesLoginData(database,"User", newUser);
      }else {
        System.out.print("Adding "+ name);
        DatabaseJdbc.addLoginData(database, "User", newUser);
      }
      
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    
    System.out.print(principal);
    
    return Collections.singletonMap("name", principal.getAttribute("name"));
  }
}
