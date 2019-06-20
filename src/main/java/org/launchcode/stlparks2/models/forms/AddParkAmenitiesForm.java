package org.launchcode.stlparks2.models.forms;

import org.launchcode.stlparks2.models.Amenity;
import org.launchcode.stlparks2.models.Park;

public class AddParkAmenitiesForm {

    private Park park;

    private Iterable<Amenity> amenities;

    private int amenityId;

    private int parkId;

    public AddParkAmenitiesForm(){}

    public AddParkAmenitiesForm(Park park, Iterable<Amenity> amenities) {
        this.park = park;
        this.amenities = amenities;
    }

    public Park getPark() {
        return park;
    }

    public void setPark(Park park) {
        this.park = park;
    }

    public Iterable<Amenity> getAmenities() {
        return amenities;
    }

    public void setAmenities(Iterable<Amenity> amenities) {
        this.amenities = amenities;
    }

    public int getAmenityId() {
        return amenityId;
    }

    public void setAmenityId(int amenityId) {
        this.amenityId = amenityId;
    }

    public int getParkId() {
        return parkId;
    }

    public void setParkId(int parkId) {
        this.parkId = parkId;
    }
}
