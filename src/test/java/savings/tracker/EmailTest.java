package savings.tracker;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
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
  
  @Test
  @Order(3)
  public void sendEmailInputNoNullTest() throws IOException {
    
    Mail mail = SendGridEmailer.buildDynamicTemplate("ASE.email.api@gmail.com");
    assertEquals(SendGridEmailer.send(mail, System.getenv("SENDGRID_API_KEY")), true);
  }
  
  @Test
  @Order(4)
  public void sendEmailInputNullTest() throws IOException {
    
    Mail mail = SendGridEmailer.buildDynamicTemplate("ASE.email.api@gmail.com");
    assertEquals(SendGridEmailer.send(null, System.getenv("SENDGRID_API_KEY")), false);
  }
  
  @Test
  @Order(5)
  public void sendEmailAPIKeyEmptyTest() throws IOException {
    Mail mail = SendGridEmailer.buildDynamicTemplate("ASE.email.api@gmail.com");
    assertEquals(SendGridEmailer.send(mail, System.getenv("")), false);
  }
  
  @Test
  @Order(6)
  public void sendEmailAPIKeyInvalidTest() throws IOException {
    Mail mail = SendGridEmailer.buildDynamicTemplate("ASE.email.api@gmail.com");
    assertEquals(SendGridEmailer.send(mail, System.getenv("123343rgasd")), false);
  }
  
  @Test
  @Order(7)
  public void sendEmailAPIKeyValidTest() throws IOException {
    Mail mail = SendGridEmailer.buildDynamicTemplate("ASE.email.api@gmail.com");
    assertEquals(SendGridEmailer.send(mail, System.getenv("SG.38487ddsaf")), true);
  }
  
  @Test
  @Order(8)
  public void sendEmailAPIKeyLongTest() throws IOException {
    Mail mail = SendGridEmailer.buildDynamicTemplate("ASE.email.api@gmail.com");
    String longString = "";
    for (int i = 0; i < 51; i++) {
      longString += "a";
    }
    assertEquals(SendGridEmailer.send(mail, System.getenv(longString)), false);
  }
  
  @Test
  @Order(9)
  public void buildDynamicTemplateEmailEmptyTest() throws IOException {
    Mail mail = SendGridEmailer.buildDynamicTemplate("");
    assertNull(mail);
  }
  
  @Test
  @Order(10)
  public void buildDynamicTemplateEmailValidTest() throws IOException {
    Mail mail = SendGridEmailer.buildDynamicTemplate("ASE.email.api@gmail.com");
    assertNotNull(mail);
  }
  
  @Test
  @Order(11)
  public void buildDynamicTemplateEmailShortTest() throws IOException {
    Mail mail = SendGridEmailer.buildDynamicTemplate("asdf");
    assertNull(mail);
  }
  
  @Test
  @Order(12)
  public void buildDynamicTemplateEmailLongTest() throws IOException {
    String longMail = "";
    for (int i = 0; i < 254; i++) {
      longMail += "a";
    }
    Mail mail = SendGridEmailer.buildDynamicTemplate(longMail);
    assertNotNull(mail);
  }
  
  @Test
  @Order(13)
  public void sendDynamicEmailEmailEmptyTest() throws IOException {
    String emptyMail = "";
    assertEquals(SendGridEmailer.sendDynamicEmail(emptyMail), false);
  }
  
  @Test
  @Order(14)
  public void sendDynamicEmailEmailValidTest() throws IOException {
    String emptyMail = "";
    assertEquals(SendGridEmailer.sendDynamicEmail("abc@abc.com"), false);
  }
  
  @Test
  @Order(15)
  public void sendDynamicEmailEmailShortTest() throws IOException {
    String shortMail = "aaaa";
    assertEquals(SendGridEmailer.sendDynamicEmail(shortMail), false);
  }
  
  @Test
  @Order(16)
  public void sendDynamicEmailEmailLongTest() throws IOException {
    String longMail = "";
    for (int i = 0; i < 254; i++) {
      longMail += "a";
    }
    assertEquals(SendGridEmailer.sendDynamicEmail(longMail), false);
  }
}
