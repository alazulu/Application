package com.example.application.models;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.application.R;

import java.util.List;

public class iletisimAdapter extends RecyclerView.Adapter<iletisimAdapter.iletisimViewHolder> {

    private List<iletisimItem> iletisimList;
    private Context context;

    public iletisimAdapter(Context context,List<iletisimItem> iletisimList) {
        this.iletisimList = iletisimList;
        this.context=context;
    }

    @NonNull
    @Override
    public iletisimViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.iletisimitem, parent, false);
        return new iletisimViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull iletisimViewHolder holder, @SuppressLint("RecyclerView") int position) {
        iletisimItem item = iletisimList.get(position);
        holder.tvtitle.setText(item.getilTitle());
        holder.tvdeger.setText(item.getilDeger());
        holder.iviletisim.setImageResource(item.getilDrawable());
        holder.iletisimitem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(position==0){
                    Uri location=Uri.parse("geo:38.34043757328855, 38.43520412501449?z=15");
                    Intent mapIntent=new Intent(Intent.ACTION_VIEW,location);
                    context.startActivity(mapIntent);
                } else if (position==1) {
                    Uri mail=Uri.parse("mailto:ays@ayssoft.com");
                    Intent mailIntent=new Intent(Intent.ACTION_SENDTO);
                    mailIntent.setData(mail);
                    context.startActivity(mailIntent);
                } else if (position==2) {
                    Uri number=Uri.parse("tel:08502977638");
                    Intent callIntent=new Intent(Intent.ACTION_DIAL,number);
                    context.startActivity(callIntent);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return iletisimList.size();
    }

    public class iletisimViewHolder extends RecyclerView.ViewHolder {
        public TextView tvtitle;
        public TextView tvdeger;
        public ImageView iviletisim;
        public ConstraintLayout iletisimitem;

        public iletisimViewHolder(@NonNull View itemView) {
            super(itemView);
            tvtitle = itemView.findViewById(R.id.tvtitle);
            tvdeger = itemView.findViewById(R.id.tvdeger);
            iviletisim = itemView.findViewById(R.id.iviletisim);
            iletisimitem=itemView.findViewById(R.id.itemiletisim);

        }
    }





}
