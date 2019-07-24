package masonemobile.restaurantlocator.ViewModels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.location.Location;
import android.support.annotation.NonNull;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import masonemobile.restaurantlocator.BuildConfig;
import masonemobile.restaurantlocator.Models.RestaurantModel;
import masonemobile.restaurantlocator.R;
import masonemobile.restaurantlocator.Utils.Utilities;

public class RestaurantViewModel extends AndroidViewModel {
    private MutableLiveData<RestaurantModel> restaurantModelMutableLiveData = new MutableLiveData<>();

    public MutableLiveData<RestaurantModel> getRestaurantModelMutableLiveData() {
        return restaurantModelMutableLiveData;
    }

    public RestaurantViewModel(@NonNull Application application) {
        super(application);
    }

    public void requestRestaurants(Location location){
        String url = String.format(Locale.CANADA, BuildConfig.url,
                location.getLatitude(),
                location.getLongitude(),
                BuildConfig.resto_count);

        // Request a string response\
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response != null) {
                            restaurantModelMutableLiveData.setValue(new Gson().fromJson(response, RestaurantModel.class));
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                // Error handling
                System.out.println(getApplication().getString(R.string.oops));
                error.printStackTrace();

            }
        }){
            @Override
            public Map<String, String> getHeaders(){
                Map<String,String> params = new HashMap<>();
                params.put("Authorization", BuildConfig.api_key);
                return params;
            }
        };

        // Add the request to the queue
        Volley.newRequestQueue(getApplication()).add(stringRequest);
    }
}
