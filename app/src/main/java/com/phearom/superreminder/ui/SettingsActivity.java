package com.phearom.superreminder.ui;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.CompoundButton;

import com.phearom.api.utils.SessionManager;
import com.phearom.superreminder.R;
import com.phearom.superreminder.databinding.ActivitySettingsBinding;
import com.phearom.superreminder.services.LocationService;

public class SettingsActivity extends AppCompatActivity {
    private final static String Key_Enabled = "enabled";
    private ActivitySettingsBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_settings);
        mBinding.setEnabled(SessionManager.init(this).getUserData(Key_Enabled, false));
        mBinding.switchLocTracker.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!SessionManager.init(SettingsActivity.this).getUserData(Key_Enabled, false))
                    startService(new Intent(SettingsActivity.this, LocationService.class));
                else
                    stopService(new Intent(SettingsActivity.this, LocationService.class));

                SessionManager.init(SettingsActivity.this).saveUserData(Key_Enabled, isChecked);
            }
        });
    }
}
