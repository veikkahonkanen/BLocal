package com.bteam.blocal.ui.shared.create_store;

import android.graphics.Bitmap;

import androidx.lifecycle.ViewModel;

import com.bteam.blocal.data.IOnCompleteCallback;
import com.bteam.blocal.data.model.StoreModel;
import com.bteam.blocal.data.repository.StoreRepository;
import com.bteam.blocal.data.repository.UserRepository;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.firestore.GeoPoint;

public class CreateStoreViewModel extends ViewModel {
    // Used to keep state
    private LatLng location;
    private String imageUrl;
    private StoreRepository storeRepository;

    public CreateStoreViewModel() {
        storeRepository = StoreRepository.getInstance();
    }

    public LatLng getLocation() {
        return location;
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void createStore(String name, String description,
                            IOnCompleteCallback<StoreModel> callback) {
        String userUid = UserRepository.getInstance().getUser().getValue().getUid();
        StoreModel storeModel = new StoreModel(name, userUid, new GeoPoint(location.latitude,
                location.longitude), description, imageUrl);
        storeRepository.createStore(storeModel, callback);
    }

    public void uploadImage(Bitmap image, IOnCompleteCallback<String> callback) {
        storeRepository.uploadStoreImage(image, callback);
    }

}
