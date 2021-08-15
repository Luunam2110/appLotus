package com.example.lotus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity  implements View.OnClickListener {
    protected FirebaseAuth mAuth;
    TextView TvVeriFy,TvName,email;
    Button BtnVeriFy,Btnaddfriend ;
    Toolbar toolbar;
    String id;
    CircleImageView Profile_image;
    DatabaseReference mreference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        mapView();
        mreference = FirebaseDatabase.getInstance().getReference();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mAuth= FirebaseAuth.getInstance();
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("info");
        Boolean searchresult = bundle.getBoolean("resultSearch");
        final Boolean myRequest = bundle.getBoolean("myRequest");
        id = bundle.getString("id");
        final FirebaseUser user = mAuth.getCurrentUser();
        final SwipeRefreshLayout Refresh= (SwipeRefreshLayout) findViewById(R.id.Refersh);
        Refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (mAuth.getCurrentUser().isEmailVerified()){
                    BtnVeriFy.setVisibility(View.INVISIBLE);
                    TvVeriFy.setVisibility(View.INVISIBLE);
                }
                Refresh.setRefreshing(false);
            }
        });
        if (!searchresult || id.equals(user.getUid())){
            getSupportActionBar().setTitle("Trang cá nhân");
            email.setText(user.getEmail());
            if (user.isEmailVerified()){
                Log.d("Khognta", "onRefresh: asfaf");
                BtnVeriFy.setVisibility(View.INVISIBLE);
                TvVeriFy.setVisibility(View.INVISIBLE);
            }
            Btnaddfriend.setVisibility(View.INVISIBLE);
        }
        else{
            email.setVisibility(View.INVISIBLE);
            getSupportActionBar().setTitle("Thêm bạn");
            Btnaddfriend.setVisibility(View.VISIBLE);
            BtnVeriFy.setVisibility(View.INVISIBLE);
            TvVeriFy.setVisibility(View.INVISIBLE);
            DatabaseReference request = mreference.child("User").child(id).child("Request").child(user.getUid());
            request.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Log.d("kiemtra","vaotruoc");
                    if (!snapshot.exists()){
                        Btnaddfriend.setText("Thêm bạn bè");
                    }
                    else {
                        Btnaddfriend.setText("Đã gửi lời mời");
                    }
                    if (myRequest){
                        Btnaddfriend.setText("Chấp nhận");
                        Log.d("Profile","Đúng");
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        DatabaseReference checkfriend = mreference.child("User").child(user.getUid()).child("Friends");
        checkfriend.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot2) {
                for (DataSnapshot snapshot1: snapshot2.getChildren()){
                    if (snapshot1.getKey().toString().equals(id)){
                        Btnaddfriend.setText("Hủy kết bạn");
                        Log.d("huy","davao");
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        getprofile();
    }
    void getprofile(){
        DatabaseReference  profile = mreference.child("User").child(id).child("Profile");
        profile.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String picture=snapshot.child("Picture").getValue().toString();
                String name = snapshot.child("UserName").getValue().toString();
                TvName.setText(name);
                if (picture.equals("default")) {
                    Profile_image.setImageResource(R.drawable.defaul_avt);
                }
                else
                    Picasso.get().load(picture).into(Profile_image);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    protected void mapView (){
        BtnVeriFy= findViewById(R.id.btnReVerifyr);
        BtnVeriFy.setOnClickListener(this);
        TvVeriFy = findViewById(R.id.TvVerfied);
        toolbar = findViewById(R.id.toolbar);
        TvName = findViewById(R.id.TvName);
        Btnaddfriend = findViewById(R.id.btnaddfriend);
        Btnaddfriend.setOnClickListener(this);
        Profile_image =findViewById(R.id.Profile_image);
        email = findViewById(R.id.email);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnReVerifyr:
                mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(ProfileActivity.this,"Verification email has been sent.",Toast.LENGTH_LONG).show();
                        }
                        else {
                            Toast.makeText(ProfileActivity.this,"Email not sent."+ task.getException().getMessage(),Toast.LENGTH_LONG).show();
                        }
                    }
                });
                break;
            case R.id.btnaddfriend:
                final FirebaseUser user = mAuth.getCurrentUser();
                DatabaseReference request = mreference.child("User").child(id).child("Request").child(user.getUid());
                DatabaseReference accept = mreference.child("User").child(user.getUid()).child("Request").child(id);
                if (Btnaddfriend.getText().toString().equals("Thêm bạn bè"))
                    request.setValue(user.getUid()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isComplete());
                        Btnaddfriend.setText("Đã gửi lời mời");
                    }
                });
                if (Btnaddfriend.getText().toString().equals("Đã gửi lời mời"))
                    request.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isComplete());
                            Btnaddfriend.setText("Thêm bạn bè");
                        }
                    });
                if (Btnaddfriend.getText().toString().equals("Chấp nhận")) {
                    accept.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (!task.isComplete())
                            Toast.makeText(getApplicationContext(),"Đã có lỗi xảy ra",Toast.LENGTH_LONG).show();
                            else {
                                String keychat = mreference.child("Chat").push().getKey();
                                mreference.child("Chat").child(keychat).child("Member").child("User1").setValue(id);
                                mreference.child("Chat").child(keychat).child("Member").child("User2").setValue(user.getUid());
                                mreference.child("User").child(user.getUid()).child("Friends").child(id).child("Chat").setValue(keychat);
                                mreference.child("User").child(id).child("Friends").child(user.getUid()).child("Chat").setValue(keychat);
                                Btnaddfriend.setText("Hủy kết bạn");
                            }
                        }
                    });
                }
                if (Btnaddfriend.getText().toString().equals("Hủy kết bạn")) {
                    Toast.makeText(getApplicationContext(),"Sự kiện hủy kết bạn chưa xử lý",Toast.LENGTH_LONG).show();
                }
                break;
        }
    }
}