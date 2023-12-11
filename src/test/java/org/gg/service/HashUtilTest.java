package org.gg.service;

import org.gg.utils.HashUtil;
import org.junit.jupiter.api.Test;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import static org.junit.jupiter.api.Assertions.*;

class HashUtilTest {

    @Test
    void testGenerateSalt() {
        String salt = HashUtil.generateSalt();

        assertNotNull(salt);
        assertEquals(44, salt.length()); // Base64-encoded length for 32 bytes
    }

    @Test
    void testHashAndVerifyPassword() throws NoSuchAlgorithmException, InvalidKeySpecException {
        String password = "myPassword";
        String salt = HashUtil.generateSalt();

        // Hash the password
        String hashedPassword = HashUtil.hashPassword(password, salt);

        // Verify the password
        assertTrue(HashUtil.verifyPassword(password, salt, hashedPassword));
    }

    @Test
    void testInvalidPassword() throws NoSuchAlgorithmException, InvalidKeySpecException {
        String password = "myPassword";
        String salt = HashUtil.generateSalt();

        // Hash the password
        String hashedPassword = HashUtil.hashPassword(password, salt);

        // Verify an incorrect password
        assertFalse(HashUtil.verifyPassword("wrongPassword", salt, hashedPassword));
    }

    @Test
    void testInvalidSalt() throws NoSuchAlgorithmException, InvalidKeySpecException {
        String password = "myPassword";
        String salt = HashUtil.generateSalt();

        // Hash the password
        String hashedPassword = HashUtil.hashPassword(password, salt);

        // Use an incorrect salt for verification
        assertFalse(HashUtil.verifyPassword(password, HashUtil.generateSalt(), hashedPassword));
    }
}
