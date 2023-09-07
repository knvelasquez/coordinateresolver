package application.service;

import org.springframework.stereotype.Service;

import javax.crypto.*;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

@Service
public class EncryptService {
    private static final int KEY_LENGTH = 256;
    private static final int IV_LENGTH = 12; // 96 bits for GCM
    private static final int TAG_LENGTH = 128;
    private static final String AES_ALGORITHM = "AES";
    private static final String AES_GCM_NO_PADDING = "AES/GCM/NoPadding";
    private static final String PBK_DF2_WITH_H_MAC_SHA256 = "PBKDF2WithHmacSHA256";

    public String from(String message, String secret) {
        SecretKey derivedKey = obtainDerivedSecretKey(secret, false);
        byte[] iv = generateAnInitializationVectorForGCM();
        Cipher cipher = createCipherForEncryption();
        initCipher(cipher, derivedKey, iv);
        byte[] encryptedBytes = getEncryptedBytes(cipher, message);
        byte[] combined = combineIvAndEncryptedBytes(iv, encryptedBytes);
        return Base64.getEncoder().encodeToString(combined);
    }

    private byte[] combineIvAndEncryptedBytes(byte[] iv, byte[] encryptedBytes) {
        // Combine IV and encrypted data
        byte[] combined = new byte[IV_LENGTH + encryptedBytes.length];
        System.arraycopy(iv, 0, combined, 0, IV_LENGTH);
        System.arraycopy(encryptedBytes, 0, combined, IV_LENGTH, encryptedBytes.length);
        return combined;
    }

    private byte[] getEncryptedBytes(Cipher cipher, String message) {
        try {
            byte[] encryptedBytes = cipher.doFinal(message.getBytes());
            return encryptedBytes;
        } catch (IllegalBlockSizeException e) {
            //throw new RuntimeException(e);
        } catch (BadPaddingException e) {
            //throw new RuntimeException(e);
        }
        return null;
    }

    private void initCipher(Cipher cipher, SecretKey derivedKey, byte[] iv) {
        try {
            cipher.init(Cipher.ENCRYPT_MODE, derivedKey, new GCMParameterSpec(TAG_LENGTH, iv));
        } catch (InvalidKeyException e) {
            //throw new RuntimeException(e);
        } catch (InvalidAlgorithmParameterException e) {
            //throw new RuntimeException(e);
        }
    }

    private Cipher createCipherForEncryption() {
        try {
            return Cipher.getInstance(AES_GCM_NO_PADDING);
        } catch (NoSuchAlgorithmException e) {
            //throw new RuntimeException(e);
        } catch (NoSuchPaddingException e) {
            //throw new RuntimeException(e);
        }
        return null;
    }


    private SecretKey obtainDerivedSecretKey(String secret, Boolean secureRandom) {
        byte[] salt = generateSalt(secureRandom);
        PBEKeySpec keySpec = new PBEKeySpec(secret.toCharArray(), salt, 10000, KEY_LENGTH);
        try {
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(PBK_DF2_WITH_H_MAC_SHA256);
            return new SecretKeySpec(keyFactory.generateSecret(keySpec).getEncoded(), AES_ALGORITHM);
        } catch (Exception e) {
            //throw new RuntimeException(e);
        }
        return null;
    }

    private byte[] generateSalt(Boolean secureRandom) {
        byte[] salt = new byte[16];
        if (secureRandom) {
            new SecureRandom().nextBytes(salt);
        }
        return salt;
    }

    private byte[] generateAnInitializationVectorForGCM() {
        byte[] iv = new byte[IV_LENGTH];
        new SecureRandom().nextBytes(iv);
        return iv;
    }
}
