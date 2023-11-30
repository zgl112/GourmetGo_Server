package org.gg.model;

/**
 * This class is responsible for define the Access token that contains the encrypted data for the front-end
 */
public class AuthResponse {
    private String token;

    // Constructor to initialize the AuthResponse with a token
    public AuthResponse(String token) {
        this.token = token;
    }

    // Getter to retrieve the token
    public String getToken() {
        return token;
    }

    // Setter to set the token
    public void setToken(String token) {
        this.token = token;
    }
}