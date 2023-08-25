package com.example.application;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.application.models.DbUser;
import com.example.application.models.arkadasAdapter;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class arkadasbulActivity extends AppCompatActivity {

    private RecyclerView rcarkadas;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private DatabaseReference db;
    private arkadasAdapter adapter=new arkadasAdapter();
    private MaterialToolbar toolbar;
    private List<String> arkadaslar=new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arkadas);
        toolbar=findViewById(R.id.aktoolbar);
        rcarkadas=findViewById(R.id.rcarkadas);
        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
        db= FirebaseDatabase.getInstance().getReference();
        toolbar.setTitle(R.string.ttl);

        rcarkadas.setLayoutManager(new LinearLayoutManager(this));
        rcarkadas.setAdapter(adapter);
        setSupportActionBar(toolbar);

        db.child("mesajlar").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snaps) {
                for (DataSnapshot a:snaps.getChildren()){
                    a.child("kisiler").getRef().orderByKey().equalTo(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snap) {
                            if (snap.child(user.getUid()).getValue(String.class)!=null){
                                arkadaslar.add(snap.child(user.getUid()).getValue(String.class));
                            }

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


        db.child("users").child(user.getUid()).child("arkadaslar").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                adapter.clear();
                for(DataSnapshot ark:snapshot.getChildren()){
                    DbUser dbUser=new DbUser();
                    db.child("users").child(ark.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snaps) {
                            if (!arkadaslar.contains(snaps.getKey())) {
                                dbUser.setUserIsim(snaps.child("ad").getValue(String.class));
                                dbUser.setUserSoyisim(snaps.child("soyad").getValue(String.class));
                                dbUser.setUserImageurl(snaps.child("image").getValue(String.class));
                                dbUser.setUseruserId(snaps.getKey());
                                adapter.addDbItem(dbUser);
                            }

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
            startActivity(new Intent(arkadasbulActivity.this,LoginActivity.class));
            finish();
        }
        else if (item.getItemId()==R.id.iletisim) {
            startActivity(new Intent(arkadasbulActivity.this, IletisimActivity.class));
        } else {
            super.onOptionsItemSelected(item);
        }

        return super.onOptionsItemSelected(item);
    }


}