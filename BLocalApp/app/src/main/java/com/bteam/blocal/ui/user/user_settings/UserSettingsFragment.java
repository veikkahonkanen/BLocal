package com.bteam.blocal.ui.user.user_settings;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.bteam.blocal.R;

public class UserSettingsFragment extends PreferenceFragmentCompat {

    UserSettingsViewModel vm;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vm = new ViewModelProvider(this).get(UserSettingsViewModel.class);

    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.user_preferences, rootKey);
    }

    @Override
    public boolean onPreferenceTreeClick(Preference preference) {
        String key = preference.getKey();
        switch (key){
            case "btn_logout":
                vm.logout();
                return true;
            case "btn_connection_info":
                NavHostFragment.findNavController(this)
                        .navigate(R.id.openUserConnectionInfo);
                return true;
        }
        return super.onPreferenceTreeClick(preference);
    }
}