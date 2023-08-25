package com.example.application;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.application.models.DbMesaj;
import com.example.application.models.DbUser;
import com.example.application.models.PicassoCache;
import com.example.application.models.cm;
import com.example.application.models.mesajAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

public class mesajActivity extends AppCompatActivity {

    private DbUser dbUser=new DbUser();
    private TextView tvmesaj;
    private EditText etmesaj;
    private Button btnmesaj;
    private ImageView ivmesaj;
    private FirebaseUser user;
    private FirebaseAuth auth;
    private DatabaseReference db, db2;
    private DbMesaj mesaj=new DbMesaj();
    private RecyclerView rcmesaj;
    private mesajAdapter adapter=new mesajAdapter();
    private Boolean isActivityVisible=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mesaj);

        String userExtra=getIntent().getStringExtra("user");
        tvmesaj=findViewById(R.id.tvmesaj);
        ivmesaj=findViewById(R.id.ivmesaj);
        btnmesaj=findViewById(R.id.btnmesaj);
        etmesaj=findViewById(R.id.etmesaj);
        rcmesaj=findViewById(R.id.rcmesaj);
        LinearLayoutManager lm=new LinearLayoutManager(this);
        lm.setStackFromEnd(true);
        rcmesaj.setLayoutManager(lm);
        rcmesaj.setAdapter(adapter);
        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
        final String[] grupAdi = new String[1];
        db = FirebaseDatabase.getInstance().getReference().child("mesajlar");
        db2 = FirebaseDatabase.getInstance().getReference().child("users");

        db2.child(userExtra).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
            dbUser.setUseruserId(snapshot.getKey());
            dbUser.setUserImageurl(snapshot.child("image").getValue(String.class));
            dbUser.setUserSoyisim(snapshot.child("soyad").getValue(String.class));
            dbUser.setUserIsim(snapshot.child("ad").getValue(String.class));

            tvmesaj.setText(dbUser.getUserIsim()+" "+dbUser.getUserSoyisim());
            String url= dbUser.getUserImageurl();
            Context c=ivmesaj.getContext();
            PicassoCache.getPicassoInstance(c).load(url).error(R.drawable.error).resize(200,200).into(ivmesaj);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        CompletableFuture<String> groupFuture = cm.findMatchingGroup(user.getUid(), userExtra);
        groupFuture.thenAccept(groupName -> {
            if (groupName != null) {
                grupAdi[0] = groupName;
                db.child(grupAdi[0]).child("mesajlar").orderByChild("timestamp").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        adapter.clear();
                        for(DataSnapshot satir : snapshot.getChildren()) {

                            mesaj = new DbMesaj();
                            mesaj.setGonderen(satir.child("gonderen").getValue(String.class));
                            mesaj.setMesaj(satir.child("mesaj").getValue(String.class));
                            mesaj.setTimestamp(satir.child("timestamp").getValue(Long.class));
                            mesaj.setOkundu(satir.child("okundu").getValue(Boolean.class));

                            adapter.addmesajItem(mesaj);

                            if(satir.child("gonderen").getValue(String.class) != null && satir.child("gonderen").getValue(String.class).equals(userExtra) && isActivityVisible && !mesaj.getOkundu()){
                                DatabaseReference okunma = satir.child("okundu").getRef();
                                okunma.setValue(true);
                            }
                        }
                        rcmesaj.scrollToPosition(adapter.getItemCount()-1);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Hata işleme kodu
                    }
                });
            } else {
                Log.d("TAG", "No matching group found.");
            }
        });




        btnmesaj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (adapter.getItemCount()==0) {
                    mesaj = new DbMesaj();
                    mesaj.setGonderen( user.getUid());
                    mesaj.setMesaj(etmesaj.getText().toString());
                    mesaj.setTimestamp( System.currentTimeMillis());
                    mesaj.setOkundu(false);

                    if (!etmesaj.getText().toString().isEmpty()) {
                        HashMap<String, String> kisi = new HashMap<>();
                        kisi.put(user.getUid(), dbUser.getUseruserId());
                        kisi.put(dbUser.getUseruserId(), user.getUid());
                        grupAdi[0] = db.push().getKey();
                        db.child(grupAdi[0]).child("mesajlar").push().setValue(mesaj);
                        db.child(grupAdi[0]).child("kisiler").setValue(kisi);
                        cm.hideKeyboard(mesajActivity.this);
                        etmesaj.setText("");
                        adapter.addmesajItem(mesaj);

                    } else {
                        Toast.makeText(mesajActivity.this, R.string.toast39, Toast.LENGTH_SHORT).show();
                    }

                }else {
                    mesaj = new DbMesaj();
                    mesaj.setGonderen( user.getUid());
                    mesaj.setMesaj(etmesaj.getText().toString());
                    mesaj.setTimestamp( System.currentTimeMillis());
                    mesaj.setOkundu(false);

                    if (!etmesaj.getText().toString().equals("")) {

                        db.child(grupAdi[0]).child("mesajlar").push().setValue(mesaj);
                        cm.hideKeyboard(mesajActivity.this);
                        etmesaj.setText("");
                        adapter.addmesajItem(mesaj);

                    } else {
                        Toast.makeText(mesajActivity.this, R.string.toast39, Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });

    }
    @Override
    protected void onResume() {
        super.onResume();
        isActivityVisible = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        isActivityVisible = false;
    }
}