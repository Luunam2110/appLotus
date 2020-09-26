package com.example.lotus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    EditText EtPhone, EtEmail, EtPass, EtRepass,EtName;
    Button btnRegister;
    TextView TvLogin;
    private FirebaseAuth mAuth;
    private DatabaseReference mReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mapView();
        mAuth = FirebaseAuth.getInstance();
        mReference = FirebaseDatabase.getInstance().getReference();
    }
    protected void mapView (){
        EtPhone = (EditText) findViewById(R.id.EtPhoneNumberR);
        EtEmail = (EditText) findViewById(R.id.EtemailR);
        EtPass = (EditText) findViewById(R.id.EtpassR);
        EtRepass = (EditText) findViewById(R.id.EtRepassR);
        btnRegister= (Button) findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(this);
        TvLogin =(TextView) findViewById(R.id.LoginR);
        TvLogin.setOnClickListener(this);
        EtName= (EditText) findViewById(R.id.EtdisplayName);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnRegister:
                Boolean Check =true;
                final String Phone= EtPhone.getText().toString().trim(),Email =EtEmail.getText().toString().trim(),Name=EtName.getText().toString().trim();
                if (Name.isEmpty()){ EtPhone.setError("Field cannot be empty"); Check=false;}else EtName.setError(null);
                if (Phone.isEmpty()){ EtPhone.setError("Field cannot be empty"); Check=false;}else EtPhone.setError(null);
                if (Email.isEmpty()){ EtEmail.setError("Field cannot be empty");Check=false;}else EtEmail.setError(null);
                if (EtPass.getText().toString().trim().isEmpty()) {EtPass.setError("Field cannot be empty");Check=false;}else EtPass.setError(null);
                if (EtRepass.getText().toString().trim().isEmpty()){ EtRepass.setError("Field cannot be empty"); Check=false;}
                else if (!EtRepass.getText().toString().trim().equalsIgnoreCase(EtPass.getText().toString().trim())) {EtRepass.setError("Those passwords didn't match.try again.");Check=false;}
                else EtRepass.setError(null);
                if (Check){
                    mAuth.createUserWithEmailAndPassword(EtEmail.getText().toString().trim(),EtPass.getText().toString().trim()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                FirebaseUser user= mAuth.getCurrentUser();
                                user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            Toast.makeText(RegisterActivity.this,"Verification email has been sent.",Toast.LENGTH_LONG).show();
                                        }
                                        else {
                                            Toast.makeText(RegisterActivity.this,"Email not sent."+ task.getException().getMessage(),Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                                HashMap<String,String> newuser = new HashMap<>();
                                newuser.put("ID",user.getUid());
                                newuser.put("UserName",Name);
                                newuser.put("PhoneNumber",Phone);
                                newuser.put("Picture","default");
                                newuser.put("Status","Online");
                                mReference.child("User").child(user.getUid()).child("Profile").setValue(newuser).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Intent newActivity = new Intent(getApplicationContext(),MainActivity.class);
                                        newActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(newActivity);
                                    }
                                });
                                finish();
                            } else {
                                Toast.makeText(RegisterActivity.this,"Error."+task.getException().getMessage(),Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
                break;
            case R.id.LoginR:
                Intent Myinten = new Intent(this,LoginActivity.class);
                Myinten.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(Myinten);
        }
    }
}