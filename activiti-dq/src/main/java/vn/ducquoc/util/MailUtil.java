package vn.ducquoc.util;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * Helper class for email operations. <br>
 * </br>
 * Some SMTP like Gmail might need to allow Less Secure Apps to use their accounts
 * (myaccount.google.com/lesssecureapps)
 * 
 * @author ducquoc
 * @see org.gbif.utils.mail.MailUtil
 * @see org.appfuse.util.MailUtil
 * @see org.apache.commons.mail.SimpleEmail
 * @see org.springframework.mail.javamail.JavaMailSenderImpl
 * @see info.magnolia.module.publicuserregistration.MailUtil
 * @see com.atlassian.mail.server.impl.SMTPMailServerImpl
 */
public class MailUtil {

    public static final String MAIL_HOST = "mail.smtp.host";
    public static final String MAIL_PORT = "mail.smtp.port";
    public static final String MAIL_SMTP_FROM = "mail.smtp.from";
    public static final String MAIL_SMTP_AUTH = "mail.smtp.auth";
    public static final String MAIL_SMTP_USER = "mail.smtp.user";
    public static final String MAIL_SMTP_PASSWORD = "mail.smtp.password";
    public static final String MAIL_TRANSPORT_PROTOCOL = "mail.transport.protocol";

    public static final String MAIL_TRANSPORT_TLS = "mail.smtp.starttls.enable";
    public static final String MAIL_SMTP_SOCKET_FACTORY_CLASS = "mail.smtp.socketFactory.class";
    public static final String MAIL_SMTP_SOCKET_FACTORY_PORT = "mail.smtp.socketFactory.port";
    public static final String MAIL_SMTP_SOCKET_FACTORY_FALLBACK = "mail.smtp.socketFactory.fallback";

    public static final String DEFAULT_SOCKET_FACTORY_CLASS = "javax.net.ssl.SSLSocketFactory";
    public static final String DEFAULT_SOCKET_FACTORY_PORT = "465"; // "587"


    public static void sendEmail(Properties mailProperties, String from, String subject, String body,
            List<String> recipients, List<String> ccRecipients, List<String> bccRecipients) {
        Session mailSession = getSessionInstance(mailProperties);
        sendEmail(mailSession, from, subject, body, recipients, ccRecipients, bccRecipients);
    }

    private static Session getSessionInstance(final Properties mailProperties) {
        Session session = null;
        if (Boolean.valueOf(mailProperties.getProperty(MAIL_SMTP_AUTH)) == false) {
            session = Session.getDefaultInstance(mailProperties);
        } else {
            if (mailProperties.get(MAIL_SMTP_SOCKET_FACTORY_CLASS) == null) {
                mailProperties.put(MAIL_SMTP_SOCKET_FACTORY_CLASS, DEFAULT_SOCKET_FACTORY_CLASS);
            }
            if (mailProperties.get(MailUtil.MAIL_SMTP_SOCKET_FACTORY_PORT) == null) {
                mailProperties.put(MailUtil.MAIL_SMTP_SOCKET_FACTORY_PORT, DEFAULT_SOCKET_FACTORY_PORT);
            }
            session = Session.getDefaultInstance(mailProperties, new javax.mail.Authenticator() {
                protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
                    String usr = mailProperties.getProperty(MAIL_SMTP_USER);
                    String pwd = mailProperties.getProperty(MAIL_SMTP_PASSWORD);
                    return new javax.mail.PasswordAuthentication(usr, pwd);
                }
            });
        }
        return session;
    }

    public static void sendEmail(Session mailSession, String from, String subject, String body,
            List<String> recipients, List<String> ccRecipients, List<String> bccRecipients) {
        try {
            MimeMessage message = new MimeMessage(mailSession);
            message.setFrom(new InternetAddress(from));
            message.setSubject(subject);
            message.setText(body);
            // message.setSentDate(new java.util.Date());

            if (recipients != null) {
                for (String to : recipients) {
                    message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
                }
            }

            if (ccRecipients != null) {
                for (String cc : ccRecipients) {
                    message.addRecipients(Message.RecipientType.CC, InternetAddress.parse(cc, false));
                }
            }

            if (bccRecipients != null) {
                for (String bcc : bccRecipients) {
                    message.addRecipients(Message.RecipientType.BCC, InternetAddress.parse(bcc, false));
                }
            }

            Transport.send(message);
        } catch (Exception ex) { // MessagingException
            throw new IllegalArgumentException("Failed to send email from: " + from, ex);
        }
    }

    //
    // shorter interfaces to send email
    //
    public static void sendEmail(Session mailSession, String from, String subject, String body,
            List<String> recipients, List<String> ccRecipients) {
        sendEmail(mailSession, from, subject, body, recipients, ccRecipients, null);
    }

    public static void sendEmail(Session mailSession, String from, String subject, String body,
            List<String> recipients) {
        sendEmail(mailSession, from, subject, body, recipients, null, null);
    }

    public static void sendEmail(Session mailSession, String subject, String body, String from, String to) {
        List<String> recipients = Arrays.asList(to);
        sendEmail(mailSession, from, subject, body, recipients, null, null);
    }

    public static void sendEmail(Properties mailProperties, String from, String subject, String body,
            List<String> recipients, List<String> ccRecipients) {
        sendEmail(mailProperties, from, subject, body, recipients, ccRecipients, null);
    }

    public static void sendEmail(Properties mailProperties, String from, String subject, String body,
            List<String> recipients) {
        sendEmail(mailProperties, from, subject, body, recipients, null, null);
    }

    public static void sendEmail(String from, String to, String subject, String body, Properties props) {
        List<String> recipients = Arrays.asList(to);
        sendEmail(props, from, subject, body, recipients, null, null);
    }

    public static void sendEmailWithCc(String from, String to, String subject, String body, Properties props,
            String... ccAddrs) {
        List<String> recipients = Arrays.asList(to);
        List<String> ccRecipients = new java.util.ArrayList<String>();
        for (String cc : ccAddrs) {
            ccRecipients.add(cc);
        }
        sendEmail(props, from, subject, body, recipients, ccRecipients, null);
    }

    //
    // extra methods
    //
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
