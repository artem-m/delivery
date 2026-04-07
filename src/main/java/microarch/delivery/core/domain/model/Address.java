package microarch.delivery.core.domain.model;

import java.util.Objects;

@lombok.Getter
public class Address {

    private final String country;
    private final String city;
    private final String street;
    private final String house;
    private final String apartment;

    private Address(String country, String city, String street, String house, String apartment) {
        this.country = country;
        this.city = city;
        this.street = street;
        this.house = house;
        this.apartment = apartment;
    }

    public static Address create(String country, String city, String street, String house, String apartment) {
        Objects.requireNonNull(country);
        Objects.requireNonNull(city);
        Objects.requireNonNull(street);
        Objects.requireNonNull(house);
        Objects.requireNonNull(apartment);
        return new Address(country, city, street, house, apartment);
    }
}
