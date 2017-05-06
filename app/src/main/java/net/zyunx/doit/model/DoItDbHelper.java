package net.zyunx.doit.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by zyun on 3/5/17.
 */

public class DoItDbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "DOIT.db";

    private static final String SQL_CREATE_THINGS = "CREATE TABLE " + DoItContract.Thing.TABLE_NAME + "("
            + DoItContract.Thing._ID + " INTEGER PRIMARY KEY,"
            + DoItContract.Thing.COLUMN_NAME_TITLE + " TEXT,"
            + DoItContract.Thing.COLUMN_NAME_CONTENT + " TEXT,"
            + DoItContract.Thing.COLUMN_NAME_STATUS + " INTEGER,"
            + DoItContract.Thing.COLUMN_NAME_CREATED_TIMESTAMP + " INTEGER,"
            + DoItContract.Thing.COLUMN_NAME_UPDATED_TIMESTAMP + " INTEGER)";
    private static final String SQL_DELETE_THINGS = "DROP TABLE IF EXISTS " + DoItContract.Thing.TABLE_NAME;


    public DoItDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_THINGS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_THINGS);
        onCreate(db);
    }
}
