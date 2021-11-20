package com.example.nhom_7;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
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

public class DanhGiaSanPham extends AppCompatActivity {
    ImageView imgSanPham;
    TextView tvTenSanPham;
    TextView tvLoaiSanPham;
    TextView tvGia;
    TextView tvMonTa;
    TextView tvDanhGia1;
    TextView tvDanhGia2;
    RatingBar rbDanhGia1;
    RatingBar rbDanhGia2;
    Button btnDanhGia;
    DatabaseReference database;
    StorageReference storage;
    SanPham sanPham;
    float danhGia = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danh_gia_san_pham);

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
        }

        getDataSanPham("-MoVYchCQt-NERKn3er9");
        btnDanhGia.setEnabled(true);

        rbDanhGia2.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                danhGia = rating;
                tvDanhGia2.setText(String.valueOf(danhGia));
            }
        });

        btnDanhGia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder b = new AlertDialog.Builder(DanhGiaSanPham.this);
                b.setTitle("Đánh giá");
                b.setMessage("Bạn có muốn đánh giá?");
                b.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sanPham.getDanhGia().add(danhGia);
                        database.child("SanPham").child(sanPham.getMaSanPham())
                                .child("danhGia").setValue(sanPham.getDanhGia());
                    }
                });
                b.setNegativeButton("Từ chối", null);
                b.setCancelable(false);
                b.show();
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
                DecimalFormat decimalFormat = new DecimalFormat("##.#");
                tvDanhGia1.setText(decimalFormat.format(getDanhGia(sanPham.getDanhGia()))+ "/5");
                rbDanhGia1.setRating(getDanhGia(sanPham.getDanhGia()));
                getAnhMon(sanPham.getAnh());
                getDataLoaiSanPham(sanPham.getLoai());
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

    private float getDanhGia(ArrayList<Float> danhGiaArrayList){
        float ratingSum = 0f;
        for(Float r: danhGiaArrayList)  {
            ratingSum += r;
        }
        return  ratingSum / danhGiaArrayList.size();
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
        tvDanhGia1 = findViewById(R.id.tvDanhGia1);
        rbDanhGia1 = findViewById(R.id.rbDanhGia1);
        tvDanhGia2 = findViewById(R.id.tvDanhGia2);
        rbDanhGia2 = findViewById(R.id.rbDanhGia2);
        tvGia = findViewById(R.id.tvGia);
        tvMonTa = findViewById(R.id.tvMonTa);
        btnDanhGia = findViewById(R.id.btnDanhGia);
    }
}