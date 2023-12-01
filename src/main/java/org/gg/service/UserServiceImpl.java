package org.gg.service;

import java.util.ArrayList;
import java.util.List;
import org.gg.model.User;
import org.gg.repository.UserRepository;
import org.gg.utils.BeanUtil;
import org.gg.utils.HashUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User addUser(User user) {
        user.setSalt(HashUtil.generateSalt());
        String password = user.getPassword();
        try {
            user.setPassword(HashUtil.hashPassword(password, user.getSalt()));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        return userRepository.save(user);
    }

    @Override
    public Optional<User> getUserById(String id) {
        return userRepository.findById(id);
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.getUsersByEmail(email);
    }


    @Override
    public User updateUser(String id, User updatedUser) {
        Optional<User> optionalExistingUser = userRepository.findById(id);

        if (optionalExistingUser.isPresent()) {
            User existingUser = optionalExistingUser.get();
            //spring framework method which copies values from one object to another by providing a list of null values to populate
            BeanUtils.copyProperties(updatedUser, existingUser, BeanUtil.getNullPropertyNames(updatedUser));
            existingUser.setId(id);

            return userRepository.save(existingUser);
        }
        return null;
    }

    @Override
    public void removeUser(String id) {
        userRepository.deleteById(id);
    }

    @Override
    public boolean exists(String email) {
        return userRepository.getUsersByEmail(email) != null;
    }
}
