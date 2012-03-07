package vn.ducquoc.jutil;

/**
 * Base exception class as wrapper for checked exception from low-level layers.
 * 
 * @author ducquoc
 * @see http://onjava.com/pub/a/onjava/2003/11/19/exceptions.html
 * @see http://today.java.net/article/2006/04/04/exception-handling-antipatterns
 */
public class UtilException extends RuntimeException {

  private static final long serialVersionUID = 20101122L;

  /**
   * @see RuntimeException#RuntimeException(String, Throwable)
   */
  public UtilException(String message, Throwable cause) {
    super(message, cause);
  }

  /**
   * @see RuntimeException#RuntimeException(String)
   */
  public UtilException(String message) {
    super(message);
  }

}
