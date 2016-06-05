package com.phearom.api.core.listener;

/**
 * Created by phearom on 5/19/16.
 */
public interface SwitchChangeHandler<T> {
    void onSwitchChanged(boolean off, T object);
}
