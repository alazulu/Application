package com.example.application.models;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.application.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DbUserAdapter extends RecyclerView.Adapter<DbUserAdapter.DbUserViewHolder> {

    private List<DbUser> dbUsers;
    static FirebaseAuth auth=FirebaseAuth.getInstance();
    static FirebaseUser currentUser =auth.getCurrentUser();
    static DatabaseReference userdb= FirebaseDatabase.getInstance().getReference("users");

    public DbUserAdapter() {dbUsers=new ArrayList<>();}

    public void addDbItem(DbUser user){
        dbUsers.add(user);
        notifyDataSetChanged();
    }


    public void clear(){
        dbUsers.clear();
    }

    @NonNull
    @Override
    public DbUserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.searchitem,parent,false);
        return new DbUserViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull DbUserViewHolder holder,int position){
        DbUser user=dbUsers.get(position);
        holder.bind(user);
    }

    @Override
    public int getItemCount(){return dbUsers.size();}

    public static class DbUserViewHolder extends RecyclerView.ViewHolder{
        private TextView ara_ad,ara_time;
        private ImageView ara_iv;
        private Button ara_btn;

        public DbUserViewHolder(@NonNull View itemView){
            super(itemView);
            ara_btn=itemView.findViewById(R.id.btn_ekle);
            ara_ad=itemView.findViewById(R.id.ara_ad);
            ara_iv=itemView.findViewById(R.id.iv_ara);
            ara_time=itemView.findViewById(R.id.ara_time);

        }

        public void bind(DbUser user){
            ara_ad.setText(user.getUserIsim()+" "+user.getUserSoyisim());
            String url= user.getUserImageurl();
            Context c=ara_iv.getContext();
            PicassoCache.getPicassoInstance(c).load(url).error(R.drawable.error).resize(200,200).into(ara_iv);
            String uid= currentUser.getUid();
            String userid= user.getUseruserId();
            if (user.getUserIstek()==0){
                ara_btn.setText(R.string.toast36);
                ara_btn.setEnabled(false);
                ara_btn.setClickable(false);
            }


            if (user.getUserZaman()!=946684800){
                ara_time.setText(cm.simpledateformat(user.getUserZaman()));
            }

            ara_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    HashMap<String,Object> istek=new HashMap<>();
                    istek.put("durum",0);
                    istek.put("zaman",System.currentTimeMillis());

                    userdb.child(userid).child("istek").child(uid).setValue(istek).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            ara_btn.setText(R.string.toast36);
                            ara_btn.setEnabled(false);
                            ara_time.setText(cm.simpledateformat(System.currentTimeMillis()));
                        }
                    });
                }
            });
        }
    }
}
