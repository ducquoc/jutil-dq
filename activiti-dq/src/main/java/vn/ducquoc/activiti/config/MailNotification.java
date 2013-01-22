package vn.ducquoc.activiti.config;

import java.io.Serializable;

/**
 * @author ducquoc
 */
public class MailNotification implements Serializable {

    private static final long serialVersionUID = 6563835465860695642L;

    private MailNotificationDetailConfig general;

    /**
     * @return the general
     */
    public MailNotificationDetailConfig getGeneral() {
        return general;
    }

    /**
     * @param general the general to set
     */
    public void setGeneral(MailNotificationDetailConfig general) {
        this.general = general;
    }

}
