package vn.ducquoc.util;

import java.util.Arrays;
import java.util.Properties;

import org.junit.Assert;
import org.junit.Test;

public class MailUtilTest {

    private static String USER = "noreply.test.vn.01@gmail.com";
    private static String PWD = "noreply@2012";
    private static String HOST = "smtp.gmail.com";

    @Test
    public void testApp() {

        Properties props = new Properties();
        props.put(MailUtil.MAIL_PORT, "25");
        props.put(MailUtil.MAIL_HOST, HOST);
        props.put(MailUtil.MAIL_SMTP_AUTH, "true");
        props.put(MailUtil.MAIL_SMTP_USER, USER);
        props.put(MailUtil.MAIL_SMTP_PASSWORD, PWD);
//        props.put(MailUtil.MAIL_SMTP_SOCKET_FACTORY_CLASS, "javax.net.ssl.SSLSocketFactory");
//        props.put(MailUtil.MAIL_SMTP_SOCKET_FACTORY_PORT, "465");
//        props.put(MailUtil.MAIL_SMTP_SOCKET_FACTORY_FALLBACK, "false");
        props.put("mail.smtp.debug", "true");
//        props.put("mail.smtp.starttls.enable", "true");

        String subject = "noreply: Test DQ";
        String body = "Invoked from unit test";

        String FROM = "noreply.test.vn.01@gmail.com";
        String TO = "ducquoc@kms-technology.com";

        MailUtil.sendEmail(props, FROM, subject, body, Arrays.asList(TO));

        Assert.assertTrue(props != null);
    }

}
