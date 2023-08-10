package com.example.application.models;

import android.content.Context;
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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class istekAdapter extends RecyclerView.Adapter<istekAdapter.istekViewHolder> {

    private static List<DbUser> dbUsers;
    static FirebaseAuth auth=FirebaseAuth.getInstance();
    static FirebaseUser currentUser=auth.getCurrentUser();
    static DatabaseReference userdb= FirebaseDatabase.getInstance().getReference("users");

    public istekAdapter() {dbUsers=new ArrayList<>();}

    public void addistekItem(DbUser user){
        dbUsers.add(user);
        notifyDataSetChanged();
    }

    public void remove(DbUser user){
        dbUsers.remove(user);
        notifyDataSetChanged();
    }

    public void clear(){dbUsers.clear();}


    @NonNull
    @Override
    public istekAdapter.istekViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.istekitem,parent,false);
        return new istekAdapter.istekViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull istekAdapter.istekViewHolder holder, int position){
        DbUser user=dbUsers.get(position);
        holder.bind(user);
    }

    @Override
    public int getItemCount(){return dbUsers.size();}

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
            Context context=istek_ad.getContext();
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
                                            Toast.makeText(itemView.getContext(), user.getUserIsim()+" ile arkadaş oldunuz",Toast.LENGTH_LONG).show();
                                            istek_kbl.setClickable(false);

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
                            Toast.makeText(itemView.getContext(), user.getUserIsim()+"'in arkadaşlık isteğini reddettiniz",Toast.LENGTH_LONG).show();

                        }
                    });

                }
            });



        }


    }



}


