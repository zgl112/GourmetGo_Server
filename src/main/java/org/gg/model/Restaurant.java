package org.gg.model;

import com.google.maps.model.LatLng;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "restaurants")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Restaurant {
    @Id
    private String id;
    private String name;
    private String contactInfo;
    private String address;
    private String imageUrl;
    private String postcode;
    private LatLng latLng;
    private RestaurantType restaurantType;
    private List<FoodItem> menu;

    public Restaurant(String s, String oldName, String oldContact, String oldAddress, String oldImage, String s1, LatLng latLng, RestaurantType restaurantType, Object o) {
    }

    @Override
    public String toString() {
        return "Location{" +
          "id='" + id + '\'' +
          ", postcode='" + postcode + '\'' +
          ", latitude=" + latitude +
          ", longitude=" + longitude +
          '}';
    }
}