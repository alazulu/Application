package com.example.application;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

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

public class sohbetlerActivity extends AppCompatActivity {

    private MaterialToolbar toolbar;
    private RecyclerView rcsohbet;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private DatabaseReference db;
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

       db.child("mesajlar").addListenerForSingleValueEvent(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot snapshot) {
               for (DataSnapshot i:snapshot.getChildren()) {
                   String grupname=i.getKey();
                   DataSnapshot kisiler= i.child("kisiler");

                   for(DataSnapshot k:kisiler.getChildren()){
                       if(k.getKey().equals(user.getUid())){
                           String arkadas=k.getValue(String.class);
                           db.child("users").child(arkadas).addListenerForSingleValueEvent(new ValueEventListener() {
                               @Override
                               public void onDataChange(@NonNull DataSnapshot snap) {
                                   DbUser sUser=new DbUser();
                                   sUser.setUserIsim(snap.child("ad").getValue(String.class));
                                   sUser.setUserSoyisim(snap.child("soyad").getValue(String.class));
                                   sUser.setUserImageurl(snap.child("image").getValue(String.class));
                                   sUser.setUserMail(i.getKey());
                                   sUser.setUseruserId(arkadas);

                                   adapter.addsohbetItem(sUser);


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