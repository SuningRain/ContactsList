package com.twoexample.adresslist;

import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.Manifest;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.twoexample.adresslist.Utils.Adapter.ContactAdapter;
import com.twoexample.adresslist.Utils.Adapter.Contacts;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    //联系人名称
    private List<Contacts> mContactsList=new ArrayList<>();

    private RecyclerView contactsView;
    ContactAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //实例化控件
        contactsView =(RecyclerView) findViewById(R.id.contacts_view);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayout.VERTICAL);
        contactsView.setLayoutManager(layoutManager);
        adapter=new ContactAdapter(mContactsList);
        contactsView.setAdapter(adapter);
        localData();
        }

    private void localData() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission
                .READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest
                    .permission.READ_CONTACTS}, 1);
        } else {
            readContacts();
        }
    }
    //读取本地联系人
    private void readContacts(){
        Cursor cursor =null;
        String image_uri="";
        Bitmap bitmap= BitmapFactory.decodeResource(this.getResources(),
                R.drawable.contact);
        try{
            cursor=getContentResolver().query(ContactsContract.CommonDataKinds.Phone
                    .CONTENT_URI,null,null,null,null);
            if (cursor!=null) {
                while(cursor.moveToNext()){
                    //获得联系人姓名
                    String displayName=cursor.getString(cursor.getColumnIndex(ContactsContract
                            .CommonDataKinds.Phone.DISPLAY_NAME));

                    Log.i("cursor",displayName);
                    //获取联系人图像
                    image_uri=cursor.getString(cursor.getColumnIndex(ContactsContract
                            .CommonDataKinds.Phone.PHOTO_URI));
                    if (image_uri!=null) {
                        try {
                            bitmap= MediaStore.Images.Media
                                    .getBitmap(this.getContentResolver(), Uri.parse(image_uri));
                        }catch (FileNotFoundException e){
                            e.printStackTrace();
                        }catch (IOException e){
                            e.printStackTrace();
                        }
                    }
                    Contacts contacts=new Contacts(displayName,bitmap);
                    mContactsList.add(contacts);
                    Log.i("mContactsList",contacts.getName());
                }
                adapter.notifyDataSetChanged();
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (cursor!=null) {
                cursor.close();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:
                if (grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED) {
                    readContacts();
                }else{
                    Toast.makeText(this,"You denied the permission",Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }
}
