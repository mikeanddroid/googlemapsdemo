package com.mike.givemewingzz.bbvamaps.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.mike.givemewingzz.bbvamaps.R;
import com.mike.givemewingzz.bbvamaps.service.OttoHelper;
import com.mike.givemewingzz.bbvamaps.ui.MapFragment;
import com.mike.givemewingzz.bbvamaps.ui.MapListFragment;

public class BaseActivity extends AppCompatActivity {

    private static final String TAG = BaseActivity.class.getSimpleName();

    private Fragment mapFragment;
    private Fragment listFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        // Create map instance
        mapFragment = MapFragment.newInstance();
        FragmentTransaction clickedTransactionMaps = getSupportFragmentManager().beginTransaction();
        clickedTransactionMaps.replace(R.id.mapFragment, mapFragment).commit();

        // Create list instance
        listFragment = MapListFragment.newInstance();
        FragmentTransaction listFragmentTransaction = getSupportFragmentManager().beginTransaction();
        listFragmentTransaction.replace(R.id.listFragment, listFragment).commit();

        // Hide List Fragment by default
        getSupportFragmentManager().beginTransaction().hide(listFragment).commit();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Toggle visibility for map and list
        if (item.getItemId() == R.id.maps_item) {

            getSupportFragmentManager().beginTransaction().show(mapFragment).commit();
            getSupportFragmentManager().beginTransaction().hide(listFragment).commit();

        } else {

            getSupportFragmentManager().beginTransaction().hide(mapFragment).commit();
            getSupportFragmentManager().beginTransaction().show(listFragment).commit();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        // UnRegister Otto
        OttoHelper.unregister(this);
        super.onPause();
    }

    @Override
    protected void onResume() {
        // Register Otto
        OttoHelper.register(this);
        super.onResume();
    }

}
