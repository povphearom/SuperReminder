package com.phearom.superreminder.ui;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.design.widget.AppBarLayout;
import android.view.Menu;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.Toast;

import com.phearom.superreminder.R;
import com.phearom.superreminder.databinding.ActivityMainBinding;
import com.phearom.superreminder.ui.fragment.BaseFragment;
import com.phearom.superreminder.ui.fragment.LocationsFragment;
import com.phearom.superreminder.ui.fragment.MapFragment;
import com.phearom.superreminder.ui.fragment.TimelineFragment;
import com.phearom.superreminder.viewmodel.LocationsViewModel;
import com.roughike.bottombar.OnMenuTabClickListener;

public class MainActivity extends BaseActivity implements OnMenuTabClickListener {

    private ActivityMainBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        setSupportActionBar(mBinding.toolbar);

        setBottomBar(mBinding.coordinate, mBinding.container, savedInstanceState);
        setBottomItemsFromMenu(R.menu.main_bottom, this);

        mBinding.appBar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (verticalOffset == 0) {
                    getBottomBar().animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();
                    mBinding.fab.animate().translationY(-mBinding.fab.getHeight()).setInterpolator(new DecelerateInterpolator(2)).start();
                } else {
                    getBottomBar().animate().translationY(mBinding.fab.getHeight()).setInterpolator(new AccelerateInterpolator(2)).start();
                    mBinding.fab.animate().translationY(mBinding.fab.getHeight() + getResources().getDimensionPixelSize(R.dimen.fab_margin)).setInterpolator(new AccelerateInterpolator(2)).start();
                }
            }
        });

        mBinding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Hello Snackbar", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onMenuTabSelected(@IdRes int menuItemId) {
        switch (menuItemId) {
            case R.id.action_schedule:
                showFragment(mBinding.container.getId(), LocationsFragment.init());
                break;
            case R.id.action_timeline:
                showFragment(mBinding.container.getId(), TimelineFragment.init());
                break;
            case R.id.action_room:
                showFragment(mBinding.container.getId(), MapFragment.init());
                break;
        }
    }

    @Override
    public void onMenuTabReSelected(@IdRes int menuItemId) {
    }

    @Override
    public void onBackPressed() {
        if (getBottomBar().getCurrentTabPosition() == 0)
            super.onBackPressed();
        else
            getBottomBar().selectTabAtPosition(0, false);
    }
}
