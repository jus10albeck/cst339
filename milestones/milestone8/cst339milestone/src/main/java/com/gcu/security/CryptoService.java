package com.gcu.security;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Locale;

import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CryptoService 
{

	private static final int GCM_TAG_LEN_BITS = 128; // 16 bytes
    private static final int GCM_IV_LEN_BYTES = 12;  // 12 bytes
    private static final int KEY_LEN_BYTES = 32;     // 256-bit keys

    private final byte[] dataKey;
    private final byte[] pepper;
    private final SecureRandom rnd = new SecureRandom();

    /**
     * Constructor. Keys can be Base64 (standard or URL-safe) or hex-encoded.
     * @param dataKeyEncoded
     * @param pepperEncoded
     */
    public CryptoService(
        @Value("${app.crypto.data-key}") String dataKeyEncoded,
        @Value("${app.crypto.username-pepper}") String pepperEncoded
    ) {
        this.dataKey = decodeKey(dataKeyEncoded, "app.crypto.data-key");
        this.pepper  = decodeKey(pepperEncoded,  "app.crypto.username-pepper");
    }

    /**
     * Decode a key from a string. Tries Base64 (standard and URL-safe) first, then hex.
     * @param s
     * @param settingName
     * @return
     */
    private static byte[] decodeKey(String s, String settingName) {
        if (s == null || s.isBlank()) {
            throw new IllegalArgumentException(settingName + " is empty");
        }
        s = s.trim();

        byte[] out = null;

        // Try standard Base64
        try {
            out = Base64.getDecoder().decode(s);
        } catch (IllegalArgumentException ignore) {
            // Try URL-safe Base64
            try {
                out = Base64.getUrlDecoder().decode(s);
            } catch (IllegalArgumentException ignore2) {
                // Try hex
                String hex = s.toLowerCase(Locale.ROOT);
                if (hex.matches("^[0-9a-f]+$") && (hex.length() % 2 == 0)) {
                    out = hexToBytes(hex);
                } else {
                    throw new IllegalArgumentException(
                        settingName + " is neither Base64 nor hex; value: " + s.substring(0, Math.min(12, s.length())) + "…");
                }
            }
        }

        if (out.length != KEY_LEN_BYTES) {
            throw new IllegalArgumentException(settingName + " must decode to "
                    + KEY_LEN_BYTES + " bytes, got " + out.length);
        }
        return out;
    }

    /**
     * Convert hex string to byte array.
     * @param hex
     * @return
     */
    private static byte[] hexToBytes(String hex) {
        int len = hex.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i/2] = (byte)((Character.digit(hex.charAt(i),16) << 4)
                             +  Character.digit(hex.charAt(i+1),16));
        }
        return data;
    }

    /** 
     * Normalize for case-insensitive uniqueness/lookups. 
     */
    public String normalizeUsername(String u) {
        return u == null ? "" : u.trim().toLowerCase(); // add Unicode NFKC if needed
    }

    /** 
     * Blind index (HMAC-SHA256). */
    public byte[] usernameHash(String normalized) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(pepper, "HmacSHA256"));
            return mac.doFinal(normalized.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    /** 
     * Encrypt with AES-256-GCM. Store as iv|tag|ciphertext. 
     */
    public byte[] encrypt(String plaintext) {
        try {
            byte[] iv = new byte[GCM_IV_LEN_BYTES];
            rnd.nextBytes(iv);
            Cipher c = Cipher.getInstance("AES/GCM/NoPadding");
            c.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(dataKey, "AES"),
                   new GCMParameterSpec(GCM_TAG_LEN_BITS, iv));
            byte[] ctPlusTag = c.doFinal(plaintext.getBytes(StandardCharsets.UTF_8));
            byte[] tag = new byte[16];
            byte[] body = new byte[ctPlusTag.length - 16];
            System.arraycopy(ctPlusTag, ctPlusTag.length - 16, tag, 0, 16);
            System.arraycopy(ctPlusTag, 0, body, 0, body.length);

            byte[] out = new byte[iv.length + tag.length + body.length];
            System.arraycopy(iv,   0, out, 0,               iv.length);
            System.arraycopy(tag,  0, out, iv.length,       tag.length);
            System.arraycopy(body, 0, out, iv.length+tag.length, body.length);
            return out;
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * Decrypt. Expects input as iv|tag|ciphertext.
     * @param stored
     * @return
     */
    public String decrypt(byte[] stored) {
        try {
            byte[] iv   = new byte[GCM_IV_LEN_BYTES];
            byte[] tag  = new byte[16];
            byte[] body = new byte[stored.length - iv.length - tag.length];
            System.arraycopy(stored, 0,                    iv,   0, iv.length);
            System.arraycopy(stored, iv.length,            tag,  0, tag.length);
            System.arraycopy(stored, iv.length+tag.length, body, 0, body.length);

            Cipher c = Cipher.getInstance("AES/GCM/NoPadding");
            c.init(Cipher.DECRYPT_MODE, new SecretKeySpec(dataKey, "AES"),
                   new GCMParameterSpec(GCM_TAG_LEN_BITS, iv));

            byte[] input = new byte[body.length + tag.length];
            System.arraycopy(body, 0, input, 0,          body.length);
            System.arraycopy(tag,  0, input, body.length, tag.length);

            byte[] pt = c.doFinal(input);
            return new String(pt, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }
}
