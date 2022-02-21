package com.example.dechantiqueapplication.Users;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dechantiqueapplication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ProfileActivity extends AppCompatActivity {


    TextView  email, nama, username, noTlp;
    ImageView fotouser,back;
    Button updateInformasi;
    FirebaseAuth fauth;
    FirebaseDatabase firebaseDatabase;
    FirebaseUser UserID;
    DatabaseReference reff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        username = findViewById(R.id.tvuserName);
        nama = findViewById(R.id.tvNama);
        email = findViewById(R.id.tvEmail);
        noTlp = findViewById(R.id.tvNoTlp);
        updateInformasi = findViewById(R.id.btnUpdateInformasi);
        fotouser= findViewById(R.id.IVfotoUser);
        back= findViewById(R.id.backKeMain_act);


        fauth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        UserID = fauth.getCurrentUser();

        reff = FirebaseDatabase.getInstance().getReference().child("User");

        tampilkan_informasi_user();

        updateInformasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent keUpdateProfile = new Intent(ProfileActivity.this, UpdateProfile.class);
                startActivity(keUpdateProfile);

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent back = new Intent(getApplicationContext(),Dashboard.class);
                startActivity(back);
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent keDashboard = new Intent(ProfileActivity.this, Dashboard.class);
        startActivity(keDashboard);
    }

    private void tampilkan_informasi_user() {
        final DatabaseReference dataReff = reff.child(UserID.getUid());
        dataReff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Member member = dataSnapshot.getValue(Member.class);
                username.setText(member.getUsername());
                email.setText(member.getEmail());
                nama.setText(member.getNama());
                noTlp.setText(member.getNoTlp());
                String uriProfile = member.getImageUrl();
                Picasso.get().load(uriProfile).into(fotouser);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ProfileActivity.this, databaseError.getCode(), Toast.LENGTH_SHORT).show();
            }
        });


    }


}
