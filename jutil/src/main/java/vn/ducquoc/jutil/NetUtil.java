package vn.ducquoc.jutil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Helper class for network operations (mostly HTTP).
 *
 * @author ducquoc
 * @see sun.net.util.URLUtil
 * @see sun.net.util.IPAddressUtil
 * @see org.apache.commons.net.util.SubnetUtils
 * @see org.apache.http.conn.util.InetAddressUtils
 * @see com.google.common.net.InetAddresses
 * @see net.codejava.networking.HttpUtility
 */
@SuppressWarnings("restriction")
public class NetUtil {

  public static boolean checkAvailable(String urlToRead) {
    boolean result = false;
    URL url = null;
    try {
      url = new URL(urlToRead);
      HttpURLConnection conn = (HttpURLConnection) url.openConnection();
      conn.setUseCaches(false);
      //conn.setRequestMethod("GET");
      //conn.setDoOutput(false);
      conn.setInstanceFollowRedirects(true);// not enough,  handle below
      int statusCode = conn.getResponseCode();
      if (statusCode == 200) {//HttpURLConnection.HTTP_OK
        result = true;
      } //else 
      if (statusCode == 301 || statusCode == 301 || statusCode == 303) {
        //redirects: HttpURLConnection.HTTP_ (MOVED_TEMP,MOVED_PERM,SEE_OTHER)
        String newUrlToRead = conn.getHeaderField("Location");
        return checkAvailable(newUrlToRead);
      }
    } catch (MalformedURLException ex) {
      System.out.println("Invalid URL: " + ex.getMessage());
    } catch (IOException ioEx) {
      System.out.println("Not connected: " + ioEx.getMessage());
    }

    return result;
  }

  public static String getAsHtml(String urlToRead) throws Exception {
      StringBuilder result = new StringBuilder();
      URL url = new URL(urlToRead);
      HttpURLConnection conn = (HttpURLConnection) url.openConnection();
      conn.setRequestMethod("GET");
      BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
      String line;
      while ((line = rd.readLine()) != null) {
         result.append(line);
      }
      rd.close();
      return result.toString();
   }


  public static void main(String[] args) {
    System.out.println("Testing using main method");

    String urlStr = "http://www.ducquoc.net";
    boolean isOkToGet = checkAvailable(urlStr);
    System.out.println("Testing " + urlStr + " up? : " + isOkToGet);

    urlStr = "http://www.ducquoc.com";//not existent
    isOkToGet = checkAvailable(urlStr);
    System.out.println("Testing " + urlStr + " up? : " + isOkToGet);

    urlStr = "http://ducquoc.wordpress.com";//redirect to HTTPS
    isOkToGet = checkAvailable(urlStr);
    System.out.println("Testing " + urlStr + " up? : " + isOkToGet);

  }

}
