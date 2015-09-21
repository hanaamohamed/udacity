package movieApp.com.activity;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

import activity.com.movietesttwo.movieApp.com.R;
import movieApp.com.classes.Response;
import movieApp.com.fragments.FavouriteFragment;
import movieApp.com.fragments.MainActivityFragment;
import movieApp.com.fragments.DetailFragment;


public class MainActivity extends ActionBarActivity implements ActionBar.TabListener,MainActivityFragment.Callback,FavouriteFragment.CallBackFavourite
{

    ViewPager pager;
    static public Boolean twoPane = false;
    String mSelectedFragments = "home";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (findViewById(R.id.fragmentDetails) != null) {
            if (savedInstanceState == null) {
                DetailFragment detailFragment = new DetailFragment();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentDetails, detailFragment, DetailFragment.DETAILS_TAG).commit();
            }
            twoPane = true;
        }
        pager = (ViewPager) findViewById(R.id.pager);
        final ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        setActionBar(actionBar);
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

    private void setActionBar(ActionBar actionBar) {

        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        ActionBar.Tab tab1 = actionBar.newTab();
        tab1.setText("Home");
        tab1.setTabListener(this);

        actionBar.addTab(tab1);

        ActionBar.Tab tab2 = actionBar.newTab();
        tab2.setText("Favourites");
        tab2.setTabListener(this);
        actionBar.addTab(tab2);
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
        pager.setCurrentItem(tab.getPosition());
       // mSelectedFragments = (String) tab.getText();
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, android.support.v4.app.FragmentTransaction ft) {

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, android.support.v4.app.FragmentTransaction ft) {

    }

    @Override
    public void onItemSelected(Response.ResultsEntity resultsEntity) {
        if (twoPane){
            Bundle args = new Bundle();
            args.putParcelable(DetailFragment.mKeyIntent,resultsEntity);
            DetailFragment detailFragment = new DetailFragment();
            detailFragment.setArguments(args);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentDetails, detailFragment, DetailFragment.DETAILS_TAG).commit();
        }else{
            Intent intent = new Intent(getApplicationContext(), MovieDetails.class);
                        intent.putExtra(DetailFragment.mKeyIntent, resultsEntity);
                        startActivity(intent);
        }
    }

    @Override
    public void getTheFirstItem(Response.ResultsEntity firstMovie) {
        if (twoPane){
            Bundle args = new Bundle();
            args.putParcelable(DetailFragment.mKeyIntent,firstMovie);
            DetailFragment detailFragment = new DetailFragment();
            detailFragment.setArguments(args);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentDetails, detailFragment, DetailFragment.DETAILS_TAG).commit();

        }
    }

    @Override
    public void getFirstFavouriteItem(Response.ResultsEntity firstMovie) {
        if (twoPane){
            Bundle args = new Bundle();
            args.putParcelable(DetailFragment.mKeyIntent,firstMovie);
            DetailFragment detailFragment = new DetailFragment();
            detailFragment.setArguments(args);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentDetails, detailFragment, DetailFragment.DETAILS_TAG).commit();

        }
    }

    @Override
    public void getSelectedFavouriteItem(Response.ResultsEntity selectedMovie) {
        if (twoPane){
            Bundle args = new Bundle();
            args.putParcelable(DetailFragment.mKeyIntent,selectedMovie);
            DetailFragment detailFragment = new DetailFragment();
            detailFragment.setArguments(args);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentDetails, detailFragment, DetailFragment.DETAILS_TAG).commit();
        }else{
            Intent intent = new Intent(getApplicationContext(), MovieDetails.class);
            intent.putExtra(DetailFragment.mKeyIntent, selectedMovie);
            startActivity(intent);
        }
    }

    private class FragmentAdapter extends FragmentPagerAdapter {
        public FragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new MainActivityFragment();
                case 1:
                    return new FavouriteFragment();
                default:
                    return null;
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getString(R.string.home);
                case 1:
                    return getString(R.string.fav);
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }
    }

}
