package vn.ducquoc.activiti;

import java.util.Arrays;
import java.util.Properties;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import vn.ducquoc.util.MailUtil;

/**
 * Unit test for simple App.
 */
public class AppTest {

    private static String USER = "noreply.test.vn.01@gmail.com";
    private static String PWD = "noreply@2012";
    private static String FROM = "noreply.test.vn.01@gmail.com";
    private static String TO = "ducquoc.vn@gmail.com"; //"quoc383011@gmail.com";
    private static String HOST = "smtp.gmail.com";

    @Ignore
    @Test
    public void testApp() {
        Assert.assertTrue(true);

        Properties props = new Properties();
//        props.put(MailUtil.MAIL_PORT, "25");
//        props.put(MailUtil.MAIL_HOST, "localhost"); //192.168.22.14
//        props.put(MailUtil.MAIL_SMTP_AUTH, false);
        props.put(MailUtil.MAIL_PORT, "25");
        props.put(MailUtil.MAIL_HOST, HOST);
        props.put(MailUtil.MAIL_SMTP_AUTH, "true");
        props.put(MailUtil.MAIL_SMTP_USER, USER);
        props.put(MailUtil.MAIL_SMTP_PASSWORD, PWD);
//        props.put(MailUtil.MAIL_SMTP_SOCKET_FACTORY_CLASS, "javax.net.ssl.SSLSocketFactory");
//        props.put(MailUtil.MAIL_SMTP_SOCKET_FACTORY_PORT, "465");
//        props.put(MailUtil.MAIL_SMTP_SOCKET_FACTORY_FALLBACK, "false");
        props.put("mail.smtp.debug", "true");
//      props.put("mail.smtp.starttls.enable", "true");
//        props.put(MailUtil.MAIL_PORT, "587");

        String subject = "noreply: Test DQ";
        String body = "Invoked from Activity via MailUtil";

        MailUtil.sendEmail(props, FROM, subject, body, Arrays.asList(TO));
    }

}
