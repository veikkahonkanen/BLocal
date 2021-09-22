package com.bteam.blocal.ui.shared;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.bteam.blocal.R;
import com.bteam.blocal.utility.NavigationResult;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

//Based on tutorial https://www.youtube.com/watch?v=Z5hONYWa0b4
public class ConfirmPlaceDialog extends DialogFragment implements OnMapReadyCallback {
    public static final String DIALOG_LOC_RESULT_KEY = "dialog_loc";
    private LatLng pos;
    private String address;
    private TextView adressTxt;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ConfirmPlaceDialogArgs args = ConfirmPlaceDialogArgs.fromBundle(getArguments());
        pos = args.getPos();
        address = args.getAddress();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.partial_confirm_location, null,
                false);
        adressTxt = v.findViewById(R.id.myAddress);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.mapp);
        mapFragment.getMapAsync(this);

        v.findViewById(R.id.btn_select).setOnClickListener(l -> {
            NavigationResult.setNavigationResult(this, DIALOG_LOC_RESULT_KEY, true);
            dismiss();
        });

        v.findViewById(R.id.btn_change_location).setOnClickListener(l -> dismiss());

        return v;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.getUiSettings().setAllGesturesEnabled(false);
        adressTxt.setText(address);
        googleMap.getUiSettings().setMyLocationButtonEnabled(false);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(pos);

        markerOptions.title(address);
        googleMap.clear();
        CameraUpdate location = CameraUpdateFactory.newLatLngZoom(pos, 16f);
        googleMap.animateCamera(location);
        googleMap.addMarker(markerOptions);
    }
}
