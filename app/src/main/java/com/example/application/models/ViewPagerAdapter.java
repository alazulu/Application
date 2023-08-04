package com.example.application.models;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.application.R;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerAdapter extends RecyclerView.Adapter<ViewPagerAdapter.PagerViewHolder> {

    private List<NewsItem> Items;

    public ViewPagerAdapter(){
        Items=new ArrayList<>();
    }

    public void addPagerAdapter(NewsItem item) {
        Items.add(item);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PagerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewpageritem, parent, false);
        return new PagerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PagerViewHolder holder, int position) {
        NewsItem item= Items.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return Items.size();
    }



    public class PagerViewHolder extends RecyclerView.ViewHolder {

        private TextView tvicerik;
        private TextView tvbaslik;
        private ImageView ivImage;
        private Button btnpager;


        public PagerViewHolder(@NonNull View itemView){
            super(itemView);
            tvicerik=itemView.findViewById(R.id.tvpager2);
            tvbaslik=itemView.findViewById(R.id.tvpager);
            ivImage=itemView.findViewById(R.id.ivpager);
            btnpager=itemView.findViewById(R.id.btnpager);
        }

        public void bind(NewsItem item){
            tvicerik.setText(item.getItemContent().toString());
            tvbaslik.setText(item.getItemTitle().toString());
            String imageurl=item.getItemImageurl().toString();
            Context context=ivImage.getContext();
            PicassoCache.getPicassoInstance(context).load(imageurl).error(R.drawable.error).resize(300,300).into(ivImage);
            String website=item.getItemUrl().toString();
            btnpager.setOnClickListener(new View.OnClickListener() {
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
