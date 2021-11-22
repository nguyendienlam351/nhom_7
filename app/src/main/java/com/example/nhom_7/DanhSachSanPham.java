package com.example.nhom_7;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nhom_7.adapter.AdapterSanPham;
import com.example.nhom_7.model.LoaiSanPham;
import com.example.nhom_7.model.SanPham;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class DanhSachSanPham extends Fragment {
    Button btnThem;
    Spinner spnLoaiSP;
    RecyclerView lvSanPham;
    ArrayList<SanPham> list = new ArrayList<SanPham>();
    AdapterSanPham adapter;
    DatabaseReference mData;
    SearchView svSearch;
    ArrayList<LoaiSanPham> loaiSanPhamArrayList;
    ArrayAdapter<LoaiSanPham> loaiSanPhamArrayAdapter;
    int viTriLoai = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_danh_sach_san_pham,container,false);
        setControl(view);
        setEvent();

        return view;
    }


    private void setEvent() {

        loaiSanPhamArrayList = new ArrayList<LoaiSanPham>();
        loaiSanPhamArrayAdapter = new ArrayAdapter<LoaiSanPham>(getActivity(), android.R.layout.simple_spinner_dropdown_item, loaiSanPhamArrayList);
        loaiSanPhamArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnLoaiSP.setAdapter(loaiSanPhamArrayAdapter);
        getDataLoaiSanPham();
        spnLoaiSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                viTriLoai = position;
                filter(svSearch.getQuery().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //Gọi firebase
        getFirebase();

        //
        adapter = new AdapterSanPham(getActivity(), R.layout.activity_item_san_pham, list);
        lvSanPham.setAdapter(adapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        lvSanPham.setLayoutManager(layoutManager);


        //Search
        svSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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


        //
        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), ThemSanPham.class);
                startActivity(intent);
            }
        });
    }

    //Hàm lọc theo tên
    private void filter(String tenSanPham){
        String maLoai = loaiSanPhamArrayList.get(viTriLoai).getMaLoai();
        ArrayList<SanPham> filterList = new ArrayList<SanPham>();
        for (SanPham sanPham : list) {
            if(sanPham.getLoai().contains(maLoai) && sanPham.getTen().toLowerCase().contains(tenSanPham.toLowerCase())){
                filterList.add(sanPham);
            }
        }

        adapter.filterList(filterList);
    }

    private void getDataLoaiSanPham() {
        com.example.nhom_7.model.LoaiSanPham tatCa = new com.example.nhom_7.model.LoaiSanPham();
        tatCa.setTenLoai("Tất cả");
        tatCa.setMaLoai("");
        loaiSanPhamArrayList.add(tatCa);

        DatabaseReference database = FirebaseDatabase.getInstance().getReference();

        database.child("LoaiSanPham").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                com.example.nhom_7.model.LoaiSanPham loaiSanPham = snapshot.getValue(com.example.nhom_7.model.LoaiSanPham.class);
                if (loaiSanPham != null) {
                    loaiSanPhamArrayList.add(loaiSanPham);
                    loaiSanPhamArrayAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                com.example.nhom_7.model.LoaiSanPham loaiSanPham = snapshot.getValue(com.example.nhom_7.model.LoaiSanPham.class);
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
                com.example.nhom_7.model.LoaiSanPham loaiSanPham = snapshot.getValue(LoaiSanPham.class);
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

    //Hàm đọc firebase
    private void getFirebase() {
        // Write a message to the database
        mData = FirebaseDatabase.getInstance().getReference("SanPham");
        mData.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                SanPham sanPham = snapshot.getValue(SanPham.class);
                if (sanPham != null) {
                    list.add(0,sanPham);
                    adapter.notifyDataSetChanged();
                }
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
    }


    private void setControl(View view) {
        btnThem = view.findViewById(R.id.btnThem);
        lvSanPham = view.findViewById(R.id.lvSanPham);
        spnLoaiSP = view.findViewById(R.id.spnLoaiSP);
        svSearch = view.findViewById(R.id.svSearch);
    }
}