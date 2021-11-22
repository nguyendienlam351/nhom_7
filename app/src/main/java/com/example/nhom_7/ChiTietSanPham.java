package com.example.nhom_7;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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

import com.example.nhom_7.model.SanPham;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.normal.TedPermission;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import gun0912.tedbottompicker.TedBottomPicker;

public class ChiTietSanPham extends AppCompatActivity {
    EditText edtTenSP, edtGiaSP, edtSoLuongSP, edtMoTaSP;
    ImageView imgAnhSP;
    Spinner spnLoaiSP, spnSizeSP;
//    CustomActionBar actionBar;
    Button btnThayDoi, btnXoa, btnThem;
    ArrayList<String> loaiSP = new ArrayList<String>();
//    ArrayList<Size> sizeSP = new ArrayList<Size>();
    SanPham sanPham;
//    ArrayAdapter<Size> arrayAdapterSizeSP;
    int sSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_san_pham);
        setControl();
        setEvent();

    }

    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference("SanPham");


    FirebaseDatabase data = FirebaseDatabase.getInstance();
    DatabaseReference mData = data.getReference("SanPham");

    private void setEvent() {
//        actionBar.setDelegation(new CustomActionBar.ActionBarDelegation() {
//            @Override
//            public void backOnClick() {
//                finish();
//            }
//        });
//
//        actionBar.setActionBarName("Chi tiết nhân viên");

        //
        //
        loaiSP.add("Quần tây");
        loaiSP.add("Quần đùi");
        loaiSP.add("Áo thun");
        loaiSP.add("Áo dài");
        ArrayAdapter<String> arrayAdapterLoaiSP = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, loaiSP);
        arrayAdapterLoaiSP.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnLoaiSP.setAdapter(arrayAdapterLoaiSP);
        //
//        arrayAdapterSizeSP = new ArrayAdapter<Size>(this, android.R.layout.simple_spinner_item, sizeSP);
//        arrayAdapterSizeSP.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spnSizeSP.setAdapter(arrayAdapterSizeSP);
//        //
//        spnSizeSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                sSize = position;
//                edtSoLuongSP.setText(String.valueOf(sizeSP.get(sSize).getSoLuong()));
//
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
        //
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            String MaNV = bundle.getString("MaSanPham", "");
            getDataNV(MaNV);
        }
        //Tạo ngày tháng năm sinh
        btnXoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Gọi hàm delete
                openDiaLogDelete(sanPham);
            }
        });
        btnThayDoi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Gọi hàm update
                openDiaLogUpdateItem(sanPham);
            }
        });
        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Size size = new Size(sizeSP.get(sSize).getTenSize(),Integer.parseInt(edtSoLuongSP.getText().toString()));
//                sizeSP.set(sSize,size);
//                arrayAdapterSizeSP.notifyDataSetChanged();
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
                Toast.makeText(ChiTietSanPham.this, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
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
        TedBottomPicker tedBottomPicker = new TedBottomPicker.Builder(ChiTietSanPham.this)
                .setOnImageSelectedListener(listener)
                .create();
        tedBottomPicker.show(getSupportFragmentManager());
    }

    //Hàm update
    private void openDiaLogUpdateItem(SanPham sanPham) {
        AlertDialog.Builder b = new AlertDialog.Builder(this);
//        new AlertDialog.Builder(this)
        b.setTitle("Thay đổi");
        b.setMessage("Bạn có muốn thay đổi?");
        b.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mData = FirebaseDatabase.getInstance().getReference("SanPham");
                String newTen = edtTenSP.getText().toString().trim();
                int newGia = Integer.parseInt(edtGiaSP.getText().toString().trim());
                String newLoai = spnLoaiSP.getSelectedItem().toString();
                String newMoTa = edtMoTaSP.getText().toString().trim();

                sanPham.setTen(newTen);
                sanPham.setGia(newGia);
                sanPham.setLoai(newLoai);
//                sanPham.setSize(sizeSP);
                sanPham.setMoTa(newMoTa);
//                mData.child(String.valueOf(sanPham.getMaSanPham())).updateChildren(sanPham.toMap());

                updateImage();
                Intent intent = new Intent(getApplicationContext(), DanhSachSanPham.class);
                startActivity(intent);
            }

        });
        b.setNegativeButton("Từ chối", null);
        b.setCancelable(false);
        b.show();
    }

    private void updateImage() {
        //Thêm hình ảnh lên firebase
        StorageReference mountainsRef = storageRef.child(sanPham.getMaSanPham() + ".png");
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
                Toast.makeText(ChiTietSanPham.this, "Lỗi", Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
            }
        });
    }

    //Hàm delete
    private void openDiaLogDelete(SanPham sanPham) {
        AlertDialog.Builder b = new AlertDialog.Builder(this);
//        new AlertDialog.Builder(this)
        b.setTitle("Xoá");
        b.setMessage("Bạn có muốn xoá?");
        b.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                storageRef.child(String.valueOf(sanPham.getAnh())).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        mData.child(String.valueOf(sanPham.getMaSanPham())).removeValue();
                        Intent intent = new Intent(getApplicationContext(), DanhSachSanPham.class);
                        startActivity(intent);
                    }
                });
            }
        });
        b.setNegativeButton("Từ chối", null);
        b.setCancelable(false);
        b.show();
    }

    //Hàm lấy dữ liệu từ màn hình RV
    private void getDataNV(String MaNV) {
        mData = FirebaseDatabase.getInstance().getReference("SanPham");
        mData.child(MaNV).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                SanPham nSanPham = snapshot.getValue(SanPham.class);
                if (nSanPham != null) {
                    sanPham = nSanPham;
                    edtTenSP.setText(sanPham.getTen());
                    edtGiaSP.setText(String.valueOf(sanPham.getGia()));
                    getAnhNhanVien(sanPham.getAnh());
                    for (int i = 0; i < loaiSP.size(); i++) {
                        if (loaiSP.get(i).equals(sanPham.getLoai())) {
                            spnLoaiSP.setSelection(i);
                        }
                    }
//                    sizeSP.addAll(sanPham.getSize());
//                    arrayAdapterSizeSP.notifyDataSetChanged();
                    edtMoTaSP.setText(sanPham.getMoTa());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });


    }

    //tẽttt
    private void getAnhNhanVien(String anh) {
        int dot = anh.lastIndexOf('.');
        String base = (dot == -1) ? anh : anh.substring(0, dot);
        String extension = (dot == -1) ? "" : anh.substring(dot + 1);
        try {
            final File file = File.createTempFile(base, extension);
            storageRef.child(anh).getFile(file)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                            imgAnhSP.setImageBitmap(bitmap);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setControl() {
        imgAnhSP = findViewById(R.id.imgAnhSP);
        edtTenSP = findViewById(R.id.edtTenSP);
        edtGiaSP = findViewById(R.id.edtGiaSP);
        edtSoLuongSP = findViewById(R.id.edtSoLuongSP);
        edtMoTaSP = findViewById(R.id.edtMoTaSP);
        spnLoaiSP = findViewById(R.id.spnLoaiSP);
        spnSizeSP = findViewById(R.id.spnSizeSP);
        btnXoa = findViewById(R.id.btnXoa);
        btnThayDoi = findViewById(R.id.btnThayDoi);
        btnThem = findViewById(R.id.btnThem);
//        actionBar = findViewById(R.id.actionBar);
    }
}