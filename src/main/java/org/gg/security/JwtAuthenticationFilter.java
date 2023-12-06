package org.gg.security;

import static org.aspectj.weaver.tools.cache.SimpleCacheFactory.path;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jwt.proc.BadJWTException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.util.Base64;
import java.util.Map;
import javax.security.sasl.AuthenticationException;
import lombok.extern.slf4j.Slf4j;
import org.gg.model.User;
import org.gg.repository.UserRepository;
import org.gg.utils.HashUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.filter.OncePerRequestFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.webjars.NotFoundException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final Base64.Decoder base64Decoder;

    private final ObjectMapper objectMapper;

    private final UserRepository userRepository;

    public JwtAuthenticationFilter(UserRepository userRepository) {

        this.userRepository = userRepository;
        this.base64Decoder = Base64.getUrlDecoder();
        this.objectMapper = new ObjectMapper();
    }

    // Method to get the accessToken cookie out of the request cookies
    private String extractJwtFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("accessToken")) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    private UserDetails loadUserByEmail(String email) {
        try {
            User user = userRepository.getUsersByEmail(email);
            // Load user details from UserRepository
            return userRepository.getUsersByEmail(email);
        }
        catch (Exception e){
            throw new NotFoundException("No User Found with name" + email);
        }
    }

    private Map<?, ?> getJwt(String jwtToken) throws Exception {
        String jwtPayload = jwtToken.split("\\.")[1];
        return objectMapper.readValue(
          new String(base64Decoder.decode(jwtPayload)),
          Map.class);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
        try {
            String jwt = extractJwtFromCookie(request);
            if (jwt != null) {

                String email = (String) getJwt(jwt).get("email");

                UserDetails userDetails = loadUserByEmail(email);

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                  userDetails, null, userDetails.getAuthorities());

                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            throw new ServletException("Error authenticating user", e);
        }
        filterChain.doFilter(request, response);
    }
}
