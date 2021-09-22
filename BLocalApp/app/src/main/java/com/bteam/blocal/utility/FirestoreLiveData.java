package com.bteam.blocal.utility;

import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;

// Inspired in the following blog:
// https://firebase.googleblog.com/2017/12/using-android-architecture-components_22.html
public class FirestoreLiveData extends LiveData<DocumentSnapshot> {
    private final DocumentReference ref;
    private final EventListener<DocumentSnapshot> listener = new MyValueEventListener();
    private ListenerRegistration registration;
    private boolean listenerRemovalPending = false;
    private final Handler handler = new Handler();
    private final Runnable removeListener = new Runnable() {
        @Override
        public void run() {
            if (registration != null) {
                registration.remove();
            }
            listenerRemovalPending = false;
        }
    };

    public FirestoreLiveData(DocumentReference query) {
        this.ref = query;
    }

    @Override
    protected void onActive() {
        super.onActive();
        if(listenerRemovalPending){
            handler.removeCallbacks(removeListener);
        }
        else{
            registration = ref.addSnapshotListener(listener);
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

    private class MyValueEventListener implements EventListener<DocumentSnapshot> {

        @Override
        public void onEvent(@Nullable DocumentSnapshot value,
                            @Nullable FirebaseFirestoreException error) {
            if (error != null) {
                //TODO: Error, listen failed
                return;
            }
            if (value != null && value.exists()) {
                setValue(value);
            } else {
                //TODO: Data is null
            }
        }
    }
}
