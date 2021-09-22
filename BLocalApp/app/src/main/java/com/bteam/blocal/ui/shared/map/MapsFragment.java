package com.bteam.blocal.ui.shared.map;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.bteam.blocal.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.snackbar.Snackbar;

public abstract class MapsFragment extends Fragment {

    private static final String TAG = "MapsFragment";
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1111;
    private static final String[] LOCATION_PERMISSIONS = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };

    private GoogleMap gMap;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_maps, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: requesting location update");
        checkLocationPermissions();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length == LOCATION_PERMISSIONS.length) {
                for (final int grantResult : grantResults) {
                    if (grantResult != PackageManager.PERMISSION_GRANTED) {
                        if (shouldShowRequestPermissionRationale
                                (Manifest.permission.ACCESS_FINE_LOCATION)) {
                            showLocationPermissionSnackbar();
                        }
                        return;
                    }
                }
                showCurrPosOnMap();
            }
        }
    }

    @SuppressLint("MissingPermission")
    private void showCurrPosOnMap() {
        if (gMap != null) {
            gMap.setMyLocationEnabled(true);
            FusedLocationProviderClient fusedLocationProviderClient = LocationServices
                    .getFusedLocationProviderClient(getActivity().getApplication());
            fusedLocationProviderClient.getLastLocation()
                    .addOnSuccessListener(location -> {
                        if (null != location) {
                            gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(
                                    location.getLatitude(), location.getLongitude()), 15));

                        } else {
                            Log.d(TAG, "requestCurrentLocation: Last location was not obtained!");
                        }
                    })
                    .addOnFailureListener(e -> Log.e(TAG, "requestCurrentLocation: " +
                            e.getLocalizedMessage(), e)
                    );
        }
    }


    private void checkLocationPermissions() {
        if (hasLocationPermissions()) {
            showCurrPosOnMap();
        } else {
            requestPermissions(LOCATION_PERMISSIONS,
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    private boolean hasLocationPermissions() {
        return ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private void showLocationPermissionSnackbar() {
        Snackbar.make(getView(), R.string.snackbar_maps_location_needed,
                Snackbar.LENGTH_LONG)
                .setAction(R.string.snackbar_maps_location_try_again,
                        v -> checkLocationPermissions())
                .show();
    }

    private OnMapReadyCallback callback = new OnMapReadyCallback() {
        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        @SuppressLint("MissingPermission")
        @Override
        public void onMapReady(GoogleMap googleMap) {
            gMap = googleMap;
            if (hasLocationPermissions()) {
                showCurrPosOnMap();
            }
            MapsFragment.this.onMapReady(googleMap);
        }
    };

    protected abstract void onMapReady(GoogleMap googleMap);
}
