package org.gg.security;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import org.springframework.security.authentication.AuthenticationManager;

public class JwtAuthenticationFilter implements Filter {
    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {

    }
}
