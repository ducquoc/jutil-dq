package vn.ducquoc.jutil;

import java.lang.reflect.Method;

import javax.swing.JOptionPane;

/**
 * Helper class for invoking browser.
 * 
 * @author ducquoc
 * @see org.apache.commons.lang.StringUtils
 * @see org.mortbay.util.StringUtil
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class BrowserUtil {

    private static final String BROWSER_NOT_FOUND = "Web browser not found \n";

    private static final String BROWSER_ERROR = "Error attempting to launch web browser: \n";

    private static final String LAUNCHING_DEFAULT_BROWSER = "[INFO] Launching default browser...";

    /**
     * Opens the default web browser to the given URL
     * 
     * @param url
     */
    public static void openUrl(String url) {
        String osName = System.getProperty("os.name");
        try {
            if (osName.startsWith("Mac OS")) { // Safari
                Class fileMgr = Class.forName("com.apple.eio.FileManager");
                Method openURL = fileMgr.getDeclaredMethod("openURL", new Class[] { String.class });
                openURL.invoke(null, new Object[] { url });

            } else if (osName.startsWith("Windows")) {
                System.out.println(LAUNCHING_DEFAULT_BROWSER);
                Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + url);

            } else { // Linux or Unix
                String[] browsers = { "google-chrome", "firefox", "opera", "konqueror", "safari", "epiphany", "mozilla" };
                String browser = null;
                for (int count = 0; count < browsers.length && browser == null; count++)
                    if (Runtime.getRuntime().exec(new String[] { "which", browsers[count] }).waitFor() == 0)
                        browser = browsers[count];
                if (browser == null)
                    throw new RuntimeException(BROWSER_NOT_FOUND);
                else
                    Runtime.getRuntime().exec(new String[] { browser, url });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, BROWSER_ERROR + ex.getMessage());
        }
    }

    public static void main(String args[]) {
        openUrl("www.google.com");
        // openUrl("www.wikipedia.org");
    }

}
