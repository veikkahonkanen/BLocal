package com.bteam.blocal.ui.user.store_detail;

import android.os.Bundle;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.bteam.blocal.data.model.Resource;
import com.bteam.blocal.data.model.StoreModel;
import com.bteam.blocal.data.repository.StoreRepository;

public class StoreDetailViewModel extends ViewModel {
    private LiveData<Resource<StoreModel>> storeDetail;

    public LiveData<Resource<StoreModel>> getStoreDetail(){
        return storeDetail;
    }

    public StoreDetailViewModel(){
        storeDetail = new MutableLiveData<>();
    }

    public void setArguments(Bundle savedInstanceState) {
        StoreRepository repository = StoreRepository.getInstance();
        String uid =  StoreDetailFragmentArgs.fromBundle(savedInstanceState).getStoreUid();
        storeDetail = repository.getStoreLive(uid);
    }
}