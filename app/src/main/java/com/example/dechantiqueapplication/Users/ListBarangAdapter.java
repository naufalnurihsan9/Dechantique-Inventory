package com.example.dechantiqueapplication.Users;

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

public class ListBarangAdapter extends RecyclerView.ViewHolder {
    ImageView imageView;
    TextView textView;



    public ListBarangAdapter(@NonNull View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.imageViewlist);
        textView = itemView.findViewById(R.id.tvlistproduk);

    }
}






