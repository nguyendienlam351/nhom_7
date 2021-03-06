package com.example.nhom_7;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nhom_7.model.ChiTietDH;
import com.example.nhom_7.model.LoaiSanPham;
import com.example.nhom_7.model.SanPham;
import com.example.nhom_7.model.TaiKhoan;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

public class XemChiTietSanPham extends AppCompatActivity {
    ImageView imgSanPham;
    TextView tvTenSanPham;
    TextView tvLoaiSanPham;
    TextView tvGia;
    TextView tvMonTa;
    TextView tvDanhGia;
    TextView tvSoLuong;
    Button btnThem;
    RatingBar rbDanhGia;
    Spinner spnSize;
    DatabaseReference database;
    StorageReference storage;
    SanPham sanPham;
    ArrayList<String> sizeArrayList;
    ArrayAdapter<String> sizeArrayAdapter;
    TaiKhoan taiKhoan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xem_chi_tiet_san_pham);

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

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        getDataGioHang(user.getUid());

        //Spinner size s???n ph???m
        sizeArrayList = new ArrayList<String>();
        sizeArrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, sizeArrayList);
        sizeArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnSize.setAdapter(sizeArrayAdapter);


        //Th??m v??o gi??? h??ng
        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!kiemtraTonTai(sanPham.getMaSanPham())) {
                    //T???o chi ti???t ????n h??ng
                    ChiTietDH chiTietDH = new ChiTietDH();
                    chiTietDH.setMaSanPham(sanPham.getMaSanPham());
                    chiTietDH.setTen(sanPham.getTen());
                    chiTietDH.setGia(sanPham.getGia());
                    chiTietDH.setAnh(sanPham.getAnh());
                    chiTietDH.setDanhGia(true);
                    chiTietDH.setSize(spnSize.getSelectedItem().toString());
                    chiTietDH.setSoLuong(1);
                    taiKhoan.getGioHang().add(0, chiTietDH);

                    database.child("TaiKhoan").child(taiKhoan.getMaKH()).child("gioHang").setValue(taiKhoan.getGioHang()).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(XemChiTietSanPham.this, "Th??m v??o gi??? h??ng th??nh c??ng", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(XemChiTietSanPham.this, "S???n ph???m ???? c?? trong gi??? h??ng", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    //Ki???m tra s???n ph???m t???n t???i trong ????n h??ng
    private boolean kiemtraTonTai(String maSanPham){
        for(ChiTietDH chiTietDH : taiKhoan.getGioHang()){
            if(chiTietDH.getMaSanPham().equals(maSanPham) && chiTietDH.getSize().equals(spnSize.getSelectedItem().toString())){
                return true;
            }
        }

        return false;
    }


    //L???y d??? li???u gi??? h??ng
    private  void getDataGioHang(String maNhanVien){
        database.child("TaiKhoan").child(maNhanVien).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                TaiKhoan nTaiKhoan = snapshot.getValue(TaiKhoan.class);
                if (nTaiKhoan != null) {
                    taiKhoan = nTaiKhoan;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    //L???y d??? li???u s???n ph???m theo m??
    private void getDataSanPham(String maSanPham) {
        database.child("SanPham").child(maSanPham).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                SanPham nSanPham = snapshot.getValue(SanPham.class);

                if(nSanPham != null) {
                    sanPham = nSanPham;
                    //Hi???n th??? d??? li???u s???n ph???m
                    tvTenSanPham.setText(sanPham.getTen());
                    NumberFormat formatter = new DecimalFormat("#,###,###");
                    tvGia.setText(formatter.format(sanPham.getGia()) + " ??");
                    tvMonTa.setText("M?? t???\n" + sanPham.getMoTa());
                    tvDanhGia.setText(getDanhGia(sanPham.getDanhGia()) + "/5");
                    //Hi???n th??? ????nh gi??
                    DecimalFormat decimalFormat = new DecimalFormat("##.#");
                    tvDanhGia.setText(decimalFormat.format(getDanhGia(sanPham.getDanhGia())) + "/5");
                    //Hi???n th??? ???nh
                    getAnhMon(sanPham.getAnh());
                    //Hi???n th??? lo???i
                    getDataLoaiSanPham(sanPham.getLoai());
                    //Hi???n thi size
                    sizeArrayList.clear();
                    sizeArrayList.addAll(sanPham.getSize());
                    sizeArrayAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    //T??nh ??i???m ????nh gi??
    private float getDanhGia(ArrayList<Float> danhGiaArrayList){
        float ratingSum = 0f;
        for(Float r: danhGiaArrayList)  {
            ratingSum += r;
        }
        return  ratingSum / danhGiaArrayList.size();
    }


    //l???y d??? li???u lo???i s???n ph???m
    private void getDataLoaiSanPham(String maLoai) {
        database.child("LoaiSanPham").child(maLoai).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                LoaiSanPham loaiSanPham = snapshot.getValue(LoaiSanPham.class);
                if(loaiSanPham!= null) {
                    tvLoaiSanPham.setText(loaiSanPham.getTenLoai());
                }
                else {
                    tvLoaiSanPham.setText("L???i lo???i s???n ph???m");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    //L???y ???nh
    private void getAnhMon(String anh) {
        //C???t chu???i
        int dot = anh.lastIndexOf('.');
        String base = (dot == -1) ? anh : anh.substring(0, dot);
        String extension = (dot == -1) ? "" : anh.substring(dot + 1);

        //l???y ???nh t??? firebase
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
        btnThem = findViewById(R.id.btnThem);
    }
}