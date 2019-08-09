package masonemobile.services;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Handler;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import masonemobile.restaurantlocator.BuildConfig;
import masonemobile.restaurantlocator.R;
import masonemobile.restaurantlocator.modules.models.RestaurantModel;
import masonemobile.restaurantlocator.modules.repository.Repository;

public class APIServices {
    private static final String TAG = "APIServices";

    public interface NetworkListener {
        void onEvent(Object response, Exception error);   //method, which can have parameters
    }

    public interface CompletionListener {
        void onCompletion(Boolean success, Exception error);
    }

    private Context context;
    private Repository dataProvider;

    public APIServices(Context context) {
        this.context = context;
        dataProvider = Repository.getInstance();
    }

    public void fetchPhoto(final String url, final CompletionListener completionListener){

        // Image was not found in cache; load it from the server
        downloadImage(url, new NetworkListener() {
            @Override
            public void onEvent(Object response, Exception error) {
                if (error == null) {
                    Bitmap bitmap = (Bitmap) response;
                    dataProvider.setPhotoThumbnails(url, bitmap);
                    completionListener.onCompletion(true, null);
                } else {
                    completionListener.onCompletion(false,error);
                }
            }
        });

    }

    public void fetchRestaurant(Location location, final CompletionListener completionListener) {
        NetworkListener networkListener = new NetworkListener() {
            @Override
            public void onEvent(final Object response, final Exception error) {
                if (error == null) {
                    RestaurantModel restaurantModel = new Gson().fromJson((String) response, RestaurantModel.class);
                    dataProvider.setBusinesses(restaurantModel.businesses);

                    completionListener.onCompletion(true, error);

                }else {
                    completionListener.onCompletion( false, error);
                }

            }
        };

        String url = String.format(Locale.CANADA, BuildConfig.url,
                location.getLatitude(),
                location.getLongitude(),
                BuildConfig.resto_count);



        requestRestaurants(url, networkListener);

    }

    private void requestRestaurants(String url, final NetworkListener networkListener){
        // Request a string response\
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response != null) {
                            networkListener.onEvent(response, null);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                // Error handling
                Log.d(TAG, "onErrorResponse: "+context.getString(R.string.oops));
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
        Volley.newRequestQueue(context).add(stringRequest);
    }

    /* Start a thread to send http request to web server use HttpURLConnection object. */
    private void downloadImage(final String reqUrl, final NetworkListener networkListener)
    {
        Thread sendHttpRequestThread = new Thread()
        {
            @Override
            public void run() {
                // Maintain http url connection.
                HttpURLConnection httpConn = null;

                try {
                    URL url = new URL(reqUrl);

                    httpConn = (HttpURLConnection)url.openConnection();

                    // Set connection timeout and read timeout value.
                    httpConn.setConnectTimeout(10000);
                    httpConn.setReadTimeout(10000);

                    httpConn.setDoInput(true);
                    httpConn.connect();
                    InputStream input = httpConn.getInputStream();
                    final Bitmap myBitmap = BitmapFactory.decodeStream(input);
                    Handler mainHandler = new Handler(context.getMainLooper());
                    mainHandler.post(new Runnable() {

                        @Override
                        public void run() {
                            networkListener.onEvent(myBitmap,null);
                        }
                    });


                }catch(IOException ex)
                {
                    Log.e(TAG, ex.getMessage(), ex);
                }finally {
                    httpConn.disconnect();
                }
            }
        };
        sendHttpRequestThread.start();
    }
}
