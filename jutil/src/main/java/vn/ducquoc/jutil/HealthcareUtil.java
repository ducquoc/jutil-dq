package vn.ducquoc.jutil;

/**
 * Helper class for Health Care related stuff.
 * 
 * @author ducquoc
 * @see com.gnuc.java.ccc.Luhn
 * @see https://www.claredi.com/download/npi_resources.php
 */
public class HealthcareUtil {

    /**
     * Checks for valid NPI number.
     * 
     * @param npiNumberText
     * @return true if valid, false otherwise
     * @see http://en.wikipedia.org/wiki/National_Provider_Identifier
     */
    public static boolean isValidNpi(String npiNumberText) {
        if (npiNumberText == null || npiNumberText.matches("^(80840)?\\d{10}$") == false) {
            return false;
        }
        String npiNumber = (npiNumberText.length() == 15) ? npiNumberText.replaceFirst("^80840", "") : npiNumberText;

        long checkDigit = Long.valueOf(npiNumber) % 10;
        String npi9Number = npiNumber.replaceFirst(String.valueOf(checkDigit) + "$", "");

        return checkDigit == calcNpiDigit(npi9Number);
    }

    public static long calcNpiDigit(String npi9Number) {
        return calcLuhnDigit(npi9Number, 9);
    }

    public static long calcLuhnDigit(String npi9Number, int length) {
        StringBuffer npiPad = new StringBuffer("");
        for (int i = 0; i < length - npi9Number.length(); i++) {
            npiPad.append("0");
        }
        String npi = npiPad.toString() + npi9Number;

        long sumOfPrefix = 24; // 80840 ==> 8 + 0*2 + 8 + 4*2 + 0 = 24
        long sumOfDigits = 0;
        for (int i = 0; i < length; i++) {
            int digitValue = Integer.valueOf(npi.substring(i, i + 1));
            if ((length - i) % 2 != 0) {
                digitValue = 2 * digitValue;
                if (digitValue > 9) {
                    digitValue = digitValue % 10 + 1;
                }
            }
            sumOfDigits = sumOfDigits + digitValue;
        }

        long finalSum = sumOfPrefix + sumOfDigits;
        long modulus10 = finalSum / 10;

        return (10 * (modulus10 + 1) - finalSum) % 10;
    }

    public static boolean luhnValidate(String numberString) {
        char[] charArray = numberString.toCharArray();
        int[] number = new int[charArray.length];
        int total = 0;

        for (int i = 0; i < charArray.length; i++) {
            number[i] = Character.getNumericValue(charArray[i]);
        }

        for (int i = number.length - 2; i > -1; i -= 2) {
            number[i] *= 2;

            if (number[i] > 9)
                number[i] -= 9;
        }

        for (int i = 0; i < number.length; i++)
            total += number[i];

        if (total % 10 != 0)
            return false;

        return true;
    }

}
