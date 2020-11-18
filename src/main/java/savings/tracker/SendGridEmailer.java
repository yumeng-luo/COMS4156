package savings.tracker;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Email;
import com.sendgrid.helpers.mail.objects.Personalization;
import java.io.IOException;

public class SendGridEmailer {
  
  /**
   * sends out mail.
   * @param mail mail
   * @return boolean on success
   * @throws IOException exception
   */
  public static boolean send(final Mail mail) throws IOException {
    final SendGrid sg = new SendGrid(System.getenv("SENDGRID_API_KEY"));
    sg.addRequestHeader("X-Mock", "true");
    final Request request = new Request();
    
    try {
      request.setMethod(Method.POST);
      request.setEndpoint("mail/send");
      request.setBody(mail.build());
      final Response response = sg.api(request);
      
      System.out.println(response.getStatusCode());
      System.out.println(response.getBody());
      System.out.println(response.getHeaders());
      
    } catch (IOException e) {
      throw e;
    }
    
    return true;
    
  }
  
  /**
   * builds mail class.
   * @return mail object
   */
  public static Mail buildDynamicTemplate() {
    Mail mail = new Mail();

    Email fromEmail = new Email();
    fromEmail.setName("Savings Tracker Team");
    fromEmail.setEmail("ASE.email.api@gmail.com");
    mail.setFrom(fromEmail);

    mail.setTemplateId("d-8bbecf6e66154f6596466202edb71ccf");

    Personalization personalization = new Personalization();
    personalization.addTo(new Email("jch2169@columbia.edu"));
    personalization.addDynamicTemplateData("weekly-total",
        "This week you saved $5.00."); 
    personalization.addDynamicTemplateData("cumul-total",
        "In total, you saved $15.00!"); 
    mail.addPersonalization(personalization);

    return mail;
  }
  
  /**
   * helper function that creates and sends email.
   * @throws IOException exception
   */
  public static void sendDynamicEmail() throws IOException {
    final Mail dynamicTemplate = buildDynamicTemplate();
    send(dynamicTemplate);
  }
  
  /**
   * Main function.
   * @param args args
   * @throws IOException exception
   */
  public static void main(String[] args) throws IOException {
    
    sendDynamicEmail();

  }
}