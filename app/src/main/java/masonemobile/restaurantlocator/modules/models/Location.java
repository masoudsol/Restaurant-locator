package masonemobile.restaurantlocator.modules.models;

import java.io.Serializable;
import java.util.List;

public class Location implements Serializable {
    public String address1;
    public String address2;
    public String address3;
    public String city;
    public String zip_code;
    public String country;
    public String state;
    public List<String> display_address;
}
