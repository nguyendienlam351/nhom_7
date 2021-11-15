package com.example.nhom_7;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class TrangTaiKhoan extends AppCompatActivity {
    private TabLayout layout;
    private ViewPager2 viewPager;
    private MyViewPagerAdapter myViewPagerAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trang_tai_khoan);
        setControl();
        setEvent();
    }

    private void setEvent() {
        myViewPagerAdapter = new MyViewPagerAdapter(this);
        viewPager.setAdapter(myViewPagerAdapter);
        new TabLayoutMediator(layout, viewPager, (tab, position) -> {
            switch (position){
                case 0:
                    tab.setText("Đăng Nhập");
                    break;
                case 1:
                    tab.setText("Đăng Kí");
                    break;
            }
        }).attach();
    }

    private void setControl() {
        layout = findViewById(R.id.tablayout);
        viewPager = findViewById(R.id.viewpager);
    }

}