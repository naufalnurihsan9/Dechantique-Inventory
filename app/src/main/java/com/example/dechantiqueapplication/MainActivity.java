//package com.example.dechantiqueapplication;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.appcompat.widget.SearchView;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.text.Editable;
//import android.text.TextWatcher;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.MenuItem;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//import android.view.Menu;
//
//import com.example.dechantiqueapplication.Users.Barang;
//import com.firebase.ui.database.FirebaseRecyclerAdapter;
//import com.firebase.ui.database.FirebaseRecyclerOptions;
//import com.google.android.gms.tasks.OnSuccessListener;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.Query;
//import com.google.firebase.database.ValueEventListener;
//import com.google.firebase.storage.FirebaseStorage;
//import com.google.firebase.storage.StorageReference;
//import com.squareup.picasso.Picasso;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class MainActivity extends AppCompatActivity  {
//    private RecyclerView mRecyclerView;
////    private ImageAdapter mAdapter;
//
//    private  List<Barang> mBarang;
//    private DatabaseReference reff;
//    private FirebaseStorage mStorage;
//    private ValueEventListener mDBListener;
//    EditText searchtext;
//    Button TambahBarang;
//    FirebaseAuth fAuth;
//    FirebaseRecyclerOptions<Barang> options;
//    FirebaseRecyclerAdapter<Barang,ListBarangAdapter> adapterbaru;
//    ImageAdapter mAdapter;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        fAuth = FirebaseAuth.getInstance();
//        TambahBarang = findViewById(R.id.btnkeTbhBarang);
//        mRecyclerView = findViewById(R.id.recyclerview);
//        searchtext = findViewById(R.id.Searchbartext);
//        mRecyclerView.setHasFixedSize(true);
//        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
//        mBarang = new ArrayList<>();
//        mAdapter = new ImageAdapter(MainActivity.this ,mBarang);
//        mRecyclerView.setAdapter(mAdapter);
//
////        mAdapter.setOnItemClickListener(MainActivity.this);
//
//        mStorage = FirebaseStorage.getInstance();
//        reff = FirebaseDatabase.getInstance().getReference().child("Produk");
//
//
//
//        searchtext.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                if (s.toString() != null){
//                    LoadData(s.toString());
//                }else{
//                    LoadData("");
//                }
//            }
//        });
//
////
////       mDBListener= reff.addValueEventListener(new ValueEventListener() {
////            @Override
////            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
////                mBarang.clear();
////                for(DataSnapshot postSnapshot: dataSnapshot.getChildren()){
////                    Barang barang = postSnapshot.getValue(Barang.class);
////                    mBarang.add(barang);
////                }
////                mAdapter.notifyDataSetChanged();
////            }
////
////            @Override
////            public void onCancelled(@NonNull DatabaseError databaseError) {
////                Toast.makeText(MainActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
////            }
////        });
//
//
//
//        TambahBarang.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(MainActivity.this , TambahProdukActivity.class);
//                startActivity(intent);
//            }
//
//        });
//
//
//        LoadData("");
//    }
//
//    private void LoadData(String data){
//        Query query = reff.orderByChild("namabarang").startAt(data).endAt(data+"\uf8ff");
//        options =     new FirebaseRecyclerOptions.Builder<Barang>().setQuery(query,Barang.class).build();
//        adapterbaru = new FirebaseRecyclerAdapter<Barang, ListBarangAdapter>(options) {
//            @Override
//            protected void onBindViewHolder(@NonNull ListBarangAdapter holder, final int position, @NonNull Barang model) {
//                holder.textView.setText(model.getNamabarang());
//                Picasso.get().load(model.getImageUrl()).into(holder.imageView);
//                holder.itemView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Intent intent = new Intent(getApplicationContext(), Detail_Barang.class);
//                        intent.putExtra("ProdukKey", getRef(position).getKey());
//                        startActivity(intent);
//                    }
//                });
//            }
//
//            @NonNull
//            @Override
//            public ListBarangAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//
//                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.listproduk, parent, false);
//                return new ListBarangAdapter(v);
//            }
//        };
//        adapterbaru.startListening();
//        mRecyclerView.setAdapter(adapterbaru);
//    }
//
//
//    private void filter(String text){
//        ArrayList<Barang> filteredList = new ArrayList<>();
//
//        for (Barang item : mBarang){
//            if (item.getNamabarang().toLowerCase().contains(text.toLowerCase())){
//                filteredList.add(item);
//            }
//        }
////        mAdapter.filterList(filteredList);
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.main_menu, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        switch (item.getItemId()){
//            case R.id.menuprofile:
//                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
//                startActivity(intent);
//                return true;
//            case R.id.menulougout:
//                fAuth.signOut();
//                Intent keLogin = new Intent(MainActivity.this, LoginActivity.class);
//                startActivity(keLogin);
//                return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }
//
////    @Override
////    public void onItemClick(int position) {
////        Barang selectedItem = mBarang.get(position);
////        final String selectedKey = selectedItem.getKodebarang();
//////        Intent keDetail = new Intent(MainActivity.this, Detail_Barang.class);
////        Intent keDetail = new Intent(getApplicationContext(), Detail_Barang.class);
////        keDetail.putExtra("key", selectedKey);
////        startActivity(keDetail);
////
////        Toast.makeText(MainActivity.this, " On Item Click di Gambar ke : " + position, Toast.LENGTH_SHORT).show();
////    }
////
////    @Override
////    public void onWhateverClick(int position) {
////        Toast.makeText(MainActivity.this, " Whatever Click di Gambar ke : " + position, Toast.LENGTH_SHORT).show();
////    }
//
//    @Override
//    public void onDeleteClick(int position) {
//        Barang selectedItem = mBarang.get(position);
//        final String selectedKey = selectedItem.getKodebarang();
//
//        StorageReference imageRef = mStorage.getReferenceFromUrl(selectedItem.getImageUrl());
//        imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
//            @Override
//            public void onSuccess(Void aVoid) {
//             reff.child(selectedKey).removeValue();
//             Toast.makeText(MainActivity.this, " Berhasil Hapus Produk", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        reff.removeEventListener(mDBListener);
////    }
////}
////
