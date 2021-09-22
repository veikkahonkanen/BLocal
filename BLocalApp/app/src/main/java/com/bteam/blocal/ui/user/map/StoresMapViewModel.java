package com.bteam.blocal.ui.user.map;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.bteam.blocal.data.model.Resource;
import com.bteam.blocal.data.model.StoreModel;
import com.bteam.blocal.data.repository.StoreRepository;

import java.util.List;

public class StoresMapViewModel extends ViewModel {
    private LiveData<Resource<List<StoreModel>>> nearbyStores;

    public LiveData<Resource<List<StoreModel>>> getNearbyStores() {
        if (nearbyStores == null) {
            nearbyStores = StoreRepository.getInstance().getStores();
        }

        return nearbyStores;
    }
}