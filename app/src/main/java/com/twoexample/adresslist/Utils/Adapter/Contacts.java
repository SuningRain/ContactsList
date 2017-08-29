package com.twoexample.adresslist.Utils.Adapter;

import android.graphics.Bitmap;

/**
 * Created by 27837 on 2017/8/25.
 */

public class Contacts {
    private String name;
    private Bitmap bitmap;


    public Contacts(String name,Bitmap bitmap){
        this.name=name;
        this.bitmap=bitmap;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Bitmap getBitmap(){
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap){
        this.bitmap=bitmap;
    }
}
