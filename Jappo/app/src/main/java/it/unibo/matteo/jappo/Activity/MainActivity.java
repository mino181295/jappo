package it.unibo.matteo.jappo.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.View;

import android.widget.ImageButton;

import it.unibo.matteo.jappo.Fragment.CompletedFragment;
import it.unibo.matteo.jappo.Fragment.FavoritesFragment;
import it.unibo.matteo.jappo.Fragment.NewOrderFragment;
import it.unibo.matteo.jappo.Fragment.OrderFragment;
import it.unibo.matteo.jappo.Model.DataModel;
import it.unibo.matteo.jappo.Model.Restaurant;
import it.unibo.matteo.jappo.R;

public class MainActivity extends AppCompatActivity implements NewOrderFragment.OnNewOrderInteractionListener,
        OrderFragment.OnOrderInteractionListener{

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private DataModel dm;

    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dm = new DataModel(getApplicationContext());
        dm.load();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        ImageButton settingsToolbarButton = (ImageButton) findViewById(R.id.settings_button);
        settingsToolbarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, SettingsActivity.class));
            }
        });

        ImageButton highscoresToolbarButton = (ImageButton) findViewById(R.id.trophy_button);
        highscoresToolbarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, HighscoresActivity.class));
            }
        });

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        mViewPager.setCurrentItem(1);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    protected void onStop() {
        new AsyncTask<Object, Object, Void>() {
            @Override
            protected Void doInBackground(Object... voids) {
                dm.uploadFavorites();
                dm.save();
                return null;
            }
        }.execute();
        super.onStop();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onNewOrderInteraction(Restaurant r) {
        dm.createOrder(r);
        dm.save();

        Fragment orderFragment = OrderFragment.newInstance(dm.getOrder());
        mSectionsPagerAdapter.replaceFragment(1,orderFragment);

        Fragment completedFragment = CompletedFragment.newInstance(dm.getOrder());
        mSectionsPagerAdapter.replaceFragment(2, completedFragment);
    }

    @Override
    public void onOrderInteraction() {
        dm.closeOrder();
        dm.save();

        Fragment newOrdFragment = NewOrderFragment.newInstance(dm.getRestaurants());
        mSectionsPagerAdapter.replaceFragment(1, newOrdFragment);

        CompletedFragment completedFragment = CompletedFragment.newInstance();
        mSectionsPagerAdapter.replaceFragment(2, completedFragment);
    }

    public void setViewerPage(int i){
        mViewPager.setCurrentItem(i);
    }

    public Fragment getViewerFragment(int i){
        return  mSectionsPagerAdapter.getItem(i);
    }

    public class SectionsPagerAdapter extends FragmentStatePagerAdapter {

        Fragment [] mainViewFragments;
        FragmentManager fm;

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
            this.fm = fm;

            mainViewFragments = new Fragment[3];

            mainViewFragments[0] = FavoritesFragment.newInstance(dm.getLoggedUser().getFavorites());

            if (dm.hasOpenOrder()){
                mainViewFragments[1] = OrderFragment.newInstance(dm.getOrder());
            } else {
                mainViewFragments[1] = NewOrderFragment.newInstance(dm.getRestaurants());
            }

            if (dm.hasOpenOrder()){
                mainViewFragments[2] = CompletedFragment.newInstance(dm.getOrder());
            } else {
                mainViewFragments[2] = CompletedFragment.newInstance();
            }
        }

        @Override
        public Fragment getItem(int position) {
            return mainViewFragments[position];
        }

        @Override
        public int getItemPosition(Object object) {
            // this method will be called for every fragment in the ViewPager
            if (object instanceof NewOrderFragment || object instanceof OrderFragment
                    || object instanceof CompletedFragment) {
                return POSITION_NONE;
            } else {
                // POSITION_NONE means something like: this fragment is no longer valid
                // triggering the ViewPager to re-build the instance of this fragment.
                return POSITION_UNCHANGED; // don't force a reload
            }
        }

        @Override
        public int getCount() {
            return mainViewFragments.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position){
                case 0: return "Preferiti";
                case 1: return "Ordinati";
                case 2: return "Arrivati";
            }
            return null;
        }

        public void replaceFragment(int i, Fragment f) {
            mainViewFragments[i] = f;
            notifyDataSetChanged();
        }
    }
}
