package com.phearom.api.core.binding;

import android.databinding.BindingAdapter;
import android.support.v7.widget.RecyclerView;

import com.phearom.api.core.adapter.BindingRecyclerViewAdapter;
import com.phearom.api.core.binder.ItemBinder;
import com.phearom.api.core.listener.BindViewHandler;
import com.phearom.api.core.listener.ClickHandler;
import com.phearom.api.core.listener.EndLessHandler;
import com.phearom.api.core.listener.LongClickHandler;
import com.phearom.api.core.listener.TopLessHandler;
import com.phearom.api.utils.DividerItemDecoration;
import com.phearom.api.utils.SpacesItemDecoration;

import java.util.Collection;

/**
 *
 */
public class RecyclerViewBindings {
    private static final int KEY_ITEMS = -123;
    private static final int KEY_CLICK_HANDLER = -124;
    private static final int KEY_LONG_CLICK_HANDLER = -125;
    private static final int KEY_BIND_VIEW_HANDLER = -126;
    private static final int KEY_END_LESS_HANDLER = -127;
    private static final int KEY_TOP_LESS_HANDLER = -128;

    @SuppressWarnings("unchecked")
    @BindingAdapter("items")
    public static <T> void setItems(RecyclerView recyclerView, Collection<T> items) {
        BindingRecyclerViewAdapter<T> adapter = (BindingRecyclerViewAdapter<T>) recyclerView.getAdapter();
        if (adapter != null) {
            adapter.setItems(items);
        } else {
            recyclerView.setTag(KEY_ITEMS, items);
        }
    }

    @SuppressWarnings("unchecked")
    @BindingAdapter("clickHandler")
    public static <T> void setHandler(RecyclerView recyclerView, ClickHandler<T> handler) {
        BindingRecyclerViewAdapter<T> adapter = (BindingRecyclerViewAdapter<T>) recyclerView.getAdapter();
        if (adapter != null) {
            adapter.setClickHandler(handler);
        } else {
            recyclerView.setTag(KEY_CLICK_HANDLER, handler);
        }
    }

    @SuppressWarnings("unchecked")
    @BindingAdapter("endLessHandler")
    public static <T> void setHandler(RecyclerView recyclerView, EndLessHandler handler) {
        BindingRecyclerViewAdapter<T> adapter = (BindingRecyclerViewAdapter<T>) recyclerView.getAdapter();
        if (adapter != null) {
            adapter.setEndLessHandler(handler);
        } else {
            recyclerView.setTag(KEY_END_LESS_HANDLER, handler);
        }
    }

    @SuppressWarnings("unchecked")
    @BindingAdapter("topLessHandler")
    public static <T> void setHandler(RecyclerView recyclerView, TopLessHandler handler) {
        BindingRecyclerViewAdapter<T> adapter = (BindingRecyclerViewAdapter<T>) recyclerView.getAdapter();
        if (adapter != null) {
            adapter.setTopLessHandler(handler);
        } else {
            recyclerView.setTag(KEY_TOP_LESS_HANDLER, handler);
        }
    }

    @SuppressWarnings("unchecked")
    @BindingAdapter("longClickHandler")
    public static <T> void setHandler(RecyclerView recyclerView, LongClickHandler<T> handler) {
        BindingRecyclerViewAdapter<T> adapter = (BindingRecyclerViewAdapter<T>) recyclerView.getAdapter();
        if (adapter != null) {
            adapter.setLongClickHandler(handler);
        } else {
            recyclerView.setTag(KEY_LONG_CLICK_HANDLER, handler);
        }
    }

    @SuppressWarnings("unchecked")
    @BindingAdapter("bindViewHandler")
    public static <T> void setHandler(RecyclerView recyclerView, BindViewHandler<T> handler) {
        BindingRecyclerViewAdapter<T> adapter = (BindingRecyclerViewAdapter<T>) recyclerView.getAdapter();
        if (adapter != null) {
            adapter.setBindViewHandler(handler);
        } else {
            recyclerView.setTag(KEY_BIND_VIEW_HANDLER, handler);
        }
    }

    @SuppressWarnings("unchecked")
    @BindingAdapter(value = {"hasFixedSize", "nestedScroll"}, requireAll = true)
    public static void setOption(RecyclerView recyclerView, boolean hasFixed, boolean nested) {
        recyclerView.setHasFixedSize(hasFixed);
        recyclerView.setNestedScrollingEnabled(nested);
    }

    @SuppressWarnings("unchecked")
    @BindingAdapter("itemViewBinder")
    public static <T> void setItemViewBinder(RecyclerView recyclerView, ItemBinder<T> itemViewMapper) {
        try {
            Collection<T> items = (Collection<T>) recyclerView.getTag(KEY_ITEMS);
            ClickHandler<T> clickHandler = (ClickHandler<T>) recyclerView.getTag(KEY_CLICK_HANDLER);
            BindViewHandler<T> bindHandler = (BindViewHandler<T>) recyclerView.getTag(KEY_BIND_VIEW_HANDLER);
            BindingRecyclerViewAdapter<T> adapter = new BindingRecyclerViewAdapter<>(itemViewMapper, items);
            if (clickHandler != null) {
                adapter.setClickHandler(clickHandler);
            }
            if (bindHandler != null) {
                adapter.setBindViewHandler(bindHandler);
            }
            recyclerView.setAdapter(adapter);
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
    }

    @BindingAdapter("itemSpace")
    public static void itemDecoration(RecyclerView recyclerView, int space) {
        RecyclerView.ItemDecoration itemDecoration;
        try {
            itemDecoration = new SpacesItemDecoration(recyclerView.getContext().getResources().getDimensionPixelOffset(space));
            recyclerView.addItemDecoration(itemDecoration);
        } catch (Exception e) {
            itemDecoration = new SpacesItemDecoration(space);
            recyclerView.addItemDecoration(itemDecoration);
        }
    }

    @BindingAdapter("hasDividerVertical")
    public static void hasDivider(RecyclerView recyclerView, boolean vertical) {
        RecyclerView.ItemDecoration itemDecoration;
        if (vertical) {
            itemDecoration = new
                    DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL_LIST);
        } else {
            itemDecoration = new
                    DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.HORIZONTAL_LIST);
        }
        recyclerView.addItemDecoration(itemDecoration);
    }
}