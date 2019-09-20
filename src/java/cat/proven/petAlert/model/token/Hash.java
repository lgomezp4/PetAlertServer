package cat.proven.petAlert.model.token;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Hash class
 *
 * @author Pet Alert
 */
public class Hash {

    /* static methods */
    /**
     * Encrypt the text received with the indicated hash function.
     *
     * @param txt, text in plain format
     * @param hashType MD5 OR SHA1 to encript
     * @return hash in hashType
     */
    public static String getHash(String txt, String hashType) {
        try {
            MessageDigest md = java.security.MessageDigest.getInstance(hashType);
            byte[] array = md.digest(txt.getBytes());
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < array.length; ++i) {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1, 3));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }

    /**
     * Call getHash with MD5 hast type.
     *
     * @param txt text to encript
     * @return hash in hashType
     */
    public static String md5(String txt) {
        return Hash.getHash(txt, "MD5");
    }

    /**
     * Call getHash with SHA1 hast type.
     *
     * @param txt text to encript
     * @return hash in hashType
     */
    public static String sha1(String txt) {
        return Hash.getHash(txt, "SHA1");
    }
}
