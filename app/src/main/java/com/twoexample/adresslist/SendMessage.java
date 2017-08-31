package com.twoexample.adresslist;

import android.content.EntityIterator;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SendMessage extends AppCompatActivity {

    private EditText messageContent;
    private Button sendMessage;
    private Button cancel;
    private String phoneNumber;
    private String contactName;
    private EditText displayName;
    private Bitmap contactPhoto;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_message);
        //实例化控件
        messageContent = (EditText) findViewById(R.id.input_message);
        sendMessage = (Button) findViewById(R.id.send_message);
        cancel = (Button) findViewById(R.id.back);
        displayName = (EditText) findViewById(R.id.select_contact);

        Intent intent=getIntent();
        phoneNumber=intent.getStringExtra("phonenumber");
        contactName=intent.getStringExtra("name");
        contactPhoto=(Bitmap) intent.getParcelableExtra("photo");
        displayName.setText(contactName);

        //点击发送短信
        sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String messageBody=messageContent.getText().toString();
                doSendSMSTo(phoneNumber,messageBody);
                Toast.makeText(SendMessage.this, "短信已发送", Toast.LENGTH_SHORT).show();
            }
        });
        //点击返回到上一级
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cancel=new Intent(SendMessage.this,ContactInformation.class);
                cancel.putExtra("name",contactName);
                cancel.putExtra("number",phoneNumber);
                cancel.putExtra("photo",contactPhoto);
                cancel.putExtra("from",4);
                startActivity(cancel);
                finish();
            }
        });
    }
    private void doSendSMSTo(String phoneNumber, String message) {
        if (PhoneNumberUtils.isGlobalPhoneNumber(phoneNumber)) {
            Uri uri = Uri.parse("smsto" + phoneNumber);
            Intent intent=new Intent(Intent.ACTION_SENDTO,uri);
            intent.putExtra("sms_body",message);
            startActivity(intent);
        }
    }
}
