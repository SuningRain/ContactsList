package com.twoexample.adresslist;

import android.Manifest;
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
import android.telephony.PhoneNumberUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ContactInformation extends AppCompatActivity {

    private Bitmap contactPhoto;
    private String contactName;
    private String contactPhoneNumber;
    private ImageView displayPhoto;
    private Button call;
    private Button sendMessage;
    private TextView displayPhoneNumber;
    private TextView displayName;
    private Button backUp;
    private Button modify;
    private int from;
    private int click;
    private int rawContactId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_information);

        //实例化控件
        displayPhoto = (ImageView) findViewById(R.id.contact_picture);
        call = (Button) findViewById(R.id.call);
        sendMessage = (Button) findViewById(R.id.sendmessage);
        displayPhoneNumber = (TextView) findViewById(R.id.display_phonenunber);
        displayName =(TextView) findViewById(R.id.contact_name);
        backUp = (Button) findViewById(R.id.back);
        modify = (Button) findViewById(R.id.modify_contact);

        //获得MainActivity中传递过来的数据，即联系人的姓名，电话号码，头像
        final Intent intent=getIntent();
        contactPhoto=(Bitmap) intent.getParcelableExtra("photo");
        from = intent.getIntExtra("from",0);
        click = intent.getIntExtra("click",0);
        switch (from) {
            case 1:
                contactName = intent.getStringExtra("name");
                contactPhoneNumber = intent.getStringExtra("number");
                rawContactId = intent.getIntExtra("rawcontactid",0);
                break;
            case 3:
                if (click == EditContact.clickCancel) {
                    contactName = intent.getStringExtra("name");
                    contactPhoneNumber = intent.getStringExtra("number");
                } else if(click==EditContact.clickSure){
                    contactName = intent.getStringExtra("newName");
                    contactPhoneNumber = intent.getStringExtra("newPhoneNumber");
                }
                break;
            case 4:
                contactName = intent.getStringExtra("name");
                contactPhoneNumber = intent.getStringExtra("number");
                break;
        }
        //将值对应实现到相应的控件上
        displayPhoto.setImageBitmap(contactPhoto);
        displayPhoneNumber.setText(contactPhoneNumber);
        displayName.setText(contactName);

        //点击打电话
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(ContactInformation.this, Manifest.permission
                        .CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(ContactInformation.this
                            , new String[]{Manifest.permission.CALL_PHONE}, 1);
                } else {
                    call();
                }
            }
        });

        //点击发送短信
        sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent messageIntent=new Intent(ContactInformation.this,SendMessage.class);
                messageIntent.putExtra("phonenumber",contactPhoneNumber);
                messageIntent.putExtra("name",contactName);
                messageIntent.putExtra("photo",contactPhoto);
                startActivity(messageIntent);
            }
        });

        //点击返回到上一级
        backUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent backIntent=new Intent(ContactInformation.this,MainActivity.class);
                startActivity(backIntent);
                finish();
            }
        });

        //点击对联系人信息进行修改
        modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent modifyIntent=new Intent(ContactInformation.this,EditContact.class);
                modifyIntent.putExtra("name",contactName);
                modifyIntent.putExtra("number",contactPhoneNumber);
                modifyIntent.putExtra("photo",contactPhoto);
                modifyIntent.putExtra("from",2);
                modifyIntent.putExtra("rawcontactid",rawContactId);
                startActivity(modifyIntent);
            }
        });
    }
    //打电话关键代码
    private void call(){
        try{
            Intent intent=new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:"+contactPhoneNumber));
            startActivity(intent);
        }catch (SecurityException e){
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,String[] permissions,int[] grantResults) {
        switch (requestCode){
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    call();
                } else {
                    Toast.makeText(this,"You denied the permission",Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }


}
