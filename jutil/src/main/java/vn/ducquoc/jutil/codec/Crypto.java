package vn.ducquoc.jutil.codec;

import java.io.UnsupportedEncodingException;
import java.security.*;
import java.security.spec.*;

import javax.crypto.*;
import javax.crypto.spec.*;

/**
 * Helper class for cryptography operations (mostly AES/RSA).
 *
 * @author ducquoc
 * @see android.util.Base64
 * @see org.apache.commons.codec.binary.Base64
 * @see com.android.server.accounts.CryptoHelper
 * @see com.mkyong.crypto.utils.CryptoUtils
 * @see org.netty.encryption.CryptUtil
 * @see org.owasp.esapi.crypto.CryptoHelper
 * @see org.owasp.java.crypto.AES
 */
@SuppressWarnings({"restriction", "unused"})
public class Crypto {

    public static final String UTF_8 = java.nio.charset.StandardCharsets.UTF_8.toString(); // "UTF-8";
    public static final String BC_PROV = "BouncyCastleProvider";

    static {
        Security.setProperty("crypto.policy", "unlimited"); // 256-bit key support
//        System.out.println(Arrays.stream(Security.getProviders()).collect(Collectors.toList()));
//        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
    }

    public static String sha(String text, boolean... sha1) throws NoSuchAlgorithmException,
            UnsupportedEncodingException {
        boolean sha160bit = (sha1.length > 0 && sha1[0]);
        MessageDigest digest = sha160bit ? MessageDigest.getInstance("SHA") : MessageDigest.getInstance("SHA-256");
        byte[] bArray = digest.digest(text.getBytes(UTF_8));
        return hex(bArray);
    }

    public static String hex(byte[] bytes, boolean... upperCase) { // base16
        StringBuilder result = new StringBuilder();
        for (byte b : bytes) {
            result.append(String.format("%02x", b));
        }
        String hex = result.toString();
        return (upperCase.length > 0 && upperCase[0]) ? hex.toUpperCase() : hex;
    }

    public static byte[] hex2byteArr(String hex) {
        byte[] ba = new byte[hex.length() / 2]; // javax.xml.bind.DatatypeConverter.parseHexBinary(hex);
        for (int i = 0; i < hex.length(); i += 2) {
            ba[i / 2] = (byte) Integer.parseInt(hex.substring(i, i + 2), 16);
        } // hex2byte: (byte) ((Character.digit(hex.charAt(i), 16) << 4) + Character.digit(hex.charAt(i + 1), 16));
        return ba;
    }

    public static String hex2b64(String hex) throws UnsupportedEncodingException {
        byte[] bArray = hex2byteArr(hex);
        return encode64ToStr(bArray);
    }

    public static byte[] encode64(byte[] bArray) {
        return java.util.Base64.getEncoder().encode(bArray);
    }

    public static String encode64ToStr(byte[] bArray) throws UnsupportedEncodingException {
        return new String(encode64(bArray), UTF_8);
    }

    public static byte[] decode64(byte[] bArray) {
        return java.util.Base64.getDecoder().decode(bArray);
    }

    public static String decode64ToStr(byte[] bArray) throws UnsupportedEncodingException {
        return new String(decode64(bArray), UTF_8);
    }

    public static String hex2bin(String hex, boolean... padLeft0) {
        String bin = new java.math.BigInteger(hex, 16).toString(2);
        return (padLeft0.length > 0 && padLeft0[0]) ? padLeftZeros(bin, 4 * hex.length()) : bin;
    }

    public static String padLeftZeros(String text, int length) {
        return String.format("%1$" + length + "s", text).replace(' ', '0');
    }

    public static String padChars(int length, Character paddingChar) {
        String paddingText = String.format("%0" + length + "d", 0); // default 0
        return paddingChar == null ? paddingText : paddingText.replace('0', paddingChar);
    }

    public static byte[] randomNonce(int numBytes) {
        byte[] nonce = new byte[numBytes];
        new java.security.SecureRandom().nextBytes(nonce); // java.util.Random() may be not secure enough
        return nonce;
    }

    public static int getNonPadRightLength(byte[] original, char paddingChar) { // default padding 0
        int lastLength = original.length;
        for (int i = original.length - 1; i > original.length - 16 && i >= 0; i--) {
            if (original[i] == (byte) paddingChar) { // default padding 0
                lastLength--;
            } else {
                break;
            }
        }
        return lastLength;
    }

    public static void main(String[] args) throws Exception {
        System.out.println("[DEBUG] Testing using main method");
//        System.out.println("hex2bin: => " + hex2bin("4BCAF3D7284F8AB3"));
//        System.out.println("hex2b64: => " + hex2b64("e26d81b8cf6c506e526a3c1f05aadaa4")); // 4m2BuM9sUG5SajwfBarapA==
//        System.out.println("hex2byteArr: => " + java.util.Arrays.toString(hex2byteArr("1a2b0e")));
//        System.out.println("sha: => " + sha("", true)); // da39a3ee5e6b4b0d3255bfef95601890afd80709

        final String K_32 = "60117D3B5EEC16D164B3C93F42FF33EA";
        final String ENC_T1_32 = "438F50FF81F83B8AD8B31535EAD4C6FF"; // "847"
        final String DEC_T1_3 = "847";
        System.out.println("AES decrypt: " + AES.decrypt(ENC_T1_32, hex2byteArr(K_32), null));
        String encrypted = AES.encrypt(DEC_T1_3, hex2byteArr(K_32), null);
        System.out.println("AES encrypted: " + encrypted);
//        String decrypted = AES.decrypt(encrypted, hex2byteArr(K_32), null);
//        System.out.println("AES decrypted: " + decrypted);
//        String encrypted64 = AES.encrypt(DEC_T1_3, hex2byteArr(K_32), null, true);
//        System.out.println("AES encrypted base64: " + encrypted64);
//        String decrypted64 = AES.decrypt(encrypted64, hex2byteArr(K_32), null);
//        System.out.println("AES decrypted base64: " + decrypted64);
    }

    /**
     * AES decryptor/encryptor (built on top of JCE or BC).
     *
     * @author ducquoc
     */
    public static class AES {
        public static final String ECB_NOPADDING = "AES/ECB/NoPadding";
        public static final String ECB_PKCS5 = "AES/ECB/PKCS5Padding";
        public static final String ECB_ZEROPADDING = "AES/ECB/ZeroBytePadding"; // BC, (or ECB_NOPADDING + padding \0 )
        public static final String CBC_NOPADDING = "AES/CBC/NoPadding";
        public static final String CBC_PKCS5 = "AES/CBC/PKCS5Padding"; // Java 7+
        public static final String GCM_NOPADDING = "AES/GCM/NoPadding";
        public static final String GCM_PKCS5 = "AES/GCM/PKCS5Padding";
        public static final String AES_DEFAULT_MODE = ECB_NOPADDING;
        public static final int IV_LENGTH = 16; // default IV length in bytes
        public static final int GCM_IV_LENGTH = 12; // default GCM IV 12 -> Tag Length 8*12 bits
        public static final char PADDING_CHAR = '\0'; // ECB padding may yield various encrypted, still decrypted OK

        /**
         * @param base64orHex - default empty/false, i.e. hex format by default.
         */
        public static String decrypt(String encrypted, byte key[], String mode, boolean... base64orHex)
                throws GeneralSecurityException, UnsupportedEncodingException {
            boolean base64InputFormat = (base64orHex.length > 0 && base64orHex[0]); // default: false -> hex
            boolean key256bits = (key.length == 256);
//            System.out.println("[decrypt] key length: " + 8 * key.length + " bits - " + hex(key, true));
            boolean inputHex = encrypted.matches("[A-Fa-f0-9]+");
            System.out.println("[decrypt] input " + (inputHex ? "Hex" : "Base64") + " detected - " + encrypted);
            byte[] ciphertextBytes = (inputHex && (!base64InputFormat))
                    ? hex2byteArr(encrypted) : decode64(encrypted.getBytes(UTF_8));
            if (mode == null || mode.trim().length() == 0) {
                mode = AES_DEFAULT_MODE;
            }

            byte[] original = null; // decryptedBytes
            Cipher cipher = Cipher.getInstance(mode); // "SunJCE" or BC_PROV
            if (mode.contains("ECB")) {
                ecbInitCipher(cipher, key);
                original = cipher.doFinal(ciphertextBytes);
            } else if (mode.contains("GCM")) {
                gcmInitCipher(cipher, key, ciphertextBytes, key256bits);
                original = cipher.doFinal(ciphertextBytes, GCM_IV_LENGTH, ciphertextBytes.length - GCM_IV_LENGTH);
            } else { // CBC
                ciphertextBytes = cbcInitCipher(cipher, key, ciphertextBytes);
                original = cipher.doFinal(ciphertextBytes);
            }

            // Possible zero bytes at the end. So we may just get nonPadding length
            String nonPadding = new String(original, 0, getNonPadRightLength(original, PADDING_CHAR), UTF_8);
//            String text = new String(original, UTF_8); // return text.replaceAll(PADDING_CHAR + "+$", "");
            return nonPadding;
        }

        private static void ecbInitCipher(Cipher cipher, byte[] key, boolean... enc) throws InvalidKeyException {
            SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
            int cMode = (enc.length > 0 && enc[0]) ? Cipher.ENCRYPT_MODE : Cipher.DECRYPT_MODE; // Cipher.ENCRYPT_MODE
            cipher.init(cMode, skeySpec); // ECB encrypt
        }

        private static void gcmInitCipher(Cipher cipher, byte[] key, byte[] ciphertextBytes, boolean key256,
                                          boolean... enc)
                throws InvalidKeyException, InvalidAlgorithmParameterException {
            SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
            byte[] gcmIv = randomNonce(GCM_IV_LENGTH); // Tag Length (bits) should be 8 * GCM IV length (bytes)
            AlgorithmParameterSpec parameterSpec = new GCMParameterSpec(
                    key256 ? 256 : 128, ciphertextBytes, 0, gcmIv.length);
            int cipherMode = (enc.length > 0 && enc[0]) ? Cipher.ENCRYPT_MODE : Cipher.DECRYPT_MODE;
            cipher.init(cipherMode, skeySpec, parameterSpec); // GCM mode
        }

        private static byte[] cbcInitCipher(Cipher cipher, byte[] key, byte[] ciphertextBytes, boolean... enc)
                throws InvalidKeyException, InvalidAlgorithmParameterException {
            SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
            byte[] ivByteArr = (ciphertextBytes == null) ? randomNonce(IV_LENGTH) : ciphertextBytes;
            IvParameterSpec iv = new IvParameterSpec(ivByteArr, 0, IV_LENGTH);
//            ciphertextBytes = java.util.Arrays.copyOfRange(ciphertextBytes, IV_LENGTH, ciphertextBytes.length);
            int cipherMode = (enc.length > 0 && enc[0]) ? Cipher.ENCRYPT_MODE : Cipher.DECRYPT_MODE;
            cipher.init(cipherMode, skeySpec, iv); // CBC mode
            return ciphertextBytes;
        }

        /**
         * @param base64orHex - default empty/false, i.e. hex format by default.
         */
        public static String encrypt(String text, byte key[], String mode, boolean... base64orHex)
                throws GeneralSecurityException, UnsupportedEncodingException {
            boolean base64OutputFormat = (base64orHex.length > 0 && base64orHex[0]); // default: false -> hex
//            System.out.println("[encrypt] key length: " + 8 * key.length + " bits - " + hex(key, true));
            if (mode == null || mode.trim().length() == 0) {
                mode = AES_DEFAULT_MODE;
            }

            byte[] encrypted = null;
            Cipher cipher = Cipher.getInstance(mode); // "SunJCE" or BC_PROV
            if (mode.contains("ECB")) {
                ecbInitCipher(cipher, key, true);
            } else if (mode.contains("GCM")) {
                gcmInitCipher(cipher, key, null, key.length == 256, true);
            } else { // CBC
                cbcInitCipher(cipher, key, null, true);
            }

            int paddingSize = 16 - (text.length() % 16);
            String padding = mode.contains("GCM") ? "" : padChars(paddingSize, PADDING_CHAR);
            String padded = text + padding;
            encrypted = cipher.doFinal(padded.getBytes(UTF_8));
            String encryptedText = base64OutputFormat ? encode64ToStr(encrypted) : hex(encrypted, true);
            return encryptedText;
        }
    }
}
