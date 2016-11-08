package erica.beakon;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by Cecelia on 11/6/16.
 */

public class ViewPagerAdapter extends FragmentPagerAdapter {
    static final int NUM_PAGES = 2;
    ArrayList<Fragment> pages;

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
        pages = new ArrayList<Fragment>();
        pages.add(new MyMovementsTab());
        pages.add(new AddMovementPage());
    }

    @Override
    public int getCount() {
        return NUM_PAGES;
    }

    @Override
    public Fragment getItem(int position) {
        if (position < getCount()) {
            return pages.get(position);
        }
        throw new IndexOutOfBoundsException("There is no such fragment at index " + String.valueOf(position) + ".");
    }
}
