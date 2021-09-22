package com.bteam.blocal.data.model;

import com.google.firebase.firestore.DocumentId;

public class ItemModel {
    @DocumentId
    private String uid;
    private String name;
    private String imageUrl;
    private float price;
    private int stock;
    private String description;
    private String code;

    public ItemModel(){

    }
    public ItemModel(String name, String imageUrl, float price, int stock, String description,
                     String code){
        this.price = price;
        this.name = name;
        this.imageUrl = imageUrl;
        this.stock = stock;
        this.description = description;
        this.code = code;
    }

    public boolean isInStock(){
        return stock > 0;
    }
    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
