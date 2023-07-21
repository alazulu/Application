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
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MaildgrActivity extends AppCompatActivity {

    EditText etmaildgrsfr,etmaildgr;
    Button btnmaildgr;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maildgr);

        etmaildgr=findViewById(R.id.etmaildgr);
        etmaildgrsfr=findViewById(R.id.etmaildgrsfr);
        btnmaildgr=findViewById(R.id.dgrmail);
        user= FirebaseAuth.getInstance().getCurrentUser();

        btnmaildgr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String eskimail=user.getEmail();
                AuthCredential credential = EmailAuthProvider.getCredential(eskimail, etmaildgrsfr.getText().toString());
                user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            user.verifyBeforeUpdateEmail(etmaildgr.getText().toString());
                            Toast.makeText(MaildgrActivity.this, "Mail adresiniz mailinize gelen doğrulama linkini onayladıktan sonra değişecektir", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(MaildgrActivity.this,LoginActivity.class));
                            finish();
                        } else {
                            Toast.makeText(MaildgrActivity.this, "İşlem Başarısız", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });


    }
}