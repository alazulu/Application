package com.example.application.models;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.application.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class mesajAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<HashMap<String,Object>> messages;
    static FirebaseAuth auth = FirebaseAuth.getInstance();
    static FirebaseUser currentUser = auth.getCurrentUser();
    static DatabaseReference userdb = FirebaseDatabase.getInstance().getReference("users");
    private static final int VIEW_TYPE_SENDER = 1;
    private static final int VIEW_TYPE_RECEIVER = 2;

    public mesajAdapter() {
        messages = new ArrayList<>();

    }

    public void addmesajItem(HashMap<String,Object> message) {
        messages.add(message);
        notifyDataSetChanged();

    }
    public void clear(){
        messages.clear();
    }

    @Override
    public int getItemViewType(int position) {
        HashMap<String,Object> message = messages.get(position);
        if (message.get("gonderen").equals(currentUser.getUid())) {
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
        HashMap<String,Object> message= messages.get(position);
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

        public SenderMessageViewHolder(@NonNull View itemView) {
            super(itemView);
            msj= itemView.findViewById(R.id.tvitemmesaj);
            tm=itemView.findViewById(R.id.tvitemzaman);
        }

        public void bind(HashMap<String,Object> message) {
            msj.setText((String) message.get("mesaj"));
            String time=cm.simpledateformat((Long) message.get("timestamp"));
            tm.setText(time);


        }
    }

    public class ReceiverMessageViewHolder extends RecyclerView.ViewHolder {
        private TextView tm2,msj2;


        public ReceiverMessageViewHolder(@NonNull View itemView) {
            super(itemView);
            msj2 = itemView.findViewById(R.id.tvitemmesaj2);
            tm2=itemView.findViewById(R.id.tvitemzaman2);
        }

        public void bind(HashMap<String,Object> message) {
            msj2.setText((String) message.get("mesaj"));
            String time=cm.simpledateformat((Long) message.get("timestamp"));
            tm2.setText(time);
        }
    }
}