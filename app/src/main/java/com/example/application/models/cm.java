package com.example.application.models;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;

public class cm {

    public static String simpledateformat(Long time) {
    Date date = new Date(time);
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yy HH:mm");
    String formattedDate = simpleDateFormat.format(date);
    return formattedDate;
    }

    public static void setLocale(Context context, String selectedLocale) {
        Locale locale = new Locale(selectedLocale);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
        writeLocaleSharedPreferances(context, selectedLocale);
    }

    public static void writeLocaleSharedPreferances(Context context, String selectedLocale) {
        SharedPreferences sharedPref = context.getSharedPreferences("locale", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("selectedLocale", selectedLocale);
        editor.apply();
    }

    public static String getLocaleSharedPreferances(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences("locale", Context.MODE_PRIVATE);
        return sharedPref.getString("selectedLocale", "");
    }

    public static boolean isValidEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static void hideKeyboard(Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        View currentFocus = ((Activity) context).getCurrentFocus();
        if (currentFocus != null) {
            imm.hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);
        }
    }

    public static CompletableFuture<String> findMatchingGroup(String id1, String id2) {
        DatabaseReference db = FirebaseDatabase.getInstance().getReference("mesajlar");
        CompletableFuture<String> future = new CompletableFuture<>();

        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot grupSnapshot : snapshot.getChildren()) {
                    DataSnapshot kisilerSnapshot = grupSnapshot.child("kisiler");

                    boolean match1 = false;
                    boolean match2 = false;

                    for (DataSnapshot kisiSnapshot : kisilerSnapshot.getChildren()) {
                        String kisiId = kisiSnapshot.getKey();

                        if (kisiId.equals(id1)) {
                            match1 = true;
                        } else if (kisiId.equals(id2)) {
                            match2 = true;
                        }

                        if (match1 && match2) {
                            String grupName = grupSnapshot.getKey();
                            future.complete(grupName);
                            return;
                        }
                    }
                }

                future.complete(null);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                future.complete(null);
            }
        });

        return future;
    }






}
