package com.bteam.blocal.utility;

import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

// Inspired in the following blog:
// https://firebase.googleblog.com/2017/12/using-android-architecture-components_22.html
public class FirebaseAuthLiveData extends LiveData<FirebaseUser> {
    private final FirebaseAuth auth;
    private final FirebaseAuth.AuthStateListener listener = new MyValueEventListener();
    private boolean listenerRemovalPending = false;
    private final Handler handler = new Handler();
    private final Runnable removeListener = new Runnable() {
        @Override
        public void run() {
            if (auth != null) {
                auth.removeAuthStateListener(listener);
            }
            listenerRemovalPending = false;
        }
    };

    public FirebaseAuthLiveData(FirebaseAuth auth) {
        this.auth = auth;
    }

    @Override
    protected void onActive() {
        super.onActive();
        if(listenerRemovalPending){
            handler.removeCallbacks(removeListener);
        }
        else{
            auth.addAuthStateListener(listener);
        }
        listenerRemovalPending = false;
    }

    @Override
    protected void onInactive() {
        super.onInactive();
        // Remove 2 seconds after to not rethrow query when user changes orientation
        // (onInactive is called)
        handler.postDelayed(removeListener, 2000);
        listenerRemovalPending = true;
    }

    private class MyValueEventListener implements FirebaseAuth.AuthStateListener {

        @Override
        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
            setValue(firebaseAuth.getCurrentUser());
        }
    }
}
