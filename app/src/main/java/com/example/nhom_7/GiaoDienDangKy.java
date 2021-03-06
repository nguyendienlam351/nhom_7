package com.example.nhom_7;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.nhom_7.model.TaiKhoan;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class GiaoDienDangKy extends Fragment {
    EditText edHoTen, edEmail, edMatKhau, edXacNhanMK, edSoDienThoai, edDiaChi;
    Button btnDangKi;
    FirebaseAuth auth;
    TaiKhoan taiKhoan = new TaiKhoan();
    DatabaseReference myref = FirebaseDatabase.getInstance().getReference("TaiKhoan");
    ArrayList<TaiKhoan> data = new ArrayList<TaiKhoan>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_giao_dien_dang_ky,container,false);
        setControl(view);
        setEvent();
        return view;
    }

    private void setEvent() {
        btnDangKi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (kiemtratrong() == true && kiemtraEmail() == true && kiemtraMK() == true && kiemtradodaimk()==true && kiemtradinhdangEmail() == true){
                    String email = edEmail.getText().toString().trim();
                    String matkhau = edMatKhau.getText().toString().trim();
                    auth.createUserWithEmailAndPassword(email,matkhau).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                taiKhoan.setHoTen(edHoTen.getText().toString());
                                taiKhoan.setEmail(edEmail.getText().toString());
                                taiKhoan.setSoDienThoai(edSoDienThoai.getText().toString());
                                taiKhoan.setDiaChi(edDiaChi.getText().toString());
                                taiKhoan.setLoaiTaiKhoan("khachhang");
                                taiKhoan.setMaKH(auth.getUid());
                                myref.child(auth.getUid()).setValue(taiKhoan);
                                Toast.makeText(getContext(), "????ng k?? th??nh c??ng !", Toast.LENGTH_LONG).show();
                                xoatrang();
                            }else {
                                Toast.makeText(getContext(), "????ng k?? th???t b???i !", Toast.LENGTH_LONG).show();
                            }
                        }
                    });

                }else{
                    Toast.makeText(getContext(), "Vui l??ng nh???p ????ng th??ng tin !", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setControl(View view) {
        edHoTen = view.findViewById(R.id.edHoTen);
        edEmail = view.findViewById(R.id.edNhapEmail);
        edMatKhau = view.findViewById(R.id.edNhapMK);
        edXacNhanMK = view.findViewById(R.id.edXacNhanMK);
        edSoDienThoai = view.findViewById(R.id.edSoDienThoai);
        edDiaChi = view.findViewById(R.id.edDiaChi);
        btnDangKi = view.findViewById(R.id.btnDangKi);
        auth = FirebaseAuth.getInstance();
    }
    private boolean kiemtratrong(){
        boolean kiemtra = true;
        if (edHoTen.getText().toString().trim().length() == 0 ){
            edHoTen.setError("Vui l??ng nh???p t??n !");
            kiemtra = false;}
        if (edEmail.getText().toString().trim().length() == 0 ){
            edEmail.setError("Vui l??ng nh???p email !");
            kiemtra = false;}
        if (edMatKhau.getText().toString().trim().length() == 0){
            edMatKhau.setError("Vui l??ng nh???p m???t kh???u !");
            kiemtra = false;}
        if (edXacNhanMK.getText().toString().trim().length() == 0){
            edXacNhanMK.setError("Vui l??ng nh???p l???i m???t kh???u !");
            kiemtra = false;}
        if (edSoDienThoai.getText().toString().trim().length() == 0){
            edSoDienThoai.setError("Vui l??ng nh???p s??? ??i???n tho???i !");
            kiemtra = false;}
        if (edDiaChi.getText().toString().trim().length() == 0){
            edDiaChi.setError("Vui l??ng nh???p ?????a ch??? !");
            kiemtra = false;
        }
        return kiemtra;
    }
    private boolean kiemtraMK(){
        boolean kiemtra = true;
        if(edMatKhau.getText().toString().compareTo(edXacNhanMK.getText().toString()) != 0){
            edXacNhanMK.setError("M???t kh???u nh???p l???i kh??ng kh???p !");
            return false;
        }
        return kiemtra;
    }
    private boolean kiemtraEmail(){
        boolean kiemtra = true;
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("TaiKhoan");
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                TaiKhoan taiKhoan = snapshot.getValue(TaiKhoan.class);
                data.add(0, taiKhoan);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        for (int i = 0; i<data.size();i++){
            TaiKhoan taiKhoan = data.get(i);
            if (edEmail.getText().toString().trim().compareTo(taiKhoan.getEmail()) == 0){
                edEmail.setError("Email ???? t???n t???i !");
                kiemtra = false;
            }
        }
        return kiemtra;
    }
    private void xoatrang(){
        edMatKhau.setText("");
        edHoTen.setText("");
        edSoDienThoai.setText("");
        edDiaChi.setText("");
        edXacNhanMK.setText("");
        edEmail.setText("");
    }
    private boolean kiemtradodaimk(){
        boolean kiemtra = true;
        if (edMatKhau.getText().toString().trim().length() < 6){
            kiemtra = false;
            edMatKhau.setError("M???t kh???u ??t nh???t 6 k?? t??? !");
        }
        return kiemtra;
    }
    private boolean kiemtradinhdangEmail(){
        boolean kiemtra = true;
        if(edEmail.getText().toString().trim().contains("@gmail.com") ==  false){
            edEmail.setError("Sai ?????nh d???ng Email (abc@gmail.com) !");
            kiemtra = false;
        }
        return kiemtra;
    }
}
