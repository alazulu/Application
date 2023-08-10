package com.example.application.models;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.application.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SplashAdapter  extends RecyclerView.Adapter<SplashAdapter.SplashViewHolder> {

private List<HashMap<String,Integer>> items;
private Context context;

public SplashAdapter(Context context){
        this.context=context;
        items=new ArrayList<>();
        }

public void addSplashAdapter(HashMap<String,Integer> item) {
        items.add(item);
        notifyDataSetChanged();
        }

@NonNull
@Override
public SplashViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.splashitem, parent, false);
        return new SplashViewHolder(view);
        }

@Override
public void onBindViewHolder(@NonNull SplashViewHolder holder, int position) {
        HashMap<String,Integer> item= items.get(position);
        holder.bind(item);
        }

@Override
public int getItemCount() {
        return items.size();
        }



public class SplashViewHolder extends RecyclerView.ViewHolder {

    private TextView tvsplash;
    private ImageView ivsplash;


    public SplashViewHolder(@NonNull View itemView){
        super(itemView);
        tvsplash=itemView.findViewById(R.id.tvsplash);
        ivsplash=itemView.findViewById(R.id.ivsplash);

    }

    public void bind(HashMap<String,Integer> item){
        tvsplash.setText(item.get("text"));
        Picasso.get().load(item.get("image")).into(ivsplash);
    }


}
}