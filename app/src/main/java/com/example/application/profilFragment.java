package com.example.application;

import static android.app.Activity.RESULT_OK;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;


public class profilFragment extends Fragment {

    private TextView adsoyad,tel,mail;
    private ImageView ivprofil;
    private FirebaseUser user;
    private Button btnsfr,btnmail,btntel,btnonaymail,btndelete;
    private FloatingActionButton btnF;
    private StorageReference storageRef = FirebaseStorage.getInstance().getReference("usersphoto");
    private DatabaseReference db;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_profil, container, false);

        adsoyad=v.findViewById(R.id.tvadsoyad);
        tel=v.findViewById(R.id.tvtel);
        mail=v.findViewById(R.id.tvmail);
        ivprofil=v.findViewById(R.id.ivprofil);
        btnmail=v.findViewById(R.id.btnmail);
        btntel=v.findViewById(R.id.btntel);
        btnsfr=v.findViewById(R.id.btnsfr);
        btnonaymail=v.findViewById(R.id.btnonaymail);
        btndelete=v.findViewById(R.id.btndelete);
        btnF=v.findViewById(R.id.btnphoto);
        db= FirebaseDatabase.getInstance().getReference("users");

        user= FirebaseAuth.getInstance().getCurrentUser();

        if (user.getDisplayName()!=null){
            adsoyad.setText(user.getDisplayName().toString());
        }

        if (user.getEmail()!=null){
            mail.setText(user.getEmail().toString());
        }

        btnsfr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),SfrdgrActivity.class));
            }
        });

        btnmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), MaildgrActivity.class));
            }
        });

        db.child(user.getUid()).child("image").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Picasso.get().load(snapshot.getValue(String.class)).resize(300, 300).into(ivprofil);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                ivprofil.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.profil));
            }
        });

        if (user.isEmailVerified()){
            btnonaymail.setVisibility(View.INVISIBLE);
        }else{
            btnonaymail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(getActivity(),getString(R.string.toast10),Toast.LENGTH_SHORT).show();
                                btnonaymail.setVisibility(View.INVISIBLE);
                            }else {
                                Toast.makeText(getActivity(),getString(R.string.toast11),Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            });
        }

        String TelNo=user.getPhoneNumber();
        if(TelNo!= null &&  !TelNo.isEmpty() ){
            tel.setText(user.getPhoneNumber().toString());
            btntel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getActivity(),TeldgrActivity.class));
                }
            });

        }else {

            btntel.setText(getString(R.string.toast12));
            btntel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getActivity(),TelkayitActivity.class));

                }
            });
        }



        btndelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert=new AlertDialog.Builder(getContext());
                alert.setTitle(getString(R.string.hsbsl));
                alert.setMessage(getString(R.string.toast13));
                alert.setPositiveButton(getString(R.string.hsbsl),new DialogInterface.OnClickListener(){
                    public  void onClick(DialogInterface dialog,int arg1){
                        user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    Toast.makeText(getContext(),getString(R.string.toast14),Toast.LENGTH_LONG).show();
                                }else {
                                    Toast.makeText(getContext(),getString(R.string.toast15),Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    }
                });
                alert.setNegativeButton(getString(R.string.toast16), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                alert.setCancelable(true);

                alert.show();
            }
        });


        btnF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                galleryLauncher.launch(galleryIntent);
            }
        });


        return v;
    }

    ActivityResultLauncher<Intent> galleryLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    Intent data = result.getData();
                    Uri selectedImage=data.getData();

                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), selectedImage);
                        Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, 300, 300, false);


                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                        byte[] data1 = baos.toByteArray();


                        StorageReference photoRef=storageRef.child(user.getUid() +"/"+ selectedImage.getLastPathSegment());
                        UploadTask utask = photoRef.putBytes(data1);
                        utask.addOnSuccessListener(taskSnapshot ->{
                            photoRef.getDownloadUrl().addOnSuccessListener(uri -> {
                                String url=uri.toString();
                                Log.d("image",url);
                                db.child(user.getUid()).child("image").setValue(url).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(getContext(),getString(R.string.toast35),Toast.LENGTH_LONG).show();
                                    }
                                });
                            }).addOnFailureListener(exception ->{
                                Toast.makeText(getContext(),getString(R.string.toast11)+": "+exception.toString(),Toast.LENGTH_LONG).show();
                            });

                        }).addOnFailureListener(exception ->{
                            Toast.makeText(getContext(),getString(R.string.toast11)+": "+exception.toString(),Toast.LENGTH_LONG).show();
                        });

                    } catch (IOException e) {
                        Toast.makeText(getContext(),getString(R.string.toast11)+": "+e.toString(),Toast.LENGTH_LONG).show();
                    }

                }
            }
    );

}