package com.example.dechantiqueapplication.Admin;

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
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dechantiqueapplication.R;
import com.example.dechantiqueapplication.Users.Barang;
import com.example.dechantiqueapplication.Users.MainActivity2;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class TambahProdukActivity extends AppCompatActivity  {
    private static final int PICK_IMAGE_REQUEST = 1;

//    private static final Pattern PATTERN_NAMA =
//            Pattern.compile("(?=.*[a-z])") ;
    EditText kodebarang, namabarang, hargabarang, banyakbarang1, banyakbarang2,banyakbarang3;
    Button pilihfotoBrng,tambahbarang, katagoribrng;
    ImageView fotobarang, back;
    TextView adaukuranlain1,adaukuranlain2;
    LinearLayout layoutke2, layoutke3;
    Barang barang;
    Uri mImageUri;
    DatabaseReference reffBarang;
    FirebaseFirestore firebaseFirestore;
    StorageReference mStoragereff;
    ProgressBar mProgressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_produk);


        kodebarang = findViewById(R.id.etKodeBarang);
        namabarang = findViewById(R.id.etNamaBarang);
        hargabarang = findViewById(R.id.erHargaBarang);
        tambahbarang = findViewById(R.id.btnTambahBarang);
        mProgressBar = findViewById(R.id.m_ProgresBartbh);
        pilihfotoBrng = findViewById(R.id.btnPilihFotoBrng);
        fotobarang = findViewById(R.id.IvProduk);
        back = findViewById(R.id.backKeMainMenu_);
        katagoribrng= findViewById(R.id.btnKatagori1);

        adaukuranlain1 = findViewById(R.id.adaukuranLain1);
        adaukuranlain2 = findViewById(R.id.adaukuranLain2);
        adaukuranlain2.setVisibility(View.GONE);

        banyakbarang1 = findViewById(R.id.etBanyakBarang1);
        banyakbarang2 = findViewById(R.id.etBanyakBarang2);
        banyakbarang3 = findViewById(R.id.etBanyakBarang3);

        barang = new Barang();
        reffBarang = FirebaseDatabase.getInstance().getReference().child("Produk");

        firebaseFirestore=FirebaseFirestore.getInstance();
        mStoragereff = FirebaseStorage.getInstance().getReference("Foto produk");

        layoutke2 = findViewById(R.id.ukuranke2);
        layoutke2.setVisibility(View.GONE);
        layoutke3 = findViewById(R.id.ukuranke3);
        layoutke3.setVisibility(View.GONE);



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

        hargabarang.addTextChangedListener(onTextChangedListener());


        tambahbarang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            UploadDataBarang();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent back = new Intent(getApplicationContext(), MainActivity_Admin.class);
                startActivity(back);
            }
        });


        katagoribrng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(TambahProdukActivity.this, v);
                popupMenu.inflate(R.menu.katagori_menu);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.katagoriDress:
                                katagoribrng.setText("Dress Pesta");
                                return true;
                            case R.id.katagoriCasual:
                                katagoribrng.setText("Dress Casual");
                                return true;
                            case R.id.katagoriTunik:
                                katagoribrng.setText("Tunik");
                                return true;
                            case R.id.katagoriOuter:
                                katagoribrng.setText("Outer");
                                return true;

                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        });




    }



    private void UploadDataBarang() {
        final String tbhKodeBarang = kodebarang.getText().toString().trim();
        final  String tbhNamaBarang = namabarang.getText().toString().trim();
        final  String tbhHargaBarang = hargabarang.getText().toString().trim();
        final String banyakbarang1_ = banyakbarang1.getText().toString().trim();
        final String banyakbarang2_ = banyakbarang2.getText().toString().trim();
        final String banyakbarang3_ = banyakbarang3.getText().toString().trim();
        final String katagoribrng_ = katagoribrng.getText().toString().trim();

      if (TextUtils.isEmpty(tbhKodeBarang)) {
            kodebarang.setError("Masukan Kode Barang");
            return;
        }

        if (TextUtils.isEmpty(tbhNamaBarang)) {
            namabarang.setError("Masukan Nama Barang");
            return;
        }
//        if (!PATTERN_NAMA.matcher(tbhNamaBarang).matches()){
//            namabarang.setError("Nama Barang Tidak boleh angka/spesial charachter");
//            return;
//        }

        if (TextUtils.isEmpty(tbhHargaBarang)) {
            hargabarang.setError("Masukan Harga Barang");
            return;
        }

        if(TextUtils.isEmpty(banyakbarang1_)){
            banyakbarang1.setError("Masukan Banyak Barang");
            return;
        }

        if (fotobarang != null){
            Toast.makeText(TambahProdukActivity.this, " Pilih Foto barang !" , Toast.LENGTH_LONG).show();
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
                            barang.setKatagori(katagoribrng_);
                            reffBarang.child(tbhKodeBarang).setValue(barang);
                            reffBarang.child(tbhKodeBarang).child("imageUrl").setValue(downloadUrl.toString());


                            Toast.makeText(TambahProdukActivity.this, "Upload successful!", Toast.LENGTH_LONG).show();

                            Handler handler =  new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
//                                    mProgressBar.setProgress(0);
                                    Intent pindah = new Intent(getApplicationContext(), MainActivity_Admin.class);
                                    startActivity(pindah);
                                }
                            },2500);
                        }
                    })

                     .addOnFailureListener(new OnFailureListener() {
                 @Override
                 public void onFailure(@NonNull Exception e) {
                        Toast.makeText(TambahProdukActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                 }
             })

                     .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                 @Override
                 public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                     Toast.makeText(TambahProdukActivity.this, "Tunggu Sebentar, Sedang Upload Data", Toast.LENGTH_SHORT).show();
//                     double progress = ( 200.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
//                     mProgressBar.setProgress((int)progress);
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
        ContentResolver  cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
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
