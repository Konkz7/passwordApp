package com.example.password;


import android.content.Context;
import android.content.SharedPreferences;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.util.Base64;
import android.util.Log;

import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;


public class Encryptor {

    private static final int SALT_LENGTH = 16;

    public static void generateAesKey() throws Exception {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");

        KeyGenParameterSpec keyGenParameterSpec = new KeyGenParameterSpec.Builder(
                "AesKeyAlias",
                KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT
        )
                .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                .setKeySize(256) // Use 256-bit AES for strong security
                .setUserAuthenticationRequired(false) // Set to true if you want biometric/authentication protection
                .build();

        keyGenerator.init(keyGenParameterSpec);
        keyGenerator.generateKey();
    }



    public static void storeSecretKey(SecretKey secretKey, Context context) throws Exception {
        // Load the Keystore
        KeyStore keyStore = KeyStore.getInstance("AndroidKeyStore");
        keyStore.load(null);

        // Retrieve the AES key from the Keystore
        SecretKey aesKey = (SecretKey) keyStore.getKey("AesKeyAlias", null);

        // Encrypt the SecretKey using AES/GCM
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        cipher.init(Cipher.ENCRYPT_MODE, aesKey);
        byte[] iv = cipher.getIV(); // Initialization vector for decryption
        byte[] encryptedKey = cipher.doFinal(secretKey.getEncoded());

        // Store the encrypted SecretKey (Base64 encoded) and IV in SharedPreferences
        SharedPreferences sharedPreferences = context.getSharedPreferences("prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("encrypted_secret_key", Base64.encodeToString(encryptedKey, Base64.DEFAULT));
        editor.putString("encryption_iv", Base64.encodeToString(iv, Base64.DEFAULT));
        editor.apply();

        Log.d("StoreSecretKey", "SecretKey stored successfully");
    }



    public static SecretKey retrieveSecretKey(Context context) throws Exception {
        // Load the Keystore
        KeyStore keyStore = KeyStore.getInstance("AndroidKeyStore");
        keyStore.load(null);

        // Retrieve the AES key from the Keystore
        SecretKey aesKey = (SecretKey) keyStore.getKey("AesKeyAlias", null);

        // Retrieve the encrypted SecretKey and IV from SharedPreferences
        SharedPreferences sharedPreferences = context.getSharedPreferences("prefs", Context.MODE_PRIVATE);
        String encryptedKeyBase64 = sharedPreferences.getString("encrypted_secret_key", null);
        String ivBase64 = sharedPreferences.getString("encryption_iv", null);

        if (encryptedKeyBase64 == null || ivBase64 == null) {
            throw new IllegalStateException("SecretKey or IV not found in SharedPreferences");
        }

        byte[] encryptedKey = Base64.decode(encryptedKeyBase64, Base64.DEFAULT);
        byte[] iv = Base64.decode(ivBase64, Base64.DEFAULT);

        // Decrypt the SecretKey using AES/GCM
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(128, iv); // 128-bit authentication tag
        cipher.init(Cipher.DECRYPT_MODE, aesKey, gcmParameterSpec);
        byte[] decryptedKey = cipher.doFinal(encryptedKey);

        // Recreate the SecretKey
        return new SecretKeySpec(decryptedKey, "AES");
    }




    public static String hashString(String input) throws NoSuchAlgorithmException {
        //MessageDigest works with MD2, MD5, SHA-1, SHA-224, SHA-256
        //SHA-384 and SHA-512
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] messageDigest = md.digest(input.getBytes());
        BigInteger bigInt = new BigInteger(1, messageDigest);
        return bigInt.toString(16);
    }

    public static String generateSalt() {
        SecureRandom random = new SecureRandom();
        StringBuilder salt = new StringBuilder(SALT_LENGTH);

        for (int i = 0; i < SALT_LENGTH; i++) {
            int randomChar = 32 + random.nextInt(95); // Generates a number between 32 and 126
            salt.append((char) randomChar);
        }

        return salt.toString();
    }

    public static SecretKey generateKeyFromPassword(String password, byte[] salt) throws Exception {
        // Define the parameters for PBKDF2
        int iterationCount = 65536; // Number of iterations
        int keyLength = 256; // Key length in bits

        // Create a key specification using the password and salt
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, iterationCount, keyLength);

        // Generate the key using PBKDF2 with HMAC-SHA256
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        byte[] keyBytes = factory.generateSecret(spec).getEncoded();

        // Return the key as a SecretKeySpec
        return new SecretKeySpec(keyBytes, "AES");
    }

    // Encrypt a string
    public static String encrypt(String plainText, SecretKey key) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] encryptedBytes = cipher.doFinal(plainText.getBytes());
        return Base64.encodeToString(encryptedBytes, Base64.DEFAULT); // Base64 for easy transport
    }

    // Decrypt a string
    public static String decrypt(String encryptedText, SecretKey key) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] decodedBytes = Base64.decode(encryptedText, Base64.DEFAULT);
        byte[] decryptedBytes = cipher.doFinal(decodedBytes);
        return new String(decryptedBytes);
    }

    

}
