package net.zyunx.doit;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;
import android.widget.TextView;

import net.zyunx.doit.model.DoItContract;
import net.zyunx.doit.model.DoItDbHelper;
import net.zyunx.doit.model.DoItService;

/**
 * An activity representing a single Thing detail screen. This
 * activity is only used narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link ThingFragment}.
 */
public class ThingDetailActivity extends AppCompatActivity {

    private static final int REQUEST_EDIT = 1;

    private int thingId;
    private int thingStatus;
    private int thingPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thing_detail);

        thingId = getIntent().getIntExtra(ThingDetailFragment.ARG_ITEM_ID, -1);
        thingStatus = getIntent().getIntExtra(ThingDetailFragment.ARG_ITEM_STATUS, -1);
        thingPosition = getIntent().getIntExtra(ThingDetailFragment.ARG_ITEM_POSITION, -1);

        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own detail action", Snackbar.LENGTH_LONG)
                //       .setAction("Action", null).show();
                Intent intent = new Intent(ThingDetailActivity.this, ThingEditActivity.class);
                intent.putExtra(ThingDetailFragment.ARG_ITEM_ID, thingId);
                intent.putExtra(ThingDetailFragment.ARG_ITEM_STATUS, thingStatus);
                intent.putExtra(ThingDetailFragment.ARG_ITEM_POSITION, thingPosition);
                startActivityForResult(intent, REQUEST_EDIT);
            }
        });

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        //
        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putInt(ThingDetailFragment.ARG_ITEM_ID,
                    getIntent().getIntExtra(ThingDetailFragment.ARG_ITEM_ID, -1));
            ThingDetailFragment fragment = new ThingDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.thing_detail_container, fragment)
                    .commit();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQUEST_EDIT:
                ThingDetailFragment fragment  = (ThingDetailFragment) getSupportFragmentManager().getFragments().get(0);
                fragment.update();
                MyThingRecyclerViewAdapter.getAdapter(thingStatus).notifyItemRemoved(thingPosition);
                MyThingRecyclerViewAdapter.getAdapter(thingStatus).notifyItemInserted(0);
                break;
            default:
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            finish();
            //Intent intent = new Intent(this, MainActivity.class);
            //navigateUpTo(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
