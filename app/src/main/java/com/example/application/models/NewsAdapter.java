package com.example.application.models;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.application.R;
import java.util.List;

public class NewsAdapter  extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {

    private List<String> data;
    private List<String> data1;
    private List<Bitmap> images;

    public NewsAdapter(List<String> data, List<String> data1, List<Bitmap> images){
        this.data=data;
        this.data1=data1;
        this.images=images;
    }

    @NonNull
    @Override
    public NewsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.itemlayout,parent,false);
        return new NewsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsAdapter.ViewHolder holder, int position) {
        String item= data.get(position);
        String item1= data1.get(position);
        Bitmap image=images.get(position);
        holder.bind(item, item1,image);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class  ViewHolder extends RecyclerView.ViewHolder{

        private TextView tvicerik;
        private TextView tvbaslik;
        private ImageView ivImage;

        public ViewHolder (@NonNull View itemView){
            super(itemView);
            tvicerik=itemView.findViewById(R.id.tvicerik);
            tvbaslik=itemView.findViewById(R.id.tvbaslik);
            ivImage=itemView.findViewById(R.id.ivimage);
        }

        public void bind(String item, String item1,Bitmap image){
            tvicerik.setText(item1);
            tvbaslik.setText(item);
            ivImage.setImageBitmap(image);
        }

    }
}
