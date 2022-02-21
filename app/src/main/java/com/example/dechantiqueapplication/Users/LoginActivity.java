package com.example.dechantiqueapplication.Users;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dechantiqueapplication.Admin.Login_Admin;
import com.example.dechantiqueapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class LoginActivity extends AppCompatActivity {
        TextView yukDaftar, lupapas, keLoginAdmin;
        FirebaseAuth fAuth;
        EditText emailLogin,PasswordLogin;
        Button Login;
        DatabaseReference reff;
        Member member;
        FirebaseUser firebaseUser, UserID;
        FirebaseDatabase firebaseDatabase;
        String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        yukDaftar=findViewById(R.id.tvYukDaftar);
        emailLogin= findViewById(R.id.etEmailLogin);
        PasswordLogin= findViewById(R.id.etPasswordLogin);
        lupapas= findViewById(R.id.LupapasLA);
        Login= findViewById(R.id.btnDiLoginUsers);
        keLoginAdmin = findViewById(R.id.keAdmin);
        fAuth =FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        member = new Member();
        reff = FirebaseDatabase.getInstance().getReference().child("User");
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if( firebaseUser != null && firebaseUser.isEmailVerified()){
            startActivity(new Intent(getApplicationContext(),Dashboard.class));
        }


        keLoginAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent keAdmin = new Intent(LoginActivity.this, Login_Admin.class);
                startActivity(keAdmin);
            }
        });
        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String emailuser = emailLogin.getText().toString().trim();
                final String passwroduser = PasswordLogin.getText().toString().trim();


                if (TextUtils.isEmpty(emailuser)) {
                    emailLogin.setError("Masukan Email Anda.");
                    return;
                }
                if (TextUtils.isEmpty(passwroduser)) {
                    PasswordLogin.setError("Masukan Password Anda.");
                    return;
                }



                fAuth.signInWithEmailAndPassword(emailuser,passwroduser).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){

                            if(fAuth.getCurrentUser().isEmailVerified()){
                                Toast.makeText(LoginActivity.this, " Berhasil Login ", Toast.LENGTH_SHORT).show();
                                UserID = fAuth.getCurrentUser();
                                if (UserID != null){
                                    currentUserId = UserID.getUid();
                                }
                                member.setPassword(passwroduser);
                                reff.child(currentUserId).child("password").setValue(passwroduser);
                                startActivity(new Intent(getApplicationContext(),Dashboard.class));
                            }else{
                                Toast.makeText(LoginActivity.this, "Cek Email Kamu lalu verifikasi akun"
                                        , Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(LoginActivity.this, " Gagal Login ", Toast.LENGTH_SHORT).show();
                    }
                });




            }

        });

        yukDaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pindahregister = (new Intent(LoginActivity.this, RegisterActivity.class));
                startActivity(pindahregister);
            }
        });

        lupapas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pindahLupaPas = (new Intent(LoginActivity.this, LupaPass.class));
                startActivity(pindahLupaPas);
            }
        });

    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(LoginActivity.this)
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
}
