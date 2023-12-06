package org.gg.model;

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
    private double latitude;
    private double longitude;
    private RestaurantType restaurantType;
    private List<FoodItem> menu;

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