package com.astar.common.library.utils;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

//TODO ADD MORE
public abstract class EncryptionUtility {

    private static boolean isInitializedEncryption = false;
    private static final String AES_GCM_NO_PADDING = "AES/GCM/NoPadding";
    private static final int AES_KEY_SIZE = 256;

    private static final int GCM_TAG_LENGTH = 128; // !BITS
    private static final int GCM_IV_LENGTH = 12; // !2 BYTES = 96 BITS

    private static final int SALT_LENGTH = 64;

    /**
     * Run to add BouncyCastleProvider
     */
    public static void initializeEncryption() {
        Security.addProvider(new BouncyCastleProvider());
        isInitializedEncryption = true;
    }

    /**
     * @param plainText
     * @return
     * @throws Exception
     * @throws NoSuchAlgorithmException
     * @throws NoSuchProviderException
     */
    public static String AESEncrypt(
            String plainText,
            SecretKey secretKey
    ) throws Exception, NoSuchAlgorithmException, NoSuchProviderException {
        if (!isInitializedEncryption) throw new Exception("Error");
        byte[] iv = new byte[GCM_IV_LENGTH];
        new SecureRandom().nextBytes(iv);
        GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
        Cipher cipher = Cipher.getInstance(AES_GCM_NO_PADDING, "BC");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, gcmParameterSpec);
        byte[] cipherText = cipher.doFinal(plainText.getBytes());
        byte[] ivAndCipherText = new byte[iv.length + cipherText.length];
        System.arraycopy(iv, 0, ivAndCipherText, 0, iv.length);
        System.arraycopy(cipherText, 0, ivAndCipherText, iv.length, cipherText.length);
        return Base64.getEncoder().encodeToString(ivAndCipherText);
    }


    /**
     *
     * @param ivAndCipherTextBase64
     * @param password
     * @param salt
     * @return
     * @throws Exception
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     * @throws NoSuchProviderException
     * @throws NoSuchPaddingException
     * @throws InvalidAlgorithmParameterException
     * @throws InvalidKeyException
     */
    public static String AESDecrypt(
            String ivAndCipherTextBase64,
            String password,
            String salt
    ) throws Exception {
        if (!isInitializedEncryption) throw new Exception("Encryption not initialized");
        byte[] ivAndCipherText = Base64.getDecoder().decode(ivAndCipherTextBase64);
        byte[] iv = new byte[GCM_IV_LENGTH];
        System.arraycopy(ivAndCipherText, 0, iv, 0, iv.length);
        byte[] cipherText = new byte[ivAndCipherText.length - GCM_IV_LENGTH];
        System.arraycopy(ivAndCipherText, GCM_IV_LENGTH, cipherText, 0, cipherText.length);
        GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
        SecretKey secretKey = AESGetKeyFromPassword(password, salt);
        Cipher cipher = Cipher.getInstance(AES_GCM_NO_PADDING, "BC");
        cipher.init(Cipher.DECRYPT_MODE, secretKey, gcmParameterSpec);
        byte[] plainTextBytes = cipher.doFinal(cipherText);
        return new String(plainTextBytes);
    }

    public static SecretKey AESGetKeyFromPassword(
            String password,
            String salt
    ) throws NoSuchAlgorithmException, NoSuchProviderException, InvalidKeySpecException {
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256", "BC");
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt.getBytes(), 65536, AES_KEY_SIZE);
        return new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");
    }

    public static String hash(String plainText) {
        return null;
    }

    public static String generateSalt(){
        byte[] salt = new byte[SALT_LENGTH];
        new SecureRandom().nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

}
