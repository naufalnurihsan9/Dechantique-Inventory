package com.example.dechantiqueapplication.Users;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.example.dechantiqueapplication.Admin.TambahProdukActivity;
import com.example.dechantiqueapplication.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MainActivity2 extends AppCompatActivity  {
    ImageView filterberdasarkanKatagori;
    EditText inputSearch;
    RecyclerView recyclerView;
    FloatingActionButton floatingbtn;
    private ValueEventListener mDBListener;
    FirebaseRecyclerOptions<Barang> options;
    FirebaseRecyclerAdapter<Barang,ListBarangAdapter>adapter;
    private List<Barang> mBarang;  ListBarangAdapter mAdapter;
    DatabaseReference Dataref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        Dataref= FirebaseDatabase.getInstance().getReference().child("Produk");

        inputSearch=findViewById(R.id.inputSearch);
        recyclerView=findViewById(R.id.recylerView);


        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setHasFixedSize(true);


        LoadData("");



        filterberdasarkanKatagori = findViewById(R.id.filterKatagori);

        filterberdasarkanKatagori.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(MainActivity2.this, v);
                popupMenu.inflate(R.menu.semua_katagori);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.Semuakatagori1:
                                LoadData("");
                                return true;
                            case R.id.katagoriDress1:
                               filterKatagoriDressPesta();
                                return true;
                            case R.id.katagoriCasual1:
                                filterKatagoriDressCasual();
                                return true;
                            case R.id.katagoriTunik1:
                                filterKatagoriTunik();
                                return true;
                            case R.id.katagoriOuter1:
                                filterKatagoriOuter();
                                return true;
                        }

                        return false;
                    }
                });
                popupMenu.show();
            }
        });


        inputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString()!=null)
                {
                    LoadData(s.toString());
                }
                else
                {
                    LoadData("");
                }

            }
        });


    }

    private void LoadData(String data) {
        Query query=Dataref.orderByChild("namabarang").startAt(data).endAt(data+"\uf8ff");

        options=new FirebaseRecyclerOptions.Builder<Barang>().setQuery(query,Barang.class).build();
        adapter=new FirebaseRecyclerAdapter<Barang, ListBarangAdapter>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ListBarangAdapter holder, final int position, @NonNull final Barang model) {
                holder.textView.setText(model.getNamabarang());
                Picasso.get().load(model.getImageUrl()).fit().into(holder.imageView);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(MainActivity2.this,Detail_Barang.class);
                        intent.putExtra("ProdukKey",getRef(position).getKey());
                        startActivity(intent);
                    }
                });

            }

            @NonNull
            @Override
            public ListBarangAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.listproduk,parent,false);
                return new ListBarangAdapter(v);
            }
        };
        adapter.startListening();
        recyclerView.setAdapter(adapter);


    }

    private void filterKatagoriDressPesta(){

        Query query=Dataref.orderByChild("katagori").equalTo("Dress Pesta");
        options=new FirebaseRecyclerOptions.Builder<Barang>().setQuery(query,Barang.class).build();
        adapter=new FirebaseRecyclerAdapter<Barang, ListBarangAdapter>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ListBarangAdapter holder, final int position, @NonNull final Barang model) {
                holder.textView.setText(model.getNamabarang());
                Picasso.get().load(model.getImageUrl()).fit().into(holder.imageView);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(MainActivity2.this,Detail_Barang.class);
                        intent.putExtra("ProdukKey",getRef(position).getKey());
                        startActivity(intent);
                    }
                });

            }

            @NonNull
            @Override
            public ListBarangAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.listproduk,parent,false);
                return new ListBarangAdapter(v);
            }
        };
        adapter.startListening();
        recyclerView.setAdapter(adapter);

    }

    private void filterKatagoriTunik(){

        Query query=Dataref.orderByChild("katagori").equalTo("Tunik");
        options=new FirebaseRecyclerOptions.Builder<Barang>().setQuery(query,Barang.class).build();
        adapter=new FirebaseRecyclerAdapter<Barang, ListBarangAdapter>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ListBarangAdapter holder, final int position, @NonNull final Barang model) {
                holder.textView.setText(model.getNamabarang());
                Picasso.get().load(model.getImageUrl()).fit().into(holder.imageView);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(MainActivity2.this,Detail_Barang.class);
                        intent.putExtra("ProdukKey",getRef(position).getKey());
                        startActivity(intent);
                    }
                });

            }

            @NonNull
            @Override
            public ListBarangAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.listproduk,parent,false);
                return new ListBarangAdapter(v);
            }
        };
        adapter.startListening();
        recyclerView.setAdapter(adapter);

    }

    private void filterKatagoriOuter(){

        Query query=Dataref.orderByChild("katagori").equalTo("Outer");
        options=new FirebaseRecyclerOptions.Builder<Barang>().setQuery(query,Barang.class).build();
        adapter=new FirebaseRecyclerAdapter<Barang, ListBarangAdapter>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ListBarangAdapter holder, final int position, @NonNull final Barang model) {
                holder.textView.setText(model.getNamabarang());
                Picasso.get().load(model.getImageUrl()).fit().into(holder.imageView);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(MainActivity2.this,Detail_Barang.class);
                        intent.putExtra("ProdukKey",getRef(position).getKey());
                        startActivity(intent);
                    }
                });

            }

            @NonNull
            @Override
            public ListBarangAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.listproduk,parent,false);
                return new ListBarangAdapter(v);
            }
        };
        adapter.startListening();
        recyclerView.setAdapter(adapter);

    }

    private void filterKatagoriDressCasual(){

        Query query=Dataref.orderByChild("katagori").equalTo("Dress Casual");
        options=new FirebaseRecyclerOptions.Builder<Barang>().setQuery(query,Barang.class).build();
        adapter=new FirebaseRecyclerAdapter<Barang, ListBarangAdapter>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ListBarangAdapter holder, final int position, @NonNull final Barang model) {
                holder.textView.setText(model.getNamabarang());
                Picasso.get().load(model.getImageUrl()).fit().into(holder.imageView);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(MainActivity2.this,Detail_Barang.class);
                        intent.putExtra("ProdukKey",getRef(position).getKey());
                        startActivity(intent);
                    }
                });

            }

            @NonNull
            @Override
            public ListBarangAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.listproduk,parent,false);
                return new ListBarangAdapter(v);
            }
        };
        adapter.startListening();
        recyclerView.setAdapter(adapter);

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent kembali= new Intent(MainActivity2.this, Dashboard.class);
        startActivity(kembali);
    }


}
