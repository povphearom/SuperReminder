package com.phearom.api.core.adapter;

import android.databinding.DataBindingUtil;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.databinding.ViewDataBinding;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.phearom.api.core.binder.ItemBinder;
import com.phearom.api.core.listener.ClickHandler;
import com.phearom.api.core.listener.LongClickHandler;

import java.lang.ref.WeakReference;
import java.util.Collection;


public class BindingBaseListViewAdapter<T> extends BaseAdapter implements View.OnClickListener, View.OnLongClickListener
{
    private static final int ITEM_MODEL = -124;
    private final WeakReferenceOnListChangedCallback onListChangedCallback;
    private final ItemBinder<T> itemBinder;
    private ObservableList<T> items;
    private LayoutInflater inflater;
    private ClickHandler<T> clickHandler;
    private LongClickHandler<T> longClickHandler;
    private int lastPosition = -1;

    public BindingBaseListViewAdapter(ItemBinder<T> itemBinder, @Nullable Collection<T> items)
    {
        this.itemBinder = itemBinder;
        this.onListChangedCallback = new WeakReferenceOnListChangedCallback<>(this);
        setItems(items);
    }

    public ObservableList<T> getItems()
    {
        return items;
    }

    public void setItems(@Nullable Collection<T> items)
    {
        if (this.items == items)
        {
            return;
        }

        if (this.items != null)
        {
            this.items.removeOnListChangedCallback(onListChangedCallback);
            notifyDataSetChanged();
        }

        if (items instanceof ObservableList)
        {
            this.items = (ObservableList<T>) items;
            notifyDataSetChanged();
            this.items.addOnListChangedCallback(onListChangedCallback);
        }
        else if (items != null)
        {
            this.items = new ObservableArrayList<>();
            this.items.addOnListChangedCallback(onListChangedCallback);
            this.items.addAll(items);
        }
        else
        {
            this.items = null;
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (inflater == null)
        {
            inflater = LayoutInflater.from(parent.getContext());
        }

        final T item = items.get(position);
        ViewDataBinding binding = DataBindingUtil.inflate(inflater, itemBinder.getLayoutRes(item), parent, false);
        binding.setVariable(itemBinder.getBindingVariable(item), item);
        binding.getRoot().setTag(ITEM_MODEL, item);
        binding.getRoot().setOnClickListener(this);
        binding.getRoot().setOnLongClickListener(this);
        binding.executePendingBindings();

        return binding.getRoot();
    }

    @Override
    public int getItemViewType(int position)
    {
        return itemBinder.getLayoutRes(items.get(position));
    }

    @Override
    public int getCount()
    {
        return items == null ? 0 : items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public void onClick(View v)
    {
        if (clickHandler != null)
        {
            T item = (T) v.getTag(ITEM_MODEL);
            clickHandler.onClick(item,v);
        }
    }

    @Override
    public boolean onLongClick(View v)
    {
        if (longClickHandler != null)
        {
            T item = (T) v.getTag(ITEM_MODEL);
            longClickHandler.onLongClick(item,v);
            return true;
        }
        return false;
    }

    public void setClickHandler(ClickHandler<T> clickHandler)
    {
        this.clickHandler = clickHandler;
    }

    public void setLongClickHandler(LongClickHandler<T> clickHandler)
    {
        this.longClickHandler = clickHandler;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        final ViewDataBinding binding;

        ViewHolder(ViewDataBinding binding)
        {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    private static class WeakReferenceOnListChangedCallback<T> extends ObservableList.OnListChangedCallback
    {

        private final WeakReference<BindingBaseListViewAdapter<T>> adapterReference;

        public WeakReferenceOnListChangedCallback(BindingBaseListViewAdapter<T> bindingRecyclerViewAdapter)
        {
            this.adapterReference = new WeakReference<>(bindingRecyclerViewAdapter);
        }

        @Override
        public void onChanged(ObservableList sender)
        {
            BaseAdapter adapter = adapterReference.get();
            if (adapter != null)
            {
                adapter.notifyDataSetChanged();
            }
        }

        @Override
        public void onItemRangeChanged(ObservableList sender, int positionStart, int itemCount)
        {
            BaseAdapter adapter = adapterReference.get();
            if (adapter != null)
            {
                adapter.notifyDataSetChanged();
            }
        }

        @Override
        public void onItemRangeInserted(ObservableList sender, int positionStart, int itemCount)
        {
            BaseAdapter adapter = adapterReference.get();
            if (adapter != null)
            {
                adapter.notifyDataSetChanged();
            }
        }

        @Override
        public void onItemRangeMoved(ObservableList sender, int fromPosition, int toPosition, int itemCount)
        {
            BaseAdapter adapter = adapterReference.get();
            if (adapter != null)
            {
                adapter.notifyDataSetChanged();
            }
        }

        @Override
        public void onItemRangeRemoved(ObservableList sender, int positionStart, int itemCount)
        {
            BaseAdapter adapter = adapterReference.get();
            if (adapter != null)
            {
                adapter.notifyDataSetChanged();
            }
        }
    }
}