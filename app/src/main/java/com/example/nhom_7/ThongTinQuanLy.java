package com.example.nhom_7;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.nhom_7.model.TaiKhoan;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ThongTinQuanLy extends Fragment {
    private TextView tvTenUser;
    private Button btnThongTinUser, btnDangXuat, btnDoiMatKhau, btnQuanLyNV;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_thong_tin_quan_ly,container,false);
        setControl(view);
        setEvent();
        return view;
    }

    private void setEvent() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("TaiKhoan/"+user.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                TaiKhoan taiKhoan = snapshot.getValue(TaiKhoan.class);
                tvTenUser.setText(String.valueOf(taiKhoan.getHoTen()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        btnThongTinUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),ThayDoiThongTin.class);
                startActivity(intent);
            }
        });
        btnDoiMatKhau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),DoiMatKhau.class);
                startActivity(intent);
            }
        });
        btnQuanLyNV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getContext(),QuanLyNhanVien.class);
                startActivity(intent);
            }
        });
        btnDangXuat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getContext(),TrangTaiKhoan.class);
                startActivity(intent);
            }
        });
    }


    private void setControl(View view) {
        btnThongTinUser = view.findViewById(R.id.btnThayDoiInfo);
        btnDangXuat = view.findViewById(R.id.btnDangXuat);
        btnDoiMatKhau = view.findViewById(R.id.btnDoiMatKhau);
        btnQuanLyNV = view.findViewById(R.id.btnQuanLyNV);
        tvTenUser = view.findViewById(R.id.tvTenUser);

    }
}
