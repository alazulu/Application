package com.example.application;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
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

                if(!srch.isEmpty()) {
                    Query queryAd = db.orderByChild("ad").startAt(srch).endAt(srch + "\uf8ff");


                    queryAd.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {


                                DbUser kullanici = new DbUser();
                                kullanici.setUserIsim(userSnapshot.child("ad").getValue(String.class));
                                kullanici.setUserSoyisim(userSnapshot.child("soyad").getValue(String.class));
                                kullanici.setUserMail(userSnapshot.child("mail").getValue(String.class));
                                kullanici.setUserTel(userSnapshot.child("tel").getValue(String.class));
                                kullanici.setUserImageurl(userSnapshot.child("image").getValue(String.class));

                                if( userSnapshot.child("istek").child(user.getUid()).child("zaman").getValue(Long.class)==null){
                                    kullanici.setUserZaman(946684800);
                                }else {
                                    kullanici.setUserZaman(userSnapshot.child("istek").child(user.getUid()).child("zaman").getValue(Long.class));
                                }

                                kullanici.setUseruserId(userSnapshot.getKey());

                                if (userSnapshot.child("istek").child(user.getUid()).child("durum").getValue()==null){
                                    kullanici.setUserIstek(1);
                                }else {
                                    kullanici.setUserIstek(userSnapshot.child("istek").child(user.getUid()).child("durum").getValue(Integer.class));
                                }

                                if (!kullanici.getUseruserId().equals(user.getUid()) || kullanici.getUserIstek()!=2){
                                    adapter.addDbItem(kullanici);
                                }

                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Toast.makeText(SearchActivity.this, "Database Hatası: " + databaseError.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }else{
                    Toast.makeText(SearchActivity.this,"Lütfen isim giriniz",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}