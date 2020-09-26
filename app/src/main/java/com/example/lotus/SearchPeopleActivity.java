package com.example.lotus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.appbar.AppBarLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SearchPeopleActivity extends AppCompatActivity {
    Toolbar toolbar;
    TextView noti;
    RecyclerView RvSearch;
    Button btnsearch;
    EditText Etphone;
    List<User> result;
    DatabaseReference mReference;
    searchadapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_people);
        mapview();
        result = new ArrayList<User>();
        RvSearch.setHasFixedSize(true);
        RvSearch.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getuser(Etphone.getText().toString());
            }
        });
    }

    private void mapview() {
        btnsearch =findViewById(R.id.BtnSearch);
        Etphone =findViewById(R.id.EtPhone);
        RvSearch = findViewById(R.id.RvSearch);
        toolbar =(Toolbar) findViewById(R.id.toolbar);
        mReference= FirebaseDatabase.getInstance().getReference().child("User");
        noti= findViewById(R.id.Tvnoti);
    }
    private void getuser(final String phone){
        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                result.clear();
                for (DataSnapshot snapshot1: snapshot.getChildren()){
                    int t=-1;
                    String id,name,picture;
                    if (snapshot1.child("Profile").child("PhoneNumber").getValue().toString().equals(phone)){
                        id = snapshot1.getKey().toString();
                        name = snapshot1.child("Profile").child("UserName").getValue().toString();
                        picture = snapshot1.child("Profile").child("Picture").getValue().toString();
                        for (int i=0;i<result.size();i++){
                            if (result.get(i).id.equals(id)) {t=i;break;}
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
                    }
                }
                if (result.size()==0)
                    noti.setText("Không có kết quả phù hợp");
                else
                    noti.setText("");
                adapter = new searchadapter(getApplicationContext(),result,false);
                RvSearch.setAdapter(adapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}