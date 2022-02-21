package com.example.dechantiqueapplication.Users;

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dechantiqueapplication.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder>  {
    @NonNull

    private Context mcontext;
    private List<Barang> mBarang;
    private List<Barang> mBarangFull;


    private OnItemClickListener mListener;

    public ImageAdapter(Context context, List<Barang> barang){
        mcontext = context;
        mBarang = barang;
        mBarangFull = new ArrayList<>(barang);

    }

    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.listproduk,parent, false );
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, final int position) {
        Barang uploadCurrent = mBarang.get(position);
        holder.textViewName.setText(uploadCurrent.getNamabarang());
        Picasso.get()
                .load(uploadCurrent.getImageUrl())
                .placeholder(R.mipmap.ic_launcher)
                .fit()
                .into(holder.imageView);

    }

    @Override
    public int getItemCount(){
        return mBarang.size();
    }

    public void filterList(ArrayList<Barang> filteredList){
        mBarang = filteredList;
        notifyDataSetChanged();
    }
    public class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener ,
            View.OnCreateContextMenuListener , MenuItem.OnMenuItemClickListener {
        public TextView textViewName;
        public ImageView imageView;
        LinearLayout linearLayout;



        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewName = itemView.findViewById(R.id.tvlistproduk);
            imageView = itemView.findViewById(R.id.imageViewlist);
            linearLayout = itemView.findViewById(R.id.layout);

            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onClick(View v) {
            if(mListener != null){
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION){
                    mListener.onItemClick(position);
                }
            }
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderTitle("Select Action");
            MenuItem deleteItem = menu.add(Menu.NONE, 2,2 , "Hapus Produk");

            deleteItem.setOnMenuItemClickListener(this);
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            if(mListener != null){
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION){
                    switch (item.getItemId()){
                        case 2:
                            mListener.onDeleteClick(position);
                            return true;
                    }
                }
            }
            return false;
        }
    }

    public interface OnItemClickListener{
        void onItemClick(int position);


        void onWhateverClick(int position);

        void onDeleteClick(int position);

    }

    public void setOnItemClickListener( OnItemClickListener listener){
            mListener = listener;
    }
}
