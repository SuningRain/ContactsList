package com.twoexample.adresslist.Utils.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.twoexample.adresslist.R;

/**
 * Created by 27837 on 2017/8/25.
 */

public class ContactsViewHolder extends RecyclerView.ViewHolder{
    ImageView contactImage;
    TextView contactName;
    View contactView;
    public ContactsViewHolder(View view){
        super(view);
        contactView=view;
        contactImage =(ImageView)view.findViewById(R.id.recycler_picture);
        contactName =(TextView)view.findViewById(R.id.recycler_name);
    }
}
