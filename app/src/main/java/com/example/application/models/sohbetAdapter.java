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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class sohbetAdapter extends RecyclerView.Adapter<sohbetAdapter.ViewHolder> {

    private List<DbUser> sUsers;
    static DatabaseReference db = FirebaseDatabase.getInstance().getReference("mesajlar");

    public sohbetAdapter() {
        sUsers = new ArrayList<>();

    }

    public void addsohbetItem(DbUser sUser) {
        sUsers.add(sUser);
        notifyDataSetChanged();
        sUsers.sort(new Comparator<DbUser>() {
            @Override
            public int compare(DbUser o1, DbUser o2) {
                Long oTime1 = o1.getUserZaman();
                Long oTime2 = o2.getUserZaman();
                return oTime2.compareTo(oTime1);
            }
        });

    }
    public void clear(){
        sUsers.clear();
        notifyDataSetChanged();
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
        Context context;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            sohbet_btn = itemView.findViewById(R.id.btn_sohbet);
            sohbet_ad = itemView.findViewById(R.id.sohbet_ad);
            sohbet_iv = itemView.findViewById(R.id.iv_sohbet);
            sohbet_time = itemView.findViewById(R.id.sohbet_time);
            sohbet_last=itemView.findViewById(R.id.sohbet_last);
            context=itemView.getContext();

        }

        public void bind(DbUser sUser) {
            sohbet_ad.setText(sUser.getUserIsim() + " " + sUser.getUserSoyisim());
            String url = sUser.getUserImageurl();
            Context c = sohbet_iv.getContext();
            PicassoCache.getPicassoInstance(c).load(url).error(R.drawable.error).resize(200, 200).into(sohbet_iv);

            if (sUser.getUserIstek()==0){
                sohbet_last.setText(sUser.getUserTel());
                String time=cm.simpledateformat(sUser.getUserZaman());
                sohbet_time.setText(time);
            }else {
                sohbet_last.setText("Siz: "+sUser.getUserTel());
                String time=cm.simpledateformat(sUser.getUserZaman());
                sohbet_time.setText(time);
            }


            sohbet_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, mesajActivity.class);
                    intent.putExtra("user", sUser.getUseruserId().toString());
                    context.startActivity(intent);

                }
            });


        }


    }

}
