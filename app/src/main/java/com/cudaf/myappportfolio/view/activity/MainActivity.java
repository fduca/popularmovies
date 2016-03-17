package com.cudaf.myappportfolio.view.activity;


import com.cudaf.myappportfolio.R;
import com.cudaf.myappportfolio.util.SortOption;
import com.cudaf.myappportfolio.view.fragment.MainFragment;
import com.cudaf.myappportfolio.view.fragment.OnSortCallback;

import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    OnSortCallback mOnSortCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FragmentManager fragmentManager = getFragmentManager();
        mOnSortCallback = (MainFragment) fragmentManager.findFragmentById(R.id.main_fragment);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sort_popular:
                mOnSortCallback.onSort(SortOption.MOST_POPULAR);
                break;
            case R.id.sort_rating:
                mOnSortCallback.onSort(SortOption.HIGHEST_RATE);
                break;
            case R.id.sort_favourite:
                mOnSortCallback.onSort(SortOption.FAVORITES);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}