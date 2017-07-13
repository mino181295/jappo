package it.unibo.matteo.jappo.Utils;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class SecurityHelper {

    private static String algorithm = "AES";
    private static byte[] keyValue = new byte[] {'0','2','3','4','5','6','7','8','9','1','2','3','4','5','6','7'};

    private static Key secret = null;

    public static Key generateKey()
            throws NoSuchAlgorithmException, InvalidKeySpecException
    {
        if (secret != null){
            return secret;
        } else {
            secret = new SecretKeySpec(keyValue, algorithm);
            return  secret;
        }
    }

    public static byte[] encryptMsg(String message)
            throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidParameterSpecException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException
    {
   /* Encrypt the message. */
        try {
            generateKey();
        } catch (InvalidKeySpecException e) {e.printStackTrace();}
        Cipher cipher = null;
        cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secret);
        byte[] cipherText = cipher.doFinal(message.getBytes("UTF-8"));
        return cipherText;
    }

    public static String decryptMsg(byte[] cipherText)
            throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidParameterSpecException, InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, UnsupportedEncodingException
    {
    /* Decrypt the message, given derived encContentValues and initialization vector. */
        try {
            generateKey();
        } catch (InvalidKeySpecException e) {e.printStackTrace();}
        Cipher cipher = null;
        cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secret);
        String decryptString = new String(cipher.doFinal(cipherText), "UTF-8");
        return decryptString;
    }
}
