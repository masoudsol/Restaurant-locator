package masonemobile.restaurantlocator.modules.repository;

import android.arch.lifecycle.MutableLiveData;
import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import masonemobile.restaurantlocator.R;
import masonemobile.restaurantlocator.modules.models.Business;

/**
 * Singleton pattern
 */
public class Repository {

    private static Repository instance;
    private List<Business> businesses = new ArrayList<>();

    private int selectedBussinuess;
    private HashMap<String, Bitmap> imageCache = new HashMap<>();

    public static Repository getInstance(){
        if(instance == null){
            instance = new Repository();
        }
        return instance;
    }

    public void setBusinesses(List<Business> businesses) {
        this.businesses = businesses;
    }

    public void setSelectedBussinuess(int selectedBussinuess) {
        this.selectedBussinuess = selectedBussinuess;
    }

    public void setPhotoThumbnails(String id, Bitmap image) {
        this.imageCache.put(id, image);
    }

    public HashMap<String, Bitmap> getPhotoThumbnails() {
        return imageCache;
    }

    public MutableLiveData<List<Business>> getRestaurantsMutableLiveData(){
        MutableLiveData<List<Business>> episodesNamesMutableLiveData = new MutableLiveData<>();
        episodesNamesMutableLiveData.setValue(businesses);

        return episodesNamesMutableLiveData;
    }

    public Bitmap getImage(final String url){
        return imageCache.get(url);
    }

    public List<Business> getBusinesses() {
        return businesses;
    }

    public int getSelectedBussinuess() {
        return selectedBussinuess;
    }

    public void sort(final int sortID) {
        Collections.sort(businesses, new Comparator<Business>(){
            @Override
            public int compare(Business businessModel, Business b1) {
                if (sortID == R.id.sort_alpha) {
                    return businessModel.name.compareToIgnoreCase(b1.name); // To compare string values
                } else if (sortID == R.id.sort_distance){
                    return Float.compare(businessModel.distance, b1.distance); // To compare integer values
                } else if (sortID == R.id.sort_rating){
                    return Float.compare(b1.rating, businessModel.rating); // To compare integer values
                }
                return 0;
            }
        });
    }
}











