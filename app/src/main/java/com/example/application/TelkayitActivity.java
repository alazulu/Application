package com.example.application;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class TelkayitActivity extends AppCompatActivity {

    private Button btnverify,btnsendcode;
    private EditText etverify;
    private FirebaseAuth auth;
    private String mVerification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_telkayit);

        auth=FirebaseAuth.getInstance();

        btnverify=findViewById(R.id.btnverify);
        btnsendcode=findViewById(R.id.btnsendcode);
        etverify=findViewById(R.id.etverify);

        btnverify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String telno=etverify.getText().toString();
                if (telno.length()==10){
                    sendcode(telno);
                    etverify.clearComposingText();
                    etverify.setHint("Doğrulama Kodunu Giriniz");
                    btnverify.setVisibility(View.INVISIBLE);
                    btnsendcode.setVisibility(View.VISIBLE);
                }else{
                    Toast.makeText(TelkayitActivity.this,"Telefon numarası giriniz",Toast.LENGTH_SHORT).show();
                }
            }
        });
        btnsendcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


    }
}