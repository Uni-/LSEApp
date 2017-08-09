package com.navercorp.android.lseapp.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;

/**
 * Created by NAVER on 2017-08-08.
 */

public enum Hash {

    ; // no instance

    private static final MessageDigest sha1MessageDigest;

    static {
        try {
            sha1MessageDigest = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static String sha1sum(byte[] blob) {
        return hexString(sha1MessageDigest.digest(blob));
    }

    private static String hexString(byte[] blob) {
        final Formatter formatter = new Formatter();
        for (byte b : blob) {
            formatter.format("%02x", b);
        }
        return formatter.toString();
    }
}
