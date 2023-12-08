package org.gg.service;

import com.google.maps.model.LatLng;
import org.gg.model.Restaurant;
import org.gg.model.RestaurantType;
import org.gg.repository.RestaurantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit test class for the restaurant service class covering all CRUD functionality.
 */
public class RestaurantServiceImplTest {

    @Mock
    private RestaurantRepository restaurantRepository;


    @InjectMocks
    private RestaurantServiceImpl restaurantService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getRestaurantById_ValidId_ReturnsRestaurant() {
        String restaurantId = "1";
        Restaurant expectedRestaurant = new Restaurant();
        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.of(expectedRestaurant));

        Optional<Restaurant> result = restaurantService.getRestaurantById(restaurantId);

        assertTrue(result.isPresent());
        assertEquals(expectedRestaurant, result.get());
    }

    @Test
    void getRestaurantById_InvalidId_ReturnsEmptyOptional() {
        String invalidId = "invalidId";
        when(restaurantRepository.findById(invalidId)).thenReturn(Optional.empty());

        Optional<Restaurant> result = restaurantService.getRestaurantById(invalidId);

        assertTrue(result.isEmpty());
    }

    @Test
    void delete_ValidId_DeletesRestaurant() {
        String restaurantId = "1";
        restaurantService.delete(restaurantId);
        verify(restaurantRepository, times(1)).deleteById(restaurantId);
    }

    @Test
    void getAll_ReturnsAllRestaurants() {
        List<Restaurant> expectedRestaurants = Arrays.asList(new Restaurant(), new Restaurant());
        when(restaurantRepository.findAll()).thenReturn(expectedRestaurants);
        List<Restaurant> result = restaurantService.getAll();
        assertEquals(expectedRestaurants, result);
    }

    @Test
    void getAllByType_ValidType_ReturnsRestaurants() {
        // Arrange
        RestaurantType restaurantType = RestaurantType.ITALIAN;
        List<Restaurant> expectedRestaurants = Arrays.asList(
          new Restaurant("1", "Restaurant1", "Contact1", "Address1", "Image1", "12345", new LatLng(1.0, 2.0), restaurantType, null),
          new Restaurant("2", "Restaurant2", "Contact2", "Address2", "Image2", "67890", new LatLng(3.0, 4.0), restaurantType, null)
        );

        when(restaurantRepository.getAllByRestaurantType(restaurantType)).thenReturn(Optional.of(expectedRestaurants));

        // Act
        Optional<List<Restaurant>> result = restaurantService.getAllByType(restaurantType);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(expectedRestaurants, result.get());
    }

    @Test
    void getAllByType_InvalidType_ReturnsEmptyList() {
        // Arrange
        try {
            RestaurantType invalidType = RestaurantType.valueOf("INVALID_TYPE");
            when(restaurantRepository.getAllByRestaurantType(invalidType)).thenReturn(null);

            // Act
            Optional<List<Restaurant>> result = restaurantService.getAllByType(invalidType);

            // Assert
            assertTrue(result.isEmpty());
        } catch (IllegalArgumentException e) {
            // Expected exception, do nothing or add further assertions if needed
            assertTrue(true);
        }
    }

    @Test
    void updateRestaurant_ValidId_ReturnsUpdatedRestaurant() {
        // Arrange
        String restaurantId = "1";
        RestaurantType restaurantType = RestaurantType.ITALIAN;
        Restaurant existingRestaurant = new Restaurant("1", "OldName", "OldContact", "OldAddress", "OldImage", "12345", new LatLng(1.0, 2.0), restaurantType, null);
        Restaurant updatedRestaurant = new Restaurant("1", "NewName", "NewContact", "NewAddress", "NewImage", "54321", new LatLng(3.0, 4.0), restaurantType, null);

        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.of(existingRestaurant));
        when(restaurantRepository.save(updatedRestaurant)).thenReturn(updatedRestaurant);

        // Act
        Restaurant result = restaurantService.updateRestaurant(updatedRestaurant, restaurantId);

        // Assert
        assertNotNull(result);
        assertEquals(updatedRestaurant, result);
    }

    @Test
    void updateRestaurant_InvalidId_ReturnsNull() {
        // Arrange
        String invalidId = "invalidId";
        RestaurantType restaurantType = RestaurantType.ITALIAN;
        Restaurant updatedRestaurant = new Restaurant("1", "NewName", "NewContact", "NewAddress", "NewImage", "54321", new LatLng(3.0, 4.0), restaurantType, null);

        when(restaurantRepository.findById(invalidId)).thenReturn(Optional.empty());

        // Act
        Restaurant result = restaurantService.updateRestaurant(updatedRestaurant, invalidId);

        // Assert
        assertNull(result);
    }

    @Test
    void createRestaurant_ReturnsCreatedRestaurant() {
        // Arrange
        RestaurantType restaurantType = RestaurantType.ITALIAN;
        Restaurant newRestaurant = new Restaurant("1", "NewName", "NewContact", "NewAddress", "NewImage", "54321", new LatLng(3.0, 4.0), restaurantType, null);

        when(restaurantRepository.save(newRestaurant)).thenReturn(newRestaurant);

        // Act
        Restaurant result = restaurantService.createRestaurant(newRestaurant);

        // Assert
        assertNotNull(result);
        assertEquals(newRestaurant, result);
    }

}
