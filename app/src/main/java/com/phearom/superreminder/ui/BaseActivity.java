package com.phearom.superreminder.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;

import com.phearom.api.bases.BottomBarAbstractActivity;
import com.phearom.superreminder.R;
import com.phearom.superreminder.ui.fragment.BaseFragment;

import java.util.HashSet;
import java.util.Set;

import static com.phearom.superreminder.R.transition.fr_fade;

/**
 * Created by phearom on 6/6/16.
 */
public class BaseActivity extends BottomBarAbstractActivity {
    private Set<BaseFragment> fragments;
    private BaseFragment currentFragment = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragments = new HashSet<>();
    }

    @SuppressWarnings("WrongConstant")
    public void showFragment(int content, BaseFragment fragment) {
        currentFragment = fragment;
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setTransition(fr_fade);
        if (fragments.contains(fragment))
            transaction.show(fragment);
        else {
            transaction.replace(content, fragment);
            transaction.commit();
        }
    }

    protected Set<BaseFragment> getFragments() {
        return fragments;
    }

    protected int getFragmentPosition() {
        int i = -1;
        for (BaseFragment f : fragments) {
            if (f.equals(currentFragment)) {
                return i;
            }
            i++;
        }
        return i;
    }
}
