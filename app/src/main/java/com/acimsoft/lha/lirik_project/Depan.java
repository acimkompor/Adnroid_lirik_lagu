package com.acimsoft.lha.lirik_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import maes.tech.intentanim.CustomIntent;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.acimsoft.lha.lirik_project.Config.SessionManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.HashMap;

public class Depan extends AppCompatActivity {

    private static Depan instance;
    public static Depan getInstance(){
        return instance;
    }
    SessionManager sm;
    public static BottomNavigationView navigation;

    private static final int TIME_INTERVAL = 2000;
    private long mBackPressed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_depan);

        navigation = findViewById(R.id.bn_main);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        loadFragment(new HomeFragment());
        instance = this;

    }

    // method untuk load fragment yang sesuai
    public boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fl_container, fragment)
                    .commit();
            return true;
        }

        return false;
    }

    public  BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment = null;

            switch (item.getItemId()) {
                case R.id.home_menu:
                    fragment = new HomeFragment();
                    break;
                case R.id.favorite_menu:
                    fragment = new FavoriteFragment();
                    break;
                case R.id.search_menu:
                    fragment = new SearchFragment();
                    break;
                case R.id.account_menu:
                    sm = new SessionManager(Depan.this);
                    HashMap<String, String> dt = sm.getSess();
                    String status = dt.get(sm.STATUS);

                    if (status!=null){
                        fragment = new SettingFragment();
                    } else {
                        startActivity(new Intent(Depan.this, Login.class));
                        CustomIntent.customType(Depan.this, "bottom-to-up");
                    }
                    break;
            }
            return loadFragment(fragment);
        }
    };

    public void onBackPressed() {
        if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis())
        {
            super.onBackPressed();
            return;
        }
        else {
            Toast.makeText(getBaseContext(), "Tekan sekali lagi untuk keluar", Toast.LENGTH_SHORT).show();
        }

        mBackPressed = System.currentTimeMillis();
    }


}
