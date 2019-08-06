package masonemobile.restaurantlocator.Views;

import android.Manifest;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.Collections;
import java.util.Comparator;

import masonemobile.restaurantlocator.Models.RestaurantModel;
import masonemobile.restaurantlocator.R;
import masonemobile.restaurantlocator.Utils.Utilities;
import masonemobile.restaurantlocator.ViewModels.RestaurantViewModel;
import masonemobile.restaurantlocator.adapters.GridViewAdapter;

public class RestaurantGridView extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {

    private Location location; // Location
    private RestaurantModel restaurantModel;
    private GridViewAdapter gridViewAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RestaurantViewModel restaurantViewModel;

    private static final String[] INITIAL_PERMS={
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };

    private static final int REQUEST_CODE =1337;

    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters

    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60; // 1 minute

    // Declaring a Location Manager
    private LocationManager locationManager;

    private final LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(final Location location) {
            RestaurantGridView.this.location = location;
            locationManager.removeUpdates(this);

            restaurantViewModel.requestRestaurants(location);
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {}

        @Override
        public void onProviderEnabled(String s) {}

        @Override
        public void onProviderDisabled(String s) {}
    };

    public Location retrieveLocation(){
        return location;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.resto_grid_layout);

        restaurantViewModel = ViewModelProviders.of(this).get(RestaurantViewModel.class);

        swipeRefreshLayout = findViewById(R.id.swiperefresh);
        swipeRefreshLayout.setRefreshing(true);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (location == null){
                    requestPermissions(INITIAL_PERMS, REQUEST_CODE);
                }else {
                    restaurantViewModel.requestRestaurants(location);
                }
            }
        });

        // Declaring a Location Manager
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        requestPermissions(INITIAL_PERMS, REQUEST_CODE);

        GridView gridView = findViewById(R.id.resto_gridview);

        gridViewAdapter = new GridViewAdapter(this);

        gridView.setAdapter(gridViewAdapter);

        restaurantViewModel.getRestaurantModelMutableLiveData().observe(this,new Observer<RestaurantModel>() {
            @Override
            public void onChanged(@Nullable RestaurantModel restaurantModel) {
                swipeRefreshLayout.setRefreshing(false);

                RestaurantGridView.this.restaurantModel = restaurantModel;

                gridViewAdapter.setRestaurantModel(restaurantModel);
                gridViewAdapter.notifyDataSetChanged();
            }
        });

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(RestaurantGridView.this, RestaurantDetailView.class);
                intent.putExtra(Utilities.getInstance().BUNDLEID, restaurantModel.businesses.get(i));
                startActivity(intent);

            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == REQUEST_CODE) {
            // Request for camera permission.
            if (grantResults.length == 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                getLocation();
            } else {
                Utilities.getInstance().showAlertView(this, getString(R.string.we_need), getString(R.string.retry), getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        RestaurantGridView.this.requestPermissions(INITIAL_PERMS, REQUEST_CODE);
                    }
                }, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        RestaurantGridView.this.swipeRefreshLayout.setRefreshing(false);

                        dialogInterface.cancel();
                    }
                });
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final int id = item.getItemId();

        Collections.sort(restaurantModel.businesses, new Comparator<RestaurantModel.Business>(){
            @Override
            public int compare(RestaurantModel.Business businessModel, RestaurantModel.Business b1) {
                if (id == R.id.sort_alpha) {
                    return businessModel.name.compareToIgnoreCase(b1.name); // To compare string values
                } else if (id == R.id.sort_distance){
                    return Float.compare(businessModel.distance, b1.distance); // To compare integer values
                } else if (id == R.id.sort_rating){
                    return Float.compare(b1.rating, businessModel.rating); // To compare integer values
                }
                return 0;
            }
        });

        gridViewAdapter.notifyDataSetChanged();

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        restaurantViewModel.getRestaurantModelMutableLiveData().removeObservers(this);

    }

    private void getLocation() {
        try {
            // Getting GPS status
            boolean isGPSEnabled = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);

            // Getting network status
            boolean isNetworkEnabled = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (isGPSEnabled || isNetworkEnabled) {

                if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }

                if (isNetworkEnabled) {
                    locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, locationListener);

                    if (locationManager != null) {
                        location = locationManager
                                .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    }
                }
                // If GPS enabled, get latitude/longitude using GPS Services
                if (isGPSEnabled) {
                    if (location == null) {
                        locationManager.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, locationListener);
                        if (locationManager != null) {
                            location = locationManager
                                    .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        }
                    }
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
