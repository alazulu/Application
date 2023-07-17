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
        user=FirebaseAuth.getInstance().getCurrentUser();

        btnkayit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (user==null){
                    auth.createUserWithEmailAndPassword(etemail.getText().toString(),etpassword.getText().toString()).addOnCompleteListener(Sing_inActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                user=FirebaseAuth.getInstance().getCurrentUser();
                                UserProfileChangeRequest profileUpdates=new UserProfileChangeRequest.Builder().setDisplayName(etad.getText().toString()+" "+etsoyad.getText().toString()).build();
                                user.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            Toast.makeText(Sing_inActivity.this,"Kullanıcı oluşturuldu",Toast.LENGTH_LONG).show();
                                            startActivity(new Intent(Sing_inActivity.this,LoginActivity.class));
                                            finish();
                                        }else {
                                            Toast.makeText(Sing_inActivity.this,"Kullanıcı oluşturuldu. Profil oluşturulamadı",Toast.LENGTH_LONG).show();
                                            startActivity(new Intent(Sing_inActivity.this,LoginActivity.class));
                                            finish();
                                        }
                                    }
                                });

                            }else {
                                Toast.makeText(Sing_inActivity.this,"Kullanıcı oluşturulamadı",Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }else {
                    Toast.makeText(Sing_inActivity.this,"Aktif kullanıcıdan çıkış yapınız",Toast.LENGTH_LONG).show();
                }

            }
        });



    }
}