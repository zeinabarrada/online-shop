package com.example.coffee_shop;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class AdminActivity extends AppCompatActivity {
    ViewPager2 viewPager;
    TabLayout tabLayout;
    @Override
            protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        viewPager=findViewById(R.id.view_pager);
        tabLayout=findViewById(R.id.tabLayout);

        List<Fragment> fragmentList=new ArrayList<>();
        fragmentList.add(new ViewProductsFragment());
        fragmentList.add(new AddProductFragment());
        fragmentList.add(new DeleteProductFragment());
        fragmentList.add(new ModifyProductFragment());
        SimpleViewPagerAdapter adapter=new SimpleViewPagerAdapter(this,fragmentList);
        viewPager.setAdapter(adapter);
        tabLayout.addTab(tabLayout.newTab().setText("View"));
        tabLayout.addTab(tabLayout.newTab().setText("Add"));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                if (position == 0) { // 0 is ViewProductsFragment
                    Fragment fragment = getSupportFragmentManager().findFragmentByTag("f" + position);
                    if (fragment instanceof ViewProductsFragment) {
                        ((ViewProductsFragment) fragment).refreshProducts();
                    }
                }
            }
        });

    }
}
