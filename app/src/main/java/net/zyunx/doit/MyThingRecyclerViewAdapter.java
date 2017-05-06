package net.zyunx.doit;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.zyunx.doit.ThingFragment.OnListFragmentInteractionListener;
import net.zyunx.doit.model.DoItDbHelper;
import net.zyunx.doit.model.DoItService;
import net.zyunx.doit.model.ThingListItem;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyThingRecyclerViewAdapter extends RecyclerView.Adapter<MyThingRecyclerViewAdapter.ViewHolder> {

    private final OnListFragmentInteractionListener mListener;

    private DoItDbHelper dbHelper;
    private int thingStatus;

    private final static Map<Integer, MyThingRecyclerViewAdapter> adapters = new HashMap<Integer, MyThingRecyclerViewAdapter>(3);
    public static MyThingRecyclerViewAdapter getAdapter(int status) {
        return adapters.get(status);
    }

    public MyThingRecyclerViewAdapter(DoItDbHelper dbHelper,
                                      int status,
                                      OnListFragmentInteractionListener listener) {
        this.thingStatus = status;
        this.dbHelper = dbHelper;
        mListener = listener;

        adapters.put(status, this);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_thing, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        List<ThingListItem> values = DoItService.getInstance(dbHelper).getThingListByStatus(thingStatus);
        holder.mItem = values.get(position);
        Integer id = values.get(position).getId();
        String title = values.get(position).getTitle();
        Log.d("bind", String.format("id:%d, title:%s", id, title));
        //holder.mIdView.setText(id.toString());
        holder.mContentView.setText(title);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem, position, ThingFragment.InteractionType.CLICK);
                }
            }
        });
        holder.mView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem, position, ThingFragment.InteractionType.LONG_CLICK);

                    return true;
                }

                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return DoItService.getInstance(dbHelper).getThingListByStatus(thingStatus).size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        //public final TextView mIdView;
        public final TextView mContentView;
        public ThingListItem mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            //mIdView = (TextView) view.findViewById(R.id.id);
            mContentView = (TextView) view.findViewById(R.id.content);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
