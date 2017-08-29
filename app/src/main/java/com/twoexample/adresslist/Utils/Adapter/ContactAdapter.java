package com.twoexample.adresslist.Utils.Adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.twoexample.adresslist.R;

import java.util.List;

/**
 * Created by 27837 on 2017/8/25.
 */

public class ContactAdapter extends RecyclerView.Adapter<ContactsViewHolder> {
    private List<Contacts> mContactsList;


    public ContactAdapter(List<Contacts> contactsList){
        mContactsList=contactsList;
    }
    @Override
    public ContactsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_item,parent,false);
        ContactsViewHolder holder=new ContactsViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ContactsViewHolder holder, int position) {
        Contacts contacts=mContactsList.get(position);
        holder.contactImage.setImageBitmap(contacts.getBitmap());
        holder.contactName.setText(contacts.getName());
        Log.i("onBindViewHolder",contacts.getBitmap()+"   "+contacts.getName());
    }

    @Override
    public int getItemCount() {
        return mContactsList.size();
    }
}
