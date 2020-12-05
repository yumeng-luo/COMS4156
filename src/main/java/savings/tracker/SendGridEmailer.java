package savings.tracker;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Email;
import com.sendgrid.helpers.mail.objects.Personalization;

import kong.unirest.json.JSONObject;

import java.io.IOException;

public class SendGridEmailer {

  /**
   * sends out mail.
   * 
   * @param mail mail
   * @return boolean on success
   * @throws IOException exception
   */
  public static boolean send(final Mail mail, String apiKey)
      throws IOException {
    if (mail == null || apiKey == "" || apiKey.startsWith("SG") == false) {
      return false;
    }
    final SendGrid sg = new SendGrid(apiKey);
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
      System.out.flush();
      
      int status = response.getStatusCode();

      if (status != 202) {
        return false;
      }
      
    } catch (IOException e) {
      throw e;
    }

    return true;

  }

  /**
   * builds mail class.
   * 
   * @return mail object
   */
  public static Mail buildDynamicTemplate(String email, String weeklySaving, String totalSaving) {
    if (email == "" || email.length() <= 5 || email.length() >= 254
        || email.contains("@") == false) {
      return null;
    }
    Mail mail = new Mail();

    Email fromEmail = new Email();
    fromEmail.setName("Savings Tracker Team");
    fromEmail.setEmail("ASE.email.api@gmail.com");
    mail.setFrom(fromEmail);

    mail.setTemplateId("d-8bbecf6e66154f6596466202edb71ccf");

    Personalization personalization = new Personalization();
    personalization.addTo(new Email(email));
    personalization.addDynamicTemplateData("weekly-total",
        "This week you saved " + weeklySaving);
    personalization.addDynamicTemplateData("cumul-total",
        "In total, you saved " + totalSaving + "!");
    mail.addPersonalization(personalization);

    return mail;
  }

  /**
   * helper function that creates and sends email.
   * 
   * @throws IOException exception
   * @return true if successful
   */
  public static boolean sendDynamicEmail(String email, String weeklySaving, String totalSaving) throws IOException {
    final Mail dynamicTemplate = buildDynamicTemplate(email, weeklySaving, totalSaving);
    return send(dynamicTemplate, System.getenv("SENDGRID_API_KEY"));
  }

  /**
   * Main function.
   * 
   * @param args args
   * @throws IOException exception
   */
  public static void main(String[] args) throws IOException {

    sendDynamicEmail("ASE.email.api@gmail.com", "5.00", "6.00");

  }
}
