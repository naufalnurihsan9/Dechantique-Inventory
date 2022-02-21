package com.example.dechantiqueapplication.Users;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dechantiqueapplication.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class UpdateProfile extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 100;
    TextView UbahPass , nama, username, noTlp;
    ImageView fotoUser,back;
    Button simpan,btnPilihFoto;
    FirebaseAuth fauth;
    FirebaseDatabase firebaseDatabase;
    StorageReference mStoragereff;
    FirebaseUser UserID;
    Uri URIfotoUser;
    DatabaseReference reff;
    String _username,_nama,_notlp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);


        username = findViewById(R.id.etuserName);
        nama = findViewById(R.id.etNama);
        noTlp = findViewById(R.id.etNoTlp);
        simpan = findViewById(R.id.btnSimpan);
        UbahPass = findViewById(R.id.btnUbahPass);
        btnPilihFoto =findViewById(R.id.btnPilihFotoUser);
        fotoUser = findViewById(R.id.IVfotoUser);
        back = findViewById(R.id.backKeProfile_act);

        fauth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        UserID = fauth.getCurrentUser();

        reff = FirebaseDatabase.getInstance().getReference().child("User");
        mStoragereff = FirebaseStorage.getInstance().getReference("Foto User").child(UserID.getUid());

        tampilkan_informasi_user();

        simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( updateusername()  | updatenama() | updatenotlp() | UploadFotoUser()){
                    Toast.makeText(UpdateProfile.this, " Berhasil Update Data ", Toast.LENGTH_SHORT).show();
                    Intent keUProfileActivity = new Intent(getApplicationContext(), ProfileActivity.class);
                    startActivity(keUProfileActivity);
                }else{
                    Toast.makeText(UpdateProfile.this, " Gagal Update Data ", Toast.LENGTH_SHORT).show();
                }

            }
        });

        UbahPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!_username.equals(username.getText().toString()) |
                        !_nama.equals(nama.getText().toString()) |
                        !_notlp.equals(noTlp.getText().toString())){
                    Toast.makeText(UpdateProfile.this, "Selesaikan dulu Update Informasi Akun", Toast.LENGTH_SHORT).show();
                }else {
                    Intent pindahLupaPas = (new Intent(UpdateProfile.this, Update_ke_LupaPass.class));
                    startActivity(pindahLupaPas);
                }
            }
        });

        btnPilihFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent back = new Intent(getApplicationContext(),ProfileActivity.class);
                startActivity(back);
            }
        });
    }


    private boolean UploadFotoUser(){

        if(URIfotoUser != null){

            StorageReference fileReference = mStoragereff.child(System.currentTimeMillis()
                    + "." +getFileExtension(URIfotoUser));

            fileReference.putFile(URIfotoUser)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                            while (!urlTask.isSuccessful());
                            Uri downloadUrl = urlTask.getResult();
                            UserID = fauth.getCurrentUser();
                            reff.child(UserID.getUid()).child("imageUrl").setValue(downloadUrl.toString());
                            Toast.makeText(UpdateProfile.this, "Upload successful!", Toast.LENGTH_LONG).show();


                        }
                    });
            return true;
        }else{
            return false;
        }
    }
    private void tampilkan_informasi_user() {
        DatabaseReference dataReff = reff.child(UserID.getUid());
        dataReff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Member member = dataSnapshot.getValue(Member.class);
                username.setText(member.getUsername());
                nama.setText(member.getNama());
                noTlp.setText(member.getNoTlp());


                _username = username.getText().toString();
                _nama = nama.getText().toString();
                _notlp = noTlp.getText().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(UpdateProfile.this, databaseError.getCode(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private boolean updateusername() {
        if(!_username.equals(username.getText().toString())){
            reff.child(UserID.getUid()).child("username").setValue(username.getText().toString());
            _username=username.getText().toString();
            return true;
        }else{
            return false;
        }
    }


    private boolean updatenama() {
        if(!_nama.equals(nama.getText().toString())){
            reff.child(UserID.getUid()).child("nama").setValue(nama.getText().toString());
            _nama=nama.getText().toString();
            return true;
        }else{
            return false;
        }
    }

    private boolean updatenotlp() {
        if(!_notlp.equals(noTlp.getText().toString())){
            reff.child(UserID.getUid()).child("noTlp").setValue(noTlp.getText().toString());
            _notlp=noTlp.getText().toString();
            return true;
        }else{
            return false;
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
            URIfotoUser = data.getData();

            Picasso.get().load(URIfotoUser).into(fotoUser);


        }
    }

    private String getFileExtension(Uri uri){
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }
}
