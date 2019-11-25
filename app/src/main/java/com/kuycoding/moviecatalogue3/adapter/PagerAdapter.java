package com.kuycoding.moviecatalogue3.adapter;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.kuycoding.moviecatalogue3.fragment.FavMoviesFragment;
import com.kuycoding.moviecatalogue3.fragment.FavTVShowFragment;

import org.jetbrains.annotations.NotNull;

public class PagerAdapter extends FragmentStatePagerAdapter {
    public PagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @NotNull
    @Override
    public Fragment getItem(int i) {
        switch (i) {
            case 0:
                return new FavMoviesFragment(true);
            case 1:
                return new FavTVShowFragment(true);
            default:
                return null;
        }
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0) {
            return "Movies";
        }
        return "TV Show";
    }

    @Override
    public int getCount() {
        return 2;
    }
}
