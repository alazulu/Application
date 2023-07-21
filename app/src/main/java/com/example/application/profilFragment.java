package com.example.application;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.concurrent.TimeUnit;


public class profilFragment extends Fragment {

    TextView adsoyad,tel,mail;
    ImageView ivprofil;
    FirebaseUser user;
    Button btnsfr,btnmail,btntel,btnonaytel,btnonaymail;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_profil, container, false);

        adsoyad=v.findViewById(R.id.tvadsoyad);
        tel=v.findViewById(R.id.tvtel);
        mail=v.findViewById(R.id.tvmail);
        ivprofil=v.findViewById(R.id.ivprofil);
        btnmail=v.findViewById(R.id.btnmail);
        btntel=v.findViewById(R.id.btntel);
        btnsfr=v.findViewById(R.id.btnsfr);
        btnonaymail=v.findViewById(R.id.btnonaymail);
        btnonaytel=v.findViewById(R.id.btnonaytel);

        user= FirebaseAuth.getInstance().getCurrentUser();

        if (user.getDisplayName()!=null){
            adsoyad.setText(user.getDisplayName().toString());
        }

        if (user.getEmail()!=null){
            mail.setText(user.getEmail().toString());
        }

        btnsfr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),SfrdgrActivity.class));
            }
        });

        btnmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), MaildgrActivity.class));
            }
        });

        if(user.getPhotoUrl()==null) {
            ivprofil.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.profil));
        }else {
            Bitmap image = null;
            try {
                image = Picasso.get().load(user.getPhotoUrl().toString()).resize(100, 100).get();
                ivprofil.setImageBitmap(image);
            } catch (IOException e) {
                ivprofil.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.profil));
                throw new RuntimeException(e);
            }

        }

        if (user.isEmailVerified()){
            btnonaymail.setVisibility(View.INVISIBLE);
        }else{
            btnonaymail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }

        if( user.getPhoneNumber().equals("") || user.getPhoneNumber()==null ){
            Log.d("TAG", user.getPhoneNumber().toString());
            btntel.setText("Telefon Kaydet");
            btnonaytel.setVisibility(View.INVISIBLE);
            btntel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getActivity(),TelkayitActivity.class));

                }
            });
        }else {
            Log.d("TG", user.getPhoneNumber().toString());
            tel.setText(user.getPhoneNumber().toString());

        }

        if (!user.isEmailVerified()){

            btnonaymail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(getActivity(),"Onay maili Gönderildi.",Toast.LENGTH_SHORT).show();
                                btnonaymail.setVisibility(View.INVISIBLE);
                            }else {
                                Toast.makeText(getActivity(),"Bir sorun oluştu.",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }
            });
        }else {
            btnonaymail.setVisibility(View.INVISIBLE);
        }


        return v;
    }

}