package org.gg.repository;


import java.util.List;
import java.util.Optional;
import org.gg.model.Restaurant;
import org.gg.model.RestaurantType;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RestaurantRepository extends MongoRepository<Restaurant, String> {
//Spring boot automatically scans classes which are extended as above, and they inherited all functionalities of MongoRepository.


    // Custom method to get all restaurants within the same postcode area
    List<Restaurant> findByPostcodeStartingWith(String prefix);

    Optional<List<Restaurant>> getAllByRestaurantType(RestaurantType type);

}
