package net.zyunx.doit;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import net.zyunx.doit.model.DoItContract;
import net.zyunx.doit.model.DoItDbHelper;
import net.zyunx.doit.model.DoItService;

public class PlanThingActivity extends AppCompatActivity {

    private EditText contentText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_thing);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        this.contentText = (EditText) findViewById(R.id.contentText);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_plan_thing, menu);
        return true;
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
            //navigateUpToFromChild(this, new Intent(this, MainActivity.class));
            String content = contentText.getText().toString();
            if (null != content && !"".equals(content)) {
                Log.d("Plan", content);
                DoItService.getInstance(new DoItDbHelper(this)).addThing(content);
                MyThingRecyclerViewAdapter.getAdapter(DoItContract.Thing.STATUS_TODO).notifyItemInserted(0);
            }
            navigateUpTo(new Intent(this, MainActivity.class));
            return true;
        }
        if (id == R.id.action_delete) {
            navigateUpToFromChild(this, new Intent(this, MainActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
