package com.example.nhom_7;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.nhom_7.model.TaiKhoan;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ThemNhanVien extends AppCompatActivity {
    EditText edHoTen, edEmail, edSoDienThoai, edDiaChi;
    Button btnDangKi;
    ArrayList<TaiKhoan> data = new ArrayList<TaiKhoan>();
    FirebaseAuth auth;
    TaiKhoan taiKhoan = new TaiKhoan();
    DatabaseReference myref = FirebaseDatabase.getInstance().getReference("TaiKhoan");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_them_nhan_vien);
        setControl();
        setEvent();
    }

    private void setEvent() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setLogo(R.drawable.logo);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setTitle("7's store");
        btnDangKi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(kiemtratrong() == true && kiemtrasdt() == true && kiemtradinhdangEmail() == true && kiemtraEmail() == true){
                    String email = edEmail.getText().toString().trim();
                    String matkhau = edSoDienThoai.getText().toString().trim();
                    auth.createUserWithEmailAndPassword(email,matkhau).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                taiKhoan.setHoTen(edHoTen.getText().toString());
                                taiKhoan.setEmail(edEmail.getText().toString());
                                taiKhoan.setSoDienThoai(edSoDienThoai.getText().toString());
                                taiKhoan.setDiaChi(edDiaChi.getText().toString());
                                taiKhoan.setLoaiTaiKhoan("nhanvien");
                                taiKhoan.setMaKH(auth.getUid());
                                myref.child(auth.getUid()).setValue(taiKhoan);
                                Toast.makeText(getApplicationContext(), "????ng k?? th??nh c??ng !", Toast.LENGTH_LONG).show();
                            }else {
                                Toast.makeText(getApplicationContext(), "????ng k?? th???t b???i !", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }else{
                    Toast.makeText(getApplicationContext(), "Vui l??ng nh???p ????ng th??ng tin !", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void setControl() {
        edHoTen = findViewById(R.id.edHoTen);
        edEmail = findViewById(R.id.edEmail);
        edSoDienThoai = findViewById(R.id.edSoDienThoai);
        edDiaChi = findViewById(R.id.edDiaChi);
        btnDangKi = findViewById(R.id.btnDangKi);
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
        if (edSoDienThoai.getText().toString().trim().length() == 0){
            edSoDienThoai.setError("Vui l??ng nh???p s??? ??i???n tho???i !");
            kiemtra = false;}
        if (edDiaChi.getText().toString().trim().length() == 0){
            edDiaChi.setError("Vui l??ng nh???p ?????a ch??? !");
            kiemtra = false;
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
    private boolean kiemtradinhdangEmail(){
        boolean kiemtra = true;
        if(edEmail.getText().toString().trim().contains("@gmail.com") ==  false){
            edEmail.setError("Sai ?????nh d???ng Email (abc@gmail.com) !");
            kiemtra = false;
        }
        return kiemtra;
    }
    private boolean kiemtrasdt (){
        boolean kiemtra = true;
        if(edSoDienThoai.getText().toString().trim().length() < 6){
            edSoDienThoai.setError("S??? ??i???n tho???i kh??ng ????ng !");
            kiemtra = false;
        }
        return kiemtra;
    }
}