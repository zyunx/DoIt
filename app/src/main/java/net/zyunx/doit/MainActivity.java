package net.zyunx.doit;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import net.zyunx.doit.model.DoItContract;
import net.zyunx.doit.model.DoItDbHelper;
import net.zyunx.doit.model.DoItService;
import net.zyunx.doit.model.ThingListItem;

public class MainActivity extends AppCompatActivity
    implements ThingFragment.OnListFragmentInteractionListener {

    public static final String TAG = ".MainActivity";

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

    private DoItDbHelper dbHelper;

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
        mViewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), PlanThingActivity.class);
                startActivity(intent);
            }
        });

        dbHelper = new DoItDbHelper(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_main, menu);
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

    @Override
    public void onListFragmentInteraction(final ThingListItem item, final int position, ThingFragment.InteractionType interactionType) {
        Log.d(TAG, "item " + item.getId() + " interaction " + interactionType.toString());

        switch (interactionType) {
            case CLICK:
                Intent intent = new Intent(this, ThingDetailActivity.class);
                intent.putExtra(ThingDetailFragment.ARG_ITEM_ID, item.getId());
                intent.putExtra(ThingDetailFragment.ARG_ITEM_STATUS, item.getStatus());
                intent.putExtra(ThingDetailFragment.ARG_ITEM_POSITION, position);
                startActivity(intent);
                break;
            case LONG_CLICK:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                switch (item.getStatus()) {
                    case DoItContract.Thing.STATUS_TODO:
                        builder.setTitle("Do it!?");
                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                DoItService.getInstance(dbHelper).doIt(item.getId());
                                MyThingRecyclerViewAdapter.getAdapter(DoItContract.Thing.STATUS_TODO).notifyItemRemoved(position);
                                MyThingRecyclerViewAdapter.getAdapter(DoItContract.Thing.STATUS_DOING).notifyItemInserted(0);

                            }
                        });
                        builder.setNegativeButton("Later on", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        break;
                    case DoItContract.Thing.STATUS_DOING:
                        builder.setTitle("Done!?");
                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                DoItService.getInstance(dbHelper).doneIt(item.getId());
                                MyThingRecyclerViewAdapter.getAdapter(DoItContract.Thing.STATUS_DOING).notifyItemRemoved(position);
                                MyThingRecyclerViewAdapter.getAdapter(DoItContract.Thing.STATUS_DONE).notifyItemInserted(0);
                            }
                        });
                        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        break;
                    case DoItContract.Thing.STATUS_DONE:
                        builder.setTitle("Redo it!?");
                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                DoItService.getInstance(dbHelper).doIt(item.getId());
                                MyThingRecyclerViewAdapter.getAdapter(DoItContract.Thing.STATUS_DONE).notifyItemRemoved(position);
                                MyThingRecyclerViewAdapter.getAdapter(DoItContract.Thing.STATUS_DOING).notifyItemInserted(0);
                            }
                        });
                        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        break;
                    default:
                }


                builder.create().show();
                break;
            default:
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
            //return PlaceholderFragment.newInstance(position + 1);
            return ThingFragment.newInstance(DoItContract.Thing.STATUS_TODO + position);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "To do";
                case 1:
                    return "Doing";
                case 2:
                    return "Done";
            }
            return null;
        }
    }
}
