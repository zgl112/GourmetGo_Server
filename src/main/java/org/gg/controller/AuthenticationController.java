package org.gg.controller;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import org.gg.model.AuthResponse;
import org.gg.model.User;
import org.gg.service.UserService;
import org.gg.utils.HashUtil;
import org.gg.utils.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/authentication")
public class AuthenticationController {

    private final UserService userService;

    public AuthenticationController(UserService userService) {
        this.userService = userService;

    }
    @Autowired
    private JwtTokenProvider jwtProvider;

    @Autowired
    public PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody User user) throws Exception{
        try {
            if (userService.exists(user.getEmail())) { // Check if the account already exists
                // Throw an exception if the account already exists
                throw new Exception("Account already exists");
            }

            if (user.getPassword() == null) {
                throw new Exception("Password is required");
            }
            user.setSalt(HashUtil.generateSalt());
            user.setPassword(HashUtil.hashPassword(user.getPassword(), user.getSalt())); // Validate the password and encode it using BCryptPasswordEncoder
            userService.addUser(user);
            String token = jwtProvider.generateToken(user);
            AuthResponse authResponse = new AuthResponse(token);
            return new ResponseEntity<>(authResponse, HttpStatus.CREATED); // Add the customer to the database and return the ResponseEntity
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // Handle exceptions and return appropriate HttpStatus
        }
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody Map<String, String> credentials)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        // Retrieve the email and password from the request body
        String email = credentials.get("email");
        String password = credentials.get("password");
        User user = userService.getUserByEmail(email);

        // Check if the user exists in the database

        if (user == null) {
            // Return an HTTP UNAUTHORIZED response if the user does not exist
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        // Verify the provided password against the stored hashed password
        if (!HashUtil.verifyPassword(password, user.getSalt(), user.getPassword())) {
            // Return an HTTP UNAUTHORIZED response if the login credentials are invalid
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        // Generate the authorization token
        String token = jwtProvider.generateToken(user);

        // Create an AuthResponse object containing the token
        AuthResponse authResponse = new AuthResponse(token);

        // Return the AuthResponse with an HTTP OK response
        return new ResponseEntity<>(authResponse, HttpStatus.OK);
    }

}
