package com.example.dechantiqueapplication.Admin;

import android.content.Context;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dechantiqueapplication.R;

import java.util.List;

public class ListBarangAdapter_Admin extends RecyclerView.ViewHolder {
    ImageView imageView;
    TextView textView;
    ImageView mDelete;


    public ListBarangAdapter_Admin(@NonNull View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.imageViewlist);
        textView = itemView.findViewById(R.id.tvlistproduk);
        mDelete = itemView.findViewById(R.id.hapusProduk_Admin);

    }
}






