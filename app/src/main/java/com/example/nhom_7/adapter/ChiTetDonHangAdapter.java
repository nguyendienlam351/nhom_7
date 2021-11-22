package com.example.nhom_7.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nhom_7.R;
import com.example.nhom_7.model.ChiTietDH;
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

public class ChiTetDonHangAdapter extends RecyclerView.Adapter<ChiTetDonHangAdapter.MyViewHolder>{
    private Activity context;
    private int layoutID;
    private ArrayList<ChiTietDH> chiTietDHArrayList;
    private StorageReference storage;

    public ChiTetDonHangAdapter(Activity context, int layoutID, ArrayList<ChiTietDH> chiTietDHArrayList){
        this.context= context;
        this.layoutID=layoutID;
        this.chiTietDHArrayList=chiTietDHArrayList;
        storage = FirebaseStorage.getInstance().getReference("SanPham");
    }
    @NonNull
    @Override
    public ChiTetDonHangAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = context.getLayoutInflater();
        CardView view =(CardView) layoutInflater.inflate(viewType, parent, false);
        return new ChiTetDonHangAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChiTetDonHangAdapter.MyViewHolder holder, int position) {
        ChiTietDH chiTietDH = chiTietDHArrayList.get(position);
        if(chiTietDH==null){
            return;
        }

        holder.tvSanPham.setText(chiTietDH.getTen());
        NumberFormat formatter = new DecimalFormat("#,###,###");
        holder.tvGia.setText(formatter.format(chiTietDH.getGia()) + " đ");
        holder.tvSoLuong.setText(String.valueOf(chiTietDH.getSoLuong()));
        holder.tvSize.setText(chiTietDH.getSize());
        getAnhMon(chiTietDH.getAnh(),holder.imvSanPham);
    }
    private void getAnhMon(String anh, ImageView imvSanPham) {
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
                            imvSanPham.setImageBitmap(bitmap);
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
        return chiTietDHArrayList.size();
    }

    public static class  MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvSanPham,tvGia,tvSoLuong,tvSize;
        ImageView imvSanPham;

        public MyViewHolder(@NonNull View itemView){
            super(itemView);
            tvSanPham=itemView.findViewById(R.id.tvSanPham);
            tvGia=itemView.findViewById(R.id.tvGia);
            tvSoLuong=itemView.findViewById(R.id.tvSoLuong);
            tvSize=itemView.findViewById(R.id.tvSize);
            imvSanPham=itemView.findViewById(R.id.imvSanPham);
        }
    }
}
