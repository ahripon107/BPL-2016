package com.allgames.sportslab.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ripon
 */
public class MatchDetailsViewPagerAdapter extends FragmentStatePagerAdapter {
    private final List<Fragment> fragmentList;
    private final List<String> titleList;

    public MatchDetailsViewPagerAdapter(FragmentManager fm) {
        super(fm);
        this.fragmentList = new ArrayList<>();
        this.titleList = new ArrayList<>();
    }

    public int getCount() {
        return this.fragmentList.size();
    }

    public Fragment getItem(int position) {
        return this.fragmentList.get(position);
    }

    public CharSequence getPageTitle(int position) {
        return this.titleList.get(position);
    }

    public final void addFragment(Fragment fragment, String title) {
        this.fragmentList.add(fragment);
        this.titleList.add(title);
    }

    public final void removeFragment(Fragment fragment, String title) {
        this.fragmentList.remove(fragment);
        this.titleList.remove(title);
    }

}