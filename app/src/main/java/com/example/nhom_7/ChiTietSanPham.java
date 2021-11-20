package com.example.nhom_7;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nhom_7.model.LoaiSanPham;
import com.example.nhom_7.model.SanPham;
import com.example.nhom_7.model.Size;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

public class ChiTietSanPham extends AppCompatActivity {
    ImageView imgSanPham;
    TextView tvTenSanPham;
    TextView tvLoaiSanPham;
    TextView tvGia;
    TextView tvMonTa;
    TextView tvDanhGia;
    TextView tvSoLuong;
    RatingBar rbDanhGia;
    Spinner spnSize;
    DatabaseReference database;
    StorageReference storage;
    SanPham sanPham;
    ArrayList<Size> sizeArrayList;
    ArrayAdapter<Size> sizeArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_san_pham);

        setControl();
        setEvent();
    }

    private void setEvent() {
        database = FirebaseDatabase.getInstance().getReference();
        storage = FirebaseStorage.getInstance().getReference("SanPham");
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if (bundle != null) {
            String maSanPham = bundle.getString("maSanPham", "");
            getDataSanPham(maSanPham);
        }

        sizeArrayList = new ArrayList<Size>();
        sizeArrayAdapter = new ArrayAdapter<Size>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, sizeArrayList);
        sizeArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnSize.setAdapter(sizeArrayAdapter);

        spnSize.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tvSoLuong.setText("Số lượng: " +sizeArrayList.get(position).getSoLuong());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }


    private void getDataSanPham(String maSanPham) {
        database.child("SanPham").child(maSanPham).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                sanPham = snapshot.getValue(SanPham.class);
                tvTenSanPham.setText(sanPham.getTen());
                NumberFormat formatter = new DecimalFormat("#,###,###");
                tvGia.setText(formatter.format(sanPham.getGia()) + " đ");
                tvMonTa.setText("Mô tả\n" + sanPham.getMoTa());
                tvDanhGia.setText(sanPham.getDanhGia()+ "/5");
//                rbDanhGia.setRating(sanPham.getDanhGia());
                getAnhMon(sanPham.getAnh());
                getDataLoaiSanPham(sanPham.getLoai());
                sizeArrayList.clear();
                sizeArrayList.addAll(sanPham.getSize());
                sizeArrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getDataLoaiSanPham(String maLoai) {
        database.child("LoaiSanPham").child(maLoai).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                LoaiSanPham loaiSanPham = snapshot.getValue(LoaiSanPham.class);
                if(loaiSanPham!= null) {
                    tvLoaiSanPham.setText(loaiSanPham.getTenLoai());
                }
                else {
                    tvLoaiSanPham.setText("Lỗi loại sản phẩm");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getAnhMon(String anh) {
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
                            imgSanPham.setImageBitmap(bitmap);
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

    private void setControl() {
        imgSanPham = findViewById(R.id.imgSanPham);
        tvTenSanPham = findViewById(R.id.tvTenSanPham);
        tvLoaiSanPham = findViewById(R.id.tvLoaiSanPham);
        tvDanhGia = findViewById(R.id.tvDanhGia);
        rbDanhGia = findViewById(R.id.rbDanhGia);
        tvGia = findViewById(R.id.tvGia);
        tvMonTa = findViewById(R.id.tvMonTa);
        spnSize = findViewById(R.id.spnSize);
        tvSoLuong = findViewById(R.id.tvSoLuong);
    }
}