package com.bteam.blocal.ui.user.store_list;

import android.annotation.SuppressLint;
import android.app.Application;
import android.location.Location;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.PagedList;

import com.bteam.blocal.data.repository.StoreRepository;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.firestore.Query;

public class StoreListViewModel extends AndroidViewModel {
    private MutableLiveData<Location> lastLocation;

    private FusedLocationProviderClient fusedLocationClient;

    public StoreListViewModel(Application application) {
        super(application);
        this.fusedLocationClient = LocationServices.getFusedLocationProviderClient(application);

        this.lastLocation = new MutableLiveData<>();
    }

    public LiveData<Location> getLastLocation() {
        return lastLocation;
    }

    public Query getQuery(){
        return StoreRepository.getInstance().getStoreQuery();
    }

    public PagedList.Config getPagingConfig(){
        // Init Paging Configuration
        return new PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setPrefetchDistance(2)
                .setPageSize(10)
                .build();
    }

    @SuppressLint("MissingPermission")
    public void updateLastLocation() {
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(location -> {
                    if (null != location) {
                        lastLocation.setValue(location);
                    }
                });
    }
}
