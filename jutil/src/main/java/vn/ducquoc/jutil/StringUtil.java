package vn.ducquoc.jutil;

/**
 * Helper class for String operations.
 * 
 * @author ducquoc
 * @see org.apache.commons.lang.StringUtils
 * @see org.mortbay.util.StringUtil
 */
public class StringUtil {

  public static boolean isPalindrome(String str) {
    long strLength = str.length();
    for (int i = 0; i < strLength / 2; i++) {
      if (str.charAt(i) != str.charAt(str.length() - 1 - i))
        return false;
    }
    return true;
  }

  public static boolean isLenientPalindrome(String str) {
    String standardizedStr = str.replaceAll("'|,|\\.|\\s", "").toLowerCase();
    return isPalindrome(standardizedStr);
  }

  public static boolean isBlank(String str) {
    if (str == null) {
      return true;
    }
    for (int i = 0; i < str.length(); i++) {
      if ((Character.isWhitespace(str.charAt(i)) == false)) {
        return false;
      }
    }
    return true;
  }

  public static boolean isNotBlank(String str) {
    return !isBlank(str);
  }

}
