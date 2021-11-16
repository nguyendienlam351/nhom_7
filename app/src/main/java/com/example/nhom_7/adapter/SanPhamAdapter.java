package com.example.nhom_7.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nhom_7.R;
import com.example.nhom_7.model.SanPham;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class SanPhamAdapter extends RecyclerView.Adapter<SanPhamAdapter.SanPhamViewHolder> {
    private Activity context;
    private int layoutID;
    private ArrayList<SanPham> sanPhamArrayList;
    private MonItemClickListener delegation;
    private StorageReference storage;

    public void setDelegation(MonItemClickListener delegation) {
        this.delegation = delegation;
    }

    public SanPhamAdapter(Activity context, int layoutID, ArrayList<SanPham> sanPhamArrayList) {
        this.context = context;
        this.layoutID = layoutID;
        this.sanPhamArrayList = sanPhamArrayList;
        storage = FirebaseStorage.getInstance().getReference("SanPham");
    }


    @NonNull
    @Override
    public SanPhamViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = context.getLayoutInflater();
        CardView view = (CardView) layoutInflater.inflate(viewType, parent, false);
        return new SanPhamViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SanPhamViewHolder holder, int position) {
        SanPham sanPham = sanPhamArrayList.get(position);

        getAnh(sanPham.getAnh(),holder.imgAnh);

        holder.tvTen.setText(sanPham.getTen());
        NumberFormat formatter = new DecimalFormat("#,###,###");
        holder.tvGia.setText(formatter.format(sanPham.getGia()) + " đ");

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(delegation != null){
                    delegation.itemClick(sanPham);
                }
                else {
                    Toast.makeText(context, "you must set delegation before", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void getAnh(String anh, ImageView imgMon) {
        int dot = anh.lastIndexOf('.');
        String base = (dot == -1) ? anh : anh.substring(0, dot);
        String extension = (dot == -1) ? "" : anh.substring(dot + 1);
        try {
            final File file = File.createTempFile(base, extension);
            storage.child(anh).getFile(file)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                            imgMon.setImageBitmap(bitmap);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public int getItemViewType(int position) {
        return layoutID;
    }

    @Override
    public int getItemCount() {
        return sanPhamArrayList.size();
    }

    public void filterList(ArrayList<SanPham> sanPhamArrayList){
        this.sanPhamArrayList = sanPhamArrayList;
        notifyDataSetChanged();
    }

    public void sortList(Comparator<SanPham> sanPhamComparator){
        Collections.sort(sanPhamArrayList, sanPhamComparator);
        notifyDataSetChanged();
    }

    public static class SanPhamViewHolder extends RecyclerView.ViewHolder {
        public ImageView imgAnh;
        public TextView tvTen;
        public TextView tvGia;

        public SanPhamViewHolder(@NonNull View itemView) {
            super(itemView);
            imgAnh = itemView.findViewById(R.id.imgAnh);
            tvTen = itemView.findViewById(R.id.tvTen);
            tvGia = itemView.findViewById(R.id.tvGia);
        }
    }

    public interface MonItemClickListener {
        public void itemClick(SanPham sanPham);
    }
}
