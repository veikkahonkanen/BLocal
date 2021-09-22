package com.bteam.blocal.ui.store.item_finder;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.bteam.blocal.data.model.ItemModel;
import com.bteam.blocal.data.model.Resource;
import com.bteam.blocal.data.repository.StoreRepository;

import java.util.List;

public class ItemFinderViewModel extends ViewModel {

    public LiveData<Resource<List<ItemModel>>> findItemUid(String barcode){
        return StoreRepository.getInstance().findItemByCode(barcode);
    }
}
