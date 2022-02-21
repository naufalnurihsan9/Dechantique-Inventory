package com.example.dechantiqueapplication.Users;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dechantiqueapplication.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class Edit_Detal_Barang extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    EditText  namabarang, hargabarang, banyakbarang1, banyakbarang2,banyakbarang3;
    Button pilihfotoBrng,simpan;
    ImageView fotobarang, back;
    TextView kodebarang, adaukuranlain1,adaukuranlain2;
    LinearLayout layoutke2, layoutke3;
    Barang barang;
    Uri mImageUri;
    DatabaseReference reffBarang;
    FirebaseFirestore firebaseFirestore;
    StorageReference mStoragereff;
    ProgressBar mProgressBar;
    String _kodebarang, _namabarang, _hargabarang, _banyakbarang1, _banyakbarang2,_banyakbarang3, key
            , cobaBanyak2, cobaBanyak3, cekNama;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit__detal__barang);


        kodebarang = findViewById(R.id.etKodeBarang);
        namabarang = findViewById(R.id.etNamaBarang);
        hargabarang = findViewById(R.id.erHargaBarang);
        simpan = findViewById(R.id.btnSimpan);
        mProgressBar = findViewById(R.id.m_ProgresBar);
        pilihfotoBrng = findViewById(R.id.btnPilihFotoBrng);
        fotobarang = findViewById(R.id.IvProduk);
        back = findViewById(R.id.backKeMainMenu_);

        adaukuranlain1 = findViewById(R.id.adaukuranLain1);
        adaukuranlain2 = findViewById(R.id.adaukuranLain2);

        banyakbarang1 = findViewById(R.id.etBanyakBarang1);
        banyakbarang2 = findViewById(R.id.etBanyakBarang2);
        banyakbarang3 = findViewById(R.id.etBanyakBarang3);

        barang = new Barang();
        reffBarang = FirebaseDatabase.getInstance().getReference().child("Produk");

        firebaseFirestore= FirebaseFirestore.getInstance();
        mStoragereff = FirebaseStorage.getInstance().getReference("Foto produk");

        layoutke2 = findViewById(R.id.ukuranke2);
        layoutke2.setVisibility(View.VISIBLE);
        layoutke3 = findViewById(R.id.ukuranke3);
        layoutke3.setVisibility(View.VISIBLE);

        hargabarang.addTextChangedListener(onTextChangedListener());

        tampilkanDetailBarang();

        adaukuranlain1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutke2.setVisibility(View.VISIBLE);
                adaukuranlain1.setVisibility(View.GONE);
                adaukuranlain2.setVisibility(View.VISIBLE);
            }
        });


        adaukuranlain2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutke3.setVisibility(View.VISIBLE);
                adaukuranlain2.setVisibility(View.GONE);
            }
        });

        pilihfotoBrng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });


        simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
           if( editNamaBarang() | editHargaBarang() | ediBanyakBarang1() | ediBanyakBarang2() | ediBanyakBarang3()) {
                    Toast.makeText(Edit_Detal_Barang.this, " Berhasil Update Data ", Toast.LENGTH_SHORT).show();
                    Intent keUProfileActivity = new Intent(getApplicationContext(), Detail_Barang.class);
                    startActivity(keUProfileActivity);
                }
                UploadDataBarang();
//                Intent pindah = new Intent(getApplicationContext(),MainActivity.class);
//                startActivity(pindah);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent back = new Intent(Edit_Detal_Barang.this,Detail_Barang.class);
                startActivity(back);
            }
        });


    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent kembalikeDetailyuk= new Intent(Edit_Detal_Barang.this, Detail_Barang.class);
        startActivity(kembalikeDetailyuk);
    }

    private void UploadDataBarang() {
        final String tbhKodeBarang = kodebarang.getText().toString().trim();
        final  String tbhNamaBarang = namabarang.getText().toString().trim();
        final  String tbhHargaBarang = hargabarang.getText().toString().trim();
        final String banyakbarang1_ = banyakbarang1.getText().toString().trim();
        final String banyakbarang2_ = banyakbarang2.getText().toString().trim();
        final String banyakbarang3_ = banyakbarang3.getText().toString().trim();


        if (TextUtils.isEmpty(tbhKodeBarang)) {
            kodebarang.setError("Masukan Kode Barang");
            return;
        }
        if (TextUtils.isEmpty(tbhNamaBarang)) {
            namabarang.setError("Masukan Nama Barang");
            return;
        }
        if (TextUtils.isEmpty(tbhHargaBarang)) {
            hargabarang.setError("Masukan Harrga Barang");
            return;
        }

        if(TextUtils.isEmpty(banyakbarang1_)){
            banyakbarang1.setError("Masukan Banyak Barang");
            return;
        }


        if(mImageUri != null){

            StorageReference fileReference = mStoragereff.child(System.currentTimeMillis()
                    + "." +getFileExtension(mImageUri));

            fileReference.putFile(mImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                            while (!urlTask.isSuccessful());
                            Uri downloadUrl = urlTask.getResult();

                            barang.setKodebarang(tbhKodeBarang);
                            barang.setNamabarang(tbhNamaBarang);
                            barang.setHargabarang(tbhHargaBarang);
                            barang.setBanyakbarang1(banyakbarang1_);
                            barang.setBanyakbarang2(banyakbarang2_);
                            barang.setBanyakbarang3(banyakbarang3_);
                            reffBarang.child(tbhKodeBarang).setValue(barang);
                            reffBarang.child(tbhKodeBarang).child("imageUrl").setValue(downloadUrl.toString());


                            Toast.makeText(Edit_Detal_Barang.this, "Upload successful!", Toast.LENGTH_LONG).show();

                            Handler handler =  new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    mProgressBar.setProgress(0);
                                    Intent pindah = new Intent(getApplicationContext(),MainActivity2.class);
                                    startActivity(pindah);
                                }
                            },2000);
                        }
                    })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Edit_Detal_Barang.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })

                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(Edit_Detal_Barang.this, "Tunggu Sebentar, Sedang Upload Data", Toast.LENGTH_SHORT).show();
                            double progress = ( 100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            mProgressBar.setProgress((int)progress);
                        }
                    });
        }
    }



    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null    ){
            mImageUri = data.getData();

            Picasso.get().load(mImageUri).into(fotobarang);
        }
    }

    private String getFileExtension(Uri uri){
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }

    private void tampilkanDetailBarang() {
        key = getIntent().getStringExtra("ProdukKey");
        reffBarang.child(key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Barang barang1 = dataSnapshot.getValue(Barang.class);
                kodebarang.setText(barang1.getKodebarang());
                hargabarang.setText(barang1.getHargabarang());
                namabarang.setText(barang1.getNamabarang());
                banyakbarang1.setText(barang1.getBanyakbarang1());

                banyakbarang2.setText(barang1.getBanyakbarang2());

                cobaBanyak2 = barang1.getBanyakbarang2();
                String banyakdanUkuran2cek = banyakbarang2.getText().toString().trim();
                if (banyakdanUkuran2cek.isEmpty()){
                    adaukuranlain1.setVisibility(View.VISIBLE);
                    layoutke2.setVisibility(View.GONE);
                    layoutke3.setVisibility(View.GONE);
                    adaukuranlain2.setVisibility(View.GONE);
                }
                banyakbarang3.setText(barang1.getBanyakbarang3());

                cobaBanyak3 = barang1.getBanyakbarang3();
                String banyakdanUkuran3cek = banyakbarang3.getText().toString().trim();

                if (banyakdanUkuran2cek.isEmpty() && banyakdanUkuran3cek.isEmpty()){
                    adaukuranlain1.setVisibility(View.VISIBLE);
                    layoutke3.setVisibility(View.GONE);
                    layoutke2.setVisibility(View.GONE);
                    adaukuranlain2.setVisibility(View.GONE);
                }
                if (banyakdanUkuran3cek.isEmpty()){
                    layoutke3.setVisibility(View.GONE);
                    adaukuranlain1.setVisibility(View.GONE);
                    adaukuranlain2.setVisibility(View.VISIBLE);
                }

                if (banyakdanUkuran2cek.isEmpty() == false && banyakdanUkuran3cek.isEmpty() == false){
                    adaukuranlain1.setVisibility(View.GONE);
                    adaukuranlain2.setVisibility(View.GONE);
                }

                String uriBarang = barang1.getImageUrl();
                Picasso.get().load(uriBarang).into(fotobarang);



                _kodebarang = kodebarang.getText().toString();
                _namabarang = namabarang.getText().toString();
                _hargabarang = hargabarang.getText().toString();
                _banyakbarang1 = banyakbarang1.getText().toString();
                _banyakbarang2 = banyakbarang2.getText().toString();
                _banyakbarang3 = banyakbarang3.getText().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



//        reffBarang.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                for (DataSnapshot ds : dataSnapshot.getChildren()) {
//                    String key = ds.getKey();
//
//                    DatabaseReference datareff = reffBarang.child(key);
//                    datareff.addValueEventListener(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                            Barang barang1 = dataSnapshot.getValue(Barang.class);
//                            kodebarang.setText(barang1.getKodebarang());
//                            hargabarang.setText(barang1.getHargabarang());
//                            namabarang.setText(barang1.getNamabarang());
//                            banyakbarang1.setText(barang1.getBanyakbarang1());
//                            banyakbarang2.setText(barang1.getBanyakbarang2());
//                            banyakbarang3.setText(barang1.getBanyakbarang3());
//                            btnlokasi_cabang.setText(barang1.getLokasi());
//                            String uriBarang = barang1.getImageUrl();
//                            Picasso.get().load(uriBarang).into(fotobarang);
//
//
//                            _kodebarang = kodebarang.getText().toString();
//                            _namabarang = namabarang.getText().toString();
//                            _hargabarang = hargabarang.getText().toString();
//                            _banyakbarang1 = banyakbarang1.getText().toString();
//                            _banyakbarang2 = banyakbarang2.getText().toString();
//                            _banyakbarang3 = banyakbarang3.getText().toString();
//                            _btnlokasi_cabang = btnlokasi_cabang.getText().toString();
//
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError databaseError) {
//                            Toast.makeText(Edit_Detal_Barang.this, databaseError.getCode(), Toast.LENGTH_SHORT).show();
//                        }
//                    });
//                }
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });

    }


    private boolean editNamaBarang() {
        if(!_namabarang.equals(namabarang.getText().toString())){
            reffBarang.child(key).child("namabarang").setValue(namabarang.getText().toString());
            _namabarang=namabarang.getText().toString();
            return true;
        }else{
            return false;
        }
    }

    private boolean editHargaBarang() {
        if(!_hargabarang.equals(hargabarang.getText().toString())){
            reffBarang.child(key).child("hargabarang").setValue(hargabarang.getText().toString());
            _hargabarang=hargabarang.getText().toString();
            return true;
        }else{
            return false;
        }
    }

    private boolean ediBanyakBarang1() {
        if(!_banyakbarang1.equals(banyakbarang1.getText().toString())){
            reffBarang.child(key).child("banyakbarang1").setValue(banyakbarang1.getText().toString());
            _banyakbarang1=banyakbarang1.getText().toString();
            return true;
        }else{
            return false;
        }
    }

    private boolean ediBanyakBarang2() {
        if(!_banyakbarang2.equals(banyakbarang2.getText().toString())){
            reffBarang.child(key).child("banyakbarang2").setValue(banyakbarang2.getText().toString());
            _banyakbarang2=banyakbarang2.getText().toString();
            return true;
        }else{
            return false;
        }
    }

    private boolean ediBanyakBarang3() {
        if(!_banyakbarang3.equals(banyakbarang3.getText().toString())){
            reffBarang.child(key).child("banyakbarang3").setValue(banyakbarang3.getText().toString());
            _banyakbarang3=banyakbarang3.getText().toString();
            return true;
        }else{
            return false;
        }
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
                hargabarang.removeTextChangedListener(this);

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
                    hargabarang.setText(formattedString);
                    hargabarang.setSelection(hargabarang.getText().length());
                } catch (NumberFormatException nfe) {
                    nfe.printStackTrace();
                }

                hargabarang.addTextChangedListener(this);
            }
        };
    }




}


