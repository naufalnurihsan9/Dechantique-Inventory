package com.example.dechantiqueapplication.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;

import com.example.dechantiqueapplication.R;
import com.example.dechantiqueapplication.Users.Dashboard;
import com.example.dechantiqueapplication.Users.LoginActivity;
import com.example.dechantiqueapplication.Users.TentangKami;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Dashboard_Admin extends AppCompatActivity {
    CardView tentangkita, listProduk, Tutorial;
    ImageView foto;
    DatabaseReference reffAdmin_;
    daftarAdmin DaftarAdminDash;

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(Dashboard_Admin.this)
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard__admin);

        tentangkita = findViewById(R.id.TentangkitadiAdmin);
        listProduk = findViewById(R.id.kemainActAdmin);
        Tutorial = findViewById(R.id.TutorialAdmin);
        foto = findViewById(R.id.fotoAdminDash);

        reffAdmin_ = FirebaseDatabase.getInstance().getReference().child("Admin");


//

        foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(Dashboard_Admin.this, v);
                popupMenu.inflate(R.menu.adminlougout);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.menulougoutAdmin:
                                reffAdmin_.child("Admin1").child("status").setValue("Belum Login");
                                Intent intent = new Intent(Dashboard_Admin.this, LoginActivity.class);
                                startActivity(intent);
                                return true;


                        }
                        return false;
                    }
                });
                popupMenu.show();
            }

        });

        tentangkita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Intent intent = new Intent(Dashboard_Admin.this, TentangKami_Admin.class);
            startActivity(intent);
            }
        });

        listProduk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent keMain = new Intent(Dashboard_Admin.this, MainActivity_Admin.class);
                startActivity(keMain);
            }
        });


    }
}