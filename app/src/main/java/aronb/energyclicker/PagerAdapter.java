package aronb.energyclicker;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import aronb.energyclicker.Tab1;
import aronb.energyclicker.Tab2;
import aronb.energyclicker.Tab3;
import aronb.energyclicker.Tab4;



public class PagerAdapter extends FragmentStatePagerAdapter {

    int NumberTabs;
    Tab1 tab1;
    Tab2 tab2;
    Tab3 tab3;
    Tab4 tab4;

    public PagerAdapter(FragmentManager fm, int NumberTabs){
        super(fm);
        this.NumberTabs = NumberTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch(position) {
            case 0:
                tab1 = new Tab1();
                return tab1;
            case 1:
                tab2 = new Tab2();
                return tab2;
            case 2:
                tab3 = new Tab3();
                return tab3;
            case 3:
                tab4 = new Tab4();
                return tab4;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return NumberTabs;
    }
}
