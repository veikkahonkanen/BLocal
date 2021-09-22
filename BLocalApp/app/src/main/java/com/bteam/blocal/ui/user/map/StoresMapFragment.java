package com.bteam.blocal.ui.user.map;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.bteam.blocal.R;
import com.bteam.blocal.data.model.StoreModel;
import com.bteam.blocal.utility.Constants;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class StoresMapFragment extends com.bteam.blocal.ui.shared.map.MapsFragment {
    private List<Marker> storeMarkers = new ArrayList<>();
    private StoresMapViewModel mapsViewModel;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mapsViewModel = new ViewModelProvider(this).get(StoresMapViewModel.class);

        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    protected void onMapReady(GoogleMap googleMap) {

        mapsViewModel.getNearbyStores().observe(getViewLifecycleOwner(), storesResource -> {
            switch (storesResource.status) {
                case SUCCESS:
                    List<StoreModel> stores = storesResource.data;
                    // Clear the store markers
                    for (Marker marker : storeMarkers) {
                        marker.remove();
                    }
                    storeMarkers.clear();

                    // Add them again
                    for (StoreModel store : stores) {
                        LatLng storePos = new LatLng(store.getLocation().getLatitude(),
                                store.getLocation().getLongitude());
                        Marker marker = googleMap.addMarker(new MarkerOptions().position(storePos)
                                .title(store.getName()));
                        marker.setTag(store);
                        storeMarkers.add(marker);
                    }
                    break;
                case ERROR:
                    Snackbar.make(getView(), R.string.err_stores_load,
                            Snackbar.LENGTH_LONG).show();
                    break;
            }
        });

        googleMap.setOnMarkerClickListener(marker -> {
            openStoreDialog((StoreModel) marker.getTag());
            return true;
        });
    }

    private void openStoreDialog(StoreModel storeModel) {
        MaterialAlertDialogBuilder alert = new MaterialAlertDialogBuilder(getContext())
                .setTitle(storeModel.getName())
                .setMessage(storeModel.getDescription())
                .setNeutralButton(R.string.lbl_close, (dialog, which) -> {
                })
                .setPositiveButton(R.string.view_store, (dialog, which) -> {
                    StoresMapFragmentDirections.OpenStoreDetailFromMaps dir =
                            StoresMapFragmentDirections
                                    .openStoreDetailFromMaps(storeModel.getUid());
                    NavHostFragment.findNavController(this)
                            .navigate(dir);
                });

        Glide.with(getContext()).load(storeModel.getImageUrl()).apply(Constants
                .getStoreDefaultOptions())
                .into(new CustomTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource,
                                                @Nullable Transition<? super Drawable> transition) {
                        alert.setIcon(resource);
                        alert.show();
                    }

                    @Override
                    public void onLoadFailed(@Nullable Drawable errorDrawable) {
                        super.onLoadFailed(errorDrawable);
                        alert.setIcon(errorDrawable);
                        alert.show();
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                    }
                });
    }
}