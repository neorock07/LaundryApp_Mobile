package com.example.laundryapp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.laundryapp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView btmV;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    btmV = findViewById(R.id.btmv);
    //bottom navigation
    setFragment(new Tab_default());
    btmV.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()){

                case R.id.home:
                    setFragment(new Tab_default());
                    return true;
                case R.id.keuangan:
                    setFragment(new Tab_uang());
                    return true;
                case R.id.transaksi:
                    setFragment(new Tab_transaksi());
                    return true;
                case R.id.lain:
                    setFragment(new Tab_lain());
                    return true;
            }
            return false;
        }
    });




    }

    private void setFragment(Fragment fragment){
        getSupportFragmentManager().beginTransaction()
                .addToBackStack(null).replace(R.id.frame_lay,fragment )
                .commit();

    }
}