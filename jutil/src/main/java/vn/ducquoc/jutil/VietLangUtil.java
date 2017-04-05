package vn.ducquoc.jutil;

/**
 * Helper class for Vietnamese string operations.
 * <br></br>
 * 
 * For example, 'á': utf-8 code <b>\u00e1</b>, HTML entity: <b>&amp;#225;</b>
 * 
 * @author ducquoc
 * @see java.text.Normalizer
 * @see http://unikey.vn
 */
public class VietLangUtil {

  public static String removeDiaMarks(String text) {
    String result = text;
    String temp = java.text.Normalizer.normalize(text, java.text.Normalizer.Form.NFD);
    java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
    result = pattern.matcher(temp).replaceAll("");

    return result;
  }

  public static String removeVietMarks(String text) {
    String result = removeDiaMarks(text);
    result = result.replaceAll("đ", "d").replaceAll("Đ", "D");

    return result;
  }

  public static String removeVnMarks(String text) {
    String result = text;//straight forward, not optimize perf
    result = result.replaceAll("[áàảãạ]", "a").replaceAll("[ÁÀẢÃẠ]", "A");
    result = result.replaceAll("[éèẻẽẹ]", "e").replaceAll("[ÉÈẺẼẸ]", "E");
    result = result.replaceAll("[íìỉĩị]", "i").replaceAll("[ÍÌỈĨỊ]", "I");
    result = result.replaceAll("[óòỏõọ]", "o").replaceAll("[ÓÒỎÕỌ]", "O");
    result = result.replaceAll("[úùủũụ]", "u").replaceAll("[ÚÙỦŨỤ]", "U");
    result = result.replaceAll("[ýỳỷỹỵ]", "y").replaceAll("[ÝỲỶỸỴ]", "Y");
    result = result.replaceAll("[ôốồổỗộ]", "o").replaceAll("[ỐỒỔỖỘ]", "O");
    result = result.replaceAll("[ơớờởỡợ]", "o").replaceAll("[ỚỜỞỠỢ]", "O");
    result = result.replaceAll("[ưứừửữự]", "u").replaceAll("[ƯỨỪỬỮỰ]", "U");
    result = result.replaceAll("[ăắằẳẵặ]", "a").replaceAll("[ĂẮẰẲẴẶ]", "A");
    result = result.replaceAll("[âấầẩẫậ]", "a").replaceAll("[ÂẤẦẨẪẬ]", "A");
    result = result.replaceAll("đ", "d").replaceAll("Đ", "D");

    return result;
  }

  // source array, order ascending UTF-8 code
  public static char[] SOURCE_CHARACTERS = { 'À', 'Á', 'Â', 'Ã', 'È', 'É',
      'Ê', 'Ì', 'Í', 'Ò', 'Ó', 'Ô', 'Õ', 'Ù', 'Ú', 'Ý', 'à', 'á', 'â',
      'ã', 'è', 'é', 'ê', 'ì', 'í', 'ò', 'ó', 'ô', 'õ', 'ù', 'ú', 'ý',
      'Ă', 'ă', 'Đ', 'đ', 'Ĩ', 'ĩ', 'Ũ', 'ũ', 'Ơ', 'ơ', 'Ư', 'ư', 'Ạ',
      'ạ', 'Ả', 'ả', 'Ấ', 'ấ', 'Ầ', 'ầ', 'Ẩ', 'ẩ', 'Ẫ', 'ẫ', 'Ậ', 'ậ',
      'Ắ', 'ắ', 'Ằ', 'ằ', 'Ẳ', 'ẳ', 'Ẵ', 'ẵ', 'Ặ', 'ặ', 'Ẹ', 'ẹ', 'Ẻ',
      'ẻ', 'Ẽ', 'ẽ', 'Ế', 'ế', 'Ề', 'ề', 'Ể', 'ể', 'Ễ', 'ễ', 'Ệ', 'ệ',
      'Ỉ', 'ỉ', 'Ị', 'ị', 'Ọ', 'ọ', 'Ỏ', 'ỏ', 'Ố', 'ố', 'Ồ', 'ồ', 'Ổ',
      'ổ', 'Ỗ', 'ỗ', 'Ộ', 'ộ', 'Ớ', 'ớ', 'Ờ', 'ờ', 'Ở', 'ở', 'Ỡ', 'ỡ',
      'Ợ', 'ợ', 'Ụ', 'ụ', 'Ủ', 'ủ', 'Ứ', 'ứ', 'Ừ', 'ừ', 'Ử', 'ử', 'Ữ',
      'ữ', 'Ự', 'ự', 'Ỳ', 'ỳ', 'Ỵ', 'ỵ', 'Ỷ', 'ỷ', 'Ỹ', 'ỹ'};

  public static char[] DESTINATION_CHARACTERS = { 'A', 'A', 'A', 'A', 'E', 'E',
      'E', 'I', 'I', 'O', 'O', 'O', 'O', 'U', 'U', 'Y', 'a', 'a', 'a',
      'a', 'e', 'e', 'e', 'i', 'i', 'o', 'o', 'o', 'o', 'u', 'u', 'y',
      'A', 'a', 'D', 'd', 'I', 'i', 'U', 'u', 'O', 'o', 'U', 'u', 'A',
      'a', 'A', 'a', 'A', 'a', 'A', 'a', 'A', 'a', 'A', 'a', 'A', 'a',
      'A', 'a', 'A', 'a', 'A', 'a', 'A', 'a', 'A', 'a', 'E', 'e', 'E',
      'e', 'E', 'e', 'E', 'e', 'E', 'e', 'E', 'e', 'E', 'e', 'E', 'e',
      'I', 'i', 'I', 'i', 'O', 'o', 'O', 'o', 'O', 'o', 'O', 'o', 'O',
      'o', 'O', 'o', 'O', 'o', 'O', 'o', 'O', 'o', 'O', 'o', 'O', 'o',
      'o', 'U', 'u', 'U', 'u', 'U', 'u', 'U', 'u', 'U', 'u', 'O', 'U',
      'u', 'U', 'u', 'Y', 'y', 'Y', 'y', 'Y', 'y', 'Y', 'y'};

  public static char removeVietMarksFast(char ch) {
    char result = ch;
    int index = java.util.Arrays.binarySearch(SOURCE_CHARACTERS, result);
    if (index >= 0) {
      result = DESTINATION_CHARACTERS[index];
    }
    return result;
  }

  public static String removeVietMarksFast(String text) {
      StringBuilder sb = new StringBuilder(text);
      for (int i = 0; i < sb.length(); i++) {
          sb.setCharAt(i, removeVietMarksFast(sb.charAt(i)));
      }
      return sb.toString();
  }

  public static void main(String[] args) {
    System.out.println("Testing using main method");
    String test1 = "Dù rằng đời ta thích hoa hồng, kẻ thù buộc ta ôm cây súng!";
    String test2 = "Giờ em đang nơi nao, Đời em đang ra sao, người có biết nỗi đau lòng anh";
    String test3 = "á à ả ã ạ Á À Ả Ã Ạ é è ẻ ẽ ẹ É È Ẻ Ẽ Ẹ í ì ỉ ĩ ị Í Ì Ỉ Ĩ Ị" +
            " ó ò ỏ õ ọ Ó Ò Ỏ Õ Ọ ố ồ ổ ỗ ộ Ố Ồ Ổ Ỗ Ộ ớ ờ ở ỡ ợ Ớ Ờ Ở Ỡ Ợ" + 
            " ú ù ủ ũ ụ Ú Ù Ủ Ũ Ụ ứ ừ ử ữ ự Ứ Ừ Ử Ữ Ự ý ỳ ỷ ỹ ỵ Ý Ỳ Ỷ Ỹ Ỵ " +
            " ắ ằ ẳ ẵ ặ Ắ Ằ Ẳ Ẵ Ặ ấ ầ ẩ ẫ ậ Ấ Ầ Ẩ Ẫ Ậ đ Đ ";

    String result1 = removeDiaMarks(test1);
    String result2 = removeDiaMarks(test2);
    String result3 = removeDiaMarks(test3);

    System.out.println("Output: " + result1 + "\n\t " + result2 +"\n\t " + result3);

    System.out.println("Output Viet: " + removeVietMarks(test3));
    System.out.println("Output VN: " + removeVnMarks(test3));
    System.out.println("Output Viet Fast: " + removeVietMarksFast(test3));
  }

}
