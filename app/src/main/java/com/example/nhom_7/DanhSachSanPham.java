package com.example.nhom_7;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class DanhSachSanPham extends AppCompatActivity {
    Button btnThem;
    Spinner spnLoaiSP;
    RecyclerView lvSanPham;
    ArrayList<SanPham> list = new ArrayList<SanPham>();
    AdapterSanPham adapter;
    DatabaseReference mData;
    SearchView svSearch;
    ArrayList<String> loaiSP = new ArrayList<String>();
//    CustomActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danh_sach_san_pham);

        setControl();
        setEvent();

    }


    private void setEvent() {
//        actionBar.setDelegation(new CustomActionBar.ActionBarDelegation() {
//            @Override
//            public void backOnClick() {
//                finish();
//            }
//        });
//
//        actionBar.setActionBarName("Danh sách nhập kho");

        //
        loaiSP.add("Tất cả");
        loaiSP.add("Quần tây");
        loaiSP.add("Quần đùi");
        loaiSP.add("Áo thun");
        loaiSP.add("Áo dài");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, loaiSP);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnLoaiSP.setAdapter(arrayAdapter);
        //Gọi firebase
        getFirebase();

        //
        adapter = new AdapterSanPham(this, R.layout.activity_item_san_pham, list);
        lvSanPham.setAdapter(adapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        lvSanPham.setLayoutManager(layoutManager);


//        //Search
//        svSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                filter(newText);
//                return true;
//            }
//        });

        //Chuyển màn hình thêm
        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), ThemSanPham.class);
                startActivity(intent);
            }
        });
    }

    //Hàm lọc theo tên
    private void filter(String search) {
        ArrayList<SanPham> filterList = new ArrayList<>();
        for (SanPham sanPham : list) {
            if (sanPham.getTen().toLowerCase().contains(search.toLowerCase())) {
                filterList.add(sanPham);
            }
            adapter.filterList(filterList);
        }
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


    private void setControl() {
        btnThem = findViewById(R.id.btnThem);
        lvSanPham = findViewById(R.id.lvSanPham);
        spnLoaiSP = findViewById(R.id.spnLoaiSP);
//        svSearch = findViewById(R.id.svSearch);
//        actionBar = findViewById(R.id.actionBar);
    }
}