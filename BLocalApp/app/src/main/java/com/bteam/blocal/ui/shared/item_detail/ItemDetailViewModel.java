package com.bteam.blocal.ui.shared.item_detail;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.bteam.blocal.data.model.ItemModel;
import com.bteam.blocal.data.model.Resource;
import com.bteam.blocal.data.repository.StoreRepository;

public class ItemDetailViewModel extends ViewModel {

    private String storeUid;
    private String itemUid;

    private LiveData<Resource<ItemModel>> itemDetail;

    public LiveData<Resource<ItemModel>> getItemDetail() {
        if (itemDetail == null) {
            StoreRepository repository = StoreRepository.getInstance();
            itemDetail = repository.getStoreItemLive(
                    storeUid == null ? repository.getMyStoreUid() : storeUid, itemUid);
        }
        return itemDetail;
    }


    public void setStoreUid(String uid) {
        storeUid = uid;
    }

    public void setItemUid(String uid){
        itemUid = uid;
    }

}
