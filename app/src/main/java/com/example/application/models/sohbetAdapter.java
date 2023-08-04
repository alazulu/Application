package com.example.application.models;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.application.R;
import com.example.application.mesajActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class sohbetAdapter extends RecyclerView.Adapter<sohbetAdapter.ViewHolder> {

    private List<DbUser> sUsers;
    static FirebaseAuth auth = FirebaseAuth.getInstance();
    static FirebaseUser currentUser = auth.getCurrentUser();
    static DatabaseReference db = FirebaseDatabase.getInstance().getReference("mesajlar");



    public sohbetAdapter() {
        sUsers = new ArrayList<>();

    }

    public void addsohbetItem(DbUser sUser) {
        sUsers.add(sUser);
        notifyDataSetChanged();


    }
    public void clear(){
        sUsers.clear();
    }

    @Override
    public int getItemCount() {
        return sUsers.size();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sohbetitem, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull sohbetAdapter.ViewHolder holder, int position) {
        DbUser sUser= sUsers.get(position);
        holder.bind(sUser);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView sohbet_ad, sohbet_time,sohbet_last;
        private ImageView sohbet_iv;
        private Button sohbet_btn;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            sohbet_btn = itemView.findViewById(R.id.btn_sohbet);
            sohbet_ad = itemView.findViewById(R.id.sohbet_ad);
            sohbet_iv = itemView.findViewById(R.id.iv_sohbet);
            sohbet_time = itemView.findViewById(R.id.sohbet_time);
            sohbet_last=itemView.findViewById(R.id.sohbet_last);

        }

        public void bind(DbUser sUser) {
            sohbet_ad.setText(sUser.getUserIsim() + " " + sUser.getUserSoyisim());
            String url = sUser.getUserImageurl();
            Context context = sohbet_iv.getContext();
            PicassoCache.getPicassoInstance(context).load(url).error(R.drawable.error).resize(200, 200).into(sohbet_iv);
            Log.d("---------",sUser.getUserMail());
            db.child(sUser.getUserMail()).child("mesajlar").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot i:snapshot.getChildren()){




                    }


                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


            sohbet_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, mesajActivity.class);
                    intent.putExtra("user", (Serializable) sUser);
                    context.startActivity(intent);

                }
            });


        }


    }











}
