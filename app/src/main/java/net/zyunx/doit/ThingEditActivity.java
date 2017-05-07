package net.zyunx.doit;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.EditText;

import net.zyunx.doit.model.DoItDbHelper;
import net.zyunx.doit.model.DoItService;

public class ThingEditActivity extends AppCompatActivity {
    private int thingId;
    private int thingStatus;
    private int thingPosition;

    private DoItDbHelper dbHelper;

    private EditText contentText;

    ThingDetailActivity parent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thing_edit);

        thingId = getIntent().getIntExtra(ThingDetailFragment.ARG_ITEM_ID, -1);
        thingStatus = getIntent().getIntExtra(ThingDetailFragment.ARG_ITEM_STATUS, -1);
        thingPosition = getIntent().getIntExtra(ThingDetailFragment.ARG_ITEM_ID, -1);

        dbHelper = new DoItDbHelper(this);

        contentText = (EditText) findViewById(R.id.contentText);
        contentText.setText(DoItService.getInstance(dbHelper).getThingById(thingId).getContent());

        parent = (ThingDetailActivity) getParent();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    private void saveAndFinish() {
        String content = contentText.getText().toString();
        DoItService.getInstance(dbHelper).updateThing(thingId, content);
        setResult(RESULT_OK, null);
        finish();
    }

    @Override
    public void onBackPressed() {
        saveAndFinish();
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
            saveAndFinish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
