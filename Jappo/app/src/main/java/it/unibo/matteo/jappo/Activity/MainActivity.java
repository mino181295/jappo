package it.unibo.matteo.jappo.Activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import android.widget.ImageButton;

import it.unibo.matteo.jappo.Fragment.CompletedFragment;
import it.unibo.matteo.jappo.Fragment.FavoritesFragment;
import it.unibo.matteo.jappo.Fragment.NewOrderFragment;
import it.unibo.matteo.jappo.Fragment.OrderFragment;
import it.unibo.matteo.jappo.Model.DataModel;
import it.unibo.matteo.jappo.Model.Restaurant;
import it.unibo.matteo.jappo.R;
import it.unibo.matteo.jappo.Utils.MediaHelper;

/**
 * Activity that is showed off when the {@link LoginActivity} has been completed and finished.
 * In this {@link android.app.Activity} is contained all the logic of the application.
 */
public class MainActivity extends AppCompatActivity implements NewOrderFragment.OnNewOrderInteractionListener,
        OrderFragment.OnOrderInteractionListener{

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    private DataModel dataModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dataModel = new DataModel(getApplicationContext());
        dataModel.load();
        /* Setting up the custom Action Bar */
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        /* Setting up the two buttons in the Action Bar */
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

        /* Setting up the three main Pages (Fragment) of the MainActivity */
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        mViewPager.setCurrentItem(1);
    }

    @Override
    protected void onStop() {
        /* Uploading the new Favorites*/
        FavoritesFragment favoritesFragment = (FavoritesFragment) getViewerFragment(0);
        dataModel.getLoggedUser().setFavorites(favoritesFragment.getFavourites());

        new AsyncTask<Object, Object, Void>() {
            @Override
            protected Void doInBackground(Object... voids) {
                dataModel.uploadFavorites();
                return null;
            }
        }.execute();
        super.onStop();
    }

    @Override
    protected void onResume() {
        /* Resuming the context and the business logic */
        super.onResume();
        dataModel.load();
        if (dataModel.hasOpenOrder()){
            Fragment orderFragment = OrderFragment.newInstance(dataModel.getOrder());
            mSectionsPagerAdapter.replaceFragment(1,orderFragment);

            Fragment completedFragment = CompletedFragment.newInstance(dataModel.getOrder());
            mSectionsPagerAdapter.replaceFragment(2, completedFragment);
        }
    }

    @Override
    protected void onPause() {
        /* Saving the business logic in the DataModel object */
        dataModel.save();
        super.onPause();
    }

    /* Override of the 2 Fragment Listeners Interaction Methods */
    @Override
    public void onOrderInteraction() {
        final int scorePoints = dataModel.getOrder().getArrived().size();
        new AsyncTask<Integer, Void, Void>(){
            @Override
            protected Void doInBackground(Integer... ints) {
                dataModel.updateUserHighScore(ints[0]);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                dataModel.closeOrder();

                Fragment newOrdFragment = NewOrderFragment.newInstance(dataModel.getRestaurants());
                mSectionsPagerAdapter.replaceFragment(1, newOrdFragment);

                CompletedFragment completedFragment = CompletedFragment.newInstance();
                mSectionsPagerAdapter.replaceFragment(2, completedFragment);
            }
        }.execute(scorePoints);
    }

    @Override
    public void onNewOrderInteraction(Restaurant r) {
        MediaHelper.createFolder(getApplicationContext());
        dataModel.createOrder(r);

        Fragment orderFragment = OrderFragment.newInstance(dataModel.getOrder());
        mSectionsPagerAdapter.replaceFragment(1,orderFragment);

        Fragment completedFragment = CompletedFragment.newInstance(dataModel.getOrder());
        mSectionsPagerAdapter.replaceFragment(2, completedFragment);
    }

    /* Methods for the fragment manipulation in the Pager */
    public Fragment getViewerFragment(int i){
        return  mSectionsPagerAdapter.getItem(i);
    }

    public void setViewerPage(int i){
        mViewPager.setCurrentItem(i);
    }

    /**
     * {@link FragmentStatePagerAdapter} that manages the three main fragments.
     */
    public class SectionsPagerAdapter extends FragmentStatePagerAdapter {
        Fragment [] mainViewFragments;
        FragmentManager fm;

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
            this.fm = fm;

            mainViewFragments = new Fragment[3];

            mainViewFragments[0] = FavoritesFragment.newInstance(dataModel.getLoggedUser().getFavorites());

            if (dataModel.hasOpenOrder()){
                mainViewFragments[1] = OrderFragment.newInstance(dataModel.getOrder());
            } else {
                mainViewFragments[1] = NewOrderFragment.newInstance(dataModel.getRestaurants());
            }

            if (dataModel.hasOpenOrder()){
                mainViewFragments[2] = CompletedFragment.newInstance(dataModel.getOrder());
            } else {
                mainViewFragments[2] = CompletedFragment.newInstance();
            }
        }

        @Override
        public Fragment getItem(int position) {
            return mainViewFragments[position];
        }

        /* Override that permits to refresh the fragments on change */
        @Override
        public int getItemPosition(Object object) {
            if (object instanceof NewOrderFragment || object instanceof OrderFragment
                    || object instanceof CompletedFragment) {
                return POSITION_NONE;
            } else {
                return POSITION_UNCHANGED;
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

        /**
         * Method tha can replace one fragment inside the Adapter and can refresh the data
         * @param i index of the fragment.
         * @param f new {@link Fragment} that is inserted inside the adapter.
         */
        public void replaceFragment(int i, Fragment f) {
            mainViewFragments[i] = f;
            notifyDataSetChanged();
        }
    }

}
