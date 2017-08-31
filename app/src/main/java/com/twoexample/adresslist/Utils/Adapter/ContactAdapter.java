package com.twoexample.adresslist.Utils.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.twoexample.adresslist.ContactInformation;
import com.twoexample.adresslist.R;

import java.util.List;

/**
 * Created by 27837 on 2017/8/25.
 */

public class ContactAdapter extends RecyclerView.Adapter<ContactsViewHolder> implements View.OnClickListener,View.OnLongClickListener{
    private List<Contacts> mContactsList;
    private Context mContext;
    private onItemClickListener monItemClickListener=null;
    private onItemLongClickListener monItemLongClickListener=null;

    public ContactAdapter(List<Contacts> contactsList,Context context){
        mContactsList=contactsList;
        mContext=context;
    }

    //定义item监听事件接口
    public static interface onItemClickListener{
        void onItemClick(View view,int position);

    }
    public static interface onItemLongClickListener{
        void onItemLongClick(View view,int position);

    }

    //对外提供接口
    //短点击
    public void setOnItemClickListener(onItemClickListener listener){
        this.monItemClickListener=listener;
    }
    //长点击
    public void setOnItemLongClickListener(onItemLongClickListener listener){
        this.monItemLongClickListener=listener;
    }
    //将点击事件转移给外部调用者

    //第一种短点击
    @Override
    public void onClick(View view) {
        if (monItemClickListener!=null) {
            monItemClickListener.onItemClick(view,(int)view.getTag());
        }
    }

    //第二种 长点击
    @Override
    public boolean onLongClick(View view) {
        if (monItemLongClickListener!=null) {
            monItemLongClickListener.onItemLongClick(view,(int)view.getTag());
        }
        return true;
    }

    @Override
    public ContactsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_item,parent,false);
        view.setOnClickListener(this);
        view.setOnLongClickListener(this);
        final ContactsViewHolder holder=new ContactsViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ContactsViewHolder holder, int position) {
        Contacts contacts=mContactsList.get(position);
        holder.contactImage.setImageBitmap(contacts.getBitmap());
        holder.contactName.setText(contacts.getName());
        holder.contactView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return mContactsList.size();
    }

}
