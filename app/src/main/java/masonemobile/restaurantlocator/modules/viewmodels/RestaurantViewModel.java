package masonemobile.restaurantlocator.modules.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.location.Location;
import android.support.annotation.NonNull;

import java.util.List;

import masonemobile.restaurantlocator.modules.models.Business;
import masonemobile.restaurantlocator.modules.repository.Repository;
import masonemobile.services.APIServices;

public class RestaurantViewModel extends AndroidViewModel {
    private MutableLiveData<List<Business>> restaurantModelMutableLiveData;
    private Repository repo = Repository.getInstance();
    private APIServices apiServices;

    public RestaurantViewModel(@NonNull Application application) {
        super(application);

        restaurantModelMutableLiveData = repo.getRestaurantsMutableLiveData();
        apiServices = new APIServices(application);

    }

    public void fetchRestaurants(Location location) {
        apiServices.fetchRestaurant(location, new APIServices.CompletionListener() {
            @Override
            public void onCompletion(Boolean success, Exception error) {
                if (success) {
                    restaurantModelMutableLiveData.postValue(repo.getBusinesses());
                }
            }
        });
    }

    public MutableLiveData<List<Business>> getRestaurantModelMutableLiveData() {
        return restaurantModelMutableLiveData;
    }

    public void sort(int sortID){
        repo.sort(sortID);
    }

    public void setSelected(int index){
        repo.setSelectedBussinuess(index);
    }
}
