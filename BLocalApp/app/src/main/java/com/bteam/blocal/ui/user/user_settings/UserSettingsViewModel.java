package com.bteam.blocal.ui.user.user_settings;

import androidx.lifecycle.ViewModel;

import com.bteam.blocal.data.repository.UserRepository;

public class UserSettingsViewModel extends ViewModel {
    public void logout() {
        UserRepository.getInstance().logout();
    }
}
