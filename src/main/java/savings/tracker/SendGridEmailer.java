package savings.tracker;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import java.io.IOException;
import java.sql.SQLException;

public class SendGridEmailer {

  /**
   * send email to target.
   * 
   * @throws SQLException Exception
   */
  public static void main(String[] args) throws IOException {
    try {
      SendGrid sg = new SendGrid(
          System.getenv("SG.5qiCjtjZQCeg4M7ZsTMAwQ.5p2luU"
              + "pUpNa620rOj9aISRIOzxhtXAZaU0jV9qoTXKA"));
      Email from = new Email("ASE.email.api@gmail.com");
      Email to = new Email("claire.luo414@gmail.com");

      String subject = "Testing sendgrid";
      Content content = new Content("text/html",
          "<em>From</em> <strong>Joe Biden</strong>");

      Mail mail = new Mail(from, subject, to, content);

      Request request = new Request();

      request.setMethod(Method.POST);
      request.setEndpoint("mail/send");
      request.setBody(mail.build());

      Response response = sg.api(request);

      System.out.println(response.getStatusCode());
      System.out.println(response.getHeaders());
      System.out.println(response.getBody());
    } catch (IOException e) {
      throw e;
    }
  }
}
