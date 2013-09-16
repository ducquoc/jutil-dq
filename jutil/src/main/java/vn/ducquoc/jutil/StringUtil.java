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

    public static String toHexText(String normalText) { // easy to read but slow
        // using format is better than BigInteger.toString(16)
        return String.format("%040x", new java.math.BigInteger(normalText.getBytes()));
    }

    public static String toHexText(String normalText, String encoding) {
        byte[] bytes = null;
        try {
            bytes = normalText.getBytes(encoding);
        } catch (Exception e) {
            // ignore error, fallback to default charset encoding like UTF-8
            bytes = normalText.getBytes(); 
        }
        return String.format("%040x", new java.math.BigInteger(bytes));
    }

    public static String toHexString(String normalText) {
        byte[] bytes = normalText.getBytes();
        StringBuffer strBuff = new StringBuffer();
        for (int i = 0; i < bytes.length; i++) {
            strBuff.append(String.format("%x", bytes[i]));
        }
        return strBuff.toString();
    }

    public static String fromHexString(String hexText) {
        StringBuffer strBuff = new StringBuffer();
        long length = hexText.length();
        for (int i = 0; i < length; i+=2) {
            strBuff.append((char) Integer.parseInt(hexText.substring(i, i + 2), 16));
        }
        return strBuff.toString();
    }

    public static String fromHexString(String hex, String sourceEncoding ) {
        java.io.ByteArrayOutputStream bout = new java.io.ByteArrayOutputStream();
        byte[] buffer = new byte[512];
        int byteStartPosition = 0;

        String result = null;
        try {
            for (int i = 0; i < hex.length(); i += 2) {
                buffer[byteStartPosition++] = (byte) Integer.parseInt(hex.substring(i, i + 2), 16);
                if (byteStartPosition >= buffer.length || i + 2 >= hex.length()) {
                    bout.write(buffer);
                    java.util.Arrays.fill(buffer, 0, buffer.length, (byte) 0);
                    byteStartPosition = 0;
                }
            }
            result = new String(bout.toByteArray(), sourceEncoding);
        } catch (Exception ex) {
            // ignore error, fallback to default charset encoding like UTF-8
            result = fromHexString(hex);
        }

        return result;
    }

    public static String bytesToHex(byte buf[]) {
        StringBuffer strbuf = new StringBuffer(buf.length * 2);

        for (int i = 0; i < buf.length; i++) {
            if (((int) buf[i] & 0xff) < 0x10) {
                strbuf.append("0");
            }
            strbuf.append(Long.toString((int) buf[i] & 0xff, 16));
        }
        return strbuf.toString();
    }

    public static String bytesAsHex(byte[] buf) {
        String s = new java.math.BigInteger(1, buf).toString(16);
        return (s.length() % 2 == 0) ? s : "0" + s;
    }

    public static String bytesToHexText(byte[] buf) { // fast
        final char[] HEX_CHARS = "0123456789abcdef".toCharArray();
        char[] chars = new char[2 * buf.length];
        for (int i = 0; i < buf.length; ++i) {
            chars[2 * i] = HEX_CHARS[(buf[i] & 0xF0) >>> 4]; // sign-bit aware
            chars[2 * i + 1] = HEX_CHARS[buf[i] & 0x0F];
        }
        return new String(chars);
    }

    public static byte[] hexTextToBytes(String hexText) {
        int length = hexText.length();
        byte[] bytes = new byte[length / 2];
        for (int i = 0; i < length; i += 2) {
            bytes[i / 2] = (byte) ((Character.digit(hexText.charAt(i), 16) << 4) + Character.digit(hexText.charAt(i + 1), 16));
        }
        return bytes;
    }

}
