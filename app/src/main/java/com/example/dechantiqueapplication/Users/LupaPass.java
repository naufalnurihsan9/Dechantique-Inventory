package com.example.dechantiqueapplication.Users;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.dechantiqueapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class LupaPass extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    private Button Lupapass;
    private EditText EmailLupa;
    private ImageView back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lupa_pass);

        firebaseAuth = FirebaseAuth.getInstance();
        Lupapass = findViewById(R.id.lupapass);
        EmailLupa = findViewById(R.id.etEmaillupa);
        back = findViewById(R.id.backKeLogin_act);

        Lupapass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.sendPasswordResetEmail(EmailLupa.getText().toString().trim())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    Toast.makeText(LupaPass.this,
                                            "Link Ubah Password telah dikirim ke Email kamu ", Toast.LENGTH_LONG).show();
                                    firebaseAuth.signOut();
                                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                    startActivity(intent);
                                }else{
                                    Toast.makeText(LupaPass.this,
                                            task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                }

                            }
                        });
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent back = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(back);
            }
        });


    }
}