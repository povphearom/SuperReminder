package com.phearom.api.core.listener;

public interface BindViewHandler<T> {
    void onBindView(T viewModel, int pos);
}