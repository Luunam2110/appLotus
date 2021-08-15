package com.example.lotus;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.solver.widgets.ChainHead;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ChatFragment extends Fragment {

    View view;
    private RecyclerView recyclerView;
    private ChatAdapter chatAdapter;
    private List<Chat> listchat;
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;
    public ChatFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.fragment_chat, container, false);
        mapview();
        LinearLayoutManager mylayout= new LinearLayoutManager(getContext());
        mylayout.setStackFromEnd(true);
        recyclerView.setLayoutManager(mylayout);
        recyclerView.setHasFixedSize(true);
        listchat = new ArrayList<Chat>();
        readChat();
        return view;
    }


    private void readChat() {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        Log.d("myid",firebaseUser.getUid());
        databaseReference = FirebaseDatabase.getInstance().getReference();
        final DatabaseReference getchat = databaseReference.child("User").child(firebaseUser.getUid()).child("Chat");
        getchat.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                final List<String> listchatid = new ArrayList<String>();
                //lấy danh sách các cuộc trò chuyện của user lưu vào listchatid
                for (DataSnapshot snapshot1: snapshot.getChildren()){
                    String chatid = snapshot1.getValue().toString();
                    if (!listchatid.contains(chatid))
                    listchatid.add(chatid);
                }
                //lấy người còn lại tham gia vào cuộc trò chuyện lưu vào list reciever
                for (int i=0;i< listchatid.size();i++){
                    DatabaseReference getreiver = databaseReference.child("Chat").child(listchatid.get(i)).child("Member");
                    final int finalI = i;
                    getreiver.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            final String receiver;
                            if (snapshot.child("User1").getValue().equals(firebaseUser.getUid()))
                                receiver = snapshot.child("User2").getValue().toString();
                            else  receiver = snapshot.child("User1").getValue().toString();
                            // lấy tên, ảnh đại diện của người còn lại trong cuộc trò chuyện và thêm vào listchat
                            DatabaseReference getPicture = databaseReference.child("User");
                            getPicture.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    final String image= snapshot.child(receiver).child("Profile").child("Picture").getValue().toString();
                                    final String name =snapshot.child(receiver).child("Profile").child("UserName").getValue().toString();
                                    //lấy thông tin về lassmess, sender lassmess, time lưu vào  listchat; thiết lập chatid cho từng phần tử của listchat
                                    DatabaseReference getmess = databaseReference.child("Chat").child(listchatid.get(finalI)).child("Lassmess");
                                    getmess.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            String sender =snapshot.child("Sender").getValue().toString();
                                            String mess = snapshot.child("Message").getValue().toString();
                                            String time = snapshot.child("time").getValue().toString();
                                            int t=-1;
                                            for (int j=0;j<listchat.size();j++){
                                                if (listchat.get(j).chatid.equals(listchatid.get(finalI)))
                                                {t=j;break;}
                                            }
                                            if (t==-1) {
                                                Chat newchat = new Chat();
                                                newchat.setPicture(image);
                                                newchat.setName(name);
                                                newchat.setChatid(listchatid.get(finalI));
                                                newchat.setTime(time);
                                                newchat.setLastsender(sender);
                                                newchat.setLastmess(mess);
                                                listchat.add(newchat);
                                            }
                                            else {
                                                listchat.get(t).setName(name);
                                                listchat.get(t).setPicture(image);
                                                listchat.get(t).setTime(time);
                                                listchat.get(t).setLastmess(mess);
                                                listchat.get(t).setLastsender(sender);
                                            }
                                            Collections.sort(listchat);
                                            chatAdapter = new ChatAdapter(getContext(),listchat);
                                            recyclerView.setAdapter(chatAdapter);
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
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    void mapview (){
        recyclerView = view.findViewById( R.id.list_chat);
    }

}