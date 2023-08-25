package com.example.application;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.application.models.NewsCheck;
import com.example.application.models.NewsItem;
import com.example.application.models.begeniAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FavoriActivity extends AppCompatActivity {

    private ViewPager2 vpbegeni;
    private TextView tvbgn;
    private begeniAdapter adapter=new begeniAdapter();
    private FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
    private DatabaseReference db= FirebaseDatabase.getInstance().getReference("favoriler").child(user.getUid());
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favori);

        tvbgn=findViewById(R.id.tvbgn);
        vpbegeni=findViewById(R.id.vpbgn);
        vpbegeni.setAdapter(adapter);

        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot a:snapshot.getChildren()){
                    NewsItem item=new NewsItem();
                    NewsCheck cItem=new NewsCheck();
                    item.setItemTitle(a.child("itemTitle").getValue(String.class));
                    item.setItemContent(a.child("itemContent").getValue(String.class));
                    item.setItemUrl(a.child("itemUrl").getValue(String.class));
                    item.setItemImageurl(a.child("itemImageurl").getValue(String.class));
                    cItem.setFkey(a.getKey());
                    cItem.setIsSwitchOn(true);

                    adapter.addbegeniAdapter(item,cItem);

                    if (adapter.getItemCount()==0){
                        tvbgn.setVisibility(View.VISIBLE);
                    }else {
                        tvbgn.setVisibility(View.INVISIBLE);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(FavoriActivity.this, R.string.toast40, Toast.LENGTH_SHORT).show();
            }
        });


    }
}