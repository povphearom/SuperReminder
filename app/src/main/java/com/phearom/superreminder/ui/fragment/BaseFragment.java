package com.phearom.superreminder.ui.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.phearom.api.keys.IntentKeys;

/**
 * Created by phearom on 5/19/16.
 */
public abstract class BaseFragment extends Fragment {
    private IntentFilter mIntentFilter;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(IntentKeys.BROADCAST_ACTION);
    }

    @Override
    public void onStart() {
        getContext().registerReceiver(receiver, mIntentFilter);
        super.onStart();
    }

    @Override
    public void onStop() {
        getContext().unregisterReceiver(receiver);
        super.onStop();
    }

    protected void onReceivedBroadcast(String action, Intent intent) {
        Log.i(action, intent.getExtras().getString(IntentKeys.EXTRA_DATA));
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            onReceivedBroadcast(intent.getAction(), intent);
        }
    };
}
