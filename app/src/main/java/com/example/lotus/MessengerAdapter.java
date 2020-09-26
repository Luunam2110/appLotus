package com.example.lotus;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MessengerAdapter extends RecyclerView.Adapter<MessengerAdapter.ViewHolder> {
    public static final int MSG_TYPE_RIGHT =0;
    public static final int MSG_TYPE_lEFT =1;
    private Context context;
    private List<Messenger> mMessage;
    private String picture;
    public MessengerAdapter (Context mcontext, List<Messenger> mMessager,String picture){
        this.context= mcontext;
        this.mMessage=mMessager;
        this.picture =picture;
    }

    @NonNull
    @Override
    public MessengerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType==MSG_TYPE_RIGHT){
            View view = LayoutInflater.from(context).inflate(R.layout.right_message_icon,parent,false);
            return new MessengerAdapter.ViewHolder(view);
        }
        else {
            View view = LayoutInflater.from(context).inflate(R.layout.left_messenger_icon,parent,false);
            return new MessengerAdapter.ViewHolder(view);
        }
    }

    @Override
    public int getItemViewType(int position) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        if (mMessage.get(position).sender.equals(mAuth.getCurrentUser().getUid())){
            return MSG_TYPE_RIGHT;
        }
        else return  MSG_TYPE_lEFT;
    }

    @Override
    public void onBindViewHolder(@NonNull final MessengerAdapter.ViewHolder holder, int position) {
        final Messenger mess =mMessage.get(position);
        holder.mess.setText(mess.messenger);
        if (picture.equals("default")){
            holder.profile_image.setImageResource(R.drawable.defaul_avt);
        }
        else {
            Picasso.get().load(picture).into(holder.profile_image);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.seen !=null){
                    if (holder.seen.getVisibility()==View.GONE){
                        holder.seen.setVisibility(View.VISIBLE);
                        holder.seen.setText(mess.getSeen());
                    }

                    else holder.seen.setVisibility(View.GONE);
                }
                if (holder.Tvtimesend.getVisibility()==View.GONE){
                    holder.Tvtimesend.setVisibility(View.VISIBLE);
                    holder.Tvtimesend.setText(mess.getTime());
                }

                else holder.Tvtimesend.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mMessage.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView mess,seen,Tvtimesend;
        public ImageView profile_image;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mess =itemView.findViewById(R.id.Tvmessage);
            Tvtimesend =itemView.findViewById(R.id.Tvtimesend);
            seen= itemView.findViewById(R.id.Tvseen);
            profile_image=itemView.findViewById(R.id.picture_receiver);
        }
    }
}
