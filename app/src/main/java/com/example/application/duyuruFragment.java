package com.example.application;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.application.models.News;
import com.example.application.models.NewsAdapter;
import com.example.application.models.NewsItem;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class duyuruFragment extends Fragment {

    private NewsAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View vd=inflater.inflate(R.layout.fragment_duyuru, container, false);
        RecyclerView rcduyuruView=vd.findViewById(R.id.rcviewduyuru);
        rcduyuruView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter=new NewsAdapter();
        rcduyuruView.setAdapter(adapter);



        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url("https://newsapi.org/v2/everything?q=Hugh_Howey&apiKey=f76b5b9e663f47549f8fbbbbbcda027a").build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responsedata;
                    responsedata = response.body().string();

                    Gson gson=new Gson();
                    News news=gson.fromJson(responsedata, News.class);



                    for (int i = 0; i < news.getArticles().size(); i++) {
                        NewsItem item=new NewsItem();
                        String imageurl=new String();
                        Bitmap image;

                        item.setItemTitle(news.getArticles().get(i).getTitle().toString());
                        item.setItemContent(news.getArticles().get(i).getContent().toString());
                        if(news.getArticles().get(i).getUrl().toString()!=null) {
                            item.setItemUrl(news.getArticles().get(i).getUrl().toString());
                        }else {
                            item.setItemUrl("https://sezeromer.com/wp-content/uploads/2018/04/error.jpg");
                        }
                        imageurl=news.getArticles().get(i).getUrlToImage();

                        if (news.getArticles().get(i).getUrlToImage()==null) {
                            imageurl="https://sezeromer.com/wp-content/uploads/2018/04/error.jpg";
                        } else {
                            imageurl=news.getArticles().get(i).getUrlToImage().toString();
                        }
                        try {
                            image = Picasso.get().load(imageurl).resize(100, 100).get();
                            if(image==null){
                                image = BitmapFactory.decodeResource(getResources(), R.drawable.error);
                            }
                        }catch (Exception e){
                            image = BitmapFactory.decodeResource(getResources(), R.drawable.error);
                        }
                        item.setItemImage(image);
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adapter.addNewsItem(item);
                            }
                        });

                    }

                } else {
                    // İstek başarısız oldu
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                // İstek başarısız oldu
                Toast.makeText(getContext(),e.toString(),Toast.LENGTH_SHORT).show();
            }
        });
        return vd;
    }



}