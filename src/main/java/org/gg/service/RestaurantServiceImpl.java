package org.gg.service;

import com.google.maps.DistanceMatrixApi;
import com.google.maps.GeoApiContext;
import com.google.maps.model.DistanceMatrix;
import com.google.maps.model.TravelMode;
import java.util.ArrayList;
import java.util.List;
import org.gg.model.RestaurantType;
import org.gg.utils.BeanUtil;
import java.util.Optional;
import org.gg.model.Restaurant;
import org.gg.repository.RestaurantRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class RestaurantServiceImpl implements RestaurantService {

    private final RestaurantRepository restaurantRepository;

    private final GeoApiContext geoApiContext;


    @Autowired
    public RestaurantServiceImpl(RestaurantRepository restaurantRepository, GeoApiContext geoApiContext) {
        this.restaurantRepository = restaurantRepository;
        this.geoApiContext = geoApiContext;
    }

    public long calculateDistance(String originPostcode, String destinationPostcode) {
        try {
            DistanceMatrix distanceMatrix = DistanceMatrixApi.newRequest(geoApiContext)
              .origins(originPostcode)
              .destinations(destinationPostcode)
              .mode(TravelMode.DRIVING)
              .await();

            return distanceMatrix.rows[0].elements[0].distance.inMeters;
        } catch (Exception e) {
            // Handle exceptions (e.g., API request error)
            e.printStackTrace();
            return -1; // Indicates an error
        }
    }

    @Override
    public Optional<Restaurant> getRestaurantById(String id) {return restaurantRepository.findById(id);}

    @Override
    public void delete(String id) { restaurantRepository.deleteById(id); }


    @Override
    public List<Restaurant> getAll() { return restaurantRepository.findAll();}

    @Override
    public List<Restaurant> getAllWithinRange(Integer range, String postcode) {
        // Get all restaurants
        List<Restaurant> allRestaurants = restaurantRepository.findAll();

        // Filter restaurants within the specified range
        List<Restaurant> restaurantsWithinRange = new ArrayList<>();
        // Add restaurants within specified range
        for (Restaurant restaurant : allRestaurants) {
            double distance = calculateDistance(postcode, restaurant.getPostcode());
            if (distance <= range) {
                restaurantsWithinRange.add(restaurant);
            }
        }

        // Check if any restaurants are found
        if (restaurantsWithinRange.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No restaurants found within the specified range");
        }

        return restaurantsWithinRange;
    }


    @Override
    public Optional<List<Restaurant>> getAllByType(RestaurantType type) {return restaurantRepository.getAllByRestaurantType(type);}

    @Override
    public Restaurant updateRestaurant(Restaurant restaurant, String id) {
        // Find existing Restaurant
        Optional<Restaurant> optionalExistingRestaurant = restaurantRepository.findById(id);

        // Patches in updated fields to new object and uses existing values if null.
        if (optionalExistingRestaurant.isPresent()) {
            Restaurant existingRestaurant = optionalExistingRestaurant.get();
            BeanUtils.copyProperties(restaurant, existingRestaurant, BeanUtil.getNullPropertyNames(restaurant));
            // Matches ID
            existingRestaurant.setId(id);

            return restaurantRepository.save(restaurant);
        }
        return null;
    }

    @Override
    public Restaurant createRestaurant(Restaurant restaurant) { return restaurantRepository.save(restaurant);}

}
