package com.example.application;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.application.models.DbUser;
import com.example.application.models.News;
import com.example.application.models.NewsAdapter;
import com.example.application.models.NewsItem;
import com.example.application.models.istekAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class bildirimFragment extends Fragment {

    private FirebaseUser user;
    private FirebaseAuth auth;
    private DatabaseReference db;
    private RecyclerView rview;
    private TextView tvbildirim;
    private istekAdapter adapter=new istekAdapter();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View vd=inflater.inflate(R.layout.fragment_bildirim, container, false);
        rview=vd.findViewById(R.id.rcbildirim);
        tvbildirim=vd.findViewById(R.id.tvbildiri);
        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
        db=FirebaseDatabase.getInstance().getReference("users");
        rview.setLayoutManager(new LinearLayoutManager(getContext()));
        rview.setAdapter(adapter);

        db.child(user.getUid()).child("istek").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                adapter.clear();
                for(DataSnapshot usersnap:snapshot.getChildren()){

                    DbUser dbUser=new DbUser();
                    String uid=usersnap.getKey().toString();
                    dbUser.setUseruserId(uid);
                    dbUser.setUserIstek(usersnap.child("durum").getValue(Integer.class));
                    dbUser.setUserZaman(usersnap.child("zaman").getValue(Long.class));
                    db.child(uid).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            dbUser.setUserImageurl(snapshot.child("image").getValue(String.class));
                            dbUser.setUserIsim(snapshot.child("ad").getValue(String.class));
                            dbUser.setUserSoyisim(snapshot.child("soyad").getValue(String.class));
                            dbUser.setUserTel(snapshot.child("tel").getValue(String.class));
                            if(dbUser.getUserIstek()==0){
                                adapter.addistekItem(dbUser);
                            }
                            if(adapter.getItemCount()==0){
                                tvbildirim.setVisibility(View.VISIBLE);
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


        return vd;
    }


}