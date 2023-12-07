package org.gg.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;
import java.util.Optional;
import org.gg.config.SecurityConfig;
import org.gg.model.Restaurant;
import org.gg.model.RestaurantType;
import org.gg.model.User;
import org.gg.repository.UserRepository;
import org.gg.service.RestaurantService;
import org.gg.utils.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

/**
 * Unit Test to test the Restaurant Controller.
 */
@WebMvcTest(RestaurantController.class)
public class RestaurantControllerTest {

    @Autowired
    public MockMvc mvc;

    @MockBean
    public RestaurantService restaurantService;


    @MockBean
    UserRepository userRepository;

    @Autowired
    private WebApplicationContext context;

    private Authentication authentication;

    @BeforeEach
    void setup() {
        this.mvc = MockMvcBuilders
          .webAppContextSetup(context)
          .apply(springSecurity())  // This is necessary for Spring Security setup
          .build();

        authentication = SecurityContextHolder.getContext().getAuthentication();
    }

    // Reduces repetitive code
    private Restaurant createSampleRestaurant() {
        // Create and return a sample Restaurant for testing
        return new Restaurant("1", "Sample Restaurant", "Contact Info", "Address", "Image URL",
          "12345", 40.7128, -74.0060, RestaurantType.ITALIAN, Collections.emptyList());
    }

    //private String createSampleJwt() {
        //User user = new User("1", "Luke", "Skywalker", "LukeSkywalker", "luke@starwars.com", "password",
        //"1", "street", "12", "1234", "1234", "1234" , "test");


        //return new JwtTokenProvider().generateToken(user);
    //}

//    @Test
//    void testGetUserById_ValidId_Success() throws Exception {
//        String validId = "123";
//        Restaurant expectedRestaurant = createSampleRestaurant();
//        when(restaurantService.getRestaurantById(eq(validId))).thenReturn(Optional.of(expectedRestaurant));
//
//        // Attempts to run method in controller
//        //ResponseEntity<Restaurant> responseEntity = restaurantController.getUserById(validId);
//
//        MockHttpServletRequestBuilder getPerform = get("/restaurant")
//          .header(HttpHeaders.AUTHORIZATION, "Bearer " + "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiI2NTcwYTE4MzlkZTdiMTFkOTU4ZjI5NmUiLCJlbWFpbCI6ImNocmlzQHRlc3QxdXNlci5jb20iLCJpYXQiOjE3MDE5NDU2OTAsImV4cCI6MTcwMTk0OTI5MH0.WRdUb3eF2oUUqD1lDd26A0v7TJZUDCU_tHMoKURCoWkLmP8Aud6dkgECLczm_UV8bHJ-0OcAOOwYEQ-F269lyw") //+ //createSampleJwt())
//          .with(csrf());
//
//        System.out.println("");
//
//        // Asserts that the response is as expected
//
//        mvc.perform(getPerform).andExpect(status().isOk())
//          .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
//          .andExpect(jsonPath("$.id").value(validId));
//
//        verify(restaurantService, times(1)).getRestaurantById(validId);
//        reset(restaurantService);
//    }


}

