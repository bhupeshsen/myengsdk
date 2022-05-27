package com.p4rc.sdk.crypto;


import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Usage:
 * <pre>
 *    String crypto = SimpleCrypto.encrypt(masterpassword, cleartext)
 *     ...
 *    String cleartext = SimpleCrypto.decrypt(masterpassword, crypto)
 *    * </pre>
 **/

public class CryptoUtils {
    private final static String HEX = "0123456789ABCDEF";
    private String key = "a";
    private String salt = "";
    private byte[] iv = new byte[16];

    public static String encrypt(String seed, String cleartext) throws Exception {
        Encryption encryption = Encryption.getDefault(seed, "1", new byte[16]);

//		byte[] rawKey = getRawKey(seed.getBytes());
//		byte[] result = encrypt(rawKey, cleartext.getBytes());

        return encryption.encryptOrNull(cleartext);
    }

    public static String decrypt(String seed, String encrypted) throws Exception {
        Encryption encryption = Encryption.getDefault(seed, "1", new byte[16]);
//		byte[] rawKey = getRawKey(seed.getBytes());
//		byte[] enc = toByte(encrypted);
//		byte[] result = decrypt(rawKey, enc);
        return encryption.decryptOrNull(encrypted);
    }
	
	/*private static byte[] encrypt(byte[] raw, byte[] clear) throws Exception {
		SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");                 
		Cipher cipher = Cipher.getInstance("AES");             
		cipher.init(Cipher.ENCRYPT_MODE, skeySpec);             
		byte[] encrypted = cipher.doFinal(clear);                 
		return encrypted;         
	}          
	
	private static byte[] decrypt(byte[] raw, byte[] encrypted) throws Exception {
		SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.DECRYPT_MODE, skeySpec);
		byte[] decrypted = cipher.doFinal(encrypted);
		return decrypted;
	}*/

    private static byte[] getRawKey(byte[] seed) throws Exception {
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
//		SecureRandom sr = SecureRandom.getInstance("SHA1PRNG","Crypto");
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        sr.setSeed(seed);
        kgen.init(256, sr); // 192 and 256 bits may not be available
        SecretKey skey = kgen.generateKey();
        byte[] raw = skey.getEncoded();
        return raw;
    }

    private static byte[] encrypt(byte[] data, byte[] key) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        byte[] iv = new byte[cipher.getBlockSize()];
        IvParameterSpec ivParams = new IvParameterSpec(iv);
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key, "AES"), ivParams);
        return cipher.doFinal(data);
    }

    private static byte[] decrypt(byte[] encrypted, byte[] key) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        byte[] ivByte = new byte[cipher.getBlockSize()];
        IvParameterSpec ivParamsSpec = new IvParameterSpec(ivByte);
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key, "AES"), ivParamsSpec);
        return cipher.doFinal(encrypted);
    }

    public static String toHex(String txt) {
        return toHex(txt.getBytes());
    }

    public static String fromHex(String hex) {
        return new String(toByte(hex));
    }

    public static byte[] toByte(String hexString) {
        int len = hexString.length() / 2;
        byte[] result = new byte[len];
        for (int i = 0; i < len; i++)
            result[i] = Integer.valueOf(hexString.substring(2 * i, 2 * i + 2), 16).byteValue();
        return result;
    }

    public static String toHex(byte[] buf) {
        if (buf == null)
            return "";
        StringBuffer result = new StringBuffer(2 * buf.length);
        for (int i = 0; i < buf.length; i++) {
            appendHex(result, buf[i]);
        }
        return result.toString();
    }

    private static void appendHex(StringBuffer sb, byte b) {
        sb.append(HEX.charAt((b >> 4) & 0x0f)).append(HEX.charAt(b & 0x0f));
    }
}