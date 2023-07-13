package com.example.application;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import androidx.appcompat.widget.Toolbar ;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import java.util.Objects;


public class HomeActivity extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager2 viewPager;
    ViewPagerFragmentAdapter adapter;
    FirebaseAuth auth;
    Toolbar toolbar;

    private String[] title=new String[]{"Anasayfa","Duyurular","Profil"};
    private int[] icon={R.drawable.home_24,R.drawable.notifications_24,R.drawable.person_24};

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        viewPager = findViewById(R.id.view_pager);
        tabLayout = findViewById(R.id.tab_layout);
        adapter=new ViewPagerFragmentAdapter(this);
        viewPager.setAdapter(adapter);
        auth=FirebaseAuth.getInstance();
        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            tab.setText(title[position]);
            tab.setIcon(icon[position]);

        }).attach();
        viewPager.setCurrentItem(0, false);


    }

    private class ViewPagerFragmentAdapter extends FragmentStateAdapter {

        public ViewPagerFragmentAdapter(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            switch (position) {
                case 0:
                    return new anasayfaFragment();
                case 1:
                    return new duyuruFragment();
                case 2:
                    return new profilFragment();
            }
            return new anasayfaFragment();
        }


        @Override
        public int getItemCount() {
            return title.length;
        }



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.homemenu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        if(item.getItemId()==R.id.cikis){
            auth.signOut();
            startActivity(new Intent(HomeActivity.this,LoginActivity.class));
            HomeActivity.this.finish();

        }
        else if (item.getItemId()==R.id.ayarlar) {

        }
        else {
            super.onOptionsItemSelected(item);
        }

        return super.onOptionsItemSelected(item);
    }


}