package com.phearom.api.core.adapter;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.phearom.api.core.binder.ItemBinder;
import com.phearom.api.core.listener.ClickHandler;
import com.phearom.api.widget.SpinRecyclerView;

import java.util.List;


public class SpinRecyclerAdapter<T> extends RecyclerView.Adapter<SpinRecyclerAdapter.ViewHolder> implements View.OnClickListener, SpinRecyclerView.ISpinAdapter {
    private static final int ITEM_MODEL = -124;
    private int TOTAL_ITEMS;
    private List<T> items;
    private ClickHandler<T> clickHandler;
    private ItemBinder<T> itemBinder;
    private int repeat = 0;

    private int mResultPos = -1;

    public SpinRecyclerAdapter(ItemBinder<T> itemBinder, List<T> items) {
        this.itemBinder = itemBinder;
        this.items = items;
        TOTAL_ITEMS = this.items.size();
    }

    public SpinRecyclerAdapter(ItemBinder<T> itemBinder) {
        this.itemBinder = itemBinder;
    }

    public void setItems(List<T> items) {
        this.items = items;
        TOTAL_ITEMS = this.items.size();
    }

    public T getItem(int pos) {
        return this.items.get(pos);
    }

    public int getPositionBy(Object t) {
        int pos = items.indexOf(t);
        if (pos > -1)
            pos = (repeat * TOTAL_ITEMS) + pos;
        else {
            pos = repeat * TOTAL_ITEMS;
        }
        mResultPos = pos;
        return pos;
    }

    public void setRepeat(int repeat) {
        this.repeat = repeat;
    }

    @Override
    public int getItemViewType(int position) {
        position = position % TOTAL_ITEMS;
        return this.itemBinder.getLayoutRes(items.get(position));
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int layoutId) {
        ViewDataBinding viewDataBinding = DataBindingUtil.inflate(LayoutInflater.from(viewGroup.getContext()), layoutId, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(viewDataBinding);
        return viewHolder;
    }

    public int getRealItemCount() {
        return TOTAL_ITEMS;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        int pos = position;
        position = position % TOTAL_ITEMS;
        T item = this.items.get(position);
        ViewDataBinding viewDataBinding = viewHolder.getBinding();
        viewDataBinding.setVariable(itemBinder.getBindingVariable(item), item);
        viewDataBinding.getRoot().setTag(ITEM_MODEL, item);
        viewDataBinding.getRoot().setOnClickListener(this);
    }

    @Override
    public int getItemCount() {
        return Integer.MAX_VALUE;
    }

    @Override
    public void onClick(View v) {
        if (null != clickHandler) {
            T item = (T) v.getTag(ITEM_MODEL);
            clickHandler.onClick(item, v);
        }
    }

    public void setClickHandler(ClickHandler<T> clickHandler) {
        this.clickHandler = clickHandler;
    }

    public interface OnResultListener {
        void onResult(int pos);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final ViewDataBinding binding;

        ViewHolder(ViewDataBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public ViewDataBinding getBinding() {
            return binding;
        }
    }

    public void onAdapterDestroy() {
        items.clear();
        items = null;
        itemBinder = null;
        clickHandler = null;
    }
}