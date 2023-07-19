package com.example.application;

import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class profilFragment extends Fragment {

    TextView adsoyad,tel,mail;
    ImageView ivprofil;
    FirebaseUser user;

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

        user= FirebaseAuth.getInstance().getCurrentUser();

        if (user.getDisplayName()!=null){
            adsoyad.setText(user.getDisplayName().toString());
        }
        if(user.getPhoneNumber()!=null){
            tel.setText(user.getPhoneNumber().toString());
        }
        if (user.getEmail()!=null){
            mail.setText(user.getEmail().toString());
        }
        ivprofil.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.profil));



        return v;
    }
}