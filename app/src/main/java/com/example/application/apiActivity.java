package com.example.application;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;

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


public class apiActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    NewsAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_api);


        recyclerView=findViewById(R.id.rcview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

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



                    List<String> data = new ArrayList<>();
                    List<String> data1 = new ArrayList<>();
                    List<String> imageurls = new ArrayList<>();

                    for (int i = 0; i < news.getTotalResults(); i++) {
                        data.add(news.getArticles().get(i).getTitle().toString());
                        data1.add(news.getArticles().get(i).getContent().toString());
                        imageurls.add(news.getArticles().get(i).getUrlToImage().toString());
                    }

                    List<Bitmap> images = new ArrayList<>();

                    for(String url: imageurls){
                        try {
                            Bitmap bitmap = Picasso.get().load(url).resize(100, 100).get();
                            if (bitmap != null) {
                                images.add(bitmap);
                            } else {
                                // Görüntü yüklenemediyse, hata durumunu yönetin
                                // Örneğin, varsayılan bir yer tutucu görüntü ekleyebilirsiniz
                                Bitmap placeholder = BitmapFactory.decodeResource(getResources(), R.drawable.error);
                                images.add(placeholder);
                            }

                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                    Log.d("data",String.valueOf(data.size()));
                    Log.d("data1",String.valueOf(data1.size()));
                    Log.d("image",String.valueOf(images.size()));


                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter=new NewsAdapter(data,data1,images);
                            recyclerView.setAdapter(adapter);
                        }
                    });

                    // Verileri işleyin
                } else {
                    // İstek başarısız oldu
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                // İstek başarısız oldu
            }
        });


    }


}