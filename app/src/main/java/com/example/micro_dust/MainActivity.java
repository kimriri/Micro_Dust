package com.example.micro_dust;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    Fragment fragment = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadFragment(new Micro_Dust());
        BottomNavigationView navigatioView = findViewById(R.id.nav_view);
        navigatioView.setOnNavigationItemSelectedListener(this);

    }


    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true; }
            return false;
    }

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {


            switch (item.getItemId()) {
                case R.id.Micro_Dust:
                    loadFragment(new Micro_Dust());
                    return true;
                case R.id.shopping:
                    loadFragment(new NOTICE());
                    return true;
                case R.id.my_page:
                    loadFragment(new My_Page());
                    return true;

            }
            return loadFragment(fragment);
        }

}
