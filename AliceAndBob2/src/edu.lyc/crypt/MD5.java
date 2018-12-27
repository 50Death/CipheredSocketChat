package edu.lyc.crypt;

import java.security.MessageDigest;

public class MD5 {

    public static String md5(String text) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] bytes = md.digest(text.getBytes("UTF-8"));
            return toHex(bytes);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static String toHex(byte[] bytes) {
        final char[] HEX_DIGITS = "0123456789ABCDEF".toCharArray();
        StringBuilder stringBuilder = new StringBuilder(bytes.length * 2);
        for (int i = 0; i < bytes.length; i++) {
            stringBuilder.append(HEX_DIGITS[(bytes[i] >> 4) & 0x0f]);
            stringBuilder.append(HEX_DIGITS[bytes[i] & 0x0f]);
        }
        return stringBuilder.toString();
    }
}
