package com.bteam.blocal.data.repository;

import android.graphics.Bitmap;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.bteam.blocal.data.IOnCompleteCallback;
import com.bteam.blocal.data.model.ItemModel;
import com.bteam.blocal.data.model.Resource;
import com.bteam.blocal.data.model.StoreModel;
import com.bteam.blocal.data.model.errors.NoDocumentException;
import com.bteam.blocal.data.model.errors.UnsuccessfulQueryException;
import com.bteam.blocal.utility.FirestoreLiveData;
import com.bteam.blocal.utility.SingleLiveEvent;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.UUID;

public class StoreRepository {

    private static StoreRepository _instance;


    private final FirebaseAuth auth;
    private final StorageReference itemsImagesStorage;
    private final StorageReference storesImagesStorage;
    private final CollectionReference storeCollection;
    private final MutableLiveData<StoreModel> myStore;

    public LiveData<StoreModel> getMyStore() {
        return myStore;
    }

    public String getMyStoreUid() {
        return myStore.getValue().getUid();
    }

    private StoreRepository() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        itemsImagesStorage = storage.getReference().child("items");
        storesImagesStorage = storage.getReference().child("stores");
        myStore = new MutableLiveData<>();
        storeCollection = db.collection("stores");
    }

    public static StoreRepository getInstance() {
        if (_instance == null) {
            _instance = new StoreRepository();
        }
        return _instance;
    }

    public CollectionReference getItemsQuery(String uid) {
        return getStoreQuery()
                .document(uid)
                .collection("items");
    }

    public CollectionReference getStoreQuery() {
        return storeCollection;
    }


    public void createStore(StoreModel model, IOnCompleteCallback<StoreModel> callback) {
        storeCollection.add(model).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentReference doc = task.getResult();
                model.setUid(doc.getId());
                myStore.setValue(model);
                callback.onSuccess(model);
            } else {
                callback.onError(new UnsuccessfulQueryException());
            }
        });
    }

    public LiveData<Resource<ItemModel>> getStoreItemLive(String storeUid, String uid) {
        FirestoreLiveData liveDat = new FirestoreLiveData(getItemsQuery(storeUid)
                .document(uid));

        return Transformations.map(liveDat, input -> Resource.success(input
                .toObject(ItemModel.class)));
    }

    public SingleLiveEvent<Resource<ItemModel>> getStoreItem(String storeUid, String uid) {
        SingleLiveEvent<Resource<ItemModel>> liveData = new SingleLiveEvent<>();
        getItemsQuery(storeUid).document(uid)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot doc = task.getResult();
                        if (doc.exists()) {
                            liveData.setValue(Resource.success(doc.toObject(ItemModel.class)));
                        } else {
                            liveData.setValue(Resource.error(new NoDocumentException(), null));
                        }
                    } else {
                        liveData.setValue(Resource.error(new UnsuccessfulQueryException(),
                                null));
                    }
                });

        return liveData;
    }

    public LiveData<Resource<List<StoreModel>>> getStores() {

        MutableLiveData<Resource<List<StoreModel>>> liveData = new MutableLiveData<>();
        storeCollection.get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot doc = task.getResult();
                        liveData.setValue(Resource.success(doc.toObjects(StoreModel.class)));
                    } else {
                        liveData.setValue(Resource.error(new UnsuccessfulQueryException(),
                                null));
                    }
                });

        return liveData;
    }

    public LiveData<Resource<StoreModel>> getStoreLive(String uid) {
        FirestoreLiveData liveDat = new FirestoreLiveData(getStoreQuery()
                .document(uid));

        return Transformations.map(liveDat, input -> Resource.success(input
                .toObject(StoreModel.class)));
    }


    //------------------------------------STORE SPECIFIC-----------------------------------------
    public void updateMyStore(IOnCompleteCallback<StoreModel> callback) {
        storeCollection
                .whereEqualTo("ownerId", auth.getCurrentUser().getUid())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot snapshots = task.getResult();
                        if (!snapshots.isEmpty()) {
                            StoreModel store = snapshots.getDocuments().get(0)
                                    .toObject(StoreModel.class);
                            myStore.setValue(store);
                            callback.onSuccess(store);
                        } else {
                            callback.onError(new NoDocumentException());
                            myStore.setValue(null);
                        }
                    } else {
                        callback.onError(new UnsuccessfulQueryException());
                        myStore.setValue(null);
                    }
                });
    }

    public LiveData<Resource<List<ItemModel>>> findItemByCode(String barcode) {
        SingleLiveEvent<Resource<List<ItemModel>>> liveData = new SingleLiveEvent<>();
        getItemsQuery(getMyStoreUid()).whereEqualTo("code", barcode)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot doc = task.getResult();
                        liveData.setValue(Resource.success(doc.toObjects(ItemModel.class)));

                    } else {
                        liveData.setValue(Resource.error(new UnsuccessfulQueryException(),
                                null));
                    }
                });

        return liveData;
    }


    public void updateItem(String uid, ItemModel model, IOnCompleteCallback<Void> callback) {
        getItemsQuery(getMyStoreUid()).document(uid).set(model).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                callback.onSuccess(null);
            } else {
                callback.onError(new UnsuccessfulQueryException());
            }
        });
    }

    public void createItem(ItemModel model, IOnCompleteCallback<ItemModel> callback) {
        getItemsQuery(getMyStoreUid()).add(model).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentReference doc = task.getResult();
                model.setUid(doc.getId());
                callback.onSuccess(model);
            } else {
                callback.onError(new UnsuccessfulQueryException());
            }
        });

    }

    public void uploadStoreImage(Bitmap image, IOnCompleteCallback<String> callback) {
        StorageReference reference = storesImagesStorage.child(UUID.randomUUID().toString());
        uploadImage(reference, image, callback);
    }

    public void uploadItemImage(Bitmap image, IOnCompleteCallback<String> callback) {
        StorageReference reference = itemsImagesStorage.child(UUID.randomUUID().toString());
        uploadImage(reference, image, callback);
    }

    private void uploadImage(StorageReference reference, Bitmap image, IOnCompleteCallback<String> callback) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = reference.putBytes(data);

        uploadTask.addOnProgressListener(snapshot -> {
            double progress = (100.0 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount();
        }).continueWithTask(task -> {
            if (!task.isSuccessful()) {
                throw task.getException();
            }
            return reference.getDownloadUrl();
        })
                .addOnFailureListener(exception -> callback.onError(exception))
                .addOnSuccessListener(uri -> callback.onSuccess(uri.toString()));
    }


}
