package org.gg.service;

import org.gg.model.User;

import java.util.Optional;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserService extends UserDetails {
    User addUser(User user);
    Optional<User> getUserById(String id);
    User updateUser(String id, User user);

    User getUserByEmail(String email);
    void removeUser(String id);

    boolean exists(String email);

    User findByUsername(String username);
}
