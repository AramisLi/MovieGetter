package com.aramis.library.component.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * DefaultFrgPagerAdapter 默认的fragment viewpager adapter
 * Created by Aramis on 2017/5/2.
 */

public class DefaultFrgPagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> list;

    public DefaultFrgPagerAdapter(FragmentManager fm, List<Fragment> list) {
        super(fm);
        this.list = list;
    }

    @Override
    public Fragment getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getCount() {
        return list.size();
    }
}
