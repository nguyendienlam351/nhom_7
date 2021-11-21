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

public class ThongTinKhachHang extends Fragment {
    private TextView tvTenUser;
    private Button btnThongTinUser, btnDangXuat, btnDoiMatKhau, btnLichSu;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_thong_tin_khach_hang,container,false);
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
        btnLichSu = view.findViewById(R.id.btnLichSu);
        tvTenUser = view.findViewById(R.id.tvTenUser);

    }
}
