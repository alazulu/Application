package com.example.application;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.application.models.DbUser;
import com.example.application.models.bildirimAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class bildirimFragment extends Fragment {

    private FirebaseUser user;
    private FirebaseAuth auth;
    private DatabaseReference db;
    private RecyclerView rview;
    private TextView tvbildirim;
    private bildirimAdapter adapter=new bildirimAdapter();


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
        db=FirebaseDatabase.getInstance().getReference();
        rview.setLayoutManager(new LinearLayoutManager(getContext()));
        rview.setAdapter(adapter);

        db.child("users").child(user.getUid()).child("istek").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                adapter.clear();
                for(DataSnapshot usersnap:snapshot.getChildren()){
                    Log.d("*******", usersnap.toString());
                    DbUser dbUser=new DbUser();
                    String uid=usersnap.getKey();
                    dbUser.setUseruserId(uid);
                    dbUser.setUserIstek(usersnap.child("durum").getValue(Integer.class));
                    dbUser.setUserZaman(usersnap.child("zaman").getValue(Long.class));
                    db.child("users").child(uid).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            dbUser.setUserImageurl(snapshot.child("image").getValue(String.class));
                            dbUser.setUserIsim(snapshot.child("ad").getValue(String.class));
                            dbUser.setUserSoyisim(snapshot.child("soyad").getValue(String.class));
                            dbUser.setUserTel(snapshot.child("tel").getValue(String.class));
                            dbUser.setUserMail("istek");
                            adapter.notifyDataSetChanged();
                            if(dbUser.getUserIstek()==0){
                                adapter.addistekItem(dbUser);
                            }
                            if(adapter.getItemCount()==0){
                                tvbildirim.setVisibility(View.VISIBLE);
                            }else {
                                tvbildirim.setVisibility(View.INVISIBLE);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
                db.child("mesajlar").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snaps) {
                        for (DataSnapshot s:snaps.getChildren()){
                            DatabaseReference s1=s.getRef();
                            s1.child("kisiler").orderByKey().equalTo(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snap) {
                                    DbUser mesajbildirim=new DbUser();
                                    if (snap.getValue()!=null){
                                        Log.d(">>>>>>>>>>>>>",snap.child(user.getUid()).getValue(String.class));
                                        Log.d("+++++++++++++",s.getKey());

                                        db.child("mesajlar").child(s.getKey()).child("mesajlar").orderByChild("timestamp").limitToLast(1).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapm) {
                                                for(DataSnapshot a:snapm.getChildren()){
                                                    if (!a.child("gonderen").getValue(String.class).equals(user.getUid()) && Boolean.FALSE.equals(a.child("okundu").getValue(Boolean.class))){
                                                        Log.d("-------------",a.child("mesaj").getValue(String.class));
                                                        db.child("users").child(snap.child(user.getUid()).getValue(String.class)).addListenerForSingleValueEvent(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(@NonNull DataSnapshot snapb) {
                                                                mesajbildirim.setUseruserId(snapb.getKey());
                                                                mesajbildirim.setUserIsim(snapb.child("ad").getValue(String.class));
                                                                mesajbildirim.setUserSoyisim(snapb.child("soyad").getValue(String.class));
                                                                mesajbildirim.setUserImageurl(snapb.child("image").getValue(String.class));
                                                                mesajbildirim.setUserZaman(a.child("timestamp").getValue(Long.class));
                                                                mesajbildirim.setUserMail("yenimesaj");
                                                                adapter.addistekItem(mesajbildirim);
                                                                if(adapter.getItemCount()==0){
                                                                    tvbildirim.setVisibility(View.VISIBLE);
                                                                }else {
                                                                    tvbildirim.setVisibility(View.INVISIBLE);
                                                                }
                                                            }
                                                            @Override
                                                            public void onCancelled(@NonNull DatabaseError error) {

                                                            }
                                                        });
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




        return vd;
    }
}