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

public class ChiTietDonHangAdapter extends RecyclerView.Adapter<ChiTietDonHangAdapter.ChiTietDonHangViewHolder>{
    private Activity context;
    private int layoutID;
    private ArrayList<ChiTietDH> chiTietDHArrayList;
    private ChiTietDonHangClickListener delegation;
    private StorageReference storage;

    public void setDelegation(ChiTietDonHangClickListener delegation) {
        this.delegation = delegation;
    }

    public ChiTietDonHangAdapter(Activity context, int layoutID, ArrayList<ChiTietDH> chiTietDHArrayList) {
        this.context = context;
        this.layoutID = layoutID;
        this.chiTietDHArrayList = chiTietDHArrayList;
        storage = FirebaseStorage.getInstance().getReference("SanPham");
    }


    @NonNull
    @Override
    public ChiTietDonHangViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = context.getLayoutInflater();
        CardView view = (CardView) layoutInflater.inflate(viewType, parent, false);
        return new ChiTietDonHangViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChiTietDonHangViewHolder holder, int position) {
        ChiTietDH chiTietDH = chiTietDHArrayList.get(position);

        getAnhMon(chiTietDH.getAnh(),holder.imgAnh);

        holder.tvTen.setText(chiTietDH.getTen());

        NumberFormat formatter = new DecimalFormat("#,###,###");
        holder.tvGia.setText(formatter.format(chiTietDH.getGia()) + " đ");

        holder.tvSoLuong.setText(String.valueOf(chiTietDH.getSoLuong()));

        holder.tvSize.setText(chiTietDH.getSize());

        holder.btnTang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(delegation != null){
                    delegation.tangSoLuong(chiTietDH);
                }
                else {
                    Toast.makeText(context, "you must set delegation before", Toast.LENGTH_SHORT).show();
                }
            }
        });

        holder.btnGiam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(delegation != null){
                    delegation.giamSoLuong(chiTietDH);
                }
                else {
                    Toast.makeText(context, "you must set delegation before", Toast.LENGTH_SHORT).show();
                }
            }
        });

        holder.btnXoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(delegation != null){
                    delegation.iconClick(chiTietDH);
                }
                else {
                    Toast.makeText(context, "you must set delegation before", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //Lấy ảnh
    private void getAnhMon(String anh, ImageView imgMon) {
        //Cắt chuỗi tên ảnh
        int dot = anh.lastIndexOf('.');
        String base = (dot == -1) ? anh : anh.substring(0, dot);
        String extension = (dot == -1) ? "" : anh.substring(dot + 1);

        //Lấy ảnh firebase
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
        return chiTietDHArrayList.size();
    }

    public static class ChiTietDonHangViewHolder extends RecyclerView.ViewHolder {
        public ImageView imgAnh;
        public TextView tvTen;
        public TextView tvGia;
        public ImageButton btnTang;
        public ImageButton btnGiam;
        public TextView tvSoLuong;
        public ImageButton btnXoa;
        public TextView tvSize;

        public ChiTietDonHangViewHolder(@NonNull View itemView) {
            super(itemView);
            imgAnh = itemView.findViewById(R.id.imgAnh);
            tvTen = itemView.findViewById(R.id.tvTen);
            tvGia = itemView.findViewById(R.id.tvGia);
            btnTang = itemView.findViewById(R.id.btnTang);
            btnGiam = itemView.findViewById(R.id.btnGiam);
            tvSoLuong = itemView.findViewById(R.id.tvSoLuong);
            btnXoa = itemView.findViewById(R.id.btnXoa);
            tvSize = itemView.findViewById(R.id.tvSize);
        }
    }
    public interface ChiTietDonHangClickListener{
        public void iconClick(ChiTietDH chiTietDH);
        public void tangSoLuong(ChiTietDH chiTietDH);
        public void giamSoLuong(ChiTietDH chiTietDH);
    }
}
