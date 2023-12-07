package org.gg.controller;

import com.google.maps.errors.ApiException;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import org.gg.model.Restaurant;
import org.gg.model.RestaurantType;
import org.gg.service.RestaurantService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Endpoint for the Restaurant functionality for the application.
 */
@RestController
@RequestMapping("/restaurant")
public class RestaurantController {

    private final RestaurantService restaurantService;

    public RestaurantController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    // Get a user by ID
    @GetMapping("/{id}")
    public ResponseEntity<Restaurant> getUserById(@PathVariable String id) {
        Optional<Restaurant> optionalRestaurant = restaurantService.getRestaurantById(id);
        return optionalRestaurant.map(restaurant -> new ResponseEntity<>(restaurant, HttpStatus.OK))
          .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping
    public ResponseEntity<List<Restaurant>> getAllRestaurants() {
        List<Restaurant> restaurants = restaurantService.getAll();
        return new ResponseEntity<>(restaurants, HttpStatus.OK);
    }

    @GetMapping("/{range}/{postcode}")
    public ResponseEntity<List<Restaurant>> getByRange(@PathVariable Integer range, @PathVariable String postcode)
      throws IOException, InterruptedException, ApiException {
        List<Restaurant> restaurants = restaurantService.getAllByRange(postcode, range);
        return new ResponseEntity<>(restaurants, HttpStatus.OK);
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<Restaurant>> getAllByType(@PathVariable RestaurantType type) {
        Optional<List<Restaurant>> optionalRestaurant = restaurantService.getAllByType(type);
        return optionalRestaurant.map(restaurants -> new ResponseEntity<>(restaurants, HttpStatus.OK))
          .orElseGet(() ->new ResponseEntity<>(HttpStatus.NOT_FOUND));

    }

    @PostMapping
    public ResponseEntity<Restaurant> createRestaurant(@RequestBody Restaurant restaurant)
      throws IOException, InterruptedException, ApiException {
        Restaurant createdRestaurant = restaurantService.createRestaurant(restaurant);
        return new ResponseEntity<>(createdRestaurant, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Restaurant> updateRestaurant(@RequestBody Restaurant restaurant, @PathVariable String id)
      throws IOException, InterruptedException, ApiException {
        Restaurant updatedRestaurant = restaurantService.updateRestaurant(restaurant, id);
        return new ResponseEntity<>(updatedRestaurant, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRestaurant(@PathVariable String id) {
        restaurantService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
