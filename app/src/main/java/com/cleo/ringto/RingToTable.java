package com.cleo.ringto;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class RingToTable
{
    // Database table
    public static final String TABLE_RINGTO = "ringto_messages";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_SERVER_ID = "server_id";
    public static final String COLUMN_FROM = "from_number";
    public static final String COLUMN_TO = "to_number";
    public static final String COLUMN_DIRECTION = "direction";
    public static final String COLUMN_DATE = "real_date";
//    public static final String COLUMN_DATE_RAW = "date_raw";
    public static final String COLUMN_TEXT = "message";
//    public static final String COLUMN_INCOMING = "incoming";
    public static final String COLUMN_STATUS = "status";
    public static final String COLUMN_READ = "read";
    public static final String COLUMN_LEFT = "alignment";

    // Database creation SQL statement
    private static final String DATABASE_CREATE = "create table "
            + TABLE_RINGTO
            + "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_SERVER_ID + " text not null, "
            + COLUMN_FROM + " text not null, "
            + COLUMN_TO + " text not null, "
            + COLUMN_DIRECTION + " text not null, "
            + COLUMN_DATE + " integer not null, "
//            + COLUMN_DATE_RAW + " text not null, "
            + COLUMN_TEXT + " text not null, "
//            + COLUMN_INCOMING + " text not null, "
            + COLUMN_STATUS + " text not null, "
            + COLUMN_READ + " text not null, "
            + COLUMN_LEFT + " boolean not null"
            + ");";

    public static void onCreate(SQLiteDatabase database)
    {
        Log.d("Database", DATABASE_CREATE);
        database.execSQL(DATABASE_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion,
                                 int newVersion)
    {
        Log.w(RingToTable.class.getName(), "Upgrading database from version "
                + oldVersion + " to " + newVersion
                + ", which will destroy all old data");
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_RINGTO);
        onCreate(database);
    }
}