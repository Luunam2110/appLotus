package com.example.lotus;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.widget.Toolbar;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.time.LocalDateTime;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {
    CircleImageView picture_profile;
    ViewPager viewPager;
    TabLayout tabLayout;
    DatabaseReference mReference;
    String myid;
    FirebaseAuth mAuth;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mapview();
        myid= mAuth.getCurrentUser().getUid();
        viewPager.setAdapter(new pageradapter(getSupportFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_baseline_chat_24);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_baseline_contacts_24);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_baseline_person_add_24);
        final DatabaseReference getpicture = mReference.child("User").child(mAuth.getCurrentUser().getUid()).child("Profile");
        getpicture.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String picture = snapshot.child("Picture").getValue().toString();
                if (picture.equals("default")) {
                    picture_profile.setImageResource(R.drawable.defaul_avt);
                }
                else
                    Picasso.get().load(picture).into(picture_profile);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        picture_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                Bundle bundle =  new Bundle();
                bundle.putString("id",mAuth.getCurrentUser().getUid());
                bundle.putBoolean("resultSearch",false);
                intent.putExtra("info",bundle);
                startActivity(intent);
            }
        });
    }
    void mapview (){
        toolbar =(Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        viewPager = findViewById(R.id.Main_viewpager);
        tabLayout = findViewById(R.id.main_tablayout);
        mReference = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        picture_profile = (CircleImageView) findViewById(R.id.profile_picture);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mReference.child("User").child(mAuth.getCurrentUser().getUid()).child("Profile").child("Status").setValue("Online");
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mReference.child("User").child(myid).child("Profile").child("Status").setValue(LocalDateTime.now().toString());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.Logout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MainActivity.this,LoginActivity.class));
                finish();
                return true;
            case R.id.Search_people:
                startActivity(new Intent(getApplicationContext(),SearchPeopleActivity.class));
                return true;
                
        }
        return false;
    }
}