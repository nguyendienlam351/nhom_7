package com.example.nhom_7;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.nhom_7.adapter.SanPhamAdapter;
import com.example.nhom_7.model.LoaiSanPham;
import com.example.nhom_7.model.SanPham;
import com.example.nhom_7.model.Size;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

public class TrangChu extends AppCompatActivity {
    RecyclerView lvDanhSach;
    ArrayList<SanPham> sanPhamArrayList;
    SanPhamAdapter sanPhamAdapter;
    ArrayList<LoaiSanPham> loaiSanPhamArrayList;
    ArrayAdapter<LoaiSanPham> loaiSanPhamArrayAdapter;
    DatabaseReference database;
    Spinner spnSapXep;
    Spinner spnLoai;
    int viTriLoai = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trang_chu);
        setControl();
        setEvent();
    }

    private void setEvent() {
        database = FirebaseDatabase.getInstance().getReference();

        sanPhamArrayList = new ArrayList<SanPham>();
        sanPhamAdapter = new SanPhamAdapter(this, R.layout.layout_item_san_pham, sanPhamArrayList);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        lvDanhSach.setLayoutManager(layoutManager);
        lvDanhSach.setAdapter(sanPhamAdapter);
        getDataSanPham();

        loaiSanPhamArrayList = new ArrayList<LoaiSanPham>();
        loaiSanPhamArrayAdapter = new ArrayAdapter<LoaiSanPham>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, loaiSanPhamArrayList);
        loaiSanPhamArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnLoai.setAdapter(loaiSanPhamArrayAdapter);
        getDataLoaiSanPham();

        spnLoai.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                viTriLoai = position;
                filter();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayList<String> sapXep = new ArrayList<>();
        sapXep.add("Mới nhất");
        sapXep.add("Giá giảm dần");
        sapXep.add("Giá tăng dần");
        sapXep.add("Yêu thích");

        ArrayAdapter<String> sapXepAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, sapXep);
        sapXepAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spnSapXep.setAdapter(sapXepAdapter);

        spnSapXep.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (spnSapXep.getSelectedItem().toString()) {
                    case "Mới nhất":
                        sanPhamAdapter.sortList(new Comparator<SanPham>() {
                            @Override
                            public int compare(SanPham o1, SanPham o2) {
                                return -(o1.getMaSanPham().compareTo(o2.getMaSanPham()));
                            }
                        });
                        break;
                    case "Giá giảm dần":
                        sanPhamAdapter.sortList(new Comparator<SanPham>() {
                            @Override
                            public int compare(SanPham o1, SanPham o2) {
                                return o2.getGia() - o1.getGia();
                            }
                        });
                        break;
                    case "Giá tăng dần":
                        sanPhamAdapter.sortList(new Comparator<SanPham>() {
                            @Override
                            public int compare(SanPham o1, SanPham o2) {
                                return o1.getGia() - o2.getGia();
                            }
                        });
                        break;
                    case "Yêu thích":
                        sanPhamAdapter.sortList(new Comparator<SanPham>() {
                            @Override
                            public int compare(SanPham o1, SanPham o2) {
                                if (o1.getDanhGia() > o2.getDanhGia()) {
                                    return -1;
                                }
                                if (o1.getDanhGia() < o2.getDanhGia()) {
                                    return 1;
                                } else {
                                    return 0;
                                }
                            }
                        });
                        break;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

//        for(int i = 1; i < 5; i++){
//            LoaiSanPham loaiSanPham = new LoaiSanPham();
//            loaiSanPham.setMaLoai(database.push().getKey());
//            loaiSanPham.setTenLoai("Loại " + i);
//
//            database.child("LoaiSanPham").child(loaiSanPham.getMaLoai()).setValue(loaiSanPham);
//        }

//        for(int i =1 ; i < 10 ; i++){
//            SanPham sanPham =  new SanPham();
//            sanPham.setMaSanPham(database.push().getKey());
//            sanPham.setTen("Sản phẩm " + i);
//            sanPham.setLoai("Loại " + i);
//            sanPham.setGia(i+ 10000);
//            sanPham.setAnh("images (3).jpg");
//            sanPham.setMoTa("Lorem Ipsum is simply dummy text of the printing and typese" +
//                    "tting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, " +
//                    "when an unknown printer took a galley of type and scrambled it to make a type specimen book.");
//            ArrayList<Size> arrayList = new ArrayList<>();
//            arrayList.add(new Size(Size.SIZE_S, i*i));
//            arrayList.add(new Size(Size.SIZE_M, i*i));
//            arrayList.add(new Size(Size.SIZE_L, i*i));
//            arrayList.add(new Size(Size.SIZE_XL, i*i));
//            sanPham.setSize(arrayList);
//            sanPham.setDanhGia(4.0 + ((double) i/10));
//            database.child(sanPham.getMaSanPham()).setValue(sanPham);
//        }

    }
    private void filter(){
        String maLoai = loaiSanPhamArrayList.get(viTriLoai).getMaLoai();
        ArrayList<SanPham> filterList = new ArrayList<SanPham>();
        for (SanPham chiTietDonHang : sanPhamArrayList) {
            if(chiTietDonHang.getLoai().contains(maLoai)){
                filterList.add(chiTietDonHang);
            }
        }

        sanPhamAdapter.filterList(filterList);
    }

    private void getDataLoaiSanPham() {
        LoaiSanPham tatCa = new LoaiSanPham();
        tatCa.setTenLoai("Tất cả");
        tatCa.setMaLoai("");
        loaiSanPhamArrayList.add(tatCa);
        database.child("LoaiSanPham").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                LoaiSanPham loaiSanPham = snapshot.getValue(LoaiSanPham.class);
                if (loaiSanPham != null) {
                    loaiSanPhamArrayList.add(loaiSanPham);
                    loaiSanPhamArrayAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                LoaiSanPham loaiSanPham = snapshot.getValue(LoaiSanPham.class);
                if (loaiSanPham == null || loaiSanPhamArrayList == null || loaiSanPhamArrayList.isEmpty()) {
                    return;
                }

                for (int i = 0; i < loaiSanPhamArrayList.size(); i++) {
                    if (loaiSanPham.getMaLoai().equals(loaiSanPhamArrayList.get(i).getMaLoai())) {
                        loaiSanPhamArrayList.set(i, loaiSanPham);
                        break;
                    }
                }
                loaiSanPhamArrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                LoaiSanPham loaiSanPham = snapshot.getValue(LoaiSanPham.class);
                if (loaiSanPham == null || loaiSanPhamArrayList == null || loaiSanPhamArrayList.isEmpty()) {
                    return;
                }

                for (int i = 0; i < loaiSanPhamArrayList.size(); i++) {
                    if (loaiSanPham.getMaLoai().equals(loaiSanPhamArrayList.get(i).getMaLoai())) {
                        loaiSanPhamArrayList.remove(loaiSanPhamArrayList.get(i));
                        break;
                    }
                }
                loaiSanPhamArrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getDataSanPham() {
        database.child("SanPham").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                SanPham sanPham = snapshot.getValue(SanPham.class);
                if (sanPham != null) {
                    sanPhamArrayList.add(0, sanPham);
                    sanPhamAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                SanPham sanPham = snapshot.getValue(SanPham.class);
                if (sanPham == null || sanPhamArrayList == null || sanPhamArrayList.isEmpty()) {
                    return;
                }

                for (int i = 0; i < sanPhamArrayList.size(); i++) {
                    if (sanPham.getMaSanPham().equals(sanPhamArrayList.get(i).getMaSanPham())) {
                        sanPhamArrayList.set(i, sanPham);
                        break;
                    }
                }
                sanPhamAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                SanPham sanPham = snapshot.getValue(SanPham.class);
                if (sanPham == null || sanPhamArrayList == null || sanPhamArrayList.isEmpty()) {
                    return;
                }

                for (int i = 0; i < sanPhamArrayList.size(); i++) {
                    if (sanPham.getMaSanPham().equals(sanPhamArrayList.get(i).getMaSanPham())) {
                        sanPhamArrayList.remove(sanPhamArrayList.get(i));
                        break;
                    }
                }
                sanPhamAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setControl() {
        lvDanhSach = findViewById(R.id.lvDanhSach);
        spnSapXep = findViewById(R.id.spnSapXep);
        spnLoai = findViewById(R.id.spnLoai);
    }
}