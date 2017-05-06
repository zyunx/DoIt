package net.zyunx.doit.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zyun on 4/5/17.
 */

public class DoItService {
    public static final String TAG = ".DoItService";

    private DoItDbHelper dbHelper;

    private DoItService(DoItDbHelper dbHelper) {
        this.dbHelper = dbHelper;
    }
    public static DoItService getInstance(DoItDbHelper dbHelper) {
        return new DoItService(dbHelper);
    }


    public List<ThingListItem> getThingListByStatus(Integer status) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {
                DoItContract.Thing._ID,
                DoItContract.Thing.COLUMN_NAME_TITLE,
                DoItContract.Thing.COLUMN_NAME_STATUS,
                DoItContract.Thing.COLUMN_NAME_CREATED_TIMESTAMP,
                DoItContract.Thing.COLUMN_NAME_UPDATED_TIMESTAMP
        };

        String selection = DoItContract.Thing.COLUMN_NAME_STATUS + " = ?";
        String[] selectionArgs = { status.toString() };
        String sortOrder = DoItContract.Thing.COLUMN_NAME_UPDATED_TIMESTAMP + " DESC";

        Cursor cursor = db.query(
                DoItContract.Thing.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder);

        List<ThingListItem> ts = new ArrayList<ThingListItem>(10);
        while (cursor.moveToNext()) {
            ThingListItem e = new ThingListItem();
            e.setId(cursor.getInt(0));
            e.setTitle(cursor.getString(1));
            e.setStatus(cursor.getInt(2));
            e.setCreatedTimestamp(cursor.getLong(3));
            e.setUpdatedTimestamp(cursor.getLong(4));
            ts.add(e);
        }
        cursor.close();
        db.close();
        return ts;
    }
    public Long getToDoCount() {
        Long c = 0L;

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select count(*) from " + DoItContract.Thing.TABLE_NAME + " where status = ?",
                new String[]{ String.valueOf(DoItContract.Thing.STATUS_TODO) });

        cursor.moveToNext();
        c = cursor.getLong(0);
        cursor.close();
        db.close();
        return c;
    }
    public List<ThingListItem> getToDoList() {
        return getThingListByStatus(DoItContract.Thing.STATUS_TODO);
    }

    public List<ThingListItem> getDoingList() {
        return getThingListByStatus(DoItContract.Thing.STATUS_DOING);
    }
    public List<ThingListItem> getDoneList() {
        return getThingListByStatus(DoItContract.Thing.STATUS_DONE);
    }

    public void doIt(int id) {
        setStatus(id, DoItContract.Thing.STATUS_DOING);
    }
    public void doneIt(int id) {
        setStatus(id, DoItContract.Thing.STATUS_DONE);
    }

    private void setStatus(int id, int status) {
        long now = System.currentTimeMillis();

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = null;

        ContentValues thingValues = new ContentValues();
        thingValues.put(DoItContract.Thing.COLUMN_NAME_STATUS, status);
        thingValues.put(DoItContract.Thing.COLUMN_NAME_UPDATED_TIMESTAMP, now);

        db.update(DoItContract.Thing.TABLE_NAME, thingValues, DoItContract.Thing._ID + "=?", new String[]{String.valueOf(id)});
        Log.d(TAG, "set status id: " + id + " status: " + status);
        db.close();
    }

    private String titleOfContent(String content) {
        int titleEnd = content.indexOf('\r');
        titleEnd = titleEnd >= 0 ? titleEnd : content.indexOf('\n');
        return titleEnd >= 0 ? content.substring(0, titleEnd) : content;
    }
    public void updateThing(long id, String content) {
        long now = System.currentTimeMillis();

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = null;

        ContentValues thingValues = new ContentValues();
        thingValues.put(DoItContract.Thing.COLUMN_NAME_TITLE, titleOfContent(content));
        thingValues.put(DoItContract.Thing.COLUMN_NAME_CONTENT, content);
        thingValues.put(DoItContract.Thing.COLUMN_NAME_UPDATED_TIMESTAMP, now);

        db.update(DoItContract.Thing.TABLE_NAME, thingValues, DoItContract.Thing._ID + "=?", new String[]{String.valueOf(id)});
        Log.d(TAG, "update thing id:" + id);
        db.close();
    }

    public Long addThing(String content) {
        String title = titleOfContent(content);
        long now = System.currentTimeMillis();

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = null;

        ContentValues eventValues = new ContentValues();
        eventValues.put(DoItContract.Thing.COLUMN_NAME_TITLE, title);
        eventValues.put(DoItContract.Thing.COLUMN_NAME_CONTENT, content);
        eventValues.put(DoItContract.Thing.COLUMN_NAME_STATUS, DoItContract.Thing.STATUS_TODO);
        eventValues.put(DoItContract.Thing.COLUMN_NAME_CREATED_TIMESTAMP, now);
        eventValues.put(DoItContract.Thing.COLUMN_NAME_UPDATED_TIMESTAMP, now);

        Long thingId = db.insert(DoItContract.Thing.TABLE_NAME, null, eventValues);
        Log.d(TAG, "add Thing id: " + thingId);
        db.close();
        return thingId;
    }

    public Thing getThingById(int id) {
        Thing thing = null;

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {
                DoItContract.Thing._ID,
                DoItContract.Thing.COLUMN_NAME_TITLE,
                DoItContract.Thing.COLUMN_NAME_CONTENT,
                DoItContract.Thing.COLUMN_NAME_STATUS,
                DoItContract.Thing.COLUMN_NAME_CREATED_TIMESTAMP,
                DoItContract.Thing.COLUMN_NAME_UPDATED_TIMESTAMP
        };

        String selection = DoItContract.Thing._ID + " = ?";
        String[] selectionArgs = { String.valueOf(id) };

        Cursor cursor = db.query(
                DoItContract.Thing.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null);
        if (cursor.moveToNext()) {
            thing = new Thing();
            thing.setId(cursor.getInt(0));
            thing.setTitle(cursor.getString(1));
            thing.setContent(cursor.getString(2));
            thing.setStatus(cursor.getInt(3));
            thing.setCreatedTimestamp(cursor.getLong(4));
            thing.setUpdatedTimestamp(cursor.getLong(5));
        }
        db.close();

        return thing;
    }
}
