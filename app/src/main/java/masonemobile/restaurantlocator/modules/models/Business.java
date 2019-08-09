package masonemobile.restaurantlocator.modules.models;

import java.io.Serializable;
import java.util.List;

public class Business implements Serializable {
    public String id;
    public String alias;
    public String name;
    public String image_url;
    public boolean is_closed;
    public String url;
    public int review_count;
    public List<Category> categories;
    public  float rating;
    public Coordinates coordinates;
    public List<String> transactions;
    public String price;
    public Location location;
    public  String phone;
    public String display_phone;
    public float distance;
}
