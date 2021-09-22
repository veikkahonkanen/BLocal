package com.bteam.blocal.ui.user.store_list;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bteam.blocal.R;
import com.bteam.blocal.data.model.StoreModel;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentSnapshot;

public class StoreListFragment extends Fragment implements StoreListAdapter.IItemClickListener {
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1111;
    private static final String[] LOCATION_PERMISSIONS = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };

    private StoreListViewModel storeListViewModel;
    private StoreListAdapter storeListAdapter;

    public StoreListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        storeListViewModel =
                new ViewModelProvider(this).get(StoreListViewModel.class);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_store_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        storeListAdapter = new StoreListAdapter(this, new FirestorePagingOptions.Builder<StoreModel>()
                .setLifecycleOwner(this)
                .setQuery(storeListViewModel.getQuery(), storeListViewModel.getPagingConfig(), StoreModel.class)
                .build());
        SwipeRefreshLayout swipeRefreshLayout = view.findViewById(R.id.swiperefresh_store);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Clear items and obtain data again
                storeListAdapter.refresh();
            }
        });
        RecyclerView recyclerView = view.findViewById(R.id.list_stores);

        storeListAdapter.bindSwipeRefresh(swipeRefreshLayout);

        recyclerView.setAdapter(storeListAdapter);

        // Update the adapter with the new location
        storeListViewModel.getLastLocation().observe(getViewLifecycleOwner(),
                location -> storeListAdapter.setLastLocation(location));
    }

    @Override
    public void onStart() {
        super.onStart();
        storeListAdapter.startListening();
        checkLocationPermissions();

        // When the permission is granted, we can trigger location update
        if (hasLocationPermissions()) {
            storeListViewModel.updateLastLocation();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        storeListAdapter.stopListening();
    }

    @Override
    public void onItemClick(DocumentSnapshot document, int index) {
        StoreModel sm = document.toObject(StoreModel.class);
        StoreListFragmentDirections.OpenStoreDetailFromList dir = StoreListFragmentDirections
                .openStoreDetailFromList(sm.getUid());
        NavHostFragment.findNavController(this)
                .navigate(dir);
    }

    private void checkLocationPermissions() {
        if (!hasLocationPermissions()) {
            requestPermissions(LOCATION_PERMISSIONS, LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    private boolean hasLocationPermissions() {
        return ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length == LOCATION_PERMISSIONS.length) {
                for (final int grantResult : grantResults) {
                    if (grantResult != PackageManager.PERMISSION_GRANTED) {
                        if (shouldShowRequestPermissionRationale(Manifest
                                .permission.ACCESS_FINE_LOCATION)) {
                            showLocationPermissionSnackbar();
                        }
                        return;
                    }
                }
            }
        }
        storeListViewModel.updateLastLocation();
    }

    private void showLocationPermissionSnackbar() {
        Snackbar.make(getView(), R.string.snackbar_calculation_location, Snackbar.LENGTH_LONG)
                .setAction(R.string.snackbar_maps_location_try_again,
                        v -> checkLocationPermissions())
                .show();
    }
}