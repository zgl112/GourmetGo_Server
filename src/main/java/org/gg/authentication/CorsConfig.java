package org.gg.authentication;
//
////import com.google.maps.GeoApiContext;
//import com.google.maps.GeoApiContext;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.CorsRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
///**
// * CORS configuration class prepared for the React frontend.
// */
//
//@Configuration
//public class CorsConfig implements WebMvcConfigurer {
//
//    @Value("${google.maps.apiKey}")
//    private String apiKey;
//
//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**")
//          .allowedOrigins("http://localhost:3000") //only allow requests from
//          .allowedMethods("GET", "POST", "PUT", "DELETE") //only allow methods of these type
//          .allowedHeaders("*");
//    }
//
//    @Bean
//    public GeoApiContext geoApiContext() {
//        return new GeoApiContext.Builder()
//          .apiKey(apiKey)
//          .build();
//    }
//}
