package com.phearom.api.core.binding;

import android.databinding.BindingAdapter;
import android.widget.ListView;

import com.phearom.api.core.adapter.BindingBaseListViewAdapter;
import com.phearom.api.core.binder.ItemBinder;
import com.phearom.api.core.listener.ClickHandler;
import com.phearom.api.core.listener.LongClickHandler;

import java.util.Collection;

public class ListViewBindings
{
    private static final int KEY_ITEMS = -123;
    private static final int KEY_CLICK_HANDLER = -124;
    private static final int KEY_LONG_CLICK_HANDLER = -125;

    @SuppressWarnings("unchecked")
    @BindingAdapter("items")
    public static <T> void setItems(ListView listView, Collection<T> items)
    {
        BindingBaseListViewAdapter<T> adapter = (BindingBaseListViewAdapter<T>) listView.getAdapter();
        if (adapter != null)
        {
            adapter.setItems(items);
        }
        else
        {
            listView.setTag(KEY_ITEMS, items);
        }
    }

    @SuppressWarnings("unchecked")
    @BindingAdapter("clickHandler")
    public static <T> void setHandler(ListView listView, ClickHandler<T> handler)
    {
        BindingBaseListViewAdapter<T> adapter = (BindingBaseListViewAdapter<T>) listView.getAdapter();
        if (adapter != null)
        {
            adapter.setClickHandler(handler);
        }
        else
        {
            listView.setTag(KEY_CLICK_HANDLER, handler);
        }
    }

    @SuppressWarnings("unchecked")
    @BindingAdapter("longClickHandler")
    public static <T> void setHandler(ListView listView, LongClickHandler<T> handler)
    {
        BindingBaseListViewAdapter<T> adapter = (BindingBaseListViewAdapter<T>) listView.getAdapter();
        if (adapter != null)
        {
            adapter.setLongClickHandler(handler);
        }
        else
        {
            listView.setTag(KEY_LONG_CLICK_HANDLER, handler);
        }
    }

    @SuppressWarnings("unchecked")
    @BindingAdapter("itemViewBinder")
    public static <T> void setItemViewBinder(ListView listView, ItemBinder<T> itemViewMapper)
    {
        Collection<T> items = (Collection<T>) listView.getTag(KEY_ITEMS);
        ClickHandler<T> clickHandler = (ClickHandler<T>) listView.getTag(KEY_CLICK_HANDLER);
        BindingBaseListViewAdapter<T> adapter = new BindingBaseListViewAdapter<>(itemViewMapper, items);
        if(clickHandler != null)
        {
            adapter.setClickHandler(clickHandler);
        }
        listView.setAdapter(adapter);
    }
}