package com.example.password;


import android.content.Context;
import android.content.SharedPreferences;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.util.Base64;

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
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;


public class Encryptor {

    private static final int SALT_LENGTH = 16;

    public static void generateKeyPair() throws NoSuchAlgorithmException, NoSuchProviderException, InvalidAlgorithmParameterException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(
                KeyProperties.KEY_ALGORITHM_RSA, "AndroidKeyStore");

        KeyGenParameterSpec keyGenParameterSpec = new KeyGenParameterSpec.Builder(
                "KeyPairAlias",
                KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT
        )
                .setDigests(KeyProperties.DIGEST_SHA256, KeyProperties.DIGEST_SHA512)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_RSA_OAEP)
                .build();

        keyPairGenerator.initialize(keyGenParameterSpec);
        keyPairGenerator.generateKeyPair();
    }


    public static void storeSecretKey(SecretKey secretKey,Context context) throws Exception {

        // Load the Keystore
        KeyStore keyStore = KeyStore.getInstance("AndroidKeyStore");
        keyStore.load(null);

        // Get the public key from the KeyPair
        PublicKey publicKey = keyStore.getCertificate("KeyPairAlias").getPublicKey();

        // Encrypt the SecretKey
        Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] encryptedKey = cipher.doFinal(secretKey.getEncoded());

        // Store the encrypted SecretKey (Base64 encoded) in SharedPreferences
        String encodedKey = Base64.encodeToString(encryptedKey, Base64.DEFAULT);
        SharedPreferences sharedPreferences = context.getSharedPreferences("prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("encrypted_secret_key", encodedKey);
        editor.apply();
    }


    public static SecretKey retrieveSecretKey(Context context) throws Exception {
        // Load the Keystore
        KeyStore keyStore = KeyStore.getInstance("AndroidKeyStore");
        keyStore.load(null);

        // Get the private key from the KeyPair
        PrivateKey privateKey = (PrivateKey) keyStore.getKey("KeyPairAlias", null);

        // Retrieve the encrypted SecretKey from SharedPreferences
        SharedPreferences sharedPreferences = context.getSharedPreferences("prefs", Context.MODE_PRIVATE);
        String encodedKey = sharedPreferences.getString("encrypted_secret_key", null);
        if (encodedKey == null) {
            throw new IllegalStateException("SecretKey not found in SharedPreferences");
        }

        byte[] encryptedKey = Base64.decode(encodedKey, Base64.DEFAULT);

        // Decrypt the SecretKey
        Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
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

    // Convert a byte array to a SecretKey
    public static SecretKey getKeyFromBytes(byte[] keyBytes) {
        return new SecretKeySpec(keyBytes, "AES");
    }

}
