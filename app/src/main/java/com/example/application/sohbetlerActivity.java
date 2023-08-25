package com.example.application;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.application.models.DbUser;
import com.example.application.models.sohbetAdapter;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class sohbetlerActivity extends AppCompatActivity {

    private MaterialToolbar toolbar;
    private RecyclerView rcsohbet;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private DatabaseReference db;
    private sohbetAdapter adapter=new sohbetAdapter();
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sohbetler);

        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
        db= FirebaseDatabase.getInstance().getReference();
        fab=findViewById(R.id.fabSohbet);
        rcsohbet=findViewById(R.id.rcsohbet);
        rcsohbet.setLayoutManager(new LinearLayoutManager(this));
        rcsohbet.setAdapter(adapter);
        toolbar=findViewById(R.id.stoolbar);
        setSupportActionBar(toolbar);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(sohbetlerActivity.this,arkadasbulActivity.class));
            }
        });



       db.child("mesajlar").addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot snapshot) {
               adapter.clear();
               for (DataSnapshot i:snapshot.getChildren()) {
                   String grupname=i.getKey();
                   DataSnapshot kisiler= i.child("kisiler");
                   for(DataSnapshot k:kisiler.getChildren()){
                       if(k.getKey().equals(user.getUid())){
                           String arkadas=k.getValue(String.class);
                           db.child("users").child(arkadas).addValueEventListener(new ValueEventListener() {
                               @Override
                               public void onDataChange(@NonNull DataSnapshot snap) {
                                   adapter.clear();
                                   DbUser sUser=new DbUser();
                                   sUser.setUserIsim(snap.child("ad").getValue(String.class));
                                   sUser.setUserSoyisim(snap.child("soyad").getValue(String.class));
                                   sUser.setUserImageurl(snap.child("image").getValue(String.class));
                                   sUser.setUserMail(i.getKey());
                                   sUser.setUseruserId(arkadas);

                                   DatabaseReference last=i.getRef();
                                   last.child("mesajlar").orderByChild("timestamp").limitToLast(1).addListenerForSingleValueEvent(new ValueEventListener() {
                                       @Override
                                       public void onDataChange(@NonNull DataSnapshot s) {
                                           for (DataSnapshot sonmesaj:s.getChildren()){
                                               sUser.setUserZaman(sonmesaj.child("timestamp").getValue(Long.class));
                                               sUser.setUserTel(sonmesaj.child("mesaj").getValue(String.class));
                                               if(sonmesaj.child("gonderen").getValue(String.class).equals(user.getUid())){
                                                   sUser.setUserIstek(1);
                                               }else {
                                                   sUser.setUserIstek(0);
                                               }
                                           }
                                           adapter.addsohbetItem(sUser);
                                       }

                                       @Override
                                       public void onCancelled(@NonNull DatabaseError error) {

                                       }
                                   });
                               }
                               @Override
                               public void onCancelled(@NonNull DatabaseError error) {

                               }
                           });
                       }
                   }
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