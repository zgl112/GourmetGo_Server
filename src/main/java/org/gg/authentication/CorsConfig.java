package org.gg.authentication;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * CORS configuration class prepared for the React frontend.
 */

@Configuration
public class CorsConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
          .allowedOrigins("http://localhost:3000") //only allow requests from
          .allowedMethods("GET", "POST", "PUT", "DELETE") //only allow methods of these type
          .allowedHeaders("*");
    }
}
