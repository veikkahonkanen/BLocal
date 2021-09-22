package com.bteam.blocal.ui.shared.place_picker;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.fragment.NavHostFragment;

import com.bteam.blocal.R;
import com.bteam.blocal.ui.shared.ConfirmPlaceDialog;
import com.bteam.blocal.ui.shared.map.MapsFragment;
import com.bteam.blocal.utility.NavigationResult;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

// Inspired by https://www.youtube.com/watch?v=Z5hONYWa0b4
public class PlacePickerFragment extends MapsFragment {
    private LatLng lastValidPos;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        NavigationResult.<Boolean>getDialogNavigationResult(this, R.id.navigation_place_picker,
                ConfirmPlaceDialog.DIALOG_LOC_RESULT_KEY, value -> {
            if (value) {
                NavigationResult.setNavigationResult(this, null, lastValidPos);
                NavHostFragment.findNavController(this).navigateUp();
            }
        });
    }

    @Override
    protected void onMapReady(GoogleMap googleMap) {
        googleMap.setOnMapClickListener(latLng -> {
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);

            String address = getAddress(latLng);
            markerOptions.title(address);
            googleMap.clear();
            CameraUpdate location = CameraUpdateFactory.newLatLngZoom(
                    latLng, 15);
            googleMap.animateCamera(location);
            googleMap.addMarker(markerOptions);
            lastValidPos = latLng;
            confirmPositionDialog(latLng, address);
        });
    }

    private void confirmPositionDialog(LatLng latLng, String address) {
        PlacePickerFragmentDirections.ConfirmLocation dir = PlacePickerFragmentDirections
                .confirmLocation(latLng, address);
        NavHostFragment.findNavController(this).navigate(dir);
    }

    private String getAddress(LatLng latLng) {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(getContext(), Locale.getDefault());

        try {
            // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            String address = "";
            if (!addresses.isEmpty()) {
                // If any additional address line present than only,
                // check with max available address lines by getMaxAddressLineIndex()
                address = addresses.get(0).getAddressLine(0);
            }
            return address;
        } catch (IOException e) {
            e.printStackTrace();
            return "No Address Found";
        }
    }
}