package vn.ducquoc.activiti.config;

import java.io.Serializable;
import java.util.Properties;

/**
 * @author ducquoc
 */
public class MailConfig implements Serializable {

    private static final long serialVersionUID = 6563835465860695642L;

    private String host;
    private int port;
    private boolean auth;
    private String user;
    private String password;

    /**
     * @return the host
     */
    public String getHost() {
        return host;
    }

    /**
     * @param host
     *            the host to set
     */
    public void setHost(String host) {
        this.host = host;
    }

    /**
     * @return the port
     */
    public int getPort() {
        return port;
    }

    /**
     * @param port
     *            the port to set
     */
    public void setPort(int port) {
        this.port = port;
    }

    /**
     * @return the auth
     */
    public boolean isAuth() {
        return auth;
    }

    /**
     * @param auth
     *            the auth to set
     */
    public void setAuth(boolean auth) {
        this.auth = auth;
    }

    /**
     * @return the user
     */
    public String getUser() {
        return user;
    }

    /**
     * @param user
     *            the user to set
     */
    public void setUser(String user) {
        this.user = user;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password
     *            the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    public Properties getProperties() {
        Properties props = new Properties();
        props.put("mail.smtp.host", getHost());
        props.put("mail.smtp.port", String.valueOf(getPort()));
        props.put("mail.smtp.auth", String.valueOf(isAuth()));
        if (getUser() != null) {
            props.put("mail.smtp.user", getUser());
        }
        if (getPassword() != null) {
            props.put("mail.smtp.password", getPassword());
        }

        // props.put("mail.smtp.debug", "true");
        // props.put("mail.transport.protocol", "smtp");
        return props;
    }
}
