package com.twoexample.adresslist;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class EditContact extends AppCompatActivity {

    private String contactName;
    private String contactPhoneNumber;
    private Bitmap contactPhoto;
    private EditText editName;
    private EditText editPhoneNumber;
    private ImageView editPhoto;
    private TextView editTitle;
    private Button back;
    private Button save;
    private String newName;
    private String newPhoneNumber;
    private String addName;
    private String addPhoneNumber;
    private int from;
    private Intent intent;
    public final static int clickCancel=0;
    public final static int clickSure=1;
    private final static int updateContact=1;
    private final static int insertContact=2;
    private int rawContactId;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_contact);

        //实例化控件
        editName = (EditText) findViewById(R.id.input_name);
        editPhoneNumber = (EditText) findViewById(R.id.input_phone_number);
        editPhoto = (ImageView) findViewById(R.id.choose_image);
        back = (Button) findViewById(R.id.edit_cancel);
        save = (Button) findViewById(R.id.sure_save);
        editTitle = (TextView) findViewById(R.id.edit_title);

        intent=getIntent();
        from=intent.getIntExtra("from",0);
        switch (from) {
            case 1:
                //
                editTitle.setText("新建联系人");
                break;
            case 2://接受ContactInformation传递过来的相应的联系人的信息
                fromInforToEdit();
                break;
            default:
        }

        //返回按钮的点击事件
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (from) {
                    case 1:
                        //点击取消按钮返回到MainActivity中去
                        Intent backToMainActivity =new Intent(EditContact.this,MainActivity.class);
                        startActivity(backToMainActivity);
                        finish();
                        break;
                    case 2:
                        //点击取消按钮返回到ContactInformation活动中去
                        Intent backToContactInformation = new Intent(EditContact.this
                                , ContactInformation.class);
                        backToContactInformation.putExtra("name",contactName);
                        backToContactInformation.putExtra("number",contactPhoneNumber);
                        backToContactInformation.putExtra("photo",contactPhoto);
                        backToContactInformation.putExtra("from",3);
                        backToContactInformation.putExtra("click",0);
                        startActivity(backToContactInformation);
                        finish();
                        break;
                    default:
                }
            }
        });

        //确定按钮的点击事件
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getPermission();
                switch (from) {
                    case 1:
                        //
                        Intent backToMainActivity=new Intent(EditContact.this,MainActivity.class);
                        startActivity(backToMainActivity);
                        finish();
                        break;
                    case 2:
                        //兼容android6.0以上的危险权限控制
                        Intent saveToInformation = new Intent(EditContact.this, ContactInformation.class);
                        saveToInformation.putExtra("newName", newName);
                        saveToInformation.putExtra("newPhoneNumber", newPhoneNumber);
                        saveToInformation.putExtra("photo", contactPhoto);
                        saveToInformation.putExtra("from", 3);
                        saveToInformation.putExtra("click", 1);
                        startActivity(saveToInformation);
                        finish();
                        break;
                    default:
                }
            }
        });
    }

    private void fromInforToEdit(){
        rawContactId=intent.getIntExtra("rawcontactid",0);
        Log.d("raw",rawContactId+"");
        contactName=intent.getStringExtra("name");
        contactPhoto=(Bitmap) intent.getParcelableExtra("photo");
        contactPhoneNumber=intent.getStringExtra("number");
        Toast.makeText(this,intent.getStringExtra("number"),Toast.LENGTH_SHORT).show();
        //初始化与联系人信息相关的控件的内容
        editName.setText(contactName);
        editPhoneNumber.setText(contactPhoneNumber);
        editPhoto.setImageBitmap(contactPhoto);
    }

    private void updateContact(){
        newName=editName.getText().toString();
        newPhoneNumber=editPhoneNumber.getText().toString();

        Uri uri=Uri.parse("content://com.android.contacts/data");
        ContentValues values1=new ContentValues();
        values1.put(ContactsContract.Data.DATA1,newName);
        ContentValues values2=new ContentValues();
        values2.put(ContactsContract.Data.DATA1,newPhoneNumber);
        try {
            getContentResolver().update(uri, values1, ContactsContract.Data.RAW_CONTACT_ID +
                    "=? and " + ContactsContract.Data.MIMETYPE + "=?"
                    , new String[]{rawContactId+"","vnd.android.cursor.item/name"});
            getContentResolver().update(uri, values2, ContactsContract.Data.RAW_CONTACT_ID +
                            "=? and " + ContactsContract.Data.MIMETYPE + "=?"
                    , new String[]{rawContactId+"","vnd.android.cursor.item/phone_v2"});
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:
                if (grantResults.length>0&&grantResults[0]== PackageManager.PERMISSION_GRANTED) {
                    if(from==1){
                        addContact();
                    }
                    else if(from==2) {
                        updateContact();
                    }
                }else {
                    Toast.makeText(this,"You denied the permission",Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }

    private void getPermission(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_CONTACTS) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_CONTACTS}, 1);
        } else {
            switch (from){
                case 1:
                    addContact();
                    break;
                case 2:
                    updateContact();
                    break;
                default:
            }
        }
    }

    private void addContact(){
        addName=editName.getText().toString();
        addPhoneNumber=editPhoneNumber.getText().toString();
        //插入raw_contact表，并获取_id属性
        Uri uri=Uri.parse("content://com.android.contacts/raw_contacts");
        ContentResolver resolver =getContentResolver();
        ContentValues values=new ContentValues();
        long contact_id = ContentUris.parseId(resolver.insert(uri,values));

        //插入data表
        uri = Uri.parse("content://com.android.contacts/data");
       //添加 联系人姓名
        values.put("raw_contact_id",contact_id);
        values.put(ContactsContract.Data.MIMETYPE,"vnd.android.cursor.item/name");
        values.put("data1",addName);
        resolver.insert(uri,values);
        values.clear();
        //添加 联系人电话
        values.put("raw_contact_id",contact_id);
        values.put(ContactsContract.Data.MIMETYPE,"vnd.android.cursor.item/phone_v2");
        values.put("data1",addPhoneNumber);
        resolver.insert(uri,values);
    }
}
