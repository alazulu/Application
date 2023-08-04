package com.example.application;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.application.models.iletisimAdapter;
import com.example.application.models.iletisimItem;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class IletisimActivity extends AppCompatActivity {


    private RecyclerView rviletisim;
    private iletisimAdapter ilAdapter;
    private List<iletisimItem> items= new ArrayList<>();
    private EditText ilname,ilmail,iltelefon;
    private TextInputEditText ilileti;
    private Button ilbtn;
    private FirebaseAuth auth;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iletisim);


        ilname=findViewById(R.id.ilname);
        ilmail=findViewById(R.id.ilmail);
        iltelefon=findViewById(R.id.iltelefon);
        ilileti=findViewById(R.id.ilileti);
        ilbtn=findViewById(R.id.ilbtn);
        rviletisim=findViewById(R.id.rviletisim);
        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
        items=new ArrayList<iletisimItem>();
        ilAdapter= new iletisimAdapter(this,items);
        rviletisim.setLayoutManager(new LinearLayoutManager(this));
        rviletisim.setAdapter(ilAdapter);

        items.add(new iletisimItem(getString(R.string.drs) ,"Malatya Teknopark A Blok Kat 1",R.drawable.location_36));
        items.add(new iletisimItem(getString(R.string.mail) ,"ays@ayssoft.com",R.drawable.email_36));
        items.add(new iletisimItem(getString(R.string.tlfn) ,"+908502977638",R.drawable.phone_36));
        ilname.setText(user.getDisplayName());
        ilmail.setText(user.getEmail());
        iltelefon.setText(user.getPhoneNumber());

        ilbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = ilname.getText().toString();
                String mail = ilmail.getText().toString();
                String telefon = iltelefon.getText().toString();
                String mesaj = ilileti.getText().toString();
                if (!name.isEmpty() && !mail.isEmpty() && !mesaj.isEmpty()) {
                    Intent emailIntent = new Intent(Intent.ACTION_SEND);
                    emailIntent.putExtra(Intent.EXTRA_EMAIL, "16701019@mersin.edu.tr");
                    emailIntent.putExtra(Intent.EXTRA_SUBJECT, name + " " + mail + " " + telefon);
                    emailIntent.putExtra(Intent.EXTRA_TEXT, mesaj);
                    startActivity(Intent.createChooser(emailIntent,getString(R.string.gndr)));
                }else {
                    Toast.makeText(IletisimActivity.this,getString(R.string.toast1),Toast.LENGTH_SHORT).show();
                }
            }
        });




    }
}