package com.example.application.models;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.application.R;
import com.example.application.mesajActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class bildirimAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static List<DbUser> dbUsers;
    static FirebaseAuth auth=FirebaseAuth.getInstance();
    static FirebaseUser currentUser=auth.getCurrentUser();
    static DatabaseReference userdb= FirebaseDatabase.getInstance().getReference("users");
    private static final int VIEW_TYPE_Mesaj = 1;
    private static final int VIEW_TYPE_Istek = 2;

    public bildirimAdapter() {dbUsers=new ArrayList<>();}

    public void addistekItem(DbUser user){
        dbUsers.add(user);
        notifyDataSetChanged();
    }

    public void remove(DbUser user){
        dbUsers.remove(user);
        notifyDataSetChanged();
    }

    public void clear(){dbUsers.clear();}


    @Override
    public int getItemViewType(int position) {
        DbUser user = dbUsers.get(position);
        if (user.getUserMail().equals("yenimesaj")) {
            return VIEW_TYPE_Mesaj;
        } else {
            return VIEW_TYPE_Istek;
        }
    }



    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_Mesaj) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bildirimitem2, parent, false);
            return new bildirimAdapter.mesajViewHolder(view);
        } else if (viewType == VIEW_TYPE_Istek) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bildirimitem, parent, false);
            return new bildirimAdapter.istekViewHolder(view);
        }
        return null;
    }



    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position){
        DbUser user=dbUsers.get(position);
        if (holder instanceof bildirimAdapter.mesajViewHolder){
            ((bildirimAdapter.mesajViewHolder) holder).bind(user);
        }else ((bildirimAdapter.istekViewHolder) holder).bind(user);
    }

    @Override
    public int getItemCount(){return dbUsers.size();}

    public class mesajViewHolder extends RecyclerView.ViewHolder{
        private ImageView ivbd;
        private TextView tvbd;
        private Button btnbd;
        Context context;

        public mesajViewHolder(@NonNull View itemView){
            super(itemView);
            tvbd=itemView.findViewById(R.id.tvbd);
            ivbd=itemView.findViewById(R.id.ivbd);
            btnbd=itemView.findViewById(R.id.btnbd);
            context=itemView.getContext();
        }
        public void bind(DbUser user){
            tvbd.setText(user.getUserIsim()+" "+user.getUserSoyisim());
            String url= user.getUserImageurl();
            Context c=ivbd.getContext();
            PicassoCache.getPicassoInstance(c).load(url).error(R.drawable.error).resize(200,200).into(ivbd);

            btnbd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dbUsers.remove(user);
                    Intent intent = new Intent(context, mesajActivity.class);
                    intent.putExtra("user", user.getUseruserId().toString());
                    context.startActivity(intent);
                }
            });


        }

    }


    public class istekViewHolder extends RecyclerView.ViewHolder{
        private TextView istek_ad;
        private ImageView istek_iv;
        private Button istek_kbl,istek_red;

        public istekViewHolder(@NonNull View itemView){
            super(itemView);
            istek_ad=itemView.findViewById(R.id.tvistek);
            istek_iv=itemView.findViewById(R.id.ivistek);
            istek_kbl=itemView.findViewById(R.id.btnistek1);
            istek_red=itemView.findViewById(R.id.btnistek2);
        }

        public void bind(DbUser user){
            istek_ad.setText(user.getUserIsim()+" "+user.getUserSoyisim());
            String url= user.getUserImageurl();
            Context context=istek_iv.getContext();
            PicassoCache.getPicassoInstance(context).load(url).error(R.drawable.error).resize(200,200).into(istek_iv);

            istek_kbl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    userdb.child(currentUser.getUid()).child("arkadaslar").child(user.getUseruserId()).setValue(" ").addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            userdb.child(user.getUseruserId()).child("arkadaslar").child(currentUser.getUid()).setValue(" ").addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    userdb.child(currentUser.getUid()).child("istek").child(user.getUseruserId()).child("durum").setValue(2).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            dbUsers.remove(user);
                                            notifyDataSetChanged();
                                            Toast.makeText(itemView.getContext(), user.getUserIsim()+" ile arkadaş oldunuz",Toast.LENGTH_LONG).show();
                                        }
                                    });
                                }
                            });
                        }
                    });
                }
            });
            istek_red.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    userdb.child(currentUser.getUid()).child("istek").child(user.getUseruserId()).child("durum").setValue(1).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            dbUsers.remove(user);
                            notifyDataSetChanged();
                            Toast.makeText(itemView.getContext(), user.getUserIsim()+"'in arkadaşlık isteğini reddettiniz",Toast.LENGTH_LONG).show();
                        }
                    });
                }
            });
        }
    }
}
