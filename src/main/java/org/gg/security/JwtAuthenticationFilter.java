package org.gg.security;

import static org.hibernate.tool.schema.SchemaToolingLogging.LOGGER;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Map;
import java.util.Optional;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.gg.model.User;
import org.gg.repository.UserRepository;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.client.RestOperations;

/**
 * Handles retrieving and verifying the jwt token sent in the cookie for every request.
 */
@Slf4j
public class JwtAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private final Base64.Decoder base64Decoder;
    private final ObjectMapper objectMapper;
    private final UserRepository userRepository;

    /**
     * Creates the authentication filter that process the kwt token and sets the principal for the session.
     *
     * @param path                  that the filter should be run on
     * @param authenticationManager used to manage authentications

     */
    public JwtAuthenticationFilter(String path, AuthenticationManager authenticationManager,
                                    UserRepository userRepository) {
        super(new AntPathRequestMatcher(path));
        setAuthenticationManager(authenticationManager);
        RestOperations rest = new RestTemplateBuilder()
          .setConnectTimeout(Duration.ofSeconds(60))
          .setReadTimeout(Duration.ofSeconds(60))
          .build();
        this.setContinueChainBeforeSuccessfulAuthentication(true);
        this.setAuthenticationSuccessHandler((request, response, authentication) -> {
        });
        this.base64Decoder = Base64.getUrlDecoder();
        this.objectMapper = new ObjectMapper();
        this.userRepository = userRepository;
    }


    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        Cookie[] cookies = request.getCookies();
        try {
            if (cookies != null) {
                Optional<Cookie> jwtCookie = Arrays.stream(cookies)
                  .filter(cookie -> cookie.getName()
                    .equals("access_token"))
                  .findFirst();
                if (jwtCookie.isPresent()) {
                    String jwtString = jwtCookie.get().getValue();
                    Map<?, ?> jwt = getJwt(jwtString);
                    User user;

                    //Check if user exists in the database:
                    Optional<User> dbUser = userRepository.findById((String) jwt.get("sub"));
                    if (dbUser.isPresent()) {
                        user = dbUser.get();
                    } else {
                        User newUser = User.builder()
                          .id((String) jwt.get("sub"))
                          .firstName((String) jwt.get("given_name"))
                          .lastName((String) jwt.get("family_name"))
                          .email((String) jwt.get("email"))

                          .build();
                        user = userRepository.save(newUser);
                    }

                    Authentication authenticated = new UsernamePasswordAuthenticationToken(
                      user, jwt, new ArrayList<>());
                    SecurityContextHolder.getContext().setAuthentication(authenticated);

                    return authenticated;
                }
            }

        } catch (Exception e) {
            // Handle the exception and return an unauthorized response
            LOGGER.info("Jwt exception:" + e);
            throw new AuthenticationServiceException("Authentication failed: " + e.getMessage());
        }
        throw new AuthenticationServiceException("No JWT token found in cookies");
    }

    private Map<?, ?> getJwt(String jwtToken) throws Exception {
        String jwtPayload = jwtToken.split("\\.")[1];
        return objectMapper.readValue(
          new String(base64Decoder.decode(jwtPayload)),
          Map.class);
    }
}
