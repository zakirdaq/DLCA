package com.fl.dlc.util;

import android.provider.BaseColumns;

public class DLDBConstants {

    public static final String DATABASE_NAME = "dl_resource.db";
    public static final int DATABASE_VERSION = 1;

    public static DLDBHelper dbhelper;

    public static abstract class DLResource implements BaseColumns {

        public static final String TABLE_NAME = "dl_resource";
        public static final String COLUMN_NAME_FORMAT = "format";
        public static final String COLUMN_NAME_OVERS_LEFT = "overs_left";
        public static final String COLUMN_NAME_WICKETS_LOST = "wickets_lost";
        public static final String COLUMN_NAME_RESOURCE_VALUE = "resource_value";
    }
}
