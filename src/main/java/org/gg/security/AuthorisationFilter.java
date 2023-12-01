package org.gg.security;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Map;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.servlet.HandlerExceptionResolver;

/**
 * Handles the authorisation aspect of the jwt.
 */
public class AuthorisationFilter extends GenericFilterBean {
    private final Base64.Decoder base64Decoder;
    private final ObjectMapper objectMapper;
    /**
     * Constructs a new instance of the AuthorizationFilter.
     *
     */
    public AuthorisationFilter() {
        this.base64Decoder = Base64.getUrlDecoder();
        this.objectMapper = new ObjectMapper();
    }

    private Map<?, ?> getJwtEmail(String jwtToken) throws Exception {
        String jwtPayload = jwtToken.split("\\.")[1];
        return objectMapper.readValue(
          new String(base64Decoder.decode(jwtPayload)),
          Map.class);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
      throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("access_token")) {
                    String jwtString = cookie.getValue();
                    // Parse JWT and extract email
                    try {
                        Map<?, ?> jwt = getJwtEmail(jwtString);


                    } catch (Exception e) {
                        logger.error(e.getMessage());
                        return;
                    }
                }
            }

        }
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
