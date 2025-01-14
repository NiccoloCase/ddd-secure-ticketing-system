package org.swe.core.utils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PasswordUtilityTest {

    @Test
    public void testHashPassword() {
        String password = "mySuperSecurePassword";
        String hashedPassword = PasswordUtility.hashPassword(password);

        // Ensure the hash is not null or empty
        assertNotNull(hashedPassword, "The hashed password should not be null");
        assertFalse(hashedPassword.isEmpty(), "The hashed password should not be empty");

        // Ensure the hash is not equal to the plain password
        assertNotEquals(password, hashedPassword, "The hashed password should not be equal to the plain password");
    }

    @Test
    public void testCheckPasswordCorrect() {
        String password = "mySuperSecurePassword";
        String hashedPassword = PasswordUtility.hashPassword(password);

        // Ensure the correct password is accepted
        assertTrue(PasswordUtility.checkPassword(password, hashedPassword), "The password should be correct");
    }

    @Test
    public void testCheckPasswordIncorrect() {
        String password = "mySuperSecurePassword";
        String incorrectPassword = "wrongPassword";
        String hashedPassword = PasswordUtility.hashPassword(password);

        // Ensure an incorrect password is rejected
        assertFalse(PasswordUtility.checkPassword(incorrectPassword, hashedPassword), "The password should be incorrect");
    }
}
