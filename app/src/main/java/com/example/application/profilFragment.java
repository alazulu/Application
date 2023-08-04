package com.example.application;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
    Button btnsfr,btnmail,btntel,btnonaymail,btndelete;


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
        btndelete=v.findViewById(R.id.btndelete);

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
                    user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(getActivity(),getString(R.string.toast10),Toast.LENGTH_SHORT).show();
                                btnonaymail.setVisibility(View.INVISIBLE);
                            }else {
                                Toast.makeText(getActivity(),getString(R.string.toast11),Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            });
        }

        if( user.getPhoneNumber().equals("") || user.getPhoneNumber() == null ){
            Log.d("TAG", user.getPhoneNumber().toString());
            btntel.setText(getString(R.string.toast12));
            btntel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getActivity(),TelkayitActivity.class));

                }
            });
        }else {
            tel.setText(user.getPhoneNumber().toString());
            btntel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getActivity(),TeldgrActivity.class));
                }
            });

        }



        btndelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert=new AlertDialog.Builder(getContext());
                alert.setTitle(getString(R.string.hsbsl));
                alert.setMessage(getString(R.string.toast13));
                alert.setPositiveButton(getString(R.string.hsbsl),new DialogInterface.OnClickListener(){
                    public  void onClick(DialogInterface dialog,int arg1){
                        user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    Toast.makeText(getContext(),getString(R.string.toast14),Toast.LENGTH_LONG).show();
                                }else {
                                    Toast.makeText(getContext(),getString(R.string.toast15),Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    }
                });
                alert.setNegativeButton(getString(R.string.toast16), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                alert.setCancelable(true);

                alert.show();
            }
        });


        return v;
    }

}