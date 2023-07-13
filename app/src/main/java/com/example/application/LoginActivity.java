package com.example.application;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    Button btn;
    FirebaseAuth auth;
    FirebaseUser user;
    EditText mail;
    EditText sifre;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        auth=FirebaseAuth.getInstance();
        btn=findViewById(R.id.button);
        mail=findViewById(R.id.etmail);
        sifre=findViewById(R.id.etsifre);


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                auth.signInWithEmailAndPassword(mail.getText().toString(),sifre.getText().toString()).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            user=auth.getCurrentUser();
                            startActivity(new Intent(LoginActivity.this,HomeActivity.class));
                            finish();
                        }
                        else {
                            Toast.makeText(LoginActivity.this,"Giriş Başarısız",Toast.LENGTH_SHORT).show();
                        }
                    }
                });


            }
        });


    }
    @Override
    public void onStart() {
        super.onStart();
        user = auth.getCurrentUser();
        if(user != null){
            startActivity(new Intent(LoginActivity.this,HomeActivity.class));
            finish();
        }
    }
}