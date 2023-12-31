package com.example.application.models;

import android.content.Context;
import android.content.Intent;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class arkadasAdapter extends RecyclerView.Adapter<arkadasAdapter.arkadasViewHolder> {

    private List<DbUser> dbUsers;
    static FirebaseAuth auth = FirebaseAuth.getInstance();
    static FirebaseUser currentUser = auth.getCurrentUser();
    static DatabaseReference userdb = FirebaseDatabase.getInstance().getReference("users");

    public arkadasAdapter() {
        dbUsers = new ArrayList<>();
    }

    public void addDbItem(DbUser user) {
        dbUsers.add(user);
        notifyDataSetChanged();
    }
    public void clear(){dbUsers.clear();}

    @NonNull
    @Override
    public arkadasAdapter.arkadasViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.searchitem, parent, false);
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
        Context context;

        public arkadasViewHolder(@NonNull View itemView) {
            super(itemView);
            context=itemView.getContext();
            ara_btn = itemView.findViewById(R.id.btn_ekle);
            ara_ad = itemView.findViewById(R.id.ara_ad);
            ara_iv = itemView.findViewById(R.id.iv_ara);
            ara_time = itemView.findViewById(R.id.ara_time);
            ara_btn.setText(context.getString(R.string.msjat));
            context=itemView.getContext();

        }

        public void bind(DbUser user) {
            ara_ad.setText(user.getUserIsim() + " " + user.getUserSoyisim());
            String url = user.getUserImageurl();
            Context c = ara_iv.getContext();
            PicassoCache.getPicassoInstance(c).load(url).error(R.drawable.error).resize(200, 200).into(ara_iv);



            ara_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, mesajActivity.class);
                    intent.putExtra("user", user.getUseruserId().toString());
                    context.startActivity(intent);

                }
            });


        }


    }
}