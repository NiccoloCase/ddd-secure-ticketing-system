package org.swe.core.DAO;

import java.util.ArrayList;

import org.swe.model.User;

public interface UserDAO {
    User getUserById(int id);
    ArrayList<User> getAllUsers();
    User getUserByEmail(String email);
    User createUser(String name, String surname, String passwordHash, String email);
    boolean updateUser(int id, String name, String surname, String passwordHash, String email);
    boolean deleteUser(int id);
}