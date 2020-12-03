package savings.tracker;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Email;
import java.io.IOException;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

@TestMethodOrder(OrderAnnotation.class)
public class EmailTest {
  
  @Test
  @Order(1)
  public void makeEmailTest() {
    Email fromEmail = new Email();
    fromEmail.setName("Savings Tracker Team");
    fromEmail.setEmail("ASE.email.api@gmail.com");
    
    assertEquals(SendGridEmailer.buildDynamicTemplate("ASE.email.api@gmail.com").getFrom(), fromEmail);
  }
  
  
  @Test
  @Order(2)
  public void sendEmailTest() throws IOException {
    
    Mail mail = SendGridEmailer.buildDynamicTemplate("ASE.email.api@gmail.com");
    assertEquals(SendGridEmailer.send(mail, System.getenv("SENDGRID_API_KEY")), true);
  }
  
  
}
