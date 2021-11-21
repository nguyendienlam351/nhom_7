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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class DoiMatKhau extends AppCompatActivity {
    private EditText edMatKhauCu, edMatKhauMoi, edXacNhanMK;
    private Button btnThayDoi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doi_mat_khau);
        setControl();
        setEvent();
    }

    private void setEvent() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setLogo(R.drawable.logo);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setTitle("7's store");
        btnThayDoi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(kiemtratrong() == true && kiemtraMKmoi() == true && kiemtradodaiMKmoi() == true){
                    thaydoi();
                }
            }
        });
    }
    private void thaydoi(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Thay đổi");
        alertDialog.setMessage("Bạn có muốn thay đổi không ? ");
        alertDialog.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(),edMatKhauCu.getText().toString());
                user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            user.updatePassword(edMatKhauMoi.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Toast.makeText(getApplicationContext(), "Đổi mật khẩu thành công !", Toast.LENGTH_LONG).show();
                                        finish();
                                    }
                                    else{
                                        Toast.makeText(getApplicationContext(), "Đổi mật khẩu thất bại !", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        }else{
                            edMatKhauCu.setError("Mật khẩu hiện tại không đúng !");
                        }
                    }
                });

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
    private boolean kiemtradodaiMKmoi(){
        boolean kiemtra = true;
        if(edMatKhauMoi.getText().toString().trim().length() < 6){
            edMatKhauMoi.setError("Mật khẩu ít nhất 6 kí tự !");
            kiemtra = false;
        }
        return kiemtra;
    }
    private boolean kiemtraMKmoi(){
        boolean kiemtra = true;
        if(edMatKhauMoi.getText().toString().trim().compareTo(edXacNhanMK.getText().toString().trim()) != 0){
            edXacNhanMK.setError("Nhật khẩu nhập lại không đúng !");
            kiemtra = false;
        }
        return kiemtra;
    }
    private boolean kiemtratrong(){
        boolean kiemtra = true;
        if(edMatKhauCu.getText().toString().trim().length() == 0){
            edMatKhauCu.setError("Nhập mật khẩu hiện tại !");
            kiemtra = false;
        }
        if(edMatKhauMoi.getText().toString().trim().length() == 0){
            edMatKhauMoi.setError("Nhập mật khẩu mới !");
            kiemtra = false;
        }
        if(edXacNhanMK.getText().toString().trim().length() == 0){
            edXacNhanMK.setError("Nhập lại mật khẩu mới !");
            kiemtra = false;
        }
        return kiemtra;
    }

    private void setControl() {
        edMatKhauCu = findViewById(R.id.edMatKhauCu);
        edMatKhauMoi = findViewById(R.id.edMatKhauMoi);
        edXacNhanMK = findViewById(R.id.edXacNhanMKMoi);
        btnThayDoi = findViewById(R.id.btnThayDoi);
    }
}