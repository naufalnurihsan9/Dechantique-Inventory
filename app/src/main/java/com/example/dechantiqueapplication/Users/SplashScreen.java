package com.example.dechantiqueapplication.Users;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.example.dechantiqueapplication.Admin.Dashboard_Admin;
import com.example.dechantiqueapplication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.example.dechantiqueapplication.Admin.daftarAdmin;

public class SplashScreen extends AppCompatActivity {
    private static int SPLASH_TIME_OUT = 2000;
    private FirebaseUser firebaseUser;


    DatabaseReference reffAdmin;
    String statussekarang;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);


        reffAdmin = FirebaseDatabase.getInstance().getReference().child("Admin");
        reffAdmin.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    daftarAdmin status = ds.getValue(daftarAdmin.class);
                    statussekarang = status.getStatus();
//                    Toast.makeText(SplashScreen.this, statussekarang,Toast.LENGTH_SHORT ).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if( firebaseUser != null && firebaseUser.isEmailVerified()){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(getApplicationContext(), Dashboard.class);
                    startActivity(intent);
                    finish();
                }
            },SPLASH_TIME_OUT);
        }
//         if (statussekarang ==("Sedang Login")){
//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    Intent intent = new Intent(getApplicationContext(), Dashboard_Admin.class);
//                    startActivity(intent);
//                    finish();
//                }
//            },SPLASH_TIME_OUT);
//
//        } if (statussekarang == ("Belum Login")){
//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
//                    startActivity(intent);
//                    finish();
//                }
//            },SPLASH_TIME_OUT);
//        }
        else{
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            },SPLASH_TIME_OUT);
        }




    }
}