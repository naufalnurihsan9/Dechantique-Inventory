package com.example.dechantiqueapplication.Users;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
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

public class Dashboard extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {
    CardView kemainAct,tentang;
    FirebaseAuth fauth;
    FirebaseDatabase firebaseDatabase;
    FirebaseUser UserID;
    DatabaseReference reff;
    TextView namauser;
    ImageView fotouserDash;



    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(Dashboard.this)
                .setIcon(R.mipmap.ic_launcher).setTitle(R.string.app_name)
                .setMessage("Yakin ingin keluar ?")
                .setPositiveButton("Yaa", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);

                    }
                })
                .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .show();
    }

    public void ShowPopUpMenu(View v ){
        PopupMenu popupMenu = new PopupMenu(this,v);
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.inflate(R.menu.main_menu);
        popupMenu.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menuprofile:
                Intent intent = new Intent(Dashboard.this, ProfileActivity.class);
                startActivity(intent);
                return true;

            case R.id.menulougout:
                fauth.signOut();
                Intent keLogin = new Intent(Dashboard.this, LoginActivity.class);
                startActivity(keLogin);
                return true;

            default:
                return false;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);


        fauth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        UserID = fauth.getCurrentUser();

        reff = FirebaseDatabase.getInstance().getReference().child("User");

        kemainAct = findViewById(R.id.kemainAct);
        tentang = findViewById(R.id.Tentangkita);
        namauser = findViewById(R.id.namauser);
        fotouserDash = findViewById(R.id.fotoUserDash);



        kemainAct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),MainActivity2.class));
            }
        });

        tentang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), TentangKami.class));
            }
        });

        final DatabaseReference dataReff = reff.child(UserID.getUid());
        dataReff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Member member = dataSnapshot.getValue(Member.class);

                namauser.setText(member.getNama());
                Picasso.get().load(member.getImageUrl()).fit().centerCrop().into(fotouserDash);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Dashboard.this, databaseError.getCode(), Toast.LENGTH_SHORT).show();
            }
        });


    fotouserDash.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ShowPopUpMenu(v);
        }
    });
    }
}