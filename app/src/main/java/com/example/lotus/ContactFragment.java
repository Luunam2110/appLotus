package com.example.lotus;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class ContactFragment extends Fragment {

    View view;
    private RecyclerView recyclerView;
    private useradapter useradapter;
    private List<Friend> muser;
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;
    public ContactFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_contact, container, false);
        mapview();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        muser = new ArrayList<Friend>();
        readUser();
        return view;
    }
    private void readUser() {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("User");
        final DatabaseReference friendid =databaseReference.child(firebaseUser.getUid()).child("Friends");
        friendid.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot snapshot) {
                for (DataSnapshot snapshot1: snapshot.getChildren()){
                    final String id =snapshot1.getKey().toString();
                    final String chat = snapshot1.child("Chat").getValue().toString();
                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot2) {
                            int finalT=-1;
                            for (int i=0;i<muser.size();i++){
                                if (muser.get(i).id.equals(id))
                                {finalT=i;break;}
                            }
                            String name,picture;
                            picture= snapshot2.child(id).child("Profile").child("Picture").getValue().toString();
                            name = snapshot2.child(id).child("Profile").child("UserName").getValue().toString();
                            if (finalT ==-1) {
                                Friend user = new Friend();
                                user.setName(name);
                                user.setId(id);
                                user.setPicture(picture);
                                user.setChat(chat);
                                muser.add(user);
                            }
                            else {
                                muser.get(finalT).setName(name);
                                muser.get(finalT).setPicture(picture);
                            }
                            Log.d("laydl","ok");
                            useradapter = new useradapter(getContext(),muser);
                            recyclerView.setAdapter(useradapter);
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
        Log.d("layok",Integer.toString(muser.size()));
    }

    void mapview (){
        recyclerView = view.findViewById( R.id.list_user);
    }
}