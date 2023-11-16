package org.gg.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class HashUtil {

    public static String generateSalt() {
        // Generate a random salt of 32 bytes
        byte[] salt = new byte[32];
        SecureRandom random = new SecureRandom();
        random.nextBytes(salt);

        // Convert the salt to a base64 string and return it
        return Base64.getEncoder().encodeToString(salt);
    }

    public static String hashPassword(String password, String saltString) throws NoSuchAlgorithmException {
        // Convert the salt from the base64 string to a byte array
        byte[] salt = Base64.getDecoder().decode(saltString);

        // Generate the hash of the password using PBKDF2
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        digest.reset();
        digest.update(salt);

        byte[] hash = digest.digest(password.getBytes());

        // Combine the hash and salt into a single byte array
        byte[] combinedHash = new byte[hash.length + salt.length];
        System.arraycopy(hash, 0, combinedHash, 0, hash.length);
        System.arraycopy(salt, 0, combinedHash, hash.length, salt.length);

        // Convert the combined hash to a base64 string and return it
        return Base64.getEncoder().encodeToString(combinedHash);
    }

    public static boolean verifyPassword(String password, String saltString, String hashedPassword)
            throws NoSuchAlgorithmException {
        // Convert the salt and hashed password from base64 strings to byte arrays
        byte[] salt = Base64.getDecoder().decode(saltString);
        byte[] hashedPasswordBytes = Base64.getDecoder().decode(hashedPassword);

        // Extract the hash and salt from the hashed password byte array
        byte[] hash = new byte[hashedPasswordBytes.length - salt.length];
        System.arraycopy(hashedPasswordBytes, 0, hash, 0, hash.length);
        byte[] saltInHash = new byte[salt.length];
        System.arraycopy(hashedPasswordBytes, hash.length, saltInHash, 0, salt.length);

        // Compare the salt and hash with the newly generated hash and salt for the given password
        return MessageDigest.isEqual(salt, saltInHash) &&
                MessageDigest.isEqual(hash, MessageDigest.getInstance("SHA-256")
                        .digest(password.getBytes()));
    }
}


