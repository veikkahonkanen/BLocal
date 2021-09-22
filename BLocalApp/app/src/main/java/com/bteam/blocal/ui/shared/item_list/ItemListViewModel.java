package com.bteam.blocal.ui.shared.item_list;

import androidx.lifecycle.ViewModel;
import androidx.paging.PagedList;

import com.bteam.blocal.data.repository.StoreRepository;
import com.google.firebase.firestore.Query;

public class ItemListViewModel extends ViewModel {

    private String storeUid;
    public Query getQuery(){
        StoreRepository storeRepository = StoreRepository.getInstance();
        return storeRepository.getItemsQuery(
                storeUid == null ? storeRepository.getMyStoreUid() : storeUid);
    }

    public PagedList.Config getPagingConfig(){
        // Init Paging Configuration
        return new PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setPrefetchDistance(2)
                .setPageSize(10)
                .build();
    }

    public void setStoreUid(String uid){
        storeUid = uid;
    }

    public String getStoreUid() {
        return storeUid;
    }
}