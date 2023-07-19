package com.example.application;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;


import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;



public class HomeActivity extends AppCompatActivity {



    FirebaseAuth auth;
    BottomNavigationView bnv;
    Toolbar toolbar;
    Fragment aktifFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        bnv=findViewById(R.id.bottom_navigation);
        auth=FirebaseAuth.getInstance();
        aktifFragment=new anasayfaFragment();

        getSupportFragmentManager().beginTransaction().replace(R.id.home_container,aktifFragment).commit();

        bnv.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fr=null;
                int id= item.getItemId();

                if (id==R.id.nav_anasayfa){
                    fr=new anasayfaFragment();
                } else if (id==R.id.nav_duyuru) {
                    fr=new duyuruFragment();
                } else if (id==R.id.nav_profil) {
                    fr=new profilFragment();
                }

                if(fr!=null) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.home_container, fr).commit();
                aktifFragment=fr;
                }
                return true;
            }
        });

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