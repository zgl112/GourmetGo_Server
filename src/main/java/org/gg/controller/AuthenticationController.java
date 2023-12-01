package org.gg.controller;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.Map;
import org.gg.model.AuthResponse;
import org.gg.model.User;

import org.gg.service.UserService;
import org.gg.utils.HashUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/authentication")
public class AuthenticationController {

    private final UserService userService;

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    public AuthenticationController(UserService userService) {
        this.userService = userService;

    }
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
            user.setPassword(HashUtil.hashPassword(user.getPassword(), user.getSalt())); // Validate the password and encode it using BCryptPasswordEncoder
            userService.addUser(user);
            String token = generateAuthToken(user.getEmail());
            AuthResponse authResponse = new AuthResponse(token);
            return new ResponseEntity<>(authResponse, HttpStatus.CREATED); // Add the customer to the database and return the ResponseEntity
        } catch (Exception e) {
            System.out.println(user.getPassword() + "herewrwer");
            System.out.println("here");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // Handle exceptions and return appropriate HttpStatus

        }
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody Map<String, String> credentials)
      throws NoSuchAlgorithmException {
        String email = credentials.get("email");
        String password = credentials.get("password");
        if (userService.exists(email)) { // Check if the customer exists in the database
            System.out.println("here");
            User user = userService.getUserByEmail(email);

            if (!HashUtil.verifyPassword(password, credentials.get("salt"), userService.getUserByEmail(email).getPassword())) { // Compare the provided password with the stored hashed password using BCryptPasswordEncoder
                // Return an HTTP UNAUTHORIZED response if the login credentials are invalid
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }

            // Generate the authorization token
            String token = generateAuthToken(user.getEmail());

            AuthResponse authResponse = new AuthResponse(token); // Create an AuthResponse object containing the token
            return new ResponseEntity<>(authResponse, HttpStatus.OK); // Return the AuthResponse with an HTTP OK response
        }

        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);// Return an HTTP UNAUTHORIZED response if the customer does not exist
    }

    private String generateAuthToken(String userEmail) {
        // Generate an authorization token for the given email
        User user = userService.getUserByEmail(userEmail);
        // Set the expiration time for the token
        Instant expirationTime = Instant.now().plus(Duration.ofHours(2));

        // add the claims of the token
        Claims claims = Jwts.claims().setSubject(userEmail);
        claims.put("exp", expirationTime.toEpochMilli());
        claims.put("id", user.getId());
        claims.put("firstName", user.getFirstName());
        claims.put("lastName", user.getLastName());
        claims.put("username", user.getUsername());
        claims.put("email", user.getEmail());
        claims.put("password", user.getPassword());
        claims.put("houseNumber", user.getHouseNumber());
        claims.put("streetName", user.getStreetName());
        claims.put("flatDetails", user.getFlatDetails());
        claims.put("postcode", user.getPostcode());
        claims.put("cardNumber", user.getCardNumber());
        claims.put("cardSecurityCode", user.getCardSecurityCode());
        claims.put("salt", user.getSalt());

        long EXPIRATION_TIME_MS = 86400000; // 24 hours in milliseconds
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + EXPIRATION_TIME_MS);
        // Generate the token

        return Jwts.builder()
          .setExpiration(expiryDate)
          .setClaims(claims)
          .compact();
    }

}
