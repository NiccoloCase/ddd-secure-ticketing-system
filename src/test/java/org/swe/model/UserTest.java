package org.swe.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

public class UserTest {

    @Test
    public void testUserConstructorAndGetters() {
        User user = new User("John", "Doe", "hashedPassword123", "john.doe@example.com", 1);

        assertEquals("John", user.getName());
        assertEquals("Doe", user.getSurname());
        assertEquals("hashedPassword123", user.getPasswordHash());
        assertEquals("john.doe@example.com", user.getEmail());
    }

    @Test
    public void testSetters() {
        User user = new User("", "", "", "", 0);

        user.setName("Alice");
        user.setSurname("Smith");
        user.setPasswordHash("newHashedPassword");
        user.setEmail("alice.smith@example.com");
        user.setId(1);

        assertEquals("Alice", user.getName());
        assertEquals("Smith", user.getSurname());
        assertEquals("newHashedPassword", user.getPasswordHash());
        assertEquals("alice.smith@example.com", user.getEmail());
        assertEquals(1, user.getId());
    }
}
