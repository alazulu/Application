package com.example.application.models;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.application.R;

import java.util.ArrayList;
import java.util.List;

public class NewsAdapter  extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {

    private List<NewsItem> newsItems;
    private int visibleItemCountThreshold = 5;

    public NewsAdapter(){
        newsItems=new ArrayList<>();
    }

    public void addNewsItem(NewsItem item){
        newsItems.add(item);
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.layoutitem,parent,false);
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        NewsItem item= newsItems.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return newsItems.size();
    }

    public static class  NewsViewHolder extends RecyclerView.ViewHolder{

        private TextView tvicerik;
        private TextView tvbaslik;
        private ImageView ivImage;
        private LinearLayout itemlay;

        public NewsViewHolder (@NonNull View itemView) {
            super(itemView);
            tvicerik = itemView.findViewById(R.id.tvicerik);
            tvbaslik = itemView.findViewById(R.id.tvbaslik);
            ivImage = itemView.findViewById(R.id.ivimage);
            itemlay = itemView.findViewById(R.id.lnitemlayout);

        }


        public void bind(NewsItem item){
            tvicerik.setText(item.getItemContent().toString());
            tvbaslik.setText(item.getItemTitle().toString());
            String imageurl=item.getItemImageurl();
            Context context=ivImage.getContext();
            PicassoCache.getPicassoInstance(context).load(imageurl).error(R.drawable.error).resize(200,200).into(ivImage);

            String website=item.getItemUrl().toString();
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
