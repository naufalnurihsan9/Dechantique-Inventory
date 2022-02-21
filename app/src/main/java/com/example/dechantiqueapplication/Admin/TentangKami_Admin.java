package com.example.dechantiqueapplication.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dechantiqueapplication.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class TentangKami_Admin extends  FragmentActivity implements OnMapReadyCallback {
    ImageView imageview1,imageview2,imageview3,imageview4,imageview5, back;
    DatabaseReference Datareff;
    TextView TextTentang;
    GoogleMap map;


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent backyuk = new Intent(TentangKami_Admin.this, Dashboard_Admin.class);
        startActivity(backyuk);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tentangkami);


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Datareff= FirebaseDatabase.getInstance().getReference().child("Tentang Kami");

        back = findViewById(R.id.backKeDashGo);

        imageview1 = findViewById(R.id.imageViewTemtang1);
        imageview2 = findViewById(R.id.imageViewTemtang2);
        imageview3 = findViewById(R.id.imageViewTemtang3);
        imageview4 = findViewById(R.id.imageViewTemtang4);
        imageview5 = findViewById(R.id.imageViewTemtang5);

        TextTentang = findViewById(R.id.textTentangKami);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backyuk2 = new Intent(TentangKami_Admin.this, Dashboard_Admin.class);
                startActivity(backyuk2);
            }
        });

        Datareff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                FotoTentang_Admin fotoTentangKami = dataSnapshot.getValue(FotoTentang_Admin.class);
                Picasso.get().load(fotoTentangKami.getFoto1()).fit().centerCrop().into(imageview1);
                Picasso.get().load(fotoTentangKami.getFoto2()).fit().centerCrop().into(imageview2);
                Picasso.get().load(fotoTentangKami.getFoto3()).fit().centerCrop().into(imageview3);
                Picasso.get().load(fotoTentangKami.getFoto4()).fit().centerCrop().into(imageview4);
                Picasso.get().load(fotoTentangKami.getFoto5()).fit().centerCrop().into(imageview5);

                TextTentang.setText(fotoTentangKami.getTextTentang());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        com.google.android.gms.maps.model.LatLng MallAmbasador = new LatLng(-6.223749, 106.826445);
        map.addMarker(new MarkerOptions().position(MallAmbasador).title("DeChantique"));
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(MallAmbasador, 10));

    }
}