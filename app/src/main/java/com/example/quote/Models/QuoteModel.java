package com.example.quote.Models;

import android.net.Uri;

public class QuoteModel {
    int id;
    String uid;
    String quoterName;
    int image;
    String quote;
    int isFav;

    public QuoteModel(){}

    public QuoteModel(int id,String uid, String quoterName, String quote, int image,int isFav) {
        this.id = id;
        this.uid = uid;
        this.quoterName = quoterName;
        this.image = image;
        this.quote = quote;
        this.isFav = isFav;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getQuoterName() {
        return quoterName;
    }

    public void setQuoterName(String quoterName) {
        this.quoterName = quoterName;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getQuote() {
        return quote;
    }

    public void setQuote(String quote) {
        this.quote = quote;
    }

    public int getIsFav() {
        return isFav;
    }

    public void setIsFav(int isFav) {
        this.isFav = isFav;
    }
}
