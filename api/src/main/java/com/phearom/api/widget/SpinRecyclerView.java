package com.phearom.api.widget;

import android.content.Context;
import android.graphics.PointF;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;

/**
 * Created by phearom on 2/11/16.
 */
public class SpinRecyclerView extends RecyclerView {
    private static int count = 0;
    private Object selected;
    private boolean enabled;
    private float MILLISECONDS_PER_INCH = 50f;
    private Context mContext;
    private SpinLayoutManager layoutManager;
    private ISpinAdapter mISpinAdapter;
    private boolean stopped;
    private OnScrolledListener mStoppedScroll;

    public SpinRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.enabled = true;
        mContext = context;
        layoutManager = new SpinLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        setLayoutManager(layoutManager);
    }

    public SpinLayoutManager setDuration(float mi_sec_per_inch) {
        this.MILLISECONDS_PER_INCH = mi_sec_per_inch;
        return layoutManager;
    }

    public void setEnableScroll(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (this.enabled) {
            return super.onTouchEvent(event);
        }
        return false;
    }

    @Override
    public void setAdapter(Adapter adapter) {
        super.setAdapter(adapter);
        if (adapter instanceof ISpinAdapter) {
            mISpinAdapter = (ISpinAdapter) adapter;
        }
    }

    public void setSelected(Object o) {
        selected = o;
    }

    public void resetCount() {
        float current = MILLISECONDS_PER_INCH;
        count = 0;
        setDuration(0);
        scrollToPosition(0);
        setDuration(current);
    }

    public int getCount() {
        return count;
    }

    public void startSpin(int size) {
        stopped = false;
        int repeat = 36 / size;
        if (null == mISpinAdapter) return;
        count++;
        mISpinAdapter.setRepeat(repeat * count);
        int pos = mISpinAdapter.getPositionBy(selected);
        smoothScrollToPosition(pos);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (this.enabled) {
            return super.onInterceptTouchEvent(event);
        }
        return false;
    }

    @Override
    public void onScrollStateChanged(int state) {
        super.onScrollStateChanged(state);
        if (state == RecyclerView.SCROLL_STATE_IDLE) {
            if (!stopped) {
                if (null != mStoppedScroll)
                    mStoppedScroll.onStopped();
                stopped = true;
            }
        }
    }

    public boolean isStopped() {
        return stopped;
    }

    public void initCenterView(final View centerIndicator, final int itemWidth) {
        setClipToPadding(false);
        centerIndicator.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int center = (centerIndicator.getLeft() + centerIndicator.getRight()) / 2;
                int padding = center - itemWidth / 2;
                setPadding(padding, 0, padding, 0);
                addOnScrollListener(new CenterLockListener(center));
            }
        });
    }

    public void setOnScrolledListener(OnScrolledListener listener) {
        this.mStoppedScroll = listener;
    }

    //Listener
    public interface OnScrolledListener {
        void onStopped();
    }

    //Listener Of Adapter
    public interface ISpinAdapter {
        int getPositionBy(Object o);

        void setRepeat(int repeat);
    }

    //LayoutManger
    public class SpinLayoutManager extends LinearLayoutManager {
        private boolean shouldGoRight = false;

        public SpinLayoutManager(Context context) {
            super(context);
        }

        public void setShouldGoRight(boolean shouldGoRight) {
            this.shouldGoRight = shouldGoRight;
            layoutManager.setReverseLayout(shouldGoRight);
        }

        @Override
        public void smoothScrollToPosition(RecyclerView recyclerView,
                                           State state, final int position) {

            SmoothScroll smoothScroller = new SmoothScroll(mContext);
            smoothScroller.setTargetPosition(position);
            startSmoothScroll(smoothScroller);
        }


        private class SmoothScroll extends LinearSmoothScroller {
            public SmoothScroll(Context context) {
                super(context);
            }

            @Override
            public PointF computeScrollVectorForPosition(int targetPosition) {
                if (getChildCount() == 0) {
                    return null;
                }
                final int firstChildPos = getPosition(getChildAt(0));
                final int direction = targetPosition < firstChildPos != shouldGoRight ? -1 : 1;
                return new PointF(direction, 0);
            }

            @Override
            protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
                return MILLISECONDS_PER_INCH / displayMetrics.densityDpi;
            }
        }
    }

    //CenterLock Scroll
    public class CenterLockListener extends OnScrollListener {
        //To avoid recursive calls
        private boolean mAutoSet = true;
        //The pivot to be snapped to
        private int mCenterPivot;

        public CenterLockListener(int center) {
            this.mCenterPivot = center;
        }

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            LinearLayoutManager lm = (LinearLayoutManager) recyclerView.getLayoutManager();
            if (mCenterPivot == 0) {
                mCenterPivot = lm.getOrientation() == LinearLayoutManager.HORIZONTAL ? (recyclerView.getLeft() + recyclerView.getRight()) : (recyclerView.getTop() + recyclerView.getBottom());
            }
            if (!mAutoSet) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    //ScrollStoppped
                    View view = findCenterView(lm);//get the view nearest to center
                    int viewCenter = lm.getOrientation() == LinearLayoutManager.HORIZONTAL ? (view.getLeft() + view.getRight()) / 2 : (view.getTop() + view.getBottom()) / 2;
                    //compute scroll from center
                    int scrollNeeded = viewCenter - mCenterPivot; // Add or subtract any offsets you need here
                    if (lm.getOrientation() == LinearLayoutManager.HORIZONTAL) {
                        recyclerView.smoothScrollBy(scrollNeeded, 0);
                    } else {
                        recyclerView.smoothScrollBy(0, (int) (scrollNeeded));
                    }
                    mAutoSet = true;
                }
            }
            if (newState == RecyclerView.SCROLL_STATE_DRAGGING || newState == RecyclerView.SCROLL_STATE_SETTLING) {
                mAutoSet = false;
            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
        }

        private View findCenterView(LinearLayoutManager lm) {
            int minDistance = 0;
            View view = null;
            View returnView = null;
            boolean notFound = true;
            for (int i = lm.findFirstVisibleItemPosition(); i <= lm.findLastVisibleItemPosition() && notFound; i++) {
                view = lm.findViewByPosition(i);
                int center = lm.getOrientation() == LinearLayoutManager.HORIZONTAL ? (view.getLeft() + view.getRight()) / 2 : (view.getTop() + view.getBottom()) / 2;
                int leastDifference = Math.abs(mCenterPivot - center);
                if (leastDifference <= minDistance || i == lm.findFirstVisibleItemPosition()) {
                    minDistance = leastDifference;
                    returnView = view;
                } else {
                    notFound = false;
                }
            }
            return returnView;
        }
    }
}
