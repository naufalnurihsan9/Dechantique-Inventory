package com.example.dechantiqueapplication.Users;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dechantiqueapplication.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;


public class Detail_Barang extends AppCompatActivity {

    TextView KodeBarang,NamaBarang,HargaBarang,banyakdanUkuran1,banyakdanUkuran2,banyakdanUkuran3, katagoridetail;
    DatabaseReference reffBarang;
    ImageView back, fotobarang;
    String Produkid, cobaBanyak2,cobaBanyak3,iniNamaBarang,iniHargaBarang;
    TextView chatAdmin;

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent kembali= new Intent(Detail_Barang.this, MainActivity2.class);
        startActivity(kembali);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail__barang);



        NamaBarang = findViewById(R.id.TVNamaBarang);
        KodeBarang = findViewById(R.id.TvKodeBarang);
        HargaBarang = findViewById(R.id.TVHargaBarang);

        banyakdanUkuran1 = findViewById(R.id.TVBanyakDanUkuran1Detail);

        banyakdanUkuran2 = findViewById(R.id.TVBanyakDanUkuran2Detail);
        banyakdanUkuran2.setVisibility(View.VISIBLE);

        banyakdanUkuran3 = findViewById(R.id.TVBanyakDanUkuran3Detail);
        banyakdanUkuran3.setVisibility(View.VISIBLE);


        back = findViewById(R.id.backKeMainMenu_2);
        fotobarang = findViewById(R.id.IvProduk_BP);
        chatAdmin = findViewById(R.id.chatAdmin);
        katagoridetail = findViewById(R.id.TVKatagoriDetail);

        reffBarang = FirebaseDatabase.getInstance().getReference().child("Produk");
        HargaBarang.addTextChangedListener(onTextChangedListener());
         Produkid = getIntent().getStringExtra("ProdukKey");


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity2.class);
                startActivity(intent);
            }
        });

        chatAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String textPesan = "Hallo kak, saya ingin beli produk " + iniNamaBarang +
                                    " yang harganya " + iniHargaBarang + " Bisa kirim hari ini ? ";

                Intent pesanWa = new Intent(Intent.ACTION_SEND);
                pesanWa.setType("text/plain");
                pesanWa.putExtra(Intent.EXTRA_TEXT, textPesan);
                pesanWa.putExtra("jid","6281316029229" + "@s.whatsapp.net");
                pesanWa.setPackage("com.whatsapp");

                startActivity(pesanWa);
            }
        });

        TampilkanDetailBarang();
    }

    private void TampilkanDetailBarang() {


        reffBarang.child(Produkid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                Barang barang1 = dataSnapshot.getValue(Barang.class);

                                iniNamaBarang = barang1.getNamabarang();
                                iniHargaBarang = barang1.getHargabarang();

                                KodeBarang.setText(barang1.getKodebarang());
                                HargaBarang.setText(barang1.getHargabarang());
                                NamaBarang.setText(barang1.getNamabarang());
                                banyakdanUkuran1.setText(barang1.getBanyakbarang1());

                                banyakdanUkuran2.setText(barang1.getBanyakbarang2());

                                 cobaBanyak2 = barang1.getBanyakbarang2();
                                 String banyakdanUkuran2_ = banyakdanUkuran2.getText().toString().trim();
                                 if (banyakdanUkuran2_.isEmpty()){
                                     banyakdanUkuran2.setVisibility(View.GONE);
                                 }else{
                                     banyakdanUkuran2.setVisibility(View.VISIBLE);
                                 }


                                banyakdanUkuran3.setText(barang1.getBanyakbarang3());

                                cobaBanyak3 = barang1.getBanyakbarang3();
                                String banyakdanUkuran3_ = banyakdanUkuran3.getText().toString().trim();
                                if (banyakdanUkuran3_.isEmpty()){
                                    banyakdanUkuran3.setVisibility(View.GONE);
                                }else{
                                    banyakdanUkuran3.setVisibility(View.VISIBLE);
                                }


                                katagoridetail.setText(barang1.getKatagori());
                                String uriBarang = barang1.getImageUrl();
                                Picasso.get().load(uriBarang).into(fotobarang);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private TextWatcher onTextChangedListener () {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                HargaBarang.removeTextChangedListener(this);

                try {
                    String originalString = s.toString();

                    Long longval;
                    if (originalString.contains(",")) {
                        originalString = originalString.replaceAll(",", "");
                    }
                    longval = Long.parseLong(originalString);

                    DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
                    formatter.applyPattern("#,###,###,###");
                    String formattedString = formatter.format(longval);

                    //setting text after format to EditText
                    HargaBarang.setText(formattedString);
                } catch (NumberFormatException nfe) {
                    nfe.printStackTrace();
                }

                HargaBarang.addTextChangedListener(this);
            }
        };
    }

}