package org.gg.utils;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

public class HashUtil {

    public static String generateSalt() {
        // Generate a random salt of 32 bytes
        byte[] salt = new byte[32];
        new SecureRandom().nextBytes(salt);
        // Convert the salt to a base64 string and return it
        return Base64.getEncoder().encodeToString(salt);
    }

    public static String hashPassword(String password, String saltString) throws NoSuchAlgorithmException, InvalidKeySpecException {
        // Convert the salt from the base64 string to a byte array
        byte[] salt = Base64.getDecoder().decode(saltString);
        // Generate the hash of the password using PBKDF2
        PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 10000, 20 * 8);
        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] hash = skf.generateSecret(spec).getEncoded();
        // Combine the hash and salt into a single byte array
        byte[] combinedHash = new byte[hash.length + salt.length];
        System.arraycopy(hash, 0, combinedHash, 0, hash.length);
        System.arraycopy(salt, 0, combinedHash, hash.length, salt.length);
        // Convert the combined hash to a base64 string and return it
        return Base64.getEncoder().encodeToString(combinedHash);
    }

    public static boolean verifyPassword(String password, String saltString, String hashedPassword)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        // Convert the salt and hashed password from base64 strings to byte arrays
        byte[] salt = Base64.getDecoder().decode(saltString);
        byte[] hashedPasswordBytes = Base64.getDecoder().decode(hashedPassword);
        // Extract the hash and salt from the hashed password byte array
        byte[] hash = new byte[20];
        System.arraycopy(hashedPasswordBytes, 0, hash, 0, 20);
        byte[] saltInHash = new byte[salt.length];
        System.arraycopy(hashedPasswordBytes, 20, saltInHash, 0, salt.length);
        // Compare the salt and hash with the newly generated hash and salt for the given password
        PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 10000, 20 * 8);
        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] testHash = skf.generateSecret(spec).getEncoded();

        return MessageDigest.isEqual(salt, saltInHash) && MessageDigest.isEqual(hash, testHash);
    }
}


