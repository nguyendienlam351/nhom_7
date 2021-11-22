package com.example.nhom_7;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

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

public class AdapterSanPham extends RecyclerView.Adapter<AdapterSanPham.MyViewHolder> {
    Activity conText;
    int layoutID;
    ArrayList<SanPham> data;
    private MyItemClickListener delegation;

    public void setDelegation(MyItemClickListener delegation) {
        this.delegation = delegation;
    }


    public AdapterSanPham(Activity conText, int layoutID, ArrayList<SanPham> data) {
        this.data = data;
        this.layoutID = layoutID;
        this.conText = conText;
//        storage = FirebaseStorage.getInstance().getReference("SanPham");

    }
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference("SanPham");

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = conText.getLayoutInflater();
        CardView view = (CardView) layoutInflater.inflate(viewType, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        SanPham sanPham = data.get(position);
        getAnhSP(sanPham.getAnh(),holder.imgAnhSP);
        holder.tvTenSP.setText(sanPham.getTen());
        holder.tvLoaiSP.setText(sanPham.getLoai());
        NumberFormat formatter = new DecimalFormat("#,###,###");
        holder.tvGiaSP.setText(formatter.format(sanPham.getGia()) + " đ");
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Chuyển màn hình
                Intent intent = new Intent(conText.getApplicationContext(), ChiTietSanPham.class);
                Bundle bundle = new Bundle();

                bundle.putString("MaSanPham", sanPham.getMaSanPham());
                intent.putExtras(bundle);
                conText.startActivity(intent);
            }
        });
    }
    //Ảnh
    private void getAnhSP(String anhSP, ImageView imgAnhSP) {
        int dot = anhSP.lastIndexOf('.');
        String base = (dot == -1) ? anhSP : anhSP.substring(0, dot);
        String extension = (dot == -1) ? "" : anhSP.substring(dot + 1);
        try {
            final File file = File.createTempFile(base, extension);
            storageRef.child(anhSP).getFile(file)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                            imgAnhSP.setImageBitmap(bitmap);
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
        return data.size();
    }

    //Search
    public void filterList(ArrayList<SanPham> sanPham) {
        this.data = sanPham;
        notifyDataSetChanged();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvTenSP, tvLoaiSP, tvGiaSP;
        ImageView imgAnhSP;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imgAnhSP = itemView.findViewById(R.id.imgAnhSP);
            tvTenSP = itemView.findViewById(R.id.tvTenSP);
            tvLoaiSP = itemView.findViewById(R.id.tvLoaiSP);
            tvGiaSP = itemView.findViewById(R.id.tvGiaSP);
        }
    }

    public interface MyItemClickListener {
        void getThongTinDMK(SanPham sanPham);
    }

}
