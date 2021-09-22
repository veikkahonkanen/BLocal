package com.bteam.blocal.data.model;

import com.google.firebase.firestore.DocumentId;
import com.google.firebase.firestore.GeoPoint;

public class StoreModel {
    @DocumentId
    private String uid;

    private String name;
    private String ownerId;
    private GeoPoint location;
    private String description;
    private String imageUrl;

    public StoreModel(){

    }

    public StoreModel(String name, String ownerId, GeoPoint location, String description,
                      String imageUrl){
        this.name = name;
        this.ownerId = ownerId;
        this.location = location;
        this.description = description;
        this.imageUrl = imageUrl;
    }

    public GeoPoint getLocation() {
        return location;
    }

    public void setLocation(GeoPoint location) {
        this.location = location;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
    public String getUid(){
        return uid;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
