package aronb.energyclicker;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import aronb.energyclicker.Tab1;
import aronb.energyclicker.Tab2;
import aronb.energyclicker.Tab3;
import aronb.energyclicker.Tab4;



public class SolarUpgradePageAdapter extends FragmentStatePagerAdapter {

    int NumberTabs;
    SUTab1 tab1;
    SUTab2 tab2;

    public SolarUpgradePageAdapter(FragmentManager fm, int NumberTabs){
        super(fm);
        this.NumberTabs = NumberTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch(position) {
            case 0:
                tab1 = new SUTab1();
                return tab1;
            case 1:
                tab2 = new SUTab2();
                return tab2;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return NumberTabs;
    }
}
