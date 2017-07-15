package it.unibo.matteo.jappo.Activity;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

import it.unibo.matteo.jappo.Fragment.CompletedFragment;
import it.unibo.matteo.jappo.Fragment.FavoritesFragment;
import it.unibo.matteo.jappo.Fragment.NewOrderFragment;
import it.unibo.matteo.jappo.R;

public class MainActivity extends AppCompatActivity {


    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        Fragment [] mainViewFragments;

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);

            mainViewFragments = new Fragment[3];
            mainViewFragments[0] = FavoritesFragment.newInstance(new ArrayList<String>());
            mainViewFragments[1] = NewOrderFragment.newInstance(new ArrayList<String>());
            mainViewFragments[2] = CompletedFragment.newInstance();
        }

        @Override
        public Fragment getItem(int position) {
            return mainViewFragments[position];
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
    }
}
