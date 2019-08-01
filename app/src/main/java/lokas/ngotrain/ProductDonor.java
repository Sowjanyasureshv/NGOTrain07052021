package lokas.ngotrain;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lokas.ngotrain.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProductDonor extends Fragment implements TabLayout.OnTabSelectedListener{


    //This is our tablayout
    private TabLayout tabLayoutd;

    //This is our viewPager
    private ViewPager viewPagerd;

    public ProductDonor() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View tabdon = inflater.inflate(R.layout.fragment_product_donor, container, false);
        (getActivity()).setTitle("Your products");

        //Initializing the tablayout
        tabLayoutd = (TabLayout) tabdon.findViewById(R.id.tabLayoutd);

        //Adding the tabs using addTab() method
        tabLayoutd.addTab(tabLayoutd.newTab().setText("Open Products"));
        tabLayoutd.addTab(tabLayoutd.newTab().setText("Closed Products"));
        //tabLayout.addTab(tabLayout.newTab().setText("Selected Bids"));
        tabLayoutd.setTabGravity(TabLayout.GRAVITY_FILL);

        //Initializing viewPager
        viewPagerd = (ViewPager) tabdon.findViewById(R.id.pagerd);

        //Creating our pager adapter
        PagerDonor adapterd = new PagerDonor(getActivity().getSupportFragmentManager(), tabLayoutd.getTabCount());

        //Adding adapter to pager
        viewPagerd.setAdapter(adapterd);

        //Adding onTabSelectedListener to swipe views
        tabLayoutd.setOnTabSelectedListener(this);
        // Inflate the layout for this fragment
        viewPagerd.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayoutd));
        return tabdon;
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        viewPagerd.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}
