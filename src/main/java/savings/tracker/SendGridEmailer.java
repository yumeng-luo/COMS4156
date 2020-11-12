package savings.tracker;
import com.sendgrid.*;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.*;

import java.io.IOException;

public class SendGridEmailer {
  
  private static void send(final Mail mail) throws IOException {
    final SendGrid sg = new SendGrid(System.getenv("SENDGRID_API_KEY"));
    sg.addRequestHeader("X-Mock", "true");

    final Request request = new Request();
    request.setMethod(Method.POST);
    request.setEndpoint("mail/send");
    request.setBody(mail.build());

    final Response response = sg.api(request);
    System.out.println(response.getStatusCode());
    System.out.println(response.getBody());
    System.out.println(response.getHeaders());
  }
  
  public static Mail buildDynamicTemplate() {
    Mail mail = new Mail();

    Email fromEmail = new Email();
    fromEmail.setName("Savings Tracker Team");
    fromEmail.setEmail("ASE.email.api@gmail.com");
    mail.setFrom(fromEmail);

    mail.setTemplateId("d-8bbecf6e66154f6596466202edb71ccf");

    Personalization personalization = new Personalization();
    personalization.addTo(new Email("<demo>@columbia.edu"));
    personalization.addDynamicTemplateData("weekly-total","This week you saved $5.00."); // substitute with string build
    personalization.addDynamicTemplateData("cumul-total","In total, you saved $15.00!"); // substitute with string build
    mail.addPersonalization(personalization);

    return mail;
  }
  
  public static void sendDynamicEmail() throws IOException {
    final Mail dynamicTemplate = buildDynamicTemplate();
    send(dynamicTemplate);
  }
  
  public static void main(String[] args) throws IOException {
    
    sendDynamicEmail();

  }
}
