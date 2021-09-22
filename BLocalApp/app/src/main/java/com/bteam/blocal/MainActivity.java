package com.bteam.blocal;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.bteam.blocal.service.ForegroundService;
import com.bteam.blocal.ui.login.LoginFragmentDirections;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements FirebaseAuth.AuthStateListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startService();
    }


    @Override
    protected void onStart() {
        super.onStart();
        FirebaseAuth.getInstance().addAuthStateListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        FirebaseAuth.getInstance().removeAuthStateListener(this);
    }

    @Override
    protected void onDestroy() {
        stopService();
        super.onDestroy();
    }

    private void navigateToLogin() {
        NavController navHostController = Navigation.findNavController(this,
                R.id.main_nav_host_fragment);
        if (navHostController.getCurrentDestination().getId() != R.id.login) {
            navHostController.navigate(LoginFragmentDirections.actionGlobalLogin());
        }

    }

    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        if (firebaseAuth.getCurrentUser() == null) {
            navigateToLogin();
        }
    }

    private void startService(){
        Intent serviceIntent = new Intent(getApplication(), ForegroundService.class);
        startService(serviceIntent);
    }

    private void stopService(){
        Intent serviceIntent = new Intent(getApplication(), ForegroundService.class);
        stopService(serviceIntent);
    }
}