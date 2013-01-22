package vn.ducquoc.activiti.config;

import java.io.Serializable;
import java.util.List;

public class MailNotificationDetailConfig implements Serializable {

    private static final long serialVersionUID = -4448270649699024160L;

    private String subject;
    private String message;
    private String sender;
    private List<String> recipients;
    private List<String> ccRecipients;
    private List<String> bccRecipients;

    /**
     * @return the bccRecipients
     */
    public List<String> getBccRecipients() {
        return bccRecipients;
    }

    /**
     * @param bccRecipients the bccRecipients to set
     */
    public void setBccRecipients(List<String> bccRecipients) {
        this.bccRecipients = bccRecipients;
    }

    /**
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * @param message the message to set
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * @return the sender
     */
    public String getSender() {
        return sender;
    }

    /**
     * @param sender
     *            the sender to set
     */
    public void setSender(String sender) {
        this.sender = sender;
    }

    /**
     * @return the recipients
     */
    public List<String> getRecipients() {
        return recipients;
    }

    /**
     * @param recipients
     *            the recipients to set
     */
    public void setRecipients(List<String> recipients) {
        this.recipients = recipients;
    }

    /**
     * @return the ccRecipients
     */
    public List<String> getCcRecipients() {
        return ccRecipients;
    }

    /**
     * @param ccRecipients
     *            the ccRecipients to set
     */
    public void setCcRecipients(List<String> ccRecipients) {
        this.ccRecipients = ccRecipients;
    }

    /**
     * @return the subject
     */
    public String getSubject() {
        return subject;
    }

    /**
     * @param subject the subject to set
     */
    public void setSubject(String subject) {
        this.subject = subject;
    }

}
