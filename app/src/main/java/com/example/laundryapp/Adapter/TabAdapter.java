package com.example.laundryapp.Adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class TabAdapter extends FragmentPagerAdapter {

    ArrayList<Fragment> arr_frg = new ArrayList<>();
    ArrayList<String> arr_title = new ArrayList<>();

    public TabAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    //constructor
    public void AddFragment(Fragment fragment, String title){
            arr_frg.add(fragment);
            arr_title.add(title);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return arr_frg.get(position);
    }

    @Override
    public int getCount() {
        return arr_frg.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return arr_title.get(position);
    }
}
