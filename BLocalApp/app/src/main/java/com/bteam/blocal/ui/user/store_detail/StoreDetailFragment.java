package com.bteam.blocal.ui.user.store_detail;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.bteam.blocal.R;
import com.bteam.blocal.utility.Constants;
import com.bumptech.glide.Glide;

public class StoreDetailFragment extends Fragment {
    private static final String TAG = "StoreDetailFragment";
    private StoreDetailViewModel storeDetailViewModel;

    private TextView storeName, storeOwner;
    private Button btnItems;
    private ImageView imageStore;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        storeDetailViewModel = new ViewModelProvider(this).get(StoreDetailViewModel.class);
        storeDetailViewModel.setArguments(getArguments());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_store_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        storeName = view.findViewById(R.id.txt_store_detail_name);
        storeOwner = view.findViewById(R.id.txt_store_description);
        btnItems = view.findViewById(R.id.btn_store_detail_items);
        imageStore = view.findViewById(R.id.img_store_detail_icon);
        btnItems.setOnClickListener(v -> navigateToItemList());

        storeDetailViewModel.getStoreDetail().observe(getViewLifecycleOwner(),
                storeModelResource -> {
            switch (storeModelResource.status) {
                case SUCCESS:
                    storeName.setText(storeModelResource.data.getName());
                    storeOwner.setText(storeModelResource.data.getDescription());
                    Glide.with(getContext()).load(storeModelResource.data.getImageUrl())
                            .apply(Constants.getStoreDefaultOptions()).into(imageStore);
                    btnItems.setEnabled(true);
                    break;
                default:
                    btnItems.setEnabled(false);
                    break;
            }
        });
    }

    private void navigateToItemList() {
        StoreDetailFragmentDirections.ShowStoreItems dir = StoreDetailFragmentDirections
                .showStoreItems(storeDetailViewModel.getStoreDetail().getValue().data.getUid());
        NavHostFragment.findNavController(this)
                .navigate(dir);
    }
}