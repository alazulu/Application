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

import com.example.application.R;
import com.example.application.models.DbUser;
import com.example.application.models.sohbetAdapter;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class sohbetlerActivity extends AppCompatActivity {

    private MaterialToolbar toolbar;
    private RecyclerView rcsohbet;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private DatabaseReference db;
    private DbUser dbUser=new DbUser();
    private sohbetAdapter adapter=new sohbetAdapter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sohbetler);

        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
        db= FirebaseDatabase.getInstance().getReference();
        rcsohbet=findViewById(R.id.rcsohbet);
        rcsohbet.setLayoutManager(new LinearLayoutManager(this));
        rcsohbet.setAdapter(adapter);
        toolbar=findViewById(R.id.stoolbar);
        setSupportActionBar(toolbar);

        db.child("mesajlar").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot grups:snapshot.getChildren()) {
                    String grupkey=grups.getKey();
                    db.child("mesajlar").child(grupkey).child("kisiler").addValueEventListener(new ValueEventListener() {
                        String arkadas="";
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapid) {
                            for(DataSnapshot ids:snapid.getChildren()){

                                if(Objects.equals(ids.getKey(), user.getUid())){
                                    arkadas= ids.getValue(String.class);
                                    Log.d("+++++++++",ids.getValue(String.class));
                                    db.child("users").child(arkadas).addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapuser) {
                                            Log.d("-->-->-->",grupkey);
                                            dbUser.setUseruserId(arkadas);
                                            dbUser.setUserMail(grupkey);
                                            dbUser.setUserIsim(snapuser.child("ad").getValue(String.class));
                                            dbUser.setUserImageurl(snapuser.child("image").getValue(String.class));
                                            dbUser.setUserSoyisim(snapuser.child("soyad").getValue(String.class));
                                            adapter.addsohbetItem(dbUser);

                                            String arkadas="";
                                            dbUser=new DbUser();


                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });

                                    adapter.clear();

                                }
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
            startActivity(new Intent(sohbetlerActivity.this,LoginActivity.class));
            finish();
        }
        else if (item.getItemId()==R.id.iletisim) {
            startActivity(new Intent(sohbetlerActivity.this, IletisimActivity.class));
        } else {
            super.onOptionsItemSelected(item);
        }

        return super.onOptionsItemSelected(item);
    }

}