package com.bteam.blocal.ui.store.edit_item;

import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.bteam.blocal.data.IOnCompleteCallback;
import com.bteam.blocal.data.model.ItemModel;
import com.bteam.blocal.data.model.Resource;
import com.bteam.blocal.data.repository.StoreRepository;
import com.bteam.blocal.utility.SingleLiveEvent;

public class EditItemViewModel extends ViewModel {
    private String imageUrl;

    public boolean getIsModeEdit(){
        return uid != null && !uid.isEmpty();
    }
    private String uid;

    private final StoreRepository storeRepository;

    private LiveData<Resource<ItemModel>> itemDetail;
    public LiveData<Resource<ItemModel>> getItemDetail(){
        return itemDetail;
    }


    public EditItemViewModel(){
        storeRepository = StoreRepository.getInstance();
    }
    public void updateItem(ItemModel model, IOnCompleteCallback<Void> callback){
        storeRepository.updateItem(uid, model ,callback);
    }

    public void createItem(ItemModel model, IOnCompleteCallback<ItemModel> callback){
        storeRepository.createItem(model, callback);
    }
    public void uploadImage(Bitmap image, IOnCompleteCallback<String> callback){
        storeRepository.uploadItemImage(image, callback);
    }

    public void setArguments(Bundle savedInstanceState) {
        StoreRepository repository = StoreRepository.getInstance();
        EditItemFragmentArgs args = EditItemFragmentArgs.fromBundle(savedInstanceState);
        if(args != null){
            String uid = args.getItemUid();
            // this function is called when the screen rotates.
            // We only want to get the data the first time
            if(itemDetail == null){
                if(uid != null && !uid.equals(this.uid)){
                    itemDetail = repository.getStoreItem(repository.getMyStoreUid(), uid);
                    this.uid = uid;
                }
                else{
                    itemDetail = new SingleLiveEvent<>();
                }
            }
        }
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}