package com.example.application;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

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

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
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

        if(user.getPhoneNumber()==""){
            btntel.setText("Telefon Kaydet");
            btnonaytel.setVisibility(View.INVISIBLE);
            btntel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getActivity(),TelkayitActivity.class));

                }
            });
        }else {
            tel.setText(user.getPhoneNumber().toString());

        }




        return v;
    }

}