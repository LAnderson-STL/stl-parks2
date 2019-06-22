package org.launchcode.stlparks2.models;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;


@Entity
public class Park {

    @Id
    @GeneratedValue
    private int id;

    @NotNull
    @Size(min=1, max=150, message="Field may not be empty")
    private String name;


    @NotNull
    @Size(min=1, max=150, message="Field may not be empty")
    private String neighborhood;

    @NotNull
    @Size(min=1, max=150, message="Field may not be empty")
    private String streetAddress;

    @NotNull
    @Size(min=1, max=150, message="Field may not be empty")
    private String city;

    @NotNull
    @Size(min=1, max=500, message="Field may not be empty")
    private String website;

    @NotNull
    @Min(value = 5, message = "Zip code must contain between 5 and 9 characters")
    private int zip;

    @ManyToMany
    private List<Amenity> amenities;

    @ManyToMany(mappedBy = "parks")
    private List<User> users;

    //constructors

    public Park(){}

    public Park(String name, String neighborhood, String streetAddress, String city, int zip, String website) {
        this.name = name;
        this.neighborhood = neighborhood;
        this.streetAddress = streetAddress;
        this.city = city;
        this.zip = zip;
        this.website = website;

    }

    public void addAmenity(Amenity amenity) {amenities.add(amenity);}

    public void deleteAmenity(Amenity amenity) { amenities.remove(amenity); }



    //getters and setters


    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getZip() {
        return zip;
    }

    public void setZip(int zip) {
        this.zip = zip;
    }

    public String getNeighborhood() {
        return neighborhood;
    }

    public void setNeighborhood(String neighborhood) {
        this.neighborhood = neighborhood;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public List<Amenity> getAmenities() {
        return amenities;
    }

    public void setAmenities(List<Amenity> amenities) {
        this.amenities = amenities;
    }



}
