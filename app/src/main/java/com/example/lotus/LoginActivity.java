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

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    EditText Etemail,Etpass;
    Button btnLogin;
    TextView TvFoget, TvRegister;
    protected FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mapView();
        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser()!=null){
            Intent openmain = new Intent(getApplicationContext(),MainActivity.class);
            openmain.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(openmain);
            finish();
        }
    }
    protected void mapView(){
        Etemail= (EditText) findViewById(R.id.Etemail);
        Etpass= (EditText) findViewById(R.id.Etpass);
        btnLogin =(Button) findViewById(R.id.Login);
        btnLogin.setOnClickListener(this);
        TvFoget = (TextView) findViewById(R.id.Forget);
        TvFoget.setOnClickListener(this);
        TvRegister = (TextView) findViewById( R.id.Register);
        TvRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.Login:
                Boolean res=true;
                String email =Etemail.getText().toString().trim(),password=Etpass.getText().toString().trim();
                if (email.isEmpty()){ Etemail.setError("Field cannot be empty");res=false;}else Etemail.setError(null);
                if (password.isEmpty()){Etpass.setError("Field cannot be empty");res=false;} else Etpass.setError(null);
                if (res){
                    mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                Intent openmain = new Intent(getApplicationContext(),MainActivity.class);
                                openmain.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(openmain);
                                finish();
                            }
                            else
                                Toast.makeText(getApplicationContext(),"Error!"+task.getException().getMessage(),Toast.LENGTH_LONG).show();
                        }

                    });
                }
                break;
            case R.id.Forget:

                break;
            case R.id.Register:
                Intent openmain = new Intent(getApplicationContext(),RegisterActivity.class);
                openmain.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(openmain);
                finish();
                break;
        }
    }
}