package com.aramis.library.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * NoTouchViewPager
 * Created by Aramis on 2017/5/16.
 */
public class NoTouchViewPager extends ViewPager {

    private boolean isScrollable = false;

    public NoTouchViewPager(Context context) {
        super(context);
    }

    public NoTouchViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    public boolean isScrollable() {
        return isScrollable;
    }

    public void setIsScrollable(boolean isScrollable) {
        this.isScrollable = isScrollable;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return isScrollable && super.onTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return isScrollable && super.onInterceptTouchEvent(ev);
    }
}
