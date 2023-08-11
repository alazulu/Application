package com.example.application;


import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.application.models.DbUser;
import com.example.application.models.DbUserAdapter;
import com.example.application.models.cm;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class SearchActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private DatabaseReference db;
    private FirebaseUser user;
    private EditText etsearch;
    private Button btnsearch;
    private RecyclerView rcsearch;
    private DbUserAdapter adapter=new DbUserAdapter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
        etsearch=findViewById(R.id.et_ara);
        etsearch.requestFocus();
        rcsearch=findViewById(R.id.rc_ara);
        btnsearch=findViewById(R.id.btn_ara);
        db= FirebaseDatabase.getInstance().getReference("users");
        rcsearch.setLayoutManager(new LinearLayoutManager(SearchActivity.this));
        rcsearch.setAdapter(adapter);

        btnsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.clear();
                cm.hideKeyboard(SearchActivity.this);
                String srch=etsearch.getText().toString().toUpperCase();

                List<String> sorguid=new ArrayList<>();
                List<String> kullanicilar =new  ArrayList<>();
                kullanicilar.add(user.getUid());
                db.child(user.getUid()).child("arkadaslar").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot users:snapshot.getChildren()) {
                            kullanicilar.add(users.getKey());
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                if(!srch.isEmpty()) {
                    Query queryAd = db.orderByChild("ad").startAt(srch).endAt(srch + "\uf8ff");
                    Query querySoyad = db.orderByChild("soyad").startAt(srch).endAt(srch+"\uf8ff");

                    querySoyad.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot sSnap: snapshot.getChildren()){

                                sorguid.add(sSnap.getKey());
                                Log.d("---------",sSnap.getKey());

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(SearchActivity.this, R.string.toast38 + error.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });

                    queryAd.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot aSnap : dataSnapshot.getChildren()) {

                                if(!sorguid.contains(aSnap.getKey())){
                                    sorguid.add(aSnap.getKey());
                                    Log.d("++++++++++",aSnap.getKey());
                                }

                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Toast.makeText(SearchActivity.this, R.string.toast38 + databaseError.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });

                        db.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot u:snapshot.getChildren()){

                                    if(sorguid.contains(u.getKey())){

                                        DbUser kullanici = new DbUser();
                                        kullanici.setUserIsim(u.child("ad").getValue(String.class));
                                        kullanici.setUserSoyisim(u.child("soyad").getValue(String.class));
                                        kullanici.setUserMail(u.child("mail").getValue(String.class));
                                        kullanici.setUserTel(u.child("tel").getValue(String.class));
                                        kullanici.setUserImageurl(u.child("image").getValue(String.class));
                                        kullanici.setUseruserId(u.getKey());

                                        if( u.child("istek").child(user.getUid()).child("zaman").getValue(Long.class)==null){
                                            kullanici.setUserZaman(946684800);
                                        }else {
                                            kullanici.setUserZaman(u.child("istek").child(user.getUid()).child("zaman").getValue(Long.class));
                                        }

                                        if (u.child("istek").child(user.getUid()).child("durum").getValue()==null){
                                            kullanici.setUserIstek(1);
                                        }else {
                                            kullanici.setUserIstek(u.child("istek").child(user.getUid()).child("durum").getValue(Integer.class));
                                        }
                                        if (kullanici.getUserIstek()!=2 ){
                                            if(!kullanicilar.contains(kullanici.getUseruserId())){
                                                adapter.addDbItem(kullanici);
                                            }
                                        }
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(SearchActivity.this, R.string.toast38 + error.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
                }else{
                    Toast.makeText(SearchActivity.this,R.string.toast37,Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}