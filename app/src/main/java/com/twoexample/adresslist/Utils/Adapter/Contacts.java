package com.twoexample.adresslist.Utils.Adapter;

import android.graphics.Bitmap;

/**
 * Created by 27837 on 2017/8/25.
 */

public class Contacts {
    private String name;
    private Bitmap bitmap;
    private String number;
    private int rawContactId;
    public Contacts(String name,Bitmap bitmap,String number,int rawContactId){
        this.name=name;
        this.bitmap=bitmap;
        this.number=number;
        this.rawContactId=rawContactId;
    }

    public int getRawContactId() {
        return rawContactId;
    }

    public void setRawContactId(int rawContactId) {
        this.rawContactId = rawContactId;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
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
