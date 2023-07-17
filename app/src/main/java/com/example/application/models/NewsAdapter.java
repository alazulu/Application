package com.example.application.models;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.application.R;
import java.util.List;

public class NewsAdapter  extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {

    private List<String> data;
    private List<String> data1;
    private List<Bitmap> images;
    private List<String> url1;

    public NewsAdapter(List<String> data, List<String> data1, List<Bitmap> images,List<String> url1){
        this.data=data;
        this.data1=data1;
        this.images=images;
        this.url1=url1;
    }

    @NonNull
    @Override
    public NewsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.itemlayout,parent,false);
        return new NewsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsAdapter. ViewHolder holder, int position) {
        String item= data.get(position);
        String item1= data1.get(position);
        Bitmap image=images.get(position);
        String item2=url1.get(position);
        holder.bind(item, item1,image,item2);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class  ViewHolder extends RecyclerView.ViewHolder{

        private TextView tvicerik;
        private TextView tvbaslik;
        private ImageView ivImage;
        private LinearLayout itemlay;

        @SuppressLint("ResourceType")
        public ViewHolder (@NonNull View itemView){
            super(itemView);
            tvicerik=itemView.findViewById(R.id.tvicerik);
            tvbaslik=itemView.findViewById(R.id.tvbaslik);
            ivImage=itemView.findViewById(R.id.ivimage);
            itemlay=itemView.findViewById(R.id.lnitemlayout);
        }

        public void bind(String item, String item1,Bitmap image,String item2){
            tvicerik.setText(item1);
            tvbaslik.setText(item);
            ivImage.setImageBitmap(image);
            String website=item2;
            itemlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openWebsite(v.getContext(), website);
                }
            });

        }
        private void openWebsite(Context context, String url) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            context.startActivity(intent);
        }

    }
}
