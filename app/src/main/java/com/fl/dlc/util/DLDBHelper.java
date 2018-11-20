package com.fl.dlc.util;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

public class DLDBHelper extends SQLiteAssetHelper {


    public DLDBHelper(Context context) {
        super(context, DLDBConstants.DATABASE_NAME, null, DLDBConstants.DATABASE_VERSION);
    }

    public Double getResource(int format, double overs_left, int wickets_lost) {

        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        qb.setTables(DLDBConstants.DLResource.TABLE_NAME);

        //System.out.println("format = " + format + " , overs_left = " + overs_left + " , wickets_lost = " + wickets_lost);

        String[] select_columns = {DLDBConstants.DLResource.COLUMN_NAME_RESOURCE_VALUE};
        /*String where_clause = DLDBConstants.DLResource.COLUMN_NAME_FORMAT +
                " = ? AND " +*/
        String where_clause = DLDBConstants.DLResource.COLUMN_NAME_OVERS_LEFT +
                " = ? AND " +
                DLDBConstants.DLResource.COLUMN_NAME_WICKETS_LOST +
                " = ?";
        //String[] where_values = {format + "", overs_left + "", wickets_lost + ""};
        String[] where_values = {overs_left + "", wickets_lost + ""};

        Cursor c = qb.query(db,
                select_columns,
                where_clause,
                where_values,
                null,
                null,
                null);

        c.moveToFirst();
        double resource_value = c.getDouble(c.getColumnIndexOrThrow(DLDBConstants.DLResource.COLUMN_NAME_RESOURCE_VALUE));
        c.close();
        //System.out.println("resource_value = " + resource_value);
        return resource_value;
    }

}
