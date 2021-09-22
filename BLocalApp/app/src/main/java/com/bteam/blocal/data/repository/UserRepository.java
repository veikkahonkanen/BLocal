package com.bteam.blocal.data.repository;

import androidx.lifecycle.LiveData;

import com.bteam.blocal.utility.FirebaseAuthLiveData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UserRepository {

    private static UserRepository _instance;
    private final FirebaseAuth auth;

    private FirebaseAuthLiveData _userLiveData;


    private UserRepository(){
        auth = FirebaseAuth.getInstance();
    }

    public static UserRepository getInstance() {
        if (_instance == null) {
            _instance = new UserRepository();
        }
        return _instance;
    }

    public LiveData<FirebaseUser> getUser(){
        if(_userLiveData == null){
            _userLiveData = new FirebaseAuthLiveData(auth);
        }
        return _userLiveData;
    }

    public void logout(){
        auth.signOut();
    }
}
