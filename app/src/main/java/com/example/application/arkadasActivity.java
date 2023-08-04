package com.example.application;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toolbar;

import com.example.application.models.DbUser;
import com.example.application.models.arkadasAdapter;
import com.example.application.models.iletisimAdapter;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class arkadasActivity extends AppCompatActivity {

    private RecyclerView rcarkadas;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private DatabaseReference db;
    private arkadasAdapter adapter=new arkadasAdapter(this);
    private MaterialToolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arkadas);
        toolbar=findViewById(R.id.aktoolbar);
        rcarkadas=findViewById(R.id.rcarkadas);
        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
        db= FirebaseDatabase.getInstance().getReference("users");
        rcarkadas.setLayoutManager(new LinearLayoutManager(this));
        rcarkadas.setAdapter(adapter);
        setSupportActionBar(toolbar);

        db.child(user.getUid()).child("arkadaslar").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                for(DataSnapshot ark:snapshot.getChildren()){
                    DbUser dbUser=new DbUser();
                    db.child(ark.getKey()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            dbUser.setUserIsim(snapshot.child("ad").getValue(String.class));
                            dbUser.setUserSoyisim(snapshot.child("soyad").getValue(String.class));
                            dbUser.setUserImageurl(snapshot.child("image").getValue(String.class));
                            dbUser.setUseruserId(snapshot.getKey());
                            adapter.addDbItem(dbUser);

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.homemenu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        if(item.getItemId()==R.id.cikis){
            auth.signOut();
            startActivity(new Intent(arkadasActivity.this,LoginActivity.class));
            finish();
        }
        else if (item.getItemId()==R.id.iletisim) {
            startActivity(new Intent(arkadasActivity.this, IletisimActivity.class));
        } else {
            super.onOptionsItemSelected(item);
        }

        return super.onOptionsItemSelected(item);
    }


}