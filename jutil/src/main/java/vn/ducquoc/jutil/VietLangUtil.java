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
    result = result.replaceAll("[ốồổỗộ]", "o").replaceAll("[ỐỒỔỖỘ]", "O");
    result = result.replaceAll("[ớờởỡợ]", "o").replaceAll("[ỚỜỞỠỢ]", "O");
    result = result.replaceAll("[ứừửữự]", "u").replaceAll("[ỨỪỬỮỰ]", "U");
    result = result.replaceAll("đ", "d").replaceAll("Đ", "D");

    return result;
  }


  public static void main(String[] args) {
    System.out.println("Testing using main method");
    String test1 = "Dù rằng đời ta thích hoa hồng, kẻ thù buộc ta ôm cây súng!";
    String test2 = "Giờ em đang nơi nao, Đời em đang ra sao, người có biết nỗi đau lòng anh";
    String test3 = "á à ả ã ạ Á À Ả Ã Ạ é è ẻ ẽ ẹ É È Ẻ Ẽ Ẹ í ì ỉ ĩ ị Í Ì Ỉ Ĩ Ị" +
            " ó ò ỏ õ ọ Ó Ò Ỏ Õ Ọ ố ồ ổ ỗ ộ Ố Ồ Ổ Ỗ Ộ ớ ờ ở ỡ ợ Ớ Ờ Ở Ỡ Ợ" + 
            " ú ù ủ ũ ụ Ú Ù Ủ Ũ Ụ ứ ừ ử ữ ự Ứ Ừ Ử Ữ Ự ý ỳ ỷ ỹ ỵ Ý Ỳ Ỷ Ỹ Ỵ đ Đ ";

    String result1 = removeDiaMarks(test1);
    String result2 = removeDiaMarks(test2);
    String result3 = removeDiaMarks(test3);

    System.out.println("Output: " + result1 + "\n\t " + result2 +"\n\t " + result3);

    System.out.println("Output Viet: " + removeVietMarks(test3));
    System.out.println("Output VN: " + removeVnMarks(test3));
  }

}
