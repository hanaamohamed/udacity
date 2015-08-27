package activity.com.movietesttwo;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

import fragments.com.finalstestone.FavouriteFragment;
import fragments.com.finalstestone.MainActivityFragment;
import fragments.com.finalstestone.ToWatchListFragment;


public class MainActivity extends ActionBarActivity implements ActionBar.TabListener {

    ViewPager pager ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pager = (ViewPager) findViewById(R.id.pager);
        final ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        ActionBar.Tab tab1 = actionBar.newTab();
        tab1.setText("Home");
        tab1.setTabListener(this);
        actionBar.addTab(tab1);

        ActionBar.Tab tab2 = actionBar.newTab();
        tab2.setText("Favourites");
        tab2.setTabListener(this);
        actionBar.addTab(tab2);

        ActionBar.Tab tab3 = actionBar.newTab();
        tab3.setText("To-Watch");
        tab3.setTabListener(this);
        actionBar.addTab(tab3);

        FragmentAdapter adapter = new FragmentAdapter(getSupportFragmentManager());
        pager.setAdapter(adapter);
        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);


            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    @Override
    public void onTabSelected(ActionBar.Tab tab,FragmentTransaction ft) {
         pager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, android.support.v4.app.FragmentTransaction ft) {

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, android.support.v4.app.FragmentTransaction ft) {

    }

    private class FragmentAdapter extends FragmentPagerAdapter{
        public FragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    return new MainActivityFragment();
                case 1:
                    return new FavouriteFragment();
                case 2:
                    return new ToWatchListFragment();
                default:
                    return null;
            }
           // return  MainActivityFragment.newInstance(position+1);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position){
                case 0:
                    return getString(R.string.home);
                case 1:
                    return getString(R.string.fav);
                case 2:
                    return getString(R.string.to_watch);
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 3;
        }
    }
}
