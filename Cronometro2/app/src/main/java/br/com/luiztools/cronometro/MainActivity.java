package br.com.luiztools.cronometro;

import android.app.Activity;
import android.content.Context;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        private static TextView section_label;
        private static long initialTime;
        private static Handler handler;
        private static boolean isRunning;
        private static final long MILLIS_IN_SEC = 1000L;
        private static final int SECS_IN_MIN = 60;

        private final static Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (isRunning) {
                    long seconds = (System.currentTimeMillis() - initialTime) / MILLIS_IN_SEC;
                    section_label.setText(String.format("%02d:%02d", seconds / SECS_IN_MIN, seconds % SECS_IN_MIN));
                    handler.postDelayed(runnable, MILLIS_IN_SEC);
                }
            }

        };

        private static HistoryAdapter adapter;
        private void setupHistory(View rootView) {
            RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.history);
            LinearLayoutManager layoutManager = new LinearLayoutManager(rootView.getContext());
            recyclerView.setLayoutManager(layoutManager);

            adapter = new HistoryAdapter(new ArrayList<History>());
            recyclerView.setAdapter(adapter);
        }

        private static void setupStopwatch(View rootView){
            section_label = (TextView)rootView.findViewById(R.id.section_label);

            FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FloatingActionButton fab = (FloatingActionButton)view;
                    if(!isRunning) {
                        isRunning = true;
                        initialTime = System.currentTimeMillis();
                        fab.setImageResource(android.R.drawable.ic_media_pause);
                        handler.postDelayed(runnable, MILLIS_IN_SEC);
                    }else{
                        isRunning = false;
                        fab.setImageResource(android.R.drawable.ic_media_play);
                        handler.removeCallbacks(runnable);

                        //atualiza a recyclerView
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm");
                        adapter.addHistory(new History(section_label.getText().toString(), sdf.format(new Date())));
                        section_label.setText("00:00");
                    }
                }
            });

            handler = new Handler();
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            final int VIEW_CRONOMETRO = 1;
            boolean isViewNew = getArguments().getInt(ARG_SECTION_NUMBER) == VIEW_CRONOMETRO;
            int layoutId = isViewNew ? R.layout.fragment_main : R.layout.fragment_history;
            View rootView = inflater.inflate(layoutId, container, false);

            if(isViewNew)
                setupStopwatch(rootView);
            else
                setupHistory(rootView);

            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 2 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "CRONÔMETRO";
                case 1:
                    return "HISTÓRICO";
            }
            return null;
        }
    }
}
