package com.example.nhom_7;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class GiaoDienDangNhap extends Fragment {
    private CheckBox cbHienmk;
    private Button btnQuenmk,btnDangNhap;
    private EditText edMatkhau,edTaikhoan;
    private FirebaseAuth mAuth;
    CustomActionBar actionBar;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_giao_dien_dang_nhap,container,false);
        setControl(view);
        setEvent();
        return view;
    }

    private void setEvent() {
        actionBar.setActionBarName("Đăng nhập");
        btnDangNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edTaikhoan.getText().toString().trim();
                String matkhau = edMatkhau.getText().toString().trim();
                int i;
                if(kiemtratrong()){
                    mAuth.signInWithEmailAndPassword(email,matkhau).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("TaiKhoan/"+mAuth.getUid());
                                myRef.child("loaiTaiKhoan").addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        String value = snapshot.getValue(String.class);
                                        if(value.compareTo("khachhang")==0){
                                            Intent intent = new Intent(getContext(),GiaoDienKhachHang.class);
                                            startActivity(intent);
                                        }else if (value.compareTo("quanly")==0){
                                            Intent intent = new Intent(getContext(), GiaoDienQuanLy.class);
                                            startActivity(intent);
                                        }else if (value.compareTo("nhanvien")==0){
                                            Intent intent = new Intent(getContext(), GiaoDienNhanVien.class);
                                            startActivity(intent);
                                        }else{
                                            Toast.makeText(getContext(), "Tài khoản không tồn tại !", Toast.LENGTH_LONG).show();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                                Toast.makeText(getContext(),"Đăng nhập thành công !",Toast.LENGTH_SHORT).show();

                            }else{
                                Toast.makeText(getContext(),"Sai tên đăng nhập hoặc mật khẩu !",Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        });
        btnQuenmk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quenmk();
            }
        });
        cbHienmk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cbHienmk.isChecked() == true){
                    edMatkhau.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
                else{
                    edMatkhau.setInputType(InputType.TYPE_CLASS_TEXT |
                            InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
            }
        });
    }

    private void setControl(View view) {
        cbHienmk = view.findViewById(R.id.cdHienmk);
        btnQuenmk = view.findViewById(R.id.btnQuenmk);
        btnDangNhap = view.findViewById(R.id.btnDangNhap);
        edMatkhau = view.findViewById(R.id.edMatKhau);
        edTaikhoan = view.findViewById(R.id.edTenDN);
        mAuth = FirebaseAuth.getInstance();
        actionBar = view.findViewById(R.id.actionbar);
    }
    private void quenmk()  {
        QuenMatKhauDialog.FullNameListener listener = new QuenMatKhauDialog.FullNameListener() {
            @Override
            public void fullNameEntered(String email) {
                mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getContext(), "Ứng dụng đã gửi link đổi mật khẩu về Email", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getContext(), "Gửi link thất bại, vui lòng nhập đúng Email", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        };
        final QuenMatKhauDialog dialog = new QuenMatKhauDialog(getContext(), listener);

        dialog.show();
    }
    private boolean kiemtratrong() {
        boolean kiemtra = true;
        if(edTaikhoan.getText().toString().trim().length() == 0){
            edTaikhoan.setError("Vui lòng nhập tài khoản !");
            kiemtra = false;
        }
        if(edMatkhau.getText().toString().trim().length() == 0){
            edMatkhau.setError("Vui lòng nhập mật khẩu !");
            kiemtra = false;
        }
        return kiemtra;
    }
}