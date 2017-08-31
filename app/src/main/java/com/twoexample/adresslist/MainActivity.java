package com.twoexample.adresslist;

import android.content.ContentResolver;
import android.content.Intent;
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
import android.view.View;
import android.widget.Button;
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
    private Button addContact;
    ContactAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //实例化控件
        contactsView =(RecyclerView) findViewById(R.id.contacts_view);
        addContact = (Button) findViewById(R.id.add_contact);

        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayout.VERTICAL);
        contactsView.setLayoutManager(layoutManager);
        adapter=new ContactAdapter(mContactsList,this);
        contactsView.setAdapter(adapter);

        adapter.setOnItemClickListener(new ContactAdapter.onItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent=new Intent(MainActivity.this,ContactInformation.class);
                intent.putExtra("name",mContactsList.get(position).getName());
                intent.putExtra("number",mContactsList.get(position).getNumber());
                intent.putExtra("photo",mContactsList.get(position).getBitmap());
                intent.putExtra("rawcontactid",mContactsList.get(position).getRawContactId());
                intent.putExtra("from",1);
                startActivity(intent);
            }
        });
        adapter.setOnItemLongClickListener(new ContactAdapter.onItemLongClickListener() {
            @Override
            public void onItemLongClick(View view, int position) {
                int rawContactId = mContactsList.get(position).getRawContactId();
                deleteContact(rawContactId, position);
                mContactsList.clear();
                readContacts();
            }
        });

        //查询本地联系人
        localData();
        //添加单个用户数据
        addContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addContactIntent =new Intent(MainActivity.this,EditContact.class);
                addContactIntent.putExtra("from",1);
                startActivity(addContactIntent);
            }
        });
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
                    //获得联系人电话号码
                    String phoneNumber=cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    //获取联系人图像
                    image_uri=cursor.getString(cursor.getColumnIndex(ContactsContract
                            .CommonDataKinds.Phone.PHOTO_URI));
                    //获得联系人的id
                    int rawContactId=cursor.getInt(cursor.getColumnIndex(ContactsContract.Data.RAW_CONTACT_ID));
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
                    Contacts contacts=new Contacts(displayName,bitmap,phoneNumber,rawContactId);
                    mContactsList.add(contacts);
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
    public void deleteContact(int rawContactId,int position){
        Uri uri1=Uri.parse("content://com.android.contacts/raw_contacts");
        Uri uri2=Uri.parse("content://com.android.contacts/data");
        ContentResolver resolver=getContentResolver();
//        Cursor cursor=resolver.query(uri1,new String[]{ContactsContract.Data._ID},"_id=?",new String[]{rawContactId+""},null);
//
//        if(cursor.moveToFirst()){
//            cursor.getInt(0);
//            resolver.delete()
//            Toast.makeText(this,"正在删除数据",Toast.LENGTH_SHORT).show();
//
//        }

        try {
            resolver.delete(uri1,"_id=?",new String[]{rawContactId+""} );
            resolver.delete(uri2,"raw_contact_id=?",new String[]{rawContactId+""});
            Toast.makeText(this,"数据删除成功",Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(this,"数据删除失败",Toast.LENGTH_SHORT).show();
        }
    }
}
