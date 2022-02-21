package com.example.dechantiqueapplication.Admin;

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


public class MainActivity_Admin extends AppCompatActivity {

    EditText inputSearch;
    RecyclerView recyclerView;
    List<Barang_Admin> mBarang;
    FloatingActionButton floatingbtn;
    ImageView filterKatagori;
    private ValueEventListener mDBListener2;
    private FirebaseStorage mStorage1;
    FirebaseRecyclerOptions<Barang_Admin> options;
    FirebaseRecyclerAdapter<Barang_Admin, ListBarangAdapter_Admin> adapter;
    DatabaseReference Dataref;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent pindahyuk = new Intent(MainActivity_Admin.this, Dashboard_Admin.class);
        startActivity(pindahyuk);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainactivity_admin);

        Dataref = FirebaseDatabase.getInstance().getReference().child("Produk");
        inputSearch = findViewById(R.id.inputSearch);
        recyclerView = findViewById(R.id.recylerView);
        floatingbtn = findViewById(R.id.floatingbtn);
        filterKatagori = findViewById(R.id.filterKatagori);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setHasFixedSize(true);
        mBarang = new ArrayList<>();
        mStorage1    = FirebaseStorage.getInstance();

        floatingbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),TambahProdukActivity.class));
            }
        });

        LoadData("");

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

    filterKatagori.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            PopupMenu popupMenu = new PopupMenu(MainActivity_Admin.this, v);
            popupMenu.inflate(R.menu.semua_katagori);
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.getItemId()){
                        case R.id.katagoriDress1:
                            filterDressPesta();
                            return true;
                        case R.id.katagoriCasual1:
                          filterDressCasual();
                            return true;
                        case R.id.katagoriTunik1:
                            Tunik();
                            return true;
                        case R.id.katagoriOuter1:
                            Outer();
                            return true;
                        case R.id.Semuakatagori1:
                            LoadData("");

                    }
                    return false;
                }
            });
            popupMenu.show();
        }
    });


    }

    private void LoadData(String data) {
        Query query=Dataref.orderByChild("namabarang").startAt(data).endAt(data+"\uf8ff");

        options=new FirebaseRecyclerOptions.Builder<Barang_Admin>().setQuery(query, Barang_Admin.class).build();
        adapter=new FirebaseRecyclerAdapter<Barang_Admin, ListBarangAdapter_Admin>(options) {

            @Override
            protected void onBindViewHolder(@NonNull ListBarangAdapter_Admin holder, final int position, @NonNull final Barang_Admin model) {
                holder.textView.setText(model.getNamabarang());
                Picasso.get().load(model.getImageUrl()).fit().into(holder.imageView);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(MainActivity_Admin.this,EditDetailBarang_Admin.class);
                        intent.putExtra("ProdukKey",getRef(position).getKey());
                        startActivity(intent);
                    }
                });
                holder.mDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new AlertDialog.Builder(MainActivity_Admin.this)
                                .setIcon(R.mipmap.ic_launcher).setTitle(R.string.app_name)
                                .setMessage("Yakin ingin Hapus ?")
                                .setPositiveButton("Yaa", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        final String selectedkey = model.getKodebarang();
                                        StorageReference imagereff1 = mStorage1.getReferenceFromUrl(model.getImageUrl());
                                        imagereff1.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Dataref.child(selectedkey).removeValue();
                                                Toast.makeText(MainActivity_Admin.this, "Berhasil Hapus Produk", Toast.LENGTH_SHORT).show();
                                            }
                                        });

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
                });

            }

            @NonNull
            @Override
            public ListBarangAdapter_Admin onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.listproduk_admin,parent,false);
                return new ListBarangAdapter_Admin(v);
            }
        };
        adapter.startListening();
        recyclerView.setAdapter(adapter);

    }


    private  void filterDressPesta(){
        Query query=Dataref.orderByChild("katagori").equalTo("Dress Pesta");

        options=new FirebaseRecyclerOptions.Builder<Barang_Admin>().setQuery(query, Barang_Admin.class).build();
        adapter=new FirebaseRecyclerAdapter<Barang_Admin, ListBarangAdapter_Admin>(options) {

            @Override
            protected void onBindViewHolder(@NonNull ListBarangAdapter_Admin holder, final int position, @NonNull final Barang_Admin model) {
                holder.textView.setText(model.getNamabarang());
                Picasso.get().load(model.getImageUrl()).fit().into(holder.imageView);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(MainActivity_Admin.this,EditDetailBarang_Admin.class);
                        intent.putExtra("ProdukKey",getRef(position).getKey());
                        startActivity(intent);
                    }
                });
                holder.mDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new AlertDialog.Builder(MainActivity_Admin.this)
                                .setIcon(R.mipmap.ic_launcher).setTitle(R.string.app_name)
                                .setMessage("Yakin ingin Hapus ?")
                                .setPositiveButton("Yaa", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        final String selectedkey = model.getKodebarang();
                                        StorageReference imagereff1 = mStorage1.getReferenceFromUrl(model.getImageUrl());
                                        imagereff1.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Dataref.child(selectedkey).removeValue();
                                                Toast.makeText(MainActivity_Admin.this, "Berhasil Hapus Produk", Toast.LENGTH_SHORT).show();
                                            }
                                        });

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
                });

            }

            @NonNull
            @Override
            public ListBarangAdapter_Admin onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.listproduk_admin,parent,false);
                return new ListBarangAdapter_Admin(v);
            }
        };
        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }


    private  void filterDressCasual(){
        Query query=Dataref.orderByChild("katagori").equalTo("Dress Casual");

        options=new FirebaseRecyclerOptions.Builder<Barang_Admin>().setQuery(query, Barang_Admin.class).build();
        adapter=new FirebaseRecyclerAdapter<Barang_Admin, ListBarangAdapter_Admin>(options) {

            @Override
            protected void onBindViewHolder(@NonNull ListBarangAdapter_Admin holder, final int position, @NonNull final Barang_Admin model) {
                holder.textView.setText(model.getNamabarang());
                Picasso.get().load(model.getImageUrl()).fit().into(holder.imageView);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(MainActivity_Admin.this,EditDetailBarang_Admin.class);
                        intent.putExtra("ProdukKey",getRef(position).getKey());
                        startActivity(intent);
                    }
                });
                holder.mDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new AlertDialog.Builder(MainActivity_Admin.this)
                                .setIcon(R.mipmap.ic_launcher).setTitle(R.string.app_name)
                                .setMessage("Yakin ingin Hapus ?")
                                .setPositiveButton("Yaa", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        final String selectedkey = model.getKodebarang();

                                        StorageReference imagereff1 = mStorage1.getReferenceFromUrl(model.getImageUrl());
                                        imagereff1.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Dataref.child(selectedkey).removeValue();
                                                Toast.makeText(MainActivity_Admin.this, "Berhasil Hapus Produk", Toast.LENGTH_SHORT).show();
                                            }
                                        });

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
                });
            }

            @NonNull
            @Override
            public ListBarangAdapter_Admin onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.listproduk_admin,parent,false);
                return new ListBarangAdapter_Admin(v);
            }
        };
        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }



    private  void Tunik(){
        Query query=Dataref.orderByChild("katagori").equalTo("Tunik");

        options=new FirebaseRecyclerOptions.Builder<Barang_Admin>().setQuery(query, Barang_Admin.class).build();
        adapter=new FirebaseRecyclerAdapter<Barang_Admin, ListBarangAdapter_Admin>(options) {

            @Override
            protected void onBindViewHolder(@NonNull ListBarangAdapter_Admin holder, final int position, @NonNull final Barang_Admin model) {
                holder.textView.setText(model.getNamabarang());
                Picasso.get().load(model.getImageUrl()).fit().into(holder.imageView);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(MainActivity_Admin.this,EditDetailBarang_Admin.class);
                        intent.putExtra("ProdukKey",getRef(position).getKey());
                        startActivity(intent);
                    }
                });
                holder.mDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new AlertDialog.Builder(MainActivity_Admin.this)
                                .setIcon(R.mipmap.ic_launcher).setTitle(R.string.app_name)
                                .setMessage("Yakin ingin Hapus ?")
                                .setPositiveButton("Yaa", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        final String selectedkey = model.getKodebarang();
                                        StorageReference imagereff1 = mStorage1.getReferenceFromUrl(model.getImageUrl());
                                        imagereff1.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Dataref.child(selectedkey).removeValue();
                                                Toast.makeText(MainActivity_Admin.this, "Berhasil Hapus Produk", Toast.LENGTH_SHORT).show();
                                            }
                                        });

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
                });

            }

            @NonNull
            @Override
            public ListBarangAdapter_Admin onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.listproduk_admin,parent,false);
                return new ListBarangAdapter_Admin(v);
            }
        };
        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }


    private  void Outer(){
        Query query=Dataref.orderByChild("katagori").equalTo("Outer");

        options=new FirebaseRecyclerOptions.Builder<Barang_Admin>().setQuery(query, Barang_Admin.class).build();
        adapter=new FirebaseRecyclerAdapter<Barang_Admin, ListBarangAdapter_Admin>(options) {

            @Override
            protected void onBindViewHolder(@NonNull ListBarangAdapter_Admin holder, final int position, @NonNull final Barang_Admin model) {
                holder.textView.setText(model.getNamabarang());
                Picasso.get().load(model.getImageUrl()).fit().into(holder.imageView);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(MainActivity_Admin.this,EditDetailBarang_Admin.class);
                        intent.putExtra("ProdukKey",getRef(position).getKey());
                        startActivity(intent);
                    }
                });
                holder.mDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new AlertDialog.Builder(MainActivity_Admin.this)
                                .setIcon(R.mipmap.ic_launcher).setTitle(R.string.app_name)
                                .setMessage("Yakin ingin Hapus ?")
                                .setPositiveButton("Yaa", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {


                                        final String selectedkey = model.getKodebarang();
                                        StorageReference imagereff1 = mStorage1.getReferenceFromUrl(model.getImageUrl());
                                        imagereff1.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Dataref.child(selectedkey).removeValue();
                                                Toast.makeText(MainActivity_Admin.this, "Berhasil Hapus Produk", Toast.LENGTH_SHORT).show();
                                            }
                                        });

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
                });

            }

            @NonNull
            @Override
            public ListBarangAdapter_Admin onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.listproduk_admin,parent,false);
                return new ListBarangAdapter_Admin(v);
            }
        };
        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }
}
