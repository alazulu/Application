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

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url("https://newsapi.org/v2/everything?q=bitcoin&apiKey=f76b5b9e663f47549f8fbbbbbcda027a").build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responsedata;
                    responsedata = response.body().string();

                    Gson gson=new Gson();
                    News news=gson.fromJson(responsedata, News.class);

                    List<String> data = new ArrayList<>();
                    List<String> data1 = new ArrayList<>();
                    List<String> imageurls = new ArrayList<>();
                    List<String> url1 = new ArrayList<>();

                    for (int i = 0; i < news.getArticles().size(); i++) {
                        data.add(news.getArticles().get(i).getTitle().toString());
                        if (news.getArticles().get(i).getUrlToImage()==null) {
                            imageurls.add("https://sezeromer.com/wp-content/uploads/2018/04/error.jpg");
                        } else {
                            imageurls.add(news.getArticles().get(i).getUrlToImage().toString());
                        }

                        data1.add(news.getArticles().get(i).getContent().toString());
                        if(news.getArticles().get(i).getUrl().toString()!=null) {
                            url1.add(news.getArticles().get(i).getUrl().toString());
                        }else {
                            url1.add("https://sezeromer.com/wp-content/uploads/2018/04/error.jpg");
                        }
                    }

                    List<Bitmap> images = new ArrayList<>();

                    for(String url: imageurls){
                        try {
                            if (url != null) {
                                Bitmap bitmap = Picasso.get().load(url).resize(100, 100).get();
                                images.add(bitmap);
                            } else {
                                Bitmap placeholder = BitmapFactory.decodeResource(getResources(), R.drawable.error);
                                images.add(placeholder);
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                            Bitmap placeholder = BitmapFactory.decodeResource(getResources(), R.drawable.error);
                            images.add(placeholder);
                        }
                    }
                    Log.d("data",String.valueOf(data.size()));
                    Log.d("data1",String.valueOf(data1.size()));
                    Log.d("images",String.valueOf(images.size()));
                    Log.d("url",String.valueOf(news.getArticles().size()));

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter = new NewsAdapter(data, data1, images,url1);
                            rcduyuruView.setAdapter(adapter);

                        }
                    });

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