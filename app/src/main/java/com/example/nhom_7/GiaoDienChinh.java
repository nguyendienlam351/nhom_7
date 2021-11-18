package com.example.nhom_7;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class GiaoDienChinh extends AppCompatActivity {
    private ViewPager2 viewPager2;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_giao_dien_chinh);
        setControl();
        setEvent();
    }

    private void setEvent() {
        MyViewPagerAdapterGDC myViewPagerAdapterGDC = new MyViewPagerAdapterGDC(this);
        viewPager2.setAdapter(myViewPagerAdapterGDC);

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                switch (position){
                    case 0:
                        bottomNavigationView.getMenu().findItem(R.id.action_home).setChecked(true);
                        break;
                    case 1:
                        bottomNavigationView.getMenu().findItem(R.id.action_manage).setChecked(true);
                        break;
                    case 2:
                        bottomNavigationView.getMenu().findItem(R.id.action_cart).setChecked(true);
                        break;
                    case 3:
                        bottomNavigationView.getMenu().findItem(R.id.action_info).setChecked(true);
                        break;
                }
            }
        });
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.action_home:
                        viewPager2.setCurrentItem(0);
                        break;
                    case R.id.action_manage:
                        viewPager2.setCurrentItem(1);
                        break;
                    case R.id.action_cart:
                        viewPager2.setCurrentItem(2);
                        break;
                    case R.id.action_info:
                        viewPager2.setCurrentItem(3);
                        break;
                }
                return true;
            }
        });
    }

    private void setControl() {
        viewPager2 = findViewById(R.id.viewPagar2);
        bottomNavigationView = findViewById(R.id.bottom_nav);
    }
}
