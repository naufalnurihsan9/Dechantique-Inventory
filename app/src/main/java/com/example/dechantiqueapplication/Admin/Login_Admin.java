package com.example.dechantiqueapplication.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dechantiqueapplication.R;
import com.example.dechantiqueapplication.Users.Dashboard;
import com.example.dechantiqueapplication.Users.LoginActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Login_Admin extends AppCompatActivity {

    EditText email_admin, password_admin;
    TextView keLoginUsers;
    Button loginAdmin;
    DatabaseReference reffAdmin;
    String EmailAdmin_benar, PasswordAdmin_benar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login__admin);

        email_admin = findViewById(R.id.etEmailLoginAdmin);
        password_admin = findViewById(R.id.etPasswordLoginAdmin);
        loginAdmin= findViewById(R.id.btnDiLoginAdmin);
        keLoginUsers = findViewById(R.id.keUsers);

        reffAdmin = FirebaseDatabase.getInstance().getReference().child("Admin");


        keLoginUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login_Admin.this, LoginActivity.class));
            }
        });


        final String Textemail_admin = email_admin.getText().toString().trim();
        final String Textpassword_admin = password_admin.getText().toString().trim();

    reffAdmin.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            for (DataSnapshot ds : dataSnapshot.getChildren()){
                daftarAdmin iniAdmin = ds.getValue(daftarAdmin.class);

//                email_admin.setText(iniAdmin.getEmailAdmin());
//                password_admin.setText(iniAdmin.getPasswordAdmin());
                EmailAdmin_benar = iniAdmin.getEmailAdmin();
                PasswordAdmin_benar = iniAdmin.getPasswordAdmin();
                String status = iniAdmin.getStatus();
                Toast.makeText(Login_Admin.this,"status anda" + status, Toast.LENGTH_SHORT ).show();
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    });



    loginAdmin.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (EmailAdmin_benar.equals(email_admin.getText().toString()) && PasswordAdmin_benar.equals(password_admin.getText().toString())){

                reffAdmin.child("Admin1").child("status").setValue("Sedang Login");
                Intent intent = new Intent(Login_Admin.this, Dashboard_Admin.class);
                Toast.makeText(Login_Admin.this, "Login Berhasil", Toast.LENGTH_LONG).show();
                startActivity(intent);
            }else{
                Toast.makeText(Login_Admin.this, "Email atau Password Salah !!", Toast.LENGTH_LONG).show();
            }
        }
});



    }

}