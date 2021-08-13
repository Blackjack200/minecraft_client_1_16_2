package net.minecraft.util;

import org.apache.logging.log4j.LogManager;
import java.security.GeneralSecurityException;
import java.security.spec.AlgorithmParameterSpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import javax.crypto.Cipher;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.security.PrivateKey;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.KeyFactory;
import java.security.spec.X509EncodedKeySpec;
import java.security.MessageDigest;
import java.io.UnsupportedEncodingException;
import java.security.PublicKey;
import java.security.KeyPairGenerator;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import org.apache.logging.log4j.Logger;

public class Crypt {
    private static final Logger LOGGER;
    
    public static SecretKey generateSecretKey() {
        try {
            final KeyGenerator keyGenerator1 = KeyGenerator.getInstance("AES");
            keyGenerator1.init(128);
            return keyGenerator1.generateKey();
        }
        catch (NoSuchAlgorithmException noSuchAlgorithmException1) {
            throw new Error((Throwable)noSuchAlgorithmException1);
        }
    }
    
    public static KeyPair generateKeyPair() {
        try {
            final KeyPairGenerator keyPairGenerator1 = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator1.initialize(1024);
            return keyPairGenerator1.generateKeyPair();
        }
        catch (NoSuchAlgorithmException noSuchAlgorithmException1) {
            noSuchAlgorithmException1.printStackTrace();
            Crypt.LOGGER.error("Key pair generation failed!");
            return null;
        }
    }
    
    public static byte[] digestData(final String string, final PublicKey publicKey, final SecretKey secretKey) {
        try {
            return digestData("SHA-1", string.getBytes("ISO_8859_1"), secretKey.getEncoded(), publicKey.getEncoded());
        }
        catch (UnsupportedEncodingException unsupportedEncodingException4) {
            unsupportedEncodingException4.printStackTrace();
            return null;
        }
    }
    
    private static byte[] digestData(final String string, final byte[]... arr) {
        try {
            final MessageDigest messageDigest3 = MessageDigest.getInstance(string);
            for (final byte[] arr2 : arr) {
                messageDigest3.update(arr2);
            }
            return messageDigest3.digest();
        }
        catch (NoSuchAlgorithmException noSuchAlgorithmException3) {
            noSuchAlgorithmException3.printStackTrace();
            return null;
        }
    }
    
    public static PublicKey byteToPublicKey(final byte[] arr) {
        try {
            final EncodedKeySpec encodedKeySpec2 = (EncodedKeySpec)new X509EncodedKeySpec(arr);
            final KeyFactory keyFactory3 = KeyFactory.getInstance("RSA");
            return keyFactory3.generatePublic((KeySpec)encodedKeySpec2);
        }
        catch (NoSuchAlgorithmException ex) {}
        catch (InvalidKeySpecException ex2) {}
        Crypt.LOGGER.error("Public key reconstitute failed!");
        return null;
    }
    
    public static SecretKey decryptByteToSecretKey(final PrivateKey privateKey, final byte[] arr) {
        return (SecretKey)new SecretKeySpec(decryptUsingKey((Key)privateKey, arr), "AES");
    }
    
    public static byte[] encryptUsingKey(final Key key, final byte[] arr) {
        return cipherData(1, key, arr);
    }
    
    public static byte[] decryptUsingKey(final Key key, final byte[] arr) {
        return cipherData(2, key, arr);
    }
    
    private static byte[] cipherData(final int integer, final Key key, final byte[] arr) {
        try {
            return setupCipher(integer, key.getAlgorithm(), key).doFinal(arr);
        }
        catch (IllegalBlockSizeException illegalBlockSizeException4) {
            illegalBlockSizeException4.printStackTrace();
        }
        catch (BadPaddingException badPaddingException4) {
            badPaddingException4.printStackTrace();
        }
        Crypt.LOGGER.error("Cipher data failed!");
        return null;
    }
    
    private static Cipher setupCipher(final int integer, final String string, final Key key) {
        try {
            final Cipher cipher4 = Cipher.getInstance(string);
            cipher4.init(integer, key);
            return cipher4;
        }
        catch (InvalidKeyException invalidKeyException4) {
            invalidKeyException4.printStackTrace();
        }
        catch (NoSuchAlgorithmException noSuchAlgorithmException4) {
            noSuchAlgorithmException4.printStackTrace();
        }
        catch (NoSuchPaddingException noSuchPaddingException4) {
            noSuchPaddingException4.printStackTrace();
        }
        Crypt.LOGGER.error("Cipher creation failed!");
        return null;
    }
    
    public static Cipher getCipher(final int integer, final Key key) {
        try {
            final Cipher cipher3 = Cipher.getInstance("AES/CFB8/NoPadding");
            cipher3.init(integer, key, (AlgorithmParameterSpec)new IvParameterSpec(key.getEncoded()));
            return cipher3;
        }
        catch (GeneralSecurityException generalSecurityException3) {
            throw new RuntimeException((Throwable)generalSecurityException3);
        }
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
}
