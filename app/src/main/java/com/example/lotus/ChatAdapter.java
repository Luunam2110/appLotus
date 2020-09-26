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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ChatAdapter  extends  RecyclerView.Adapter<ChatAdapter.ViewHolder>{
    private Context context;
    private List<Chat> mChat;
    public ChatAdapter (Context mcontext, List<Chat> chats){
        this.context= mcontext;
        this.mChat=chats;
    }
    @NonNull
    @Override
    public ChatAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.chat_item,parent,false);
            return new ChatAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ChatAdapter.ViewHolder holder, int position) {
        final Chat chat =mChat.get(position);
        holder.name.setText(chat.name);
        final FirebaseUser  user = FirebaseAuth.getInstance().getCurrentUser();
        if (chat.picture.equals("default")){
            holder.profile_image.setImageResource(R.drawable.defaul_avt);
        }
        else {
            Picasso.get().load(chat.picture).into(holder.profile_image);
        }
        getastatus(chat.getChatid(),user,holder.online);
        if (chat.lastsender.equals(user.getUid())){
        holder.lassmsg.setText("Bạn: "+chat.lastmess);}
        else {holder.lassmsg.setText(chat.lastmess);}
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference referencechat = FirebaseDatabase.getInstance().getReference().child("Chat").child(chat.chatid).child("Member");
                referencechat.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String key;
                        if (snapshot.child("User1").getValue().toString().equals(user.getUid()))
                            key = snapshot.child("User2").getValue().toString();
                        else key = snapshot.child("User1").getValue().toString();
                        Intent intent = new Intent(context,MessengerActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("chat",chat.chatid);
                        bundle.putString("ID",key);
                        bundle.putString("Picture",chat.picture);
                        bundle.putString("Name",chat.name);
                        intent.putExtra("chat",bundle);
                        context.startActivity(intent);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }
    void getastatus (String chatid, final FirebaseUser user, final TextView online){
        DatabaseReference referencechat = FirebaseDatabase.getInstance().getReference().child("Chat").child(chatid).child("Member");
        referencechat.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot snapshot) {
                String key;
                if (snapshot.child("User1").getValue().toString().equals(user.getUid()))
                    key= snapshot.child("User2").getValue().toString();
                else key= snapshot.child("User1").getValue().toString();
                DatabaseReference referencestatus = FirebaseDatabase.getInstance().getReference().child("User").child(key).child("Profile").child("Status");
                Log.d("key",key);
                referencestatus.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot1) {
                        if (!snapshot1.getValue().toString().equals("Online"))
                            online.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    @Override
    public int getItemCount() {
        return mChat.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView name, lassmsg,online;
        public ImageView profile_image;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            lassmsg =itemView.findViewById(R.id.lassmsg);
            online=itemView.findViewById(R.id.online);
            name =itemView.findViewById(R.id.user_name);
            profile_image=itemView.findViewById(R.id.Profile_image);
        }
    }
}
