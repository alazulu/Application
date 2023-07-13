package com.example.application;

import androidx.appcompat.app.AppCompatActivity;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.application.models.Articles;
import com.example.application.models.News;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;


public class apiActivity extends AppCompatActivity {


    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_api);
        tv=findViewById(R.id.textVw);

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url("https://newsapi.org/v2/everything?q=Hugh_Howey&apiKey=f76b5b9e663f47549f8fbbbbbcda027a").build();


        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseData = response.body().string();

                    Gson gson= new Gson();
                    News haber=gson.fromJson(responseData, News.class);
                    //String artic=gson.toJson(haber.getArticles());
                    //Articles makale=gson.fromJson(artic,Articles.class);
                    String al=haber.getArticles().get(1).getUrl().toString();
                    Log.d("articles",al);




                    // Verileri işleyin
                    /*try {
                        JSONObject jsonObject=new JSONObject(responseData);
                        int result=jsonObject.getInt("totalResults");
                        JSONArray makaleler=(JSONArray) jsonObject.get("articles");

                        for(int i = 0; i < result; i++) {
                            JSONObject makale=null;
                            makale=makaleler.getJSONObject(i);
                            Log.d("articles",makale.get("url").toString());

                        }
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }*/

                } else {
                    Toast.makeText(apiActivity.this,"else",Toast.LENGTH_LONG).show();
                    // İstek başarısız oldu
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                // İstek başarısız oldu
                Toast.makeText(apiActivity.this,"onfailure",Toast.LENGTH_LONG).show();
            }
        });


    }





}