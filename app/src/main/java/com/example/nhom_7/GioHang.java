package com.example.nhom_7;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.nhom_7.adapter.ChiTietDonHangAdapter;
import com.example.nhom_7.model.ChiTietDH;
import com.example.nhom_7.model.DonHang;
import com.example.nhom_7.model.TaiKhoan;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class GioHang extends AppCompatActivity {
    RecyclerView lvDanhSachSanPham;
    Button btnThanhToan;
    ChiTietDonHangAdapter chiTietDonHangAdapter;
    DatabaseReference database;
    TaiKhoan taiKhoan = new TaiKhoan();
    ArrayList<ChiTietDH> chiTietDHArrayList;
    int tong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gio_hang);

        setControl();
        setEvent();
    }

    private void setEvent() {
        chiTietDHArrayList = new ArrayList<ChiTietDH>();
        database = FirebaseDatabase.getInstance().getReference();

        getDataDonHang("8PnGpfFjB3Z6evSiAzDp9Xzwy7y2");

        chiTietDonHangAdapter = new ChiTietDonHangAdapter(GioHang.this, R.layout.layout_item_chi_tiet_don_hang, chiTietDHArrayList);
        chiTietDonHangAdapter.setDelegation(new ChiTietDonHangAdapter.ChiTietDonHangClickListener() {
            @Override
            public void iconClick(ChiTietDH chiTietDH) {
                taiKhoan.getGioHang().remove(chiTietDH);
                database.child("TaiKhoan").child(taiKhoan.getMaKH()).child("gioHang").setValue(taiKhoan.getGioHang());
            }

            @Override
            public void tangSoLuong(ChiTietDH chiTietDH) {
                chiTietDH.setSoLuong(chiTietDH.getSoLuong() + 1);
                database.child("TaiKhoan").child(taiKhoan.getMaKH()).child("gioHang").setValue(taiKhoan.getGioHang());
            }

            @Override
            public void giamSoLuong(ChiTietDH chiTietDH) {
                if (chiTietDH.getSoLuong() > 1) {
                    chiTietDH.setSoLuong(chiTietDH.getSoLuong() - 1);
                    database.child("TaiKhoan").child(taiKhoan.getMaKH()).child("gioHang").setValue(taiKhoan.getGioHang());
                }
            }
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(GioHang.this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        lvDanhSachSanPham.setLayoutManager(layoutManager);
        lvDanhSachSanPham.setAdapter(chiTietDonHangAdapter);

        btnThanhToan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chiTietDHArrayList.size() != 0) {
                    AlertDialog.Builder b = new AlertDialog.Builder(GioHang.this);
                    b.setTitle("Thay đổi");
                    b.setMessage("Bạn có muốn thay đổi?");
                    b.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Calendar calendar = Calendar.getInstance();
                            final int year = calendar.get(Calendar.YEAR);
                            final int month = calendar.get(Calendar.MONTH);
                            final int day = calendar.get(Calendar.DAY_OF_MONTH);

                            String maHoaDon = database.child("LichSuDonHang").push().getKey();
                            DonHang donHang = new DonHang();
                            donHang.setMaDH(maHoaDon);
                            donHang.setNgayDat(day + "/" + (month + 1) + "/" + year);
                            donHang.setTong(tong);
                            taiKhoan.setGioHang(null);
                            donHang.setTaiKhoan(taiKhoan);
                            donHang.setTrangThai("Chờ");

                            database.child("LichSuDonHang").child(maHoaDon).setValue(donHang).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    database.child("TaiKhoan").child(String.valueOf(taiKhoan.getMaKH())).child("gioHang").removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Toast.makeText(GioHang.this, "Thanh toán thành công", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(GioHang.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });
                    b.setNegativeButton("Từ chối", null);
                    b.setCancelable(false);
                    b.show();

                } else {
                    Toast.makeText(GioHang.this, "Hãy chọn thêm sản phẩm", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //Lấy dữ liệu giỏ hàng
    private void getDataDonHang(String maTaiKhoan) {
        database.child("TaiKhoan").child(maTaiKhoan).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                TaiKhoan nTaiKhoan = snapshot.getValue(TaiKhoan.class);
                if (nTaiKhoan != null) {
                    taiKhoan = nTaiKhoan;
                    chiTietDHArrayList.clear();
                    chiTietDHArrayList.addAll(taiKhoan.getGioHang());
                    chiTietDonHangAdapter.notifyDataSetChanged();
                    //Tính tổng
                    tinhTong();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    //Tính tổng giá trị
    private void tinhTong() {
        int tong = 0;
        for (ChiTietDH item : chiTietDHArrayList) {
            tong += item.getGia() * item.getSoLuong();
        }
        this.tong = tong;

        NumberFormat formatter = new DecimalFormat("#,###,###");
        btnThanhToan.setText("Thanh toán: " + formatter.format(tong) + " đ");
    }

    private void setControl() {
        lvDanhSachSanPham = findViewById(R.id.lvDanhSachSanPham);
        btnThanhToan = findViewById(R.id.btnThanhToan);
    }
}