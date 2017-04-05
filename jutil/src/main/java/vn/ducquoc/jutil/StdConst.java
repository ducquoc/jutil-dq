package vn.ducquoc.jutil;

/**
 * Base constant class as for standards (ISO, RFC, ...) if not exists yet.
 * 
 * @author ducquoc
 * @see javax.swing.SwingConstants
 * @see java.time.format.DateTimeFormatter.ISO_INSTANT (Java 8)
 */
public interface StdConst {

  static final String ISO8601_UTC_JPATTERN = "yyyy-MM-dd'T'HH:mm'Z'";

  static final String RFC1123_JPATTERN = "EEE, d MMM yyyy HH:mm:ss z";

}
