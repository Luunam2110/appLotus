package com.example.lotus;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

public class useradapter extends RecyclerView.Adapter<useradapter.ViewHolder> {
    private Context context;
    private List<Friend> muser;
    public useradapter(Context mcontext, List<Friend> muser){
        this.context= mcontext;
        this.muser=muser;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =LayoutInflater.from(context).inflate(R.layout.user_item,parent,false);
        return new useradapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final Friend user =muser.get(position);
        DatabaseReference statusreference = FirebaseDatabase.getInstance().getReference().child("User").child(user.getId()).child("Profile").child("Status");
        statusreference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.getValue().toString().equals("Online"))
                holder.online.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        holder.username.setText(user.name);
        if (user.getPicture().equals("default")){
            holder.profile_image.setImageResource(R.drawable.defaul_avt);
        }
        else {
            Picasso.get().load(user.getPicture()).into(holder.profile_image);
        }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, MessengerActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("chat", user.chat);
                    bundle.putString("ID", user.id);
                    bundle.putString("Picture", user.picture);
                    bundle.putString("Name", user.name);
                    intent.putExtra("chat", bundle);
                    context.startActivity(intent);
                }
            });
        }

    @Override
    public int getItemCount() {
        return muser.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView username,online;
        public ImageView profile_image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            username =itemView.findViewById(R.id.user_name);
            profile_image=itemView.findViewById(R.id.Profile_image);
            online =itemView.findViewById(R.id.online);
        }
    }
}
