package com.example.application;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;


import java.util.concurrent.TimeUnit;

public class TelkayitActivity extends AppCompatActivity {

    private Button btnverify,btnsendcode;
    private EditText etverify;
    private FirebaseAuth auth;
    private FirebaseUser user,userd;
    DatabaseReference db;
    private String mVerification,uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_telkayit);

        auth = FirebaseAuth.getInstance();
        userd=auth.getCurrentUser();
        uid=auth.getCurrentUser().getUid();
        db=FirebaseDatabase.getInstance().getReference("users").child(uid);
        //btnverify = findViewById(R.id.btnverify);
        btnsendcode=findViewById(R.id.btnsendcode);
        etverify = findViewById(R.id.etverify);
        String deneme=String.valueOf(db);
        Log.e("user", String.valueOf( userd.isEmailVerified()));
        btnsendcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNo ="+90"+ etverify.getText().toString();
                if (phoneNo.length() == 13) {
                    db.child("tel").setValue(phoneNo).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(TelkayitActivity.this,"Telefon no eklendi",Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(TelkayitActivity.this,HomeActivity.class));
                                finish();
                            }else {
                                Toast.makeText(TelkayitActivity.this,"Bir hata oluştu",Toast.LENGTH_SHORT).show();
                                etverify.setText("");
                            }
                        }
                    });

                    /*sendtel(telno);
                    etverify.setText("");
                    etverify.setHint("Doğrulama Kodunu Giriniz");
                    etverify.setFilters(new InputFilter[]{new InputFilter.LengthFilter(6)});
                    btnverify.setVisibility(View.INVISIBLE);
                    btnsendcode.setVisibility(View.VISIBLE);*/
                } else {
                    Toast.makeText(TelkayitActivity.this, "Geçerli bir telefon numarası giriniz", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
       /* btnverify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String verifycode= etverify.getText().toString();
                if (verifycode.length()==6){
                    sendcode(verifycode);
                }else {
                    Toast.makeText(TelkayitActivity.this,"Geçerli bir doğrulama kodu giriniz",Toast.LENGTH_SHORT).show();
                }
            }
        });




    private void sendtel(String tel){

        PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks=new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                Log.e("hata", "onVerificationCompleted");
                Toast.makeText(TelkayitActivity.this,"Doğrulama Tamamlandı",Toast.LENGTH_LONG).show();

            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Log.e("hata","Verification Error:"+e.getMessage());
                Toast.makeText(TelkayitActivity.this,"Doğrulama Başarısız:"+e.getMessage(),Toast.LENGTH_LONG).show();


            }


            public void onCodeSend(@NonNull String verificationId,@NonNull PhoneAuthProvider.ForceResendingToken token){
                Log.e("hata","Kod Gönderildi");
                mVerification=verificationId;
            }
        };

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                tel,
                2,
                TimeUnit.MINUTES,
                this,
                mCallbacks);
    }

    private void sendcode(String VerifyCode){
        PhoneAuthCredential credential=PhoneAuthProvider.getCredential(mVerification,VerifyCode);

        auth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Log.e("hata","Verification Success");
                    Toast.makeText(TelkayitActivity.this,"Kod Başarıyla Doğrulandı :)",Toast.LENGTH_LONG).show();
                    startActivity(new Intent(TelkayitActivity.this,LoginActivity.class));
                } else if (task.getException()instanceof FirebaseAuthInvalidCredentialsException) {
                    Log.e("hata","Verification Failed, Invalid credentials");
                    Toast.makeText(TelkayitActivity.this,"Hatalı Doğrulama Kodu!",Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(TelkayitActivity.this,"Beklenmedik bir hata oluştu",Toast.LENGTH_LONG).show();
                }
            }
        });
    }*/

}