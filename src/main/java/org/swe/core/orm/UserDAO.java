package org.swe.core.orm;

import org.swe.model.User;
import java.util.ArrayList;

public interface UserDAO {
     User getUser(int id);
     ArrayList<User> getAllUsers();
     //User findUserByEmail(String email);
     boolean addUser(User user);
     boolean updateUser(User user);
     boolean deleteUser(int id);
     
}
