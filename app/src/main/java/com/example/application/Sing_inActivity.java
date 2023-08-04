package com.example.application;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class Sing_inActivity extends AppCompatActivity {

    Button btnkayit;
    EditText etad,etsoyad,etemail,etpassword;
    FirebaseAuth auth;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sing_in);

        btnkayit=findViewById(R.id.btnkayit);
        etad=findViewById(R.id.etisim);
        etsoyad=findViewById(R.id.etsoyisim);
        etemail=findViewById(R.id.etemail);
        etpassword=findViewById(R.id.etpassword);
        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();

        btnkayit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (user==null){
                    if (isValidEmail(etemail.getText().toString())){
                        if (etpassword.getText().toString().length()==8){
                            if (etad.getText().toString().length()>=3 && etsoyad.getText().toString().length()>=2){
                            auth.createUserWithEmailAndPassword(etemail.getText().toString(),etpassword.getText().toString()).addOnCompleteListener(Sing_inActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){
                                        user=FirebaseAuth.getInstance().getCurrentUser();
                                        UserProfileChangeRequest profileUpdates=new UserProfileChangeRequest.Builder().setDisplayName(etad.getText().toString().toUpperCase().trim()+" "+etsoyad.getText().toString().toUpperCase().trim()).build();
                                        user.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()){
                                                    Toast.makeText(Sing_inActivity.this,getString(R.string.toast20),Toast.LENGTH_LONG).show();
                                                    startActivity(new Intent(Sing_inActivity.this,LoginActivity.class));
                                                    finish();
                                                }else {
                                                    Toast.makeText(Sing_inActivity.this,getString(R.string.toast21),Toast.LENGTH_LONG).show();
                                                    startActivity(new Intent(Sing_inActivity.this,LoginActivity.class));
                                                    finish();
                                                }
                                            }
                                        });

                                    }else {
                                        Toast.makeText(Sing_inActivity.this,getString(R.string.toast22),Toast.LENGTH_LONG).show();
                                    }
                                }
                            });}else {
                                Toast.makeText(Sing_inActivity.this,getString(R.string.toast23),Toast.LENGTH_LONG).show();
                            }
                        }else {
                            Toast.makeText(Sing_inActivity.this,getString(R.string.toast24),Toast.LENGTH_LONG).show();
                        }
                    }else {
                        Toast.makeText(Sing_inActivity.this,getString(R.string.toast7),Toast.LENGTH_LONG).show();
                    }
                }else {
                    Toast.makeText(Sing_inActivity.this,getString(R.string.toast26),Toast.LENGTH_LONG).show();
                    auth.signOut();
                }

            }
        });



    }
    private boolean isValidEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}