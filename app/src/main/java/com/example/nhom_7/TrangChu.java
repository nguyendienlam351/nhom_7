package com.example.nhom_7;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SearchView;
import android.widget.Spinner;

import com.example.nhom_7.adapter.SanPhamAdapter;
import com.example.nhom_7.model.LoaiSanPham;
import com.example.nhom_7.model.SanPham;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Comparator;

public class TrangChu extends Fragment {
    RecyclerView lvDanhSach;
    ArrayList<SanPham> sanPhamArrayList;
    SanPhamAdapter sanPhamAdapter;
    ArrayList<LoaiSanPham> loaiSanPhamArrayList;
    ArrayAdapter<LoaiSanPham> loaiSanPhamArrayAdapter;
    DatabaseReference database;
    SearchView edtTimKiem;
    Spinner spnSapXep;
    Spinner spnLoai;
    int viTriLoai = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_trang_chu,container,false);
        setControl(view);
        setEvent();

        return view;
    }

    private void setEvent() {
        database = FirebaseDatabase.getInstance().getReference();

        //Danh sách sản phẩm
        sanPhamArrayList = new ArrayList<SanPham>();
        sanPhamAdapter = new SanPhamAdapter(getActivity(), R.layout.layout_item_san_pham, sanPhamArrayList);
        sanPhamAdapter.setDelegation(new SanPhamAdapter.ItemClickListener() {
            @Override
            public void itemClick(SanPham sanPham) {
                Intent intent = new Intent(getActivity(), XemChiTietSanPham.class);
                Bundle bundle = new Bundle();
                bundle.putString("maSanPham", sanPham.getMaSanPham());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        lvDanhSach.setLayoutManager(layoutManager);
        lvDanhSach.setAdapter(sanPhamAdapter);
        getDataSanPham();

        //Spinner loại sản phẩm
        loaiSanPhamArrayList = new ArrayList<LoaiSanPham>();
        loaiSanPhamArrayAdapter = new ArrayAdapter<LoaiSanPham>(getActivity(), android.R.layout.simple_spinner_dropdown_item, loaiSanPhamArrayList);
        loaiSanPhamArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnLoai.setAdapter(loaiSanPhamArrayAdapter);
        getDataLoaiSanPham();
        spnLoai.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                viTriLoai = position;
                filter(edtTimKiem.getQuery().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //Spinner sắp xếp
        ArrayList<String> sapXep = new ArrayList<>();
        sapXep.add("Mới nhất");
        sapXep.add("Giá giảm dần");
        sapXep.add("Giá tăng dần");
        sapXep.add("Yêu thích");

        ArrayAdapter<String> sapXepAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, sapXep);
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
                                if (getDanhGia(o1.getDanhGia()) > getDanhGia(o2.getDanhGia())) {
                                    return -1;
                                }
                                if (getDanhGia(o1.getDanhGia()) < getDanhGia(o2.getDanhGia())) {
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

        //Search view tìm kiếm
        edtTimKiem.setOnQueryTextListener(new android.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return true;
            }
        });

    }

    //Lọc danh sách sản phẩm
    private void filter(String tenSanPham){
        String maLoai = loaiSanPhamArrayList.get(viTriLoai).getMaLoai();
        ArrayList<SanPham> filterList = new ArrayList<SanPham>();
        for (SanPham sanPham : sanPhamArrayList) {
            if(sanPham.getLoai().contains(maLoai) && sanPham.getTen().toLowerCase().contains(tenSanPham.toLowerCase())){
                filterList.add(sanPham);
            }
        }

        sanPhamAdapter.filterList(filterList);
    }


    //Lấy dữ liệu loại sản phẩm
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


    //Lấy dữ liệu sản phẩm
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


    //Tính điểm đánh giá
    private float getDanhGia(ArrayList<Float> danhGiaArrayList){
        float ratingSum = 0f;
        for(Float r: danhGiaArrayList)  {
            ratingSum += r;
        }
        return  ratingSum / danhGiaArrayList.size();
    }

    private void setControl(View view) {
        lvDanhSach = view.findViewById(R.id.lvDanhSach);
        spnSapXep = view.findViewById(R.id.spnSapXep);
        spnLoai = view.findViewById(R.id.spnLoai);
        edtTimKiem = view.findViewById(R.id.edtTimKiem);
    }
}