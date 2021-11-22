package com.example.nhom_7;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.nhom_7.model.TaiKhoan;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ThayDoiThongTin extends AppCompatActivity {
    private EditText edHoTen, edSoDienThoai, edDiaChi;
    private Button btnLuu;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("TaiKhoan/"+user.getUid());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thay_doi_thong_tin);
        setControl();
        setEvent();
    }

    private void setEvent() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setLogo(R.drawable.logo);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setTitle("7's store");
        getUserInfo();
        btnLuu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                luu();
            }
        });
    }

    private void luu(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Lưu");
        alertDialog.setMessage("Bạn có muốn lưu không ? ");
        alertDialog.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                TaiKhoan taiKhoan = new TaiKhoan();
                taiKhoan.setMaKH(user.getUid());
                taiKhoan.setLoaiTaiKhoan("khachhang");
                taiKhoan.setHoTen(edHoTen.getText().toString());
                taiKhoan.setSoDienThoai(edSoDienThoai.getText().toString());
                taiKhoan.setDiaChi(edDiaChi.getText().toString());
                taiKhoan.setEmail(user.getEmail());
                reference.setValue(taiKhoan);
                Toast.makeText(getApplicationContext(), "Thông tin đã được thay đổi !", Toast.LENGTH_LONG).show();
                finish();
            }
        });
        alertDialog.setNegativeButton("Không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        alertDialog.show();
    }
    private void setControl() {
        edHoTen = findViewById(R.id.edHoTen);
        edSoDienThoai = findViewById(R.id.edSoDienThoai);
        edDiaChi = findViewById(R.id.edDiaChi);
        btnLuu = findViewById(R.id.btnLuu);
    }
    private void getUserInfo(){
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                TaiKhoan taiKhoan = snapshot.getValue(TaiKhoan.class);
                edHoTen.setText(String.valueOf(taiKhoan.getHoTen()));
                edSoDienThoai.setText(String.valueOf(taiKhoan.getSoDienThoai()));
                edDiaChi.setText(String.valueOf(taiKhoan.getDiaChi()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}