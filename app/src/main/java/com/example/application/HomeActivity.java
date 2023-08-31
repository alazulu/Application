package com.example.application;

import static com.example.application.models.cm.getLocaleSharedPreferances;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.example.application.models.cm;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;


public class HomeActivity extends AppCompatActivity {



    private FirebaseAuth auth;
    private FirebaseUser user;
    private DatabaseReference db=FirebaseDatabase.getInstance().getReference();
    private DrawerLayout drawerLayout;
    private NavigationView nv;
    private BottomNavigationView bnv;
    private Toolbar toolbar;
    private Fragment aktifFragment;
    private TextView tvheader, tvheader1;
    private ImageView ivheader;
    private MaterialButtonToggleGroup toggleGroup;
    private Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        toggleGroup=findViewById(R.id.btntoggle);
        drawerLayout=findViewById(R.id.drawerLayout);
        nv=findViewById(R.id.modal_navigation);
        bnv=findViewById(R.id.bottom_navigation);
        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
        View header=nv.getHeaderView(0);
        tvheader=header.findViewById(R.id.tvheader);
        tvheader1=header.findViewById(R.id.tvheader1);
        ivheader=header.findViewById(R.id.ivheader);
        aktifFragment=new anasayfaFragment();
        context=this;

        String uid= user.getUid();
        SharedPreferences sharedPref = getSharedPreferences("locale", Context.MODE_PRIVATE);
        String token=sharedPref.getString("fcmtoken", "");
        storeFCMToken(uid,token);


        if (getLocaleSharedPreferances(HomeActivity.this).equals("en")){
            toggleGroup.check(R.id.btneng);
        }else {
            toggleGroup.check(R.id.btntr);
        }
        db.child("users").child(user.getUid()).child("image").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Picasso.get().load(snapshot.getValue(String.class)).resize(100, 100).into(ivheader);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                ivheader.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.profil));
            }
        });



        tvheader.setText(user.getDisplayName());
        tvheader1.setText(user.getEmail());


        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.menu_24);

        toolbar.setNavigationOnClickListener(view -> drawerLayout.openDrawer(nv));
        nv.setNavigationItemSelectedListener(item -> {
            int itemid=item.getItemId();
            if(itemid==R.id.nav_arkadas){
                startActivity(new Intent(HomeActivity.this,arkadasActivity.class));
            }else if (itemid==R.id.nav_favori) {
                startActivity(new Intent(HomeActivity.this,FavoriActivity.class));
            } else if (itemid==R.id.nav_ara) {
                startActivity(new Intent(HomeActivity.this,SearchActivity.class));
            } else if (itemid==R.id.nav_mesaj) {
                startActivity(new Intent(HomeActivity.this,sohbetlerActivity.class));
            }

            drawerLayout.closeDrawers();
            return true;
        });

        getSupportFragmentManager().beginTransaction().replace(R.id.home_container,aktifFragment).commit();

        bnv.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fr=null;
                int id= item.getItemId();

                if (id==R.id.nav_anasayfa){
                    fr=new anasayfaFragment();
                } else if (id==R.id.nav_duyuru) {
                    fr=new bildirimFragment();
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

        BadgeDrawable badge=bnv.getOrCreateBadge(R.id.nav_duyuru);
        badge.setBackgroundColor(getColor(R.color.red));
        final Boolean[] badgecontroler = {false,false};
        badge.setVisible(false);

        db.child("users").child(user.getUid()).child("istek").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d("111111111",snapshot.toString());
                for (DataSnapshot s:snapshot.getChildren()){
                    if(s.child("durum").getValue(Integer.class)==0){
                        badgecontroler[0] =true;
                    }

                }

                db.child("mesajlar").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snaps) {
                        for (DataSnapshot sn:snaps.getChildren()){
                            if(sn.child("kisiler").toString().contains(user.getUid())){
                                sn.child("mesajlar").getRef().addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snap) {
                                        for(DataSnapshot sa:snap.getChildren()){
                                            if (!sa.child("gonderen").getValue(String.class).equals(user.getUid()) && !sa.child("okundu").getValue(Boolean.class)) {
                                                badgecontroler[1] =true;
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }

                        }
                        if(badgecontroler[0] || badgecontroler[1]){
                            badge.setVisible(true);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        toggleGroup.addOnButtonCheckedListener(new MaterialButtonToggleGroup.OnButtonCheckedListener() {
            @Override
            public void onButtonChecked(MaterialButtonToggleGroup group, int checkedId, boolean isChecked) {
                // Seçilen öğenin ID'sini alın ve seçili öğenin metnini Toast ile gösterin.
                if (isChecked) {
                    if(checkedId==R.id.btneng){
                        Toast.makeText(HomeActivity.this,R.string.toast34,Toast.LENGTH_SHORT).show();
                        cm.setLocale(HomeActivity.this,"en");
                        startActivity(new Intent(HomeActivity.this,LoginActivity.class));
                        finish();
                    }else {
                        Toast.makeText(HomeActivity.this,R.string.toast34,Toast.LENGTH_SHORT).show();
                        cm.setLocale(HomeActivity.this,"tr");
                        startActivity(new Intent(HomeActivity.this,LoginActivity.class));
                        finish();
                    }
                }
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
        else if (item.getItemId()==R.id.iletisim) {
            startActivity(new Intent(HomeActivity.this, IletisimActivity.class));
        } else if (item.getItemId() == android.R.id.home) {
            drawerLayout.openDrawer(nv);
            return true;
        } else {
            super.onOptionsItemSelected(item);
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        // Eğer çekmece açıksa, geri düğmesine basınca kapat
        if (drawerLayout.isDrawerOpen(nv)) {
            drawerLayout.closeDrawers();
        } else {
            super.onBackPressed();
        }
    }

    public void storeFCMToken(String userId, String fcmToken) {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(userId);
        userRef.child("fcmToken").setValue(fcmToken);
    }




}