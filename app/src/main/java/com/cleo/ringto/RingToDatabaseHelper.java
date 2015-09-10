package com.cleo.ringto;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by terrin on 4/7/15.
 */
public class RingToDatabaseHelper extends SQLiteOpenHelper
{
    private static final String DATABASE_NAME = "ringto.db";
    private static final int DATABASE_VERSION = 1;
    static final String TABLE_RINGTO = "ringto_messages";

    public RingToDatabaseHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Method is called during creation of the database
    @Override
    public void onCreate(SQLiteDatabase database)
    {
        RingToTable.onCreate(database);
    }

    // Method is called during an upgrade of the database,
    // e.g. if you increase the database version
    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion)
    {
        RingToTable.onUpgrade(database, oldVersion, newVersion);
    }
}