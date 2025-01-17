package org.swe.core.DAO;

import java.util.ArrayList;

import org.swe.model.User;

public interface UserDAO {
     User getUser(int id);
     ArrayList<User> getAllUsers();
     User findUserByEmail(String email);
     boolean addUser(User user);
     boolean updateUser(User user);
     boolean deleteUser(int id);
     
}
