package com.bteam.blocal.ui.user.main_user;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.bteam.blocal.data.repository.UserRepository;
import com.google.firebase.auth.FirebaseUser;

public class MainUserViewModel extends ViewModel {
    public LiveData<FirebaseUser> getCurrentUser(){
        return UserRepository.getInstance().getUser();
    }
}
