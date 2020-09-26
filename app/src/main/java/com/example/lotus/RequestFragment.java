package com.example.lotus;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class RequestFragment extends Fragment {
    RecyclerView Rvrequest;
    View view;
    List<User> result;
    public RequestFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_request, container, false);
        Rvrequest =view.findViewById(R.id.Rvrequest);
        Rvrequest.setHasFixedSize(true);
        result = new ArrayList<User>();
        Rvrequest.setLayoutManager(new LinearLayoutManager(getContext()));
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String myID = mAuth.getCurrentUser().getUid();
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("User");
        DatabaseReference getid = reference.child(myID).child("Request");
        getid.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<String> idrequest = new ArrayList<String>();
                for (DataSnapshot snapshot1: snapshot.getChildren()){
                    if (!idrequest.contains(snapshot1.getValue().toString()))
                    idrequest.add(snapshot1.getValue().toString());
                    for (int i=0;i<idrequest.size();i++){
                        reference.child(idrequest.get(i)).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot2) {
                                int t=-1;
                                String id = snapshot2.getKey();
                                String name = snapshot2.child("Profile").child("UserName").getValue().toString();
                                String picture = snapshot2.child("Profile").child("Picture").getValue().toString();
                                for (int j=0;j<result.size();j++){
                                    if (result.get(j).id.equals(id)) {t=j;break;}
                                }
                                if (t==-1) {
                                    User user = new User();
                                    user.setId(id);
                                    user.setPicture(picture);
                                    user.setUsername(name);
                                    result.add(user);
                                }
                                else {
                                    result.get(t).setUsername(name);
                                    result.get(t).setPicture(picture);
                                }
                                searchadapter adapter = new searchadapter(getContext(),result,true);
                                Rvrequest.setAdapter(adapter);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return view;
    }
}