package lokas.ngotrain;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by Bala on 26-09-2016.
 */
public class PagerDonor extends FragmentStatePagerAdapter {

    //integer to count number of tabs
    int tabCountd;

    //Constructor to the class
    public PagerDonor(FragmentManager fm, int tabCount) {
        super(fm);
        //Initializing tab count
        this.tabCountd= tabCount;
    }

    //Overriding method getItem
    @Override
    public Fragment getItem(int position) {
        //Returning the current tabs
        switch (position) {
            case 0:
                ProductOpenDonor donor1 = new ProductOpenDonor();
                return donor1;
            case 1:
                ProductCloseDonor donor2 = new ProductCloseDonor();
                return donor2;
            /*case 2:
                Tab3 tab3 = new Tab3();
                return tab3;*/
            default:
                return null;
        }
    }

    //Overriden method getCount to get the number of tabs
    @Override
    public int getCount() {
        return tabCountd;
    }
}
