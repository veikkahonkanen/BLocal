package com.bteam.blocal.ui.shared.connection_info;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bteam.blocal.R;
import com.google.android.material.snackbar.Snackbar;

import java.util.Calendar;

public class ConnectionInfoFragment extends Fragment {
    private ConnectionInfoViewModel viewModel;

    private Button btnCheck;
    private TextView txtInfo;

    public ConnectionInfoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = new ViewModelProvider(this).get(ConnectionInfoViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_connection_info, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnCheck = view.findViewById(R.id.connection_info_button);
        txtInfo = view.findViewById(R.id.connection_info_text);

        btnCheck.setOnClickListener(v -> getConnectionInfo());

        viewModel.getConnectionInfo().observe(getViewLifecycleOwner(), s -> txtInfo.setText(s));
    }

    private void getConnectionInfo() {
        Snackbar.make(getView(), R.string.snackbar_connection_info, Snackbar.LENGTH_SHORT).show();
        ConnectivityManager cm = (ConnectivityManager) getContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        String connectionInfo = Calendar.getInstance().getTime().toString();

        if (null == info) {
            connectionInfo += "\n" + this.getResources().getString(R.string.no_connection);
        } else {
            connectionInfo += "\n" + this.getResources().getString(R.string.network_type) + info.getTypeName();
            if (info.getTypeName().equals("WIFI")) {
                WifiManager mainWifi = (WifiManager) getContext().getApplicationContext()
                        .getSystemService(Context.WIFI_SERVICE);
                WifiInfo currentWifi = mainWifi.getConnectionInfo();
                connectionInfo += "\n\nWiFi SSID: " + currentWifi.getSSID() +
                        "\n" + this.getResources().getString(R.string.wifi_link_speed) + currentWifi.getLinkSpeed() +
                        " " + WifiInfo.LINK_SPEED_UNITS +
                        "\n" + this.getResources().getString(R.string.wifi_frequency) + currentWifi.getFrequency() +
                        " " + WifiInfo.FREQUENCY_UNITS;
            } else {
                connectionInfo += "\n" + this.getResources().getString(R.string.network_subtype) + info.getSubtypeName();
            }
        }
        viewModel.setConnectionInfo(connectionInfo);
    }
}