package net.zyunx.doit.model;

import android.provider.BaseColumns;

/**
 * Created by zyun on 3/5/17.
 */

public class DoItContract {
    public static class Thing implements BaseColumns {
        public static final String TABLE_NAME = "things";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_CONTENT = "content";
        public static final String COLUMN_NAME_STATUS = "status";
        public static final String COLUMN_NAME_CREATED_TIMESTAMP = "created_timestamp";
        public static final String COLUMN_NAME_UPDATED_TIMESTAMP = "updated_timestamp";


        public static final int STATUS_TODO = 1;
        public static final int STATUS_DOING = 2;
        public static final int STATUS_DONE = 3;
    }
}
