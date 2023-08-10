package com.example.application;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import java.util.concurrent.TimeUnit;

public class TeldgrActivity extends AppCompatActivity {

    private Button btnverify, btnsendcode;
    private EditText etverify;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private String mVerification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_telkayit);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        btnverify = findViewById(R.id.btnverify);
        btnsendcode = findViewById(R.id.btnsendcode);
        etverify = findViewById(R.id.etverify);
        btnsendcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String phoneNo = "+90" + etverify.getText().toString();

                if (phoneNo.length() == 13) {
                    sendtel(phoneNo);
                    etverify.setText("");
                    etverify.setHint(getString(R.string.toast25));
                    etverify.setFilters(new InputFilter[]{new InputFilter.LengthFilter(6)});
                    btnverify.setVisibility(View.VISIBLE);
                    btnsendcode.setVisibility(View.INVISIBLE);
                } else {
                    Toast.makeText(TeldgrActivity.this, getString(R.string.toast27), Toast.LENGTH_SHORT).show();
                }

            }
        });

        btnverify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String verifycode = etverify.getText().toString();
                if (verifycode.length() == 6) {
                    sendcode(verifycode);
                } else {
                    Toast.makeText(TeldgrActivity.this, getString(R.string.toast28), Toast.LENGTH_SHORT).show();
                }

            }
        });
    }


    private void sendtel(String tel) {

        PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                Toast.makeText(TeldgrActivity.this, getString(R.string.toast29), Toast.LENGTH_LONG).show();

            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Toast.makeText(TeldgrActivity.this, getString(R.string.toast30) + e.getMessage(), Toast.LENGTH_LONG).show();

            }


            public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken token) {
                mVerification = verificationId;
            }
        };

        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(auth).setPhoneNumber(tel).setTimeout(60L, TimeUnit.SECONDS).setCallbacks(mCallbacks).build();

        PhoneAuthProvider.verifyPhoneNumber(options);

    }

    private void sendcode(String VerifyCode) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerification, VerifyCode);
        user.updatePhoneNumber(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(TeldgrActivity.this, getString(R.string.toast31), Toast.LENGTH_LONG).show();
                    startActivity(new Intent(TeldgrActivity.this, LoginActivity.class));
                    finish();
                } else if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                    Toast.makeText(TeldgrActivity.this, getString(R.string.toast32), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(TeldgrActivity.this, task.getException().toString(), Toast.LENGTH_LONG).show();
                }
            }
        });


    }
}



