package com.example.application;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class profilFragment extends Fragment {

    TextView tv1;
    FirebaseUser user;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_profil, container, false);
        tv1=v.findViewById(R.id.tv1);

        user= FirebaseAuth.getInstance().getCurrentUser();
        if(user!=null){
            tv1.setText(user.getDisplayName().toString());}
        else {
            Toast.makeText(getActivity(),"Kullanıcı adı bulunamadı",Toast.LENGTH_SHORT).show();
        }


        return v;
    }
}