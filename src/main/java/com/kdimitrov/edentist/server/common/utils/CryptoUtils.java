package com.kdimitrov.edentist.server.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class CryptoUtils {

    final private static Logger logger = LoggerFactory.getLogger(CryptoUtils.class);

    private final static String DEFAULT_KEY = "edentistmegasecretapplicaitonkey123456789";
    private static String encryptionKey = DEFAULT_KEY;
    private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();

    public static String getEncryptionKey() {
        return encryptionKey;
    }

    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars);
    }

    public static String getSecretHash() throws NoSuchAlgorithmException {
        String key = getEncryptionKey();
        if (logger.isDebugEnabled()) {
            logger.debug("key: " + key);
        }
        final MessageDigest digest = MessageDigest.getInstance("SHA-256");
        final byte[] hashbytes = digest.digest(key.getBytes(StandardCharsets.UTF_8));
        return bytesToHex(hashbytes);
    }
}
