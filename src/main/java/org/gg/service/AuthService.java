package org.gg.service;

import java.util.ArrayList;
import java.util.List;
import org.gg.model.User;
import org.gg.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    // Load user by username
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Find customer by email in the repository
        User user = userRepository.getUsersByEmail(email);
        if (user == null) {
            // Throw an exception if the customer is not found
            throw new UsernameNotFoundException("User not found");
        }

        // Create a list of granted authorities
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));

        // Create and return a User object with the customer's email, password, and authorities
        return user;
    }
}


