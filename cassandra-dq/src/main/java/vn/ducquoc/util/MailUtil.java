package vn.ducquoc.util;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import vn.ducquoc.jutil.UtilException;

/**
 * Helper class for email operations.
 * 
 * @author ducquoc
 * @see org.gbif.utils.mail.MailUtil
 * @see org.appfuse.util.MailUtil
 */
public final class MailUtil {

  public static void sendEmail(String from, String to, String subject, String body, Properties props) {
    try {
      Session session = Session.getDefaultInstance(props);
      MimeMessage message = new MimeMessage(session);
      message.setFrom(new InternetAddress(from));
      message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
      message.setSubject(subject);
      message.setText(body);

      Transport.send(message);
    } catch (Exception ex) {
      throw new UtilException("Failed to send email from: " + from, ex);
    }
  }

  public static void sendEmailWithCc(String from, String to, String subject, String body, Properties props,
      String... ccAddrs) {
    try {
      Session session = Session.getDefaultInstance(props);
      MimeMessage message = new MimeMessage(session);
      message.setFrom(new InternetAddress(from));
      message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
      message.setSubject(subject);
      message.setText(body);

      for (String cc : ccAddrs) {
        if (cc != null) {
          message.setRecipients(Message.RecipientType.CC, InternetAddress.parse(cc, false));
        }
      }
      Transport.send(message);
    } catch (Exception ex) {
      throw new UtilException("Failed to send email from: " + from, ex);
    }
  }

  public static String getUsernameFromEmailAddress(String emailAddr) {
    if (emailAddr == null) {
      return "";
    }
    int atIndex = emailAddr.indexOf('@');
    if (atIndex == -1) {
      return "";
    }
    return emailAddr.substring(0, atIndex);
  }

  public static String getDomainFromEmailAddress(String emailAddr) {
    if (emailAddr == null) {
      return "";
    }
    int atIndex = emailAddr.indexOf('@');
    if (atIndex == -1) {
      return "";
    }
    return emailAddr.substring(atIndex + 1);
  }

}
