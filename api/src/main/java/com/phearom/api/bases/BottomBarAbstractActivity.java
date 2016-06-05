package com.phearom.api.bases;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.view.View;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.BottomBarTab;
import com.roughike.bottombar.OnMenuTabClickListener;
import com.roughike.bottombar.OnTabClickListener;

/**
 * Created by phearom on 6/5/16.
 */
public abstract class BottomBarAbstractActivity extends BaseAbstractActivity {
    private BottomBar _bottomBar;
    private Object[] colors;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected BottomBar getBottomBar() {
        return _bottomBar;
    }

    protected void setBottomBar(CoordinatorLayout layout, View view, Bundle bundle) {
        _bottomBar = BottomBar.attachShy(layout, view, bundle);
        _bottomBar.noNavBarGoodness();
    }

    protected void setBottomBar(Activity activity, Bundle bundle) {
        _bottomBar = BottomBar.attach(activity, bundle);
        _bottomBar.noNavBarGoodness();
    }

    protected void setBottomBar(View view, Bundle bundle) {
        _bottomBar = BottomBar.attach(view, bundle);
        _bottomBar.noNavBarGoodness();
    }

    protected void setBottomItemsFromMenu(int menuId, OnMenuTabClickListener onMenuTabClickListener) {
        if (null == _bottomBar)
            return;
        _bottomBar.setItemsFromMenu(menuId, onMenuTabClickListener);
    }

    protected void mapColorForTab(Object... colors) {
        this.colors = colors;
        try {
            if (null == _bottomBar)
                return;
            int i = 0;
            for (Object c : colors) {
                if (c instanceof String) {
                    _bottomBar.mapColorForTab(i, (String) c);
                    i++;
                } else {
                    _bottomBar.mapColorForTab(i, (Integer) c);
                    i++;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void updateStatusBarColor() {
        if (null == colors)
            return;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Object object = colors[_bottomBar.getCurrentTabPosition()];
            int color = (object instanceof String) ? Color.parseColor((String) object) : (Integer) object;
            getWindow().setStatusBarColor(color);
            if (null != getSupportActionBar())
                getSupportActionBar().setBackgroundDrawable(new ColorDrawable(color));

        }
    }

    protected void addBottomTab(BottomBarTab... tabs) {
        if (null == _bottomBar)
            return;
        _bottomBar.setItems(tabs);
    }

    protected void setOnTabClickListener(OnTabClickListener tabClickListener) {
        if (null == _bottomBar)
            return;
        _bottomBar.setOnTabClickListener(tabClickListener);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        _bottomBar.onSaveInstanceState(outState);
    }
}
