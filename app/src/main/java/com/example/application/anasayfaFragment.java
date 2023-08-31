package com.example.application;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.application.models.News;
import com.example.application.models.NewsCheck;
import com.example.application.models.NewsItem;
import com.example.application.models.ViewPagerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class anasayfaFragment extends Fragment {

    private ViewPager2 viewPager;
    private ViewPagerAdapter viewPagerAdapter;
    private FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
    private DatabaseReference db= FirebaseDatabase.getInstance().getReference("favoriler").child(user.getUid());
    private List<String> itemlist=new ArrayList<>();
    private List<String> fkeys=new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View va=inflater.inflate(R.layout.fragment_anasayfa, container, false);
            viewPager=va.findViewById(R.id.viewpager);

            viewPagerAdapter=new ViewPagerAdapter();
            viewPager.setAdapter(viewPagerAdapter);




        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot b:snapshot.getChildren()){
                    itemlist.add(b.child("itemTitle").getValue(String.class));
                    fkeys.add(b.getKey());
                }

                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder().url("https://newsapi.org/v2/everything?q=spa&apiKey=f76b5b9e663f47549f8fbbbbbcda027a").build();

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
                                item.setItemImageurl(imageurl);


                                NewsCheck cItem=new NewsCheck();
                                if(itemlist.contains(item.getItemTitle())){
                                    cItem.setFkey(fkeys.get(itemlist.indexOf(item.getItemTitle())));
                                    cItem.setIsSwitchOn(true);
                                }else {
                                    cItem.setFkey("");
                                    cItem.setIsSwitchOn(false);
                                }
                                Activity activity = getActivity();
                                if (activity != null) {
                                    activity.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            viewPagerAdapter.addPagerAdapter(item,cItem);
                                        }
                                    });
                                }


                            }

                        }else {
                            // İstek başarısız oldu
                        }
                    }

                    @Override
                    public void onFailure(Call call, IOException e) {
                        // İstek başarısız oldu
                        Toast.makeText(getContext(),e.toString(),Toast.LENGTH_SHORT).show();
                    }
                });




            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return va;
    }




}