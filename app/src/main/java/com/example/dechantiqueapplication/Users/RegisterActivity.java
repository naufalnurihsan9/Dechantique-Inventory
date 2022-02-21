package com.example.dechantiqueapplication.Users;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dechantiqueapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

public class RegisterActivity extends AppCompatActivity {
    private static final String TAG = "TAG" ;
    EditText nama,username,email,noTlp,password;
    TextView yukLogin;
    Button daftar;
    FirebaseAuth fAuth;
    FirebaseFirestore firebaseFirestore;
    DatabaseReference reff;
    FirebaseUser userID;
    Member member;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        username =findViewById(R.id.etuserName);
        nama =findViewById(R.id.etNama);
        email= findViewById(R.id.etEmail);
        noTlp= findViewById(R.id.etNoTlp);
        password= findViewById(R.id.etPassword);
        daftar= findViewById(R.id.btnDftrSkrng);
        yukLogin = findViewById(R.id.tvYukLogin);


        member = new Member();
        reff= FirebaseDatabase.getInstance().getReference().child("User");
        fAuth =FirebaseAuth.getInstance();
        firebaseFirestore=FirebaseFirestore.getInstance();


        yukLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pindah2 = (new Intent(RegisterActivity.this, LoginActivity.class));
                startActivity(pindah2);
            }
        });


        daftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String passworddaftar = password.getText().toString().trim();
                final String emaildaftar = email.getText().toString();
                final String userdaftar = nama.getText().toString();
                final String unamedaftar = username.getText().toString();
                final String noTlpdaftar = noTlp.getText().toString();

                if (TextUtils.isEmpty(emaildaftar)) {
                    email.setError("Masukan Email Anda.");
                    return;
                }
                if (TextUtils.isEmpty(passworddaftar)) {
                    password.setError("Masukan Password Anda.");
                    return;
                }

                if(passworddaftar.length() < 6 ){
                    password.setError(" Password Minimal 6 karakter.");
                    return;
                }


                fAuth.createUserWithEmailAndPassword(emaildaftar,passworddaftar).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){
                            fAuth.getCurrentUser().sendEmailVerification()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {

                                                Toast.makeText(RegisterActivity.this, "Registered successfully. Please check your email for verification",
                                                        Toast.LENGTH_LONG).show();
                                            }else {
                                                Toast.makeText(RegisterActivity.this,  task.getException().getMessage(),
                                                        Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });

                            member.setEmail(emaildaftar);
                            member.setNoTlp(noTlpdaftar);
                            member.setNama(userdaftar);
                            member.setUsername(unamedaftar);
                            member.setPassword(passworddaftar);
                            userID = fAuth.getCurrentUser();
                            reff.child(userID.getUid()).setValue(member);

                            Toast.makeText(RegisterActivity.this, " Berhasil Daftar ", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                            intent.putExtra("phoneNo", noTlpdaftar);
                            startActivity(intent);

                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(RegisterActivity.this, " Gagal Daftar ", Toast.LENGTH_SHORT).show();
                    }
                });



            }
        });
    }





}
