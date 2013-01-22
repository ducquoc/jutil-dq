package vn.ducquoc.activiti.config;

import java.io.Serializable;

/**
 * @author ducquoc
 */
public class TenantConfig implements Serializable {

    private static final long serialVersionUID = -6648748332111152721L;

    private MailConfig mailConfig;

    private MailNotification mailNotification;

    /**
     * @return the mailConfig
     */
    public MailConfig getMailConfig() {
        return mailConfig;
    }

    /**
     * @param mailConfig the mailConfig to set
     */
    public void setMailConfig(MailConfig mailConfig) {
        this.mailConfig = mailConfig;
    }

    /**
     * @return the mainNotification
     */
    public MailNotification getMailNotification() {
        return mailNotification;
    }

    /**
     * @param mailNotification the mainNotification to set
     */
    public void setMailNotification(MailNotification mailNotification) {
        this.mailNotification = mailNotification;
    }

}
