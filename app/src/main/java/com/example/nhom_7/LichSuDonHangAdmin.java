package com.example.nhom_7;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nhom_7.adapter.LichSuDonHangAdminAdapter;
import com.example.nhom_7.model.DonHang;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class LichSuDonHangAdmin extends Fragment {
    RecyclerView lvLichSuDH;
    ArrayList<DonHang> data = new ArrayList<DonHang>();
    LichSuDonHangAdminAdapter myRecyclerViewAdapter;
    DatabaseReference database;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_lich_su_don_hang_admin,container,false);
        setConTrol(view);
        setEvent();

        return view;
    }

    //Lấy dữ liệu firebase
    private void getlist(){
        database= FirebaseDatabase.getInstance().getReference("LichSuDonHang");
        database.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                DonHang donHang = snapshot.getValue(DonHang.class);
                if(donHang != null){
                    data.add(0,donHang);
                    myRecyclerViewAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                DonHang donHang = snapshot.getValue(DonHang.class);
                if(donHang == null || data==null||data.isEmpty()){
                    return;
                }
                for (int i = 0;i<data.size();i++){
                    if(donHang.getMaDH()==data.get(i).getMaDH()){
                        data.set(i,donHang);
                    }
                }
                myRecyclerViewAdapter.notifyDataSetChanged();
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
    private void setEvent() {
        myRecyclerViewAdapter = new LichSuDonHangAdminAdapter(getActivity(),R.layout.layout_item_lich_su_don_hang_admin,data);
        myRecyclerViewAdapter.setDelegation(new LichSuDonHangAdminAdapter.MyItemClickListener() {
            @Override
            public void getXacNhanDonHang(DonHang lichSuDH) {
                database=FirebaseDatabase.getInstance().getReference("LichSuDonHang");
                String trangThai = "Xác nhận";
                lichSuDH.setTrangThai(trangThai);
                database.child(String.valueOf(lichSuDH.getMaDH())).updateChildren(lichSuDH.toMap());
            }

            @Override
            public void getHuyDonHang(DonHang lichSuDH) {
                database=FirebaseDatabase.getInstance().getReference("LichSuDonHang");
                String trangThai = "Huỷ";
                lichSuDH.setTrangThai(trangThai);
                database.child(String.valueOf(lichSuDH.getMaDH())).updateChildren(lichSuDH.toMap());
            }

            @Override
            public void onClick(DonHang lichSuDH) {
                Intent intent = new Intent(getActivity(),ChiTietDonHang.class);
                Bundle bundle = new Bundle();
                bundle.putString("maDonHang", lichSuDH.getMaDH());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        lvLichSuDH.setAdapter(myRecyclerViewAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        lvLichSuDH.setLayoutManager(layoutManager);
        getlist();
    }

    private void setConTrol(View view) {
        lvLichSuDH= view.findViewById(R.id.lvLichSuDH);
    }
}
