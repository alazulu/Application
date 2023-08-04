package com.example.application.models;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.application.IletisimActivity;
import com.example.application.R;
import com.example.application.mesajActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class arkadasAdapter extends RecyclerView.Adapter<arkadasAdapter.arkadasViewHolder> {

    private List<DbUser> dbUsers;
    private Context context;
    static FirebaseAuth auth = FirebaseAuth.getInstance();
    static FirebaseUser currentUser = auth.getCurrentUser();
    static DatabaseReference userdb = FirebaseDatabase.getInstance().getReference("users");

    public arkadasAdapter(Context context) {
        dbUsers = new ArrayList<>();
        this.context = context;
    }

    public void addDbItem(DbUser user) {
        dbUsers.add(user);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public arkadasAdapter.arkadasViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.araitem, parent, false);
        return new arkadasAdapter.arkadasViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull arkadasAdapter.arkadasViewHolder holder, int position) {
        DbUser user = dbUsers.get(position);
        holder.bind(user);
    }

    @Override
    public int getItemCount() {
        return dbUsers.size();
    }

    public static class arkadasViewHolder extends RecyclerView.ViewHolder {
        private TextView ara_ad, ara_time;
        private ImageView ara_iv;
        private Button ara_btn;

        public arkadasViewHolder(@NonNull View itemView) {
            super(itemView);
            ara_btn = itemView.findViewById(R.id.btn_ekle);
            ara_ad = itemView.findViewById(R.id.ara_ad);
            ara_iv = itemView.findViewById(R.id.iv_ara);
            ara_time = itemView.findViewById(R.id.ara_time);
            ara_btn.setText("Mesaj at");

        }

        public void bind(DbUser user) {
            ara_ad.setText(user.getUserIsim() + " " + user.getUserSoyisim());
            String url = user.getUserImageurl();
            Context context = ara_iv.getContext();
            PicassoCache.getPicassoInstance(context).load(url).error(R.drawable.error).resize(200, 200).into(ara_iv);



            ara_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, mesajActivity.class);
                    intent.putExtra("user", (Serializable) user);
                    context.startActivity(intent);

                }
            });


        }


    }
}