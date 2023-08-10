package com.example.application;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FavoriActivity extends AppCompatActivity {

    DatabaseReference db;
    FirebaseUser user;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favori);

        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
        db=FirebaseDatabase.getInstance().getReference();










    }
}