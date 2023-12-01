package org.gg.service;

import org.gg.model.User;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;

public interface UserService {
    User addUser(User user);
    Optional<User> getUserById(String id);
    User updateUser(String id, User user);

    User getUserByEmail(String email);
    void removeUser(String id);

    boolean exists(String email);
}
