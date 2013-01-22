package vn.ducquoc.activiti.task;

import java.util.List;
import java.util.Properties;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.activiti.engine.impl.el.Expression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import vn.ducquoc.activiti.config.MailConfig;
import vn.ducquoc.util.MailUtil;

public class CustomMailTask implements JavaDelegate {
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomMailTask.class.getName());
    
    private Expression subject;
    private Expression content;
    private Expression sender;
    private Expression recipients;
    private Expression cc;
    private Expression bcc;
    
    @SuppressWarnings("unchecked")
    // @Override
    public void execute(DelegateExecution execution) throws Exception {
        LOGGER.info("Prepare sending email ... ");

        try {
            MailConfig mailConf = (MailConfig) execution.getVariable("mailConfig");

            Properties props = mailConf.getProperties();
            if (!"localhost".equalsIgnoreCase(mailConf.getHost())) {
                props.setProperty("mail.host", mailConf.getHost()); // MailUtil.MAIL_HOST
            }
            if (mailConf.isAuth() == true) {
                props.put(MailUtil.MAIL_SMTP_AUTH, String.valueOf(mailConf.isAuth()));
                props.put(MailUtil.MAIL_SMTP_USER, mailConf.getUser());
                props.put(MailUtil.MAIL_SMTP_PASSWORD, mailConf.getPassword());
                // props.put(MailUtil.MAIL_SMTP_SOCKET_FACTORY_CLASS,
                // MailUtil.DEFAULT_SOCKET_FACTORY_CLASS);
                // props.put(MailUtil.MAIL_SMTP_SOCKET_FACTORY_PORT, "465");
            }

            String from = (String) sender.getValue(execution);
            String sub = (String) subject.getValue(execution);
            String msg = (String) content.getValue(execution);

            List<String> toList = (List<String>) recipients.getValue(execution);

            List<String> ccList = java.util.Collections.EMPTY_LIST;
            if (cc != null) {
                ccList = (List<String>) cc.getValue(execution);
            }
            List<String> bccList = java.util.Collections.EMPTY_LIST;
            if (bcc != null) {
                bccList = (List<String>) bcc.getValue(execution);
            }

            MailUtil.sendEmail(props, from, sub, msg, toList, ccList, bccList);
        } catch (Exception ex) {
            LOGGER.error("==== SENDING EMAIL ERRORED ====\n {}", ex.getMessage(), ex);
        }

    }

}
