package com.example.application;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

    private DbUser dbUser;
    private TextView tvmesaj;
    private EditText etmesaj;
    private Button btnmesaj;
    private ImageView ivmesaj;
    private FirebaseUser user;
    private FirebaseAuth auth;
    private DatabaseReference db;
    private HashMap<String,Object> mesaj=new HashMap<>();
    private RecyclerView rcmesaj;
    private mesajAdapter adapter=new mesajAdapter();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mesaj);

        dbUser=(DbUser) getIntent().getSerializableExtra("user");
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


        CompletableFuture<String> groupFuture = cm.findMatchingGroup(user.getUid(), dbUser.getUseruserId());
        groupFuture.thenAccept(groupName -> {
            if (groupName != null) {
                grupAdi[0] =groupName;
                db.child(groupName).child("mesajlar").orderByChild("timestamp").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot satir:snapshot.getChildren()){
                            mesaj=new HashMap<>();
                            mesaj.put("gonderen",satir.child("gonderen").getValue(String.class));
                            mesaj.put("mesaj",satir.child("mesaj").getValue(String.class));
                            mesaj.put("timestamp",satir.child("timestamp").getValue(Long.class));
                            adapter.addmesajItem(mesaj);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            } else {
                // Eşleşen grup bulunamadığında burada işlemler yapabilirsiniz
                Log.d("TAG", "No matching group found.");
            }
        });


            btnmesaj.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (adapter.getItemCount()==0) {
                        mesaj = new HashMap<>();
                        mesaj.put("gonderen", user.getUid());
                        mesaj.put("mesaj", etmesaj.getText().toString());
                        mesaj.put("timestamp", System.currentTimeMillis());

                        if (!etmesaj.getText().toString().equals("")) {
                            HashMap<String, String> kisi = new HashMap<>();
                            kisi.put(user.getUid(), dbUser.getUseruserId());
                            kisi.put(dbUser.getUseruserId(), user.getUid());
                            String grupkey = db.push().getKey();
                            db.child(grupkey).child("mesajlar").push().setValue(mesaj);
                            db.child(grupkey).child("kisiler").setValue(kisi);
                            cm.hideKeyboard(mesajActivity.this);
                            etmesaj.setText("");
                            adapter.clear();
                            adapter.addmesajItem(mesaj);

                        } else {
                            Toast.makeText(mesajActivity.this, "Mesaj giriniz", Toast.LENGTH_SHORT).show();
                        }

                    }else {

                        mesaj = new HashMap<>();
                        mesaj.put("gonderen", user.getUid());
                        mesaj.put("mesaj", etmesaj.getText().toString());
                        mesaj.put("timestamp", System.currentTimeMillis());

                        if (!etmesaj.getText().toString().equals("")) {
                            db.child(grupAdi[0]).child("mesajlar").push().setValue(mesaj);
                            cm.hideKeyboard(mesajActivity.this);
                            etmesaj.setText("");
                            adapter.clear();
                            adapter.addmesajItem(mesaj);

                        } else {
                            Toast.makeText(mesajActivity.this, "Mesaj giriniz", Toast.LENGTH_SHORT).show();
                        }

                    }
                }
            });





        tvmesaj.setText(dbUser.getUserIsim()+" "+dbUser.getUserSoyisim());
        String url= dbUser.getUserImageurl();
        Context c=ivmesaj.getContext();
        PicassoCache.getPicassoInstance(c).load(url).error(R.drawable.error).resize(200,200).into(ivmesaj);

    }

   /* private CompletableFuture<String> findMatchingGroup(String id1, String id2) {
        DatabaseReference db = FirebaseDatabase.getInstance().getReference("mesajlar");
        CompletableFuture<String> future = new CompletableFuture<>();

        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot grupSnapshot : snapshot.getChildren()) {
                    DataSnapshot kisilerSnapshot = grupSnapshot.child("kisiler");

                    boolean match1 = false;
                    boolean match2 = false;

                    for (DataSnapshot kisiSnapshot : kisilerSnapshot.getChildren()) {
                        String kisiId = kisiSnapshot.getKey();

                        if (kisiId.equals(id1)) {
                            match1 = true;
                        } else if (kisiId.equals(id2)) {
                            match2 = true;
                        }

                        if (match1 && match2) {
                            String grupName = grupSnapshot.getKey();
                            future.complete(grupName);
                            return;
                        }
                    }
                }

                future.complete(null);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("TAG", "Database Error: " + error.getMessage());
                future.complete(null);
            }
        });

        return future;
    }*/



}