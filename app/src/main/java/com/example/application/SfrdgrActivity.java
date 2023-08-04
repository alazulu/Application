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

public class SfrdgrActivity extends AppCompatActivity {

    EditText ysfr,esfr;
    Button sgonder;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sfrdgr);

        esfr=findViewById(R.id.eskisfr);
        ysfr=findViewById(R.id.yenisfr);
        sgonder=findViewById(R.id.btnsfrdgr);
        user= FirebaseAuth.getInstance().getCurrentUser();

        sgonder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(esfr.length()==8 && ysfr.length()==8){
                    String mail = user.getEmail();
                    AuthCredential credential = EmailAuthProvider.getCredential(mail, esfr.getText().toString());
                    user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                user.updatePassword(ysfr.getText().toString());
                                Toast.makeText(SfrdgrActivity.this, getString(R.string.toast17), Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(SfrdgrActivity.this, LoginActivity.class));
                                finish();
                            } else {
                                Toast.makeText(SfrdgrActivity.this, getString(R.string.toast18), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }else {
                    Toast.makeText(SfrdgrActivity.this, getString(R.string.toast19), Toast.LENGTH_SHORT).show();
                }
            }
        });



    }
}