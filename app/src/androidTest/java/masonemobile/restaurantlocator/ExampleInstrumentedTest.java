package masonemobile.restaurantlocator;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.location.LocationManager;
import android.support.annotation.Nullable;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import masonemobile.restaurantlocator.Models.RestaurantModel;
import masonemobile.restaurantlocator.ViewModels.RestaurantViewModel;
import masonemobile.restaurantlocator.Views.RestaurantGridView;

import static android.content.Context.LOCATION_SERVICE;
import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {

    @Rule
    public ActivityTestRule<RestaurantGridView> mainActivityActivityTestRule =
            new ActivityTestRule<>(RestaurantGridView.class);

    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("masonemobile.restaurantlocator", appContext.getPackageName());
    }

    @Test
    public void checkPermissions(){
        RestaurantGridView view = mainActivityActivityTestRule.getActivity();

        LocationManager locationManager = (LocationManager) view.getSystemService(LOCATION_SERVICE);

        // Getting GPS status
        assert locationManager != null;
        boolean isGPSEnabled = locationManager
                .isProviderEnabled(LocationManager.GPS_PROVIDER);

        // Getting network status
        boolean isNetworkEnabled = locationManager
                .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (!isGPSEnabled && !isNetworkEnabled) {
            fail();
        }
    }

    @Test
    public void checkLocation(){
        RestaurantGridView view = mainActivityActivityTestRule.getActivity();

        //This coordinate should be set to your location
        //Delta is the margin of forgiveness for location comparison
        double latitutde = 37.422;
        double delta = 0.1;
        double longtitude = -122.084;

        assertEquals(view.retrieveLocation().getLatitude(), latitutde, delta);
        assertEquals(view.retrieveLocation().getLongitude(), longtitude, delta);

    }

    @Test
    public void requestRestaurants() {
        RestaurantGridView view = mainActivityActivityTestRule.getActivity();

        RestaurantViewModel restaurantViewModel = ViewModelProviders.of(view).get(RestaurantViewModel.class);

        final MutableLiveData<RestaurantModel> restaurantViewModelMutableLiveData = restaurantViewModel.getRestaurantModelMutableLiveData();
        restaurantViewModelMutableLiveData.observe(view,new Observer<RestaurantModel>() {
            @Override
            public void onChanged(@Nullable RestaurantModel restaurantModel) {
                assertNotEquals(null, restaurantModel);
                if (restaurantModel != null) {
                    assertEquals(BuildConfig.resto_count, restaurantModel.businesses.size());
                }
            }
        });

    }
}
