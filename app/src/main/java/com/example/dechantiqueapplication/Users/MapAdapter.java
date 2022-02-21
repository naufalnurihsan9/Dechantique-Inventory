package com.example.dechantiqueapplication.Users;

import androidx.fragment.app.FragmentActivity;

import com.example.dechantiqueapplication.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapAdapter extends FragmentActivity implements OnMapReadyCallback {
    GoogleMap map;

    @Override
    public void onMapReady(GoogleMap googleMap) {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        map = googleMap;

        com.google.android.gms.maps.model.LatLng MallAmbasador = new LatLng(-6.223749, 106.826445);
        map.addMarker(new MarkerOptions().position(MallAmbasador).title("DeChantique"));
        map.moveCamera(CameraUpdateFactory.newLatLng(MallAmbasador));

    }
}
