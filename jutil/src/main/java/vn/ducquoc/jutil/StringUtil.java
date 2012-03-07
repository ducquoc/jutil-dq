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

}
