package com.example.nhom_7;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.normal.TedPermission;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import gun0912.tedbottompicker.TedBottomPicker;

public class ThemSanPham extends AppCompatActivity {
    EditText edtTenSP, edtGiaSP, edtSoLuongSP, edtMoTaSP;
    ImageView imgAnhSP;
    Spinner spnLoaiSP, spnSizeSP;
    int sSize;
    //    CustomActionBar actionBar;
    Button btnThem, btnThayDoi;
    DatabaseReference mData;
    ArrayList<String> loaiSP = new ArrayList<String>();
    ArrayList<Size> sizeSP = new ArrayList<Size>();
    SanPham sanPham;
    ArrayAdapter<Size> arrayAdapterSizeSP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_them_san_pham);

        setControl();
        setEvent();

    }

    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference("SanPham");

    private void setEvent() {
//        actionBar.setDelegation(new CustomActionBar.ActionBarDelegation() {
//            @Override
//            public void backOnClick() {
//                finish();
//            }
//        });
//
//        actionBar.setActionBarName("Thêm nhân viên");
        //
        loaiSP.add("Quần tây");
        loaiSP.add("Quần đùi");
        loaiSP.add("Áo thun");
        loaiSP.add("Áo dài");
        ArrayAdapter<String> arrayAdapterLoaiSP = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, loaiSP);
        arrayAdapterLoaiSP.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnLoaiSP.setAdapter(arrayAdapterLoaiSP);
        //
        sizeSP.add(new Size(Size.SIZE_S, 0));
        sizeSP.add(new Size(Size.SIZE_M, 0));
        sizeSP.add(new Size(Size.SIZE_L, 0));
        sizeSP.add(new Size(Size.SIZE_XL, 0));
        arrayAdapterSizeSP = new ArrayAdapter<Size>(this, android.R.layout.simple_spinner_item, sizeSP);
        arrayAdapterSizeSP.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnSizeSP.setAdapter(arrayAdapterSizeSP);

        spnSizeSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sSize = position;
                edtSoLuongSP.setText(String.valueOf(sizeSP.get(sSize).getSoLuong()));

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //Gọi hàm để trống dữ liệu
        edtTenSP.addTextChangedListener(watcher);
        edtGiaSP.addTextChangedListener(watcher);
        edtSoLuongSP.addTextChangedListener(watcher);
        edtMoTaSP.addTextChangedListener(watcher);
        //Tạo ngày, tháng, năm sinh

        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mData = FirebaseDatabase.getInstance().getReference();
                String TenSP = edtTenSP.getText().toString().trim();
                int GiaSP = Integer.parseInt(edtGiaSP.getText().toString());
                String LoaiSP = spnLoaiSP.getSelectedItem().toString().trim();
                String MoTaSP = edtMoTaSP.getText().toString().trim();
                String MaSP = mData.push().getKey();
                String AnhSP = MaSP + ".png";
                SanPham sanPham = new SanPham(MaSP, AnhSP, TenSP, GiaSP, LoaiSP, MoTaSP, sizeSP);
                sanPham.setMaSanPham(MaSP);
                mData.child("SanPham").child(MaSP).setValue(sanPham);

                //Thêm hình ảnh lên firebase
                StorageReference mountainsRef = storageRef.child(MaSP + ".png");
                imgAnhSP.setDrawingCacheEnabled(true);
                imgAnhSP.buildDrawingCache();
                Bitmap bitmap = ((BitmapDrawable) imgAnhSP.getDrawable()).getBitmap();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                byte[] data = baos.toByteArray();

                UploadTask uploadTask = mountainsRef.putBytes(data);
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        Toast.makeText(ThemSanPham.this, "Lỗi", Toast.LENGTH_SHORT).show();
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                        // ...
                    }
                });
                //Chuyển màn hình
                Intent intent = new Intent(getApplicationContext(), DanhSachSanPham.class);
                startActivity(intent);
            }
        });
        btnThayDoi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Size size = new Size(sizeSP.get(sSize).getTenSize(),Integer.parseInt(edtSoLuongSP.getText().toString()));
                sizeSP.set(sSize,size);
                arrayAdapterSizeSP.notifyDataSetChanged();
            }
        });
        //Chọn hình ảnh
        imgAnhSP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestPermission();
            }
        });

    }

    //Kiểm tra requestPermission
    private void requestPermission() {
        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                OpenImagePicker();
            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                Toast.makeText(ThemSanPham.this, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
            }
        };
        TedPermission.create()
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE)
                .check();
    }

    private void OpenImagePicker() {
        TedBottomPicker.OnImageSelectedListener listener = new TedBottomPicker.OnImageSelectedListener() {
            @Override
            public void onImageSelected(Uri uri) {
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                    imgAnhSP.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        TedBottomPicker tedBottomPicker = new TedBottomPicker.Builder(ThemSanPham.this)
                .setOnImageSelectedListener(listener)
                .create();
        tedBottomPicker.show(getSupportFragmentManager());
    }

    //Hàm không được để trống dữ liệu
    private TextWatcher watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String newTenSP = edtTenSP.getText().toString().trim();
            String newGiaSP = edtGiaSP.getText().toString().trim();
            String newSoLuongSP = edtSoLuongSP.getText().toString().trim();
            String newMoTaSP = edtGiaSP.getText().toString().trim();
            btnThem.setEnabled(!newTenSP.isEmpty() && !newGiaSP.isEmpty() && !newSoLuongSP.isEmpty() && !newMoTaSP.isEmpty());
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    //Ánh xạ
    private void setControl() {

        imgAnhSP = findViewById(R.id.imgAnhSP);
        edtTenSP = findViewById(R.id.edtTenSP);
        edtGiaSP = findViewById(R.id.edtGiaSP);
        edtSoLuongSP = findViewById(R.id.edtSoLuongSP);
        edtMoTaSP = findViewById(R.id.edtMoTaSP);
        spnLoaiSP = findViewById(R.id.spnLoaiSP);
        spnSizeSP = findViewById(R.id.spnSizeSP);
        btnThem = findViewById(R.id.btnThem);
        btnThayDoi = findViewById(R.id.btnThayDoi);
        btnThem.setEnabled(false);
//        actionBar = findViewById(R.id.actionBar);

    }
}