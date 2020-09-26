package com.example.lotus;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.data.DataBufferSafeParcelable;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessengerActivity extends AppCompatActivity {
    CircleImageView profile_image;
    TextView username,textonline,online;
    FirebaseUser fUser;
    DatabaseReference reference;
    Intent intent;
    Toolbar toolbar;
    ImageButton btnsend;
    EditText EtSend;
    FirebaseAuth mAuth;
    List<Messenger> Listmess;
    RecyclerView Recyclerview_chat;
    private  MessengerAdapter messengerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messenger);
        mapview();
        //set Recyclerview_chat
        Recyclerview_chat.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        Recyclerview_chat.setLayoutManager(linearLayoutManager);

        //toolbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //lấy thông tin từ bundle
        final Bundle chat = intent.getBundleExtra("chat");
        String picture = chat.getString("Picture");
        String name = chat.getString("Name");
        String chatid = chat.getString("chat");
        final String idsender =chat.getString("ID");
        if (picture.equals("default")){
            profile_image.setImageResource(R.drawable.defaul_avt);
        }
        else
            Picasso.get().load(picture).into(profile_image);
        username.setText(name);
        reference.child("User").child(idsender).child("Profile").child("Status").addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue().toString().equals("Online")){
                    textonline.setVisibility(View.INVISIBLE);
                }
                else {
                    textonline.setVisibility(View.VISIBLE);
                    textonline.setText(lasttimeonline(snapshot.getValue().toString()));
                    online.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        btnsend.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                String sender = mAuth.getCurrentUser().getUid();
                String mes = EtSend.getText().toString();
                String receiver = chat.getString("chat");
                sendmessage(sender,receiver,mes,idsender);
                EtSend.setText("");
            }
        });
        getMessenger(chatid,picture);
    }
    public  void mapview (){
        textonline =findViewById(R.id.textonline);
        online= findViewById(R.id.online);
        toolbar =(Toolbar) findViewById(R.id.toolbar);
        profile_image = (CircleImageView) findViewById(R.id.profile_picture);
        username =findViewById(R.id.user_name);
        intent =getIntent();
        fUser= FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference();
        btnsend= (ImageButton) findViewById(R.id.Btn_send);
        EtSend =(EditText) findViewById(R.id.EtSend);
        mAuth = FirebaseAuth.getInstance();
        Listmess = new ArrayList<Messenger>();
        Recyclerview_chat = (RecyclerView) findViewById(R.id.Recyclerview_chat);
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    String lasttimeonline(String timeoffline)  {
        String now = LocalDateTime.now().toString();
        List<Integer> gettime = new ArrayList<Integer>();
        gettime.add(Integer.parseInt(now.substring(0,4))-Integer.parseInt(timeoffline.substring(0,4)));
        Log.d("timegsdfs",gettime.get(0).toString());
        gettime.add(Integer.parseInt(now.substring(5,7)) -Integer.parseInt(timeoffline.substring(5,7)));
        gettime.add(Integer.parseInt(now.substring(8,10))-Integer.parseInt(timeoffline.substring(8,10)));
        gettime.add(Integer.parseInt(now.substring(11,13))-Integer.parseInt(timeoffline.substring(11,13)));
        gettime.add(Integer.parseInt(now.substring(14,16))-Integer.parseInt(timeoffline.substring(14,16)));
        String notifi ;
        if (gettime.get(0)>0)notifi =gettime.get(0)+" năm trước";
        else if (gettime.get(1)>0)notifi =gettime.get(1)+" tháng trước";
        else if (gettime.get(2)>0)notifi =gettime.get(2)+" ngày trước";
        else if (gettime.get(3)>0)notifi =gettime.get(3)+" giờ trước";
        else if (gettime.get(4)>0)notifi =gettime.get(4)+" phút trước";
        else notifi="một phút trước";
        return  notifi;
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void sendmessage(final String sender, final String receiver, String message, final String id){
        final FirebaseUser me = FirebaseAuth.getInstance().getCurrentUser();
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("time",LocalDateTime.now().toString());
        hashMap.put("Sender",sender);
        hashMap.put("Message",message);
        hashMap.put("seen","Noseen");
        DatabaseReference getchat =reference.child("Chat").child(receiver).child("Messenger");
        getchat.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists())
                {
                    DatabaseReference createchat = reference.child("User");
                    createchat.child(id).child("Chat").child(receiver).setValue(receiver);
                    createchat.child(me.getUid()).child("Chat").child(receiver).setValue(receiver);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        DatabaseReference lastchat  = reference.child("Chat").child(receiver).child("Lassmess");
        lastchat.setValue(hashMap);
        getchat.push().setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (!task.isSuccessful())
                    Toast.makeText(getApplicationContext(),"Send error!"+task.getException().getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }
    private void getMessenger (String chat, final String picture){
        final DatabaseReference getmess = reference.child("Chat").child(chat).child("Messenger");
        getmess.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Listmess.clear();
                for (DataSnapshot snapshot1: snapshot.getChildren()){
                    String Sender = snapshot1.child("Sender").getValue().toString();
                    String Message = snapshot1.child("Message").getValue().toString();
                    String time = snapshot1.child("time").getValue().toString();
                    String seen = snapshot1.child("seen").getValue().toString();
                    if (seen.equals("Noseen") && !Sender.equals(mAuth.getCurrentUser().getUid())){
                        Log.d("tongmess",Integer.toString(Listmess.size()));
                        getmess.child(snapshot1.getKey()).child("seen").setValue(LocalDateTime.now().toString());
                    }
                    Messenger mMess =new Messenger();
                    mMess.setSender(Sender);
                    mMess.setTime(gettime(time,false));
                    mMess.setSeen(gettime(seen,true));
                    mMess.setTime(time);
                    mMess.setMessenger(Message);
                    Listmess.add(mMess);
                }
                Log.d("tongmess",Integer.toString(Listmess.size()));
                messengerAdapter = new MessengerAdapter(getApplicationContext(),Listmess,picture);
                Recyclerview_chat.setAdapter(messengerAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    String gettime (String seen, Boolean time){
        String result;
        if (seen.equals("Noseen"))
            result = "Đã gửi";
        else {
            String now = LocalDateTime.now().toString();
            List<Integer> gettime = new ArrayList<Integer>();
            gettime.add(Integer.parseInt(now.substring(0,4))-Integer.parseInt(seen.substring(0,4)));
            gettime.add(Integer.parseInt(now.substring(5,7)) -Integer.parseInt(seen.substring(5,7)));
            gettime.add(Integer.parseInt(now.substring(8,10))-Integer.parseInt(seen.substring(8,10)));
            gettime.add(Integer.parseInt(now.substring(11,13))-Integer.parseInt(seen.substring(11,13)));
            gettime.add(Integer.parseInt(now.substring(14,16))-Integer.parseInt(seen.substring(14,16)));
            if (gettime.get(0)>0)result =seen.substring(11,13)+":" +seen.substring(14,16)+"Ngày "+seen.substring(8,10)+" tháng "+ seen.substring(5,7)+ " năm "+seen.substring(0,4);
            else if (gettime.get(1)>0)result =seen.substring(11,13)+":"+seen.substring(14,16)+":"+"Ngày "+seen.substring(8,10)+" tháng "+ seen.substring(5,7);
            else if (gettime.get(2)>0)result =seen.substring(11,13)+":"+seen.substring(14,16)+":"+"Ngày "+seen.substring(8,10);
            else result =seen.substring(11,13)+":"+seen.substring(14,16);
            if (time) result= "Đã xem lúc "+ result;
        }
        return result;
    }
}