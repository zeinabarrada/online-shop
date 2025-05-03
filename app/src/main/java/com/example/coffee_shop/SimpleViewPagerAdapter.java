package com.example.coffee_shop;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.List;

public class SimpleViewPagerAdapter extends FragmentStateAdapter {
    private List<Fragment> fragmentList;
    public SimpleViewPagerAdapter(FragmentActivity fragmentActivity,List<Fragment> fragmentList)
    {
        super(FragmentActivity);
        this.fragmentList=fragmentList;
    }
    @NonNull
    @Override
    public Fragment createFragment(int position)
    {
        return fragmentList.get(position);
    }
    public int getItemCount(){return  fragmentList.size();}
}
