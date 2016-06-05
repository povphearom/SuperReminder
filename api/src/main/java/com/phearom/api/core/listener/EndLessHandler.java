package com.phearom.api.core.listener;

public abstract class EndLessHandler {
    private int offset = 3;
    private boolean loading = false;

    public void setLoading(boolean loading) {
        this.loading = loading;
    }

    public boolean isLoading() {
        return loading;
    }

    public abstract void onLoad(int pos);

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }
}