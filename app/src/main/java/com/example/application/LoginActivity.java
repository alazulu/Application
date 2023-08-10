package com.example.application;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.application.models.cm;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    Button btn,btn1;
    FirebaseAuth auth;
    FirebaseUser user;
    EditText mail;
    EditText sifre;
    TextView tvreset;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
        btn=findViewById(R.id.button);
        btn1=findViewById(R.id.button2);
        mail=findViewById(R.id.etmail);
        sifre=findViewById(R.id.etsifre);
        tvreset=findViewById(R.id.tvreset);

        tvreset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cm.isValidEmail(mail.getText().toString())){
                    auth.sendPasswordResetEmail(mail.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(LoginActivity.this,getString(R.string.toast2),Toast.LENGTH_LONG).show();
                            }else {
                                Toast.makeText(LoginActivity.this,getString(R.string.toast3),Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }else {
                    Toast.makeText(LoginActivity.this,getString(R.string.toast4),Toast.LENGTH_LONG).show();
                }

            }
        });



        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cm.isValidEmail(mail.getText().toString())) {
                    if(sifre.getText().toString().length()==8) {
                        auth.signInWithEmailAndPassword(mail.getText().toString(), sifre.getText().toString()).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                                    finish();
                                } else {
                                    Toast.makeText(LoginActivity.this, getString(R.string.toast5), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }else {
                        Toast.makeText(LoginActivity.this, getString(R.string.toast6), Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(LoginActivity.this, getString(R.string.toast7), Toast.LENGTH_SHORT).show();
                }

            }
        });

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, Sing_inActivity.class));
            }
        });


    }
    @Override
    public void onStart() {
        super.onStart();
        cm.setLocale(LoginActivity.this,cm.getLocaleSharedPreferances(LoginActivity.this));
        user = auth.getCurrentUser();
        if(user != null){
            startActivity(new Intent(LoginActivity.this,HomeActivity.class));
            finish();
        }
    }




}