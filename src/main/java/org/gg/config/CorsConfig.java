package org.gg.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
          .allowedOrigins("http://localhost:3000") //only allow requests from this address
          // TODO: Replace with web hosted front end
          .allowedMethods("GET", "POST", "PUT", "DELETE") //only allow methods of these type
          .allowedHeaders("*");
    }
}
