package com.example.nhom_7;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nhom_7.adapter.ChiTetDonHangAdapter;
import com.example.nhom_7.model.ChiTietDH;
import com.example.nhom_7.model.DonHang;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

public class ChiTietDonHang extends AppCompatActivity {
    TextView tvMa,tvNgayDat,tvTong,tvTrangThai,tvHoTen,tvEmail,tvSDT,tvDiaChi;
    RecyclerView lvChiTietDH;
    ArrayList<ChiTietDH> data = new ArrayList<ChiTietDH>();
    ChiTetDonHangAdapter myRecyclerViewAdapter;
    DatabaseReference database;
    DonHang donHang;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_don_hang);
        setConTrol();
        setEvent();
    }

    //Lấy dữ liệu firebase
    private void getlist(String maDonHang){
        database= FirebaseDatabase.getInstance().getReference("LichSuDonHang");
        database.child(maDonHang).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                donHang = snapshot.getValue(DonHang.class);
                if(donHang!=null){
                    tvMa.setText(donHang.getMaDH());
                    tvNgayDat.setText(donHang.getNgayDat());
                    NumberFormat formatter = new DecimalFormat("#,###,###");
                    tvTong.setText(formatter.format(donHang.getTong())+ " đ");
                    tvTrangThai.setText(donHang.getTrangThai());
                    tvHoTen.setText(donHang.getTaiKhoan().getHoTen());
                    tvEmail.setText(donHang.getTaiKhoan().getEmail());
                    tvSDT.setText(donHang.getTaiKhoan().getSoDienThoai());
                    tvDiaChi.setText(donHang.getTaiKhoan().getDiaChi());
                    data.addAll(donHang.getChiTietDonHangs());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void setEvent() {
        myRecyclerViewAdapter = new ChiTetDonHangAdapter(this,R.layout.layout_chi_tiet_don_hang,data);
        lvChiTietDH.setAdapter(myRecyclerViewAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        lvChiTietDH.setLayoutManager(layoutManager);
        //getlist();
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if (bundle != null) {
            String maDonHang = bundle.getString("maDonHang", "");

            getlist(maDonHang);
        }
    }

    private void setConTrol() {
        lvChiTietDH=findViewById(R.id.lvChiTietDH);
        tvMa=findViewById(R.id.tvMa);
        tvNgayDat=findViewById(R.id.tvNgayDat);
        tvTong=findViewById(R.id.tvTong);
        tvTrangThai=findViewById(R.id.tvTrangthai);
        tvHoTen=findViewById(R.id.tvHoTen);
        tvEmail=findViewById(R.id.tvEmail);
        tvSDT=findViewById(R.id.tvSDT);
        tvDiaChi=findViewById(R.id.tvDiaChi);
    }
}