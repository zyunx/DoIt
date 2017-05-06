package net.zyunx.doit;

import android.app.Activity;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.zyunx.doit.model.DoItDbHelper;
import net.zyunx.doit.model.DoItService;
import net.zyunx.doit.model.Thing;

/**
 * A fragment representing a single Thing detail screen.
 * in two-pane mode (on tablets) or a {@link ThingDetailActivity}
 * on handsets.
 */
public class ThingDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";
    public static final String ARG_ITEM_STATUS = "item_status";
    public static final String ARG_ITEM_POSITION = "item_position";

    /**
     * The dummy content this fragment is presenting.
     */
    private Thing mItem;

    private DoItDbHelper dbHelper;
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ThingDetailFragment() {
        this.dbHelper = new DoItDbHelper(getContext());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            //mItem = DummyContent.ITEM_MAP.get(getArguments().getString(ARG_ITEM_ID));
            Log.d(ThingDetailFragment.class.getName(), "ITEM_ID: " + getArguments().getInt(ARG_ITEM_ID));
            dbHelper = new DoItDbHelper(getContext());
            mItem = DoItService.getInstance(dbHelper).getThingById(getArguments().getInt(ARG_ITEM_ID));

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(mItem.getTitle());
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.thing_detail, container, false);

        // Show the dummy content as text in a TextView.
        if (mItem != null) {
            ((TextView) rootView.findViewById(R.id.thing_detail)).setText(mItem.getContent());
        }

        return rootView;
    }
}
