package vn.ducquoc.jutil;

/**
 * Helper class for opening browser and retrieving related information.
 * 
 * @author ducquoc
 * @see com.intellij.ide.BrowserUtil
 * @see org.apache.uima.internal.util.BrowserUtil
 * @see com.centerkey.utils.BareBonesBrowserLaunch
 * @see weka.gui.BrowserHelper
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class BrowserUtil {

    private static final String BROWSER_NOT_FOUND_ERROR = "[ERROR] Web browser not found \n";

    private static final String BROWSER_ERROR = "[ERROR] failed to launch web browser: \n";

    private static final String LAUNCHING_DEFAULT_BROWSER = "[INFO] Launching default browser...";

    /**
     * Opens the default web browser to the given URL
     * 
     * @param url
     */
    public static void openUrl(String url) {
        String osName = System.getProperty("os.name");
        try {
            System.out.println(LAUNCHING_DEFAULT_BROWSER);
            if (osName.startsWith("Mac OS")) {
                // Runtime.getRuntime().exec("open " + url);

                Class fileMgr = Class.forName("com.apple.eio.FileManager");
                java.lang.reflect.Method openURL = fileMgr.getDeclaredMethod("openURL", new Class[] { String.class });
                openURL.invoke(null, new Object[] { url });

            } else if (osName.startsWith("Windows")) {
                Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + url);

            } else { // Linux or Unix
                String[] browsers = { "google-chrome", "firefox", "opera", "konqueror", "epiphany", "mozilla", "links" };
                String browser = null;
                for (int count = 0; count < browsers.length && browser == null; count++)
                    if (Runtime.getRuntime().exec(new String[] { "which", browsers[count] }).waitFor() == 0)
                        browser = browsers[count];
                if (browser == null)
                    throw new RuntimeException(BROWSER_NOT_FOUND_ERROR);
                else
                    Runtime.getRuntime().exec(new String[] { browser, url });
            }
        } catch (Exception ex) {
            // System.out.println(BROWSER_ERROR + ex.getMessage());
            javax.swing.JOptionPane.showMessageDialog(null, BROWSER_ERROR + ex.getMessage());

            // java.awt.Desktop.getDesktop().browse(new URI(url)); // JDK6+
        }
    }

    public static void main(String args[]) {
        String url = (args.length == 0) ? "www.google.com" : args[0];

        openUrl(url);
    }

}
