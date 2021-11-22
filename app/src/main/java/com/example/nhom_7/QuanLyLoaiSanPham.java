package com.example.nhom_7;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nhom_7.adapter.AdapterLoaiSanPham;
import com.example.nhom_7.model.LoaiSanPham;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class QuanLyLoaiSanPham extends Fragment {
    EditText edtNhapTenLoai;
    Button btnAdd, btnCancel, btnThayDoi;
    LinearLayout layout;
    RecyclerView lvLoai;
    ArrayList<LoaiSanPham> data = new ArrayList<LoaiSanPham>();
    AdapterLoaiSanPham myRecyclerViewAdapter;
    DatabaseReference database;
    LoaiSanPham selected = new LoaiSanPham();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_quan_ly_loai_san_pham, container, false);
        setControl(view);
        setEvent();

        return view;
    }

    //Lấy danh sách loại món trên RealTimeDatabase
    private void getListBanFromRealTimeDatabase() {
        database = FirebaseDatabase.getInstance().getReference("LoaiSanPham");
        Query query = database.orderByChild("tenLoaiMon");
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                LoaiSanPham loaiSP = snapshot.getValue(LoaiSanPham.class);
                if (loaiSP != null) {
                    data.add(loaiSP);
                    myRecyclerViewAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                LoaiSanPham loaiSP = snapshot.getValue(LoaiSanPham.class);
                if (loaiSP == null || data == null || data.isEmpty()) {
                    return;
                }
                for (int i = 0; i < data.size(); i++) {
                    if (loaiSP.getMaLoai() == data.get(i).getMaLoai()) {
                        data.set(i, loaiSP);
                    }
                }
                myRecyclerViewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                LoaiSanPham loaiSP = snapshot.getValue(LoaiSanPham.class);
                if (loaiSP == null || data == null || data.isEmpty()) {
                    return;
                }
                for (int i = 0; i < data.size(); i++) {
                    if (loaiSP.getMaLoai() == data.get(i).getMaLoai()) {
                        data.remove(data.get(i));
                        break;
                    }
                }
                myRecyclerViewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "Faild", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setEvent() {

        myRecyclerViewAdapter = new AdapterLoaiSanPham(getActivity(), R.layout.activity_loai_san_pham, data);
        myRecyclerViewAdapter.setDelegation(new AdapterLoaiSanPham.MyItemClickListener() {
            @Override
            public void getDeleteLoaiSP(LoaiSanPham loaiSanPham) {
                openDiaLogDeleteItem(loaiSanPham);
            }

            @Override
            public void getUpDateLoaiSP(LoaiSanPham loaiSanPham) {
                int dot = loaiSanPham.getTenLoai().lastIndexOf(' ');
                String catChuoi = (dot == -1) ? "" : loaiSanPham.getTenLoai().substring(dot + 1);
                selected.setMaLoai(loaiSanPham.getMaLoai());
                selected.setTenLoai(catChuoi);
                edtNhapTenLoai.setText(catChuoi);
                if (layout.getVisibility() == View.GONE) {
                    btnAdd.setVisibility(View.GONE);
                    layout.setVisibility(View.VISIBLE);
                }
            }
        });

        btnThayDoi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDiaLogUpdateItem();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtNhapTenLoai.setText("");
                btnAdd.setVisibility(View.VISIBLE);
                layout.setVisibility(View.GONE);

            }
        });
        lvLoai.setAdapter(myRecyclerViewAdapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        lvLoai.setLayoutManager(layoutManager);
        //Hàm thêm loại món mới
        edtNhapTenLoai.addTextChangedListener(watcher);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = edtNhapTenLoai.getText().toString().trim();
                String maLoaiSP = database.push().getKey();
                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                LoaiSanPham loaiSP = new LoaiSanPham();
                loaiSP.setTenLoai(name);
                loaiSP.setMaLoai(maLoaiSP);

                database.orderByChild("tenLoai").equalTo(loaiSP.getTenLoai()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            edtNhapTenLoai.setError("Tên loại sản phẩm đã tồn tại");
                        } else {
                            mDatabase.child("LoaiSanPham").child(maLoaiSP).setValue(loaiSP).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(getActivity(), "Thành công", Toast.LENGTH_SHORT).show();
                                    edtNhapTenLoai.setText("");
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
        getListBanFromRealTimeDatabase();

    }

    //Hàm vô hiệu hoá nút thêm khi không nhập gì
    private TextWatcher watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String name = edtNhapTenLoai.getText().toString().trim();
            btnAdd.setEnabled(!name.isEmpty());
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    //Hàm Sửa
    private void openDiaLogUpdateItem() {
        new AlertDialog.Builder(getActivity())
                .setTitle("Thay đổi")
                .setMessage("Bạn có muốn thay đổi?")
                .setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        database = FirebaseDatabase.getInstance().getReference("LoaiSanPham");
                        String newTenLoaiSP = edtNhapTenLoai.getText().toString().trim();
                        selected.setTenLoai("Loại " + newTenLoaiSP);
                        database.orderByChild("tenLoaiMon").equalTo(selected.getTenLoai()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {
                                    edtNhapTenLoai.setError("Tên loại sản phẩm đã tồn tại");
                                } else {
                                    database.child(String.valueOf(selected.getMaLoai())).updateChildren(selected.toMap()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Toast.makeText(getActivity(), "Thành công", Toast.LENGTH_SHORT).show();
                                            edtNhapTenLoai.setText("");
                                            selected = new LoaiSanPham();
                                            btnAdd.setVisibility(View.VISIBLE);
                                            layout.setVisibility(View.GONE);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }

                })
                .setNegativeButton("Từ chối", null)
                .setCancelable(false)
                .show();
    }

    //Hàm xoá
    private void openDiaLogDeleteItem(LoaiSanPham loaiSanPham) {
        new AlertDialog.Builder(getActivity())
                .setTitle("Xoá")
                .setMessage("Bạn có muốn xoá?")
                .setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        database = FirebaseDatabase.getInstance().getReference("LoaiSanPham");
                        database.child(String.valueOf(loaiSanPham.getMaLoai())).removeValue();
                    }

                })
                .setNegativeButton("Từ chối", null)
                .setCancelable(false)
                .show();
    }

    private void setControl(View view) {
        lvLoai = view.findViewById(R.id.lvLoai);
        edtNhapTenLoai = view.findViewById(R.id.edtNhapTenLoai);
        btnAdd = view.findViewById(R.id.btnAdd);
        btnCancel = view.findViewById(R.id.btnCancel);
        btnThayDoi = view.findViewById(R.id.btnThayDoi);
        layout = view.findViewById(R.id.Layout);
        btnAdd.setEnabled(false);
    }
}
