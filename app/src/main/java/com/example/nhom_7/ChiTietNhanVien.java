package com.example.nhom_7;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.nhom_7.model.NhanVien;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class ChiTietNhanVien extends AppCompatActivity {
    EditText edtHoTen, edtSDT, edtDiaChi, edtEmail;
    NhanVien nhanVien;
    Button btnSua,btnXoa;
    DatabaseReference mData;
    FirebaseUser User;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_nhan_vien);
        setControl();
        setEvent();

    }


    private void setEvent() {
//      firebaseAuth = FirebaseAuth.getInstance();
//      User = firebaseAuth.getCurrentUser();

        mData = FirebaseDatabase.getInstance().getReference("TaiKhoan");
        //
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            String MaNV = bundle.getString("MaKH", "");
            getDataNV(MaNV);
        }

    }
    //Hàm lấy dữ liệu từ màn hình RV
    private void getDataNV(String MaNV) {
        mData.child(MaNV).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                NhanVien nNhanVien = snapshot.getValue(NhanVien.class);
                if (nNhanVien != null) {
                    nhanVien = nNhanVien;
                    edtHoTen.setText(nhanVien.getHoTen());
                    edtSDT.setText(String.valueOf(nhanVien.getSoDienThoai()));
                    edtEmail.setText(nhanVien.getEmail());
                    edtDiaChi.setText(nhanVien.getDiaChi());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        btnSua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mData.orderByChild("soDienThoai").equalTo(edtSDT.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            edtSDT.setError("Số điện thoại đã tồn tại !");
                        }else{
                            if(kiemtratrong() == true){
                                openDiaLogUpdateItem(nhanVien);
                            }
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
        btnXoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDiaLogDelete(nhanVien);
            }
        });
    }
    //kiểm tra trống
    private boolean kiemtratrong (){
        boolean kiemtra = true;
        if (edtHoTen.getText().toString().trim().length() == 0){
            edtHoTen.setError("Nhập họ tên nhân viên !");
            kiemtra = false;
        }
        if (edtEmail.getText().toString().trim().length() == 0){
            edtEmail.setError("Nhập email !");
            kiemtra = false;
        }
        if (edtSDT.getText().toString().trim().length() == 0){
            edtSDT.setError("Nhập số điện thoại !");
            kiemtra = false;
        }
        if (edtDiaChi.getText().toString().trim().length() == 0){
            edtDiaChi.setError("Nhập địa chỉ !");
            kiemtra = false;
        }
        return kiemtra;
    }
    //Hàm update
    private void openDiaLogUpdateItem(NhanVien nhanVien) {
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setTitle("Thay đổi");
        b.setMessage("Bạn có muốn thay đổi?");
        b.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newHoTen = edtHoTen.getText().toString().trim();
                String newSDT = edtSDT.getText().toString();
                String newEmail = edtEmail.getText().toString().trim();
                String newDiaChi = edtDiaChi.getText().toString().trim();

                nhanVien.setHoTen(newHoTen);
                nhanVien.setSoDienThoai(newSDT);
                nhanVien.setEmail(newEmail);
                nhanVien.setDiaChi(newDiaChi);
                mData.child(String.valueOf(nhanVien.getMaKH())).updateChildren(nhanVien.toMap());

                Intent intent = new Intent(getApplicationContext(), QuanLyNhanVien.class);
                startActivity(intent);
            }

        });
        b.setNegativeButton("Từ chối", null);
        b.setCancelable(false);
        b.show();
    }

    //Hàm delete
    private void openDiaLogDelete(NhanVien nhanVien) {
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setTitle("Xoá");
        b.setMessage("Bạn có muốn xoá?");
        b.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mData.child(String.valueOf(nhanVien.getMaKH())).removeValue();
                Intent intent = new Intent(getApplicationContext(), QuanLyNhanVien.class);
                startActivity(intent);
            }
        });
        b.setNegativeButton("Từ chối", null);
        b.setCancelable(false);
        b.show();
    }

    private void setControl() {
        edtHoTen = findViewById(R.id.edtHoTen);
        edtSDT = findViewById(R.id.edtSDT);
        edtDiaChi = findViewById(R.id.edtDiaChi);
        edtEmail = findViewById(R.id.edtEmail);
        btnXoa=findViewById(R.id.btnXoa);
        btnSua=findViewById(R.id.btnSua);
    }
}