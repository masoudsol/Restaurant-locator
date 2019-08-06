package masonemobile.restaurantlocator.Models;

import java.io.Serializable;
import java.util.List;

public class RestaurantModel implements Serializable{
    public List<Business> businesses;

    public class Business implements Serializable{
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

        public class Category implements Serializable{
            public String alias;
            public String title;
        }

        public class Coordinates implements Serializable{
            public String latitude;
            public String longitude;
        }

        public class Location implements Serializable{
            public String address1;
            public String address2;
            public String address3;
            public String city;
            public String zip_code;
            public String country;
            public String state;
            public List<String> display_address;
        }
    }
}
