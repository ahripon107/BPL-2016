package com.banglacricket.bdcricketteam.activity;

import android.os.Bundle;
import android.view.MenuItem;

import com.banglacricket.bdcricketteam.R;
import com.banglacricket.bdcricketteam.util.RoboAppCompatActivity;

/**
 * @author Ripon
 */

public class CommonActivity extends RoboAppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                overridePendingTransition(R.anim.right_in, R.anim.right_out);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.right_in, R.anim.right_out);
    }
}
