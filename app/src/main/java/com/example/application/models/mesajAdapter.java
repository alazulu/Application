package com.example.application.models;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.application.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class mesajAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<DbMesaj> messages;
    static FirebaseAuth auth = FirebaseAuth.getInstance();
    static FirebaseUser currentUser = auth.getCurrentUser();
    private static final int VIEW_TYPE_SENDER = 1;
    private static final int VIEW_TYPE_RECEIVER = 2;

    public mesajAdapter() {
        messages = new ArrayList<>();

    }

    public void addmesajItem(DbMesaj message) {
        messages.add(message);
        notifyDataSetChanged();

    }
    public void clear(){
        messages.clear();
    }

    @Override
    public int getItemViewType(int position) {
        DbMesaj message = messages.get(position);
        if (message.getGonderen().equals(currentUser.getUid())) {
            return VIEW_TYPE_SENDER;
        } else {
            return VIEW_TYPE_RECEIVER;
        }
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_SENDER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mesajitem, parent, false);
            return new SenderMessageViewHolder(view);
        } else if (viewType == VIEW_TYPE_RECEIVER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mesajitem2, parent, false);
            return new ReceiverMessageViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        DbMesaj message= messages.get(position);
        if (holder instanceof SenderMessageViewHolder){
            ((SenderMessageViewHolder) holder).bind(message);
        }else ((ReceiverMessageViewHolder) holder).bind(message);
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public class SenderMessageViewHolder extends RecyclerView.ViewHolder {
        private TextView tm,msj;
        private ImageView ivcheck;

        public SenderMessageViewHolder(@NonNull View itemView) {
            super(itemView);
            msj= itemView.findViewById(R.id.tvitemmesaj);
            tm=itemView.findViewById(R.id.tvitemzaman);
            ivcheck=itemView.findViewById(R.id.ivcheck);
        }

        public void bind(DbMesaj message) {
            msj.setText((String) message.getMesaj());
            String time=cm.simpledateformat(message.getTimestamp());
            tm.setText(time);
            Boolean okundu= message.getOkundu();
            if (okundu==null || okundu){
                ivcheck.setColorFilter(Color.rgb(239,127,30));
            }else {
                ivcheck.setColorFilter(Color.rgb(187,187,187));
            }

        }
    }

    public class ReceiverMessageViewHolder extends RecyclerView.ViewHolder {
        private TextView tm2,msj2;

        public ReceiverMessageViewHolder(@NonNull View itemView) {
            super(itemView);
            msj2 = itemView.findViewById(R.id.tvitemmesaj2);
            tm2=itemView.findViewById(R.id.tvitemzaman2);
        }

        public void bind(DbMesaj message) {
            msj2.setText((String) message.getMesaj());
            String time=cm.simpledateformat(message.getTimestamp());
            tm2.setText(time);
        }
    }
}