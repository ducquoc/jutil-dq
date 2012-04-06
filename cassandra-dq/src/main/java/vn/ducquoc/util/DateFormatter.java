package vn.ducquoc.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.apache.commons.lang.time.FastDateFormat;

/**
 * Helper class for formatting and parsing Date.
 * 
 * @author ducquoc
 * @see org.apache.catalina.util.FastDateFormat
 * @see org.apache.jasper.util.FastDateFormat
 */
public final class DateFormatter {

  private final SimpleDateFormat sdf;
  private final FastDateFormat fdf;

  public DateFormatter(String pattern, TimeZone tz) {
    this.sdf = new SimpleDateFormat(pattern);
    if (tz != null) {
      this.sdf.setTimeZone(tz);
      this.fdf = FastDateFormat.getInstance(pattern, tz);
    } else {
      this.fdf = FastDateFormat.getInstance(pattern);
    }
  }

  public DateFormatter(String pattern) {
    this(pattern, null);
  }

  public Date parse(String s) throws ParseException {
    // SimpleDateFormat is not thread-safe so we synchronize it here before use.
    // In this case, it's faster than new instance or clone or ThreadLocal
    synchronized (sdf) {
      return sdf.parse(s);
    }
  }

  public String format(Object d) {
    return fdf.format(d);
  }

  public String toPattern() {
    return this.sdf.toPattern();
  }
}
