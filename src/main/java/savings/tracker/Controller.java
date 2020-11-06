package savings.tracker;

import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
public class Controller {
  @RequestMapping("/frontend")
  public String index() {
    return "Placeholder for frontend";
  }

  @GetMapping("/user")
  public Map<String, Object> user(@AuthenticationPrincipal OAuth2User principal) {
    System.out.print(principal);
    return Collections.singletonMap("name", principal.getAttribute("login"));
  }
}
