package org.gg.service;

import java.util.List;
import java.util.Optional;
import org.gg.model.Restaurant;
import org.gg.model.RestaurantType;

/**
 * Service interface for managing restaurants.
 */
public interface RestaurantService {

    /**
     * Retrieve a restaurant by its unique identifier.
     *
     * @param id The unique identifier of the restaurant.
     * @return An {@link Optional} containing the restaurant if found, or an empty optional otherwise.
     */
    Optional<Restaurant> getRestaurantById(String id);

    // Delete a restaurant by its unique identifier.
    void delete(String id);

    // Retrieve a list of all restaurants.
    List<Restaurant> getAll();

    List<Restaurant> getAllWithinRange(Integer range, String postcode);

    Optional<List<Restaurant>> getAllByType(RestaurantType type);

    //  Update an existing restaurant.
    Restaurant updateRestaurant(Restaurant restaurant, String id);

    // Create a new restaurant.
    Restaurant createRestaurant(Restaurant restaurant);

}
