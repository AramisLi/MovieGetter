package com.aramis.library.component.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * DefaultViewPagerAdapter 默认的view viewpager adapter
 * Created by Aramis on 2017/5/2.
 */

public class DefaultViewPagerAdapter extends PagerAdapter {
    private List<View> list;

    public DefaultViewPagerAdapter(List<View> list) {
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(list.get(position));
        return list.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(list.get(position));
    }
}
