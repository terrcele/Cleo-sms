package com.cleo.ringto;

import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by terrin on 4/7/15.
 */
public class RingToDataSource
{
    // Database fields
    private SQLiteDatabase database = null;
    private RingToDatabaseHelper dbHelper = null;
//    private String[] allColumns = {
//            RingToTable.COLUMN_ID,
//            RingToTable.COLUMN_SERVER_ID,
//            RingToTable.COLUMN_FROM,
//            RingToTable.COLUMN_TO,
//            RingToTable.COLUMN_DIRECTION,
//            RingToTable.COLUMN_DATE,
//            RingToTable.COLUMN_DATE_RAW,
//            RingToTable.COLUMN_TEXT,
//            RingToTable.COLUMN_INCOMING,
//            RingToTable.COLUMN_STATUS,
//            RingToTable.COLUMN_READ,
//            RingToTable.COLUMN_LEFT};
    private String[] allColumns = {
        RingToTable.COLUMN_ID,
        RingToTable.COLUMN_SERVER_ID,
        RingToTable.COLUMN_FROM,
        RingToTable.COLUMN_TO,
        RingToTable.COLUMN_DIRECTION,
        RingToTable.COLUMN_DATE,
        RingToTable.COLUMN_TEXT,
        RingToTable.COLUMN_STATUS,
        RingToTable.COLUMN_READ,
        RingToTable.COLUMN_LEFT};

    public RingToDataSource(Context context)
    {
        dbHelper = new RingToDatabaseHelper(context);
    }

    public void open() throws SQLException
    {
        database = dbHelper.getWritableDatabase();
    }

    public void close()
    {
        dbHelper.close();
    }

//    public void createMessage(boolean left, long id, String from, String to, String direction, String date, String date_raw,
//                                 String text, String incoming, String status, String read)
    public void createMessage(boolean left, long message_id, String from, String to, String direction,
    String time, String text, String status, String read)
    {
        Message newMessage = null;
        ContentValues values = new ContentValues();
        values.put(RingToTable.COLUMN_LEFT, left);
        values.put(RingToTable.COLUMN_SERVER_ID, message_id);
        values.put(RingToTable.COLUMN_FROM, from);
        values.put(RingToTable.COLUMN_TO, to);
        values.put(RingToTable.COLUMN_DIRECTION, direction);
        //values.put(RingToTable.COLUMN_DATE, time);
//        values.put(RingToTable.COLUMN_DATE_RAW, date_raw);
        values.put(RingToTable.COLUMN_TEXT, text);
//        values.put(RingToTable.COLUMN_INCOMING, incoming);
        values.put(RingToTable.COLUMN_STATUS, status);
        values.put(RingToTable.COLUMN_READ, read);

        Log.d("Time: ", time);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS'Z'");
        Date date = new Date();
        format.setTimeZone(TimeZone.getTimeZone("UTC"));

        try
        {
            String times[] = getChangeDate(time);
            String times2[] = time.split("T");

//            date = format.parse(times[1] + " " + times[0]);
//            date = format.parse(time);
            date = format.parse(times2[0] + " " + times2[1]);
        }

        catch(ParseException e)
        {
            Log.d("Parse Exception of date", e.toString());
        }

        Log.d("Date Time: ", Long.toString(date.getTime()));
        Log.d("Date Time2: ", format.format(date));

        values.put(RingToTable.COLUMN_DATE, Long.toString(date.getTime()));

//        Cursor cursor_test = database.query(RingToDatabaseHelper.TABLE_RINGTO,
//                allColumns, RingToTable.COLUMN_SERVER_ID + " =? AND " + RingToTable.COLUMN_DATE + " =? ", new String[]{Long.toString(message_id), time},
//                null, null, null);

        Cursor cursor_test = database.query(RingToDatabaseHelper.TABLE_RINGTO,
                allColumns, RingToTable.COLUMN_SERVER_ID + " =? AND " + RingToTable.COLUMN_DATE + " =? ", new String[]{Long.toString(message_id), Long.toString(date.getTime())},
                null, null, null);

        if(cursor_test == null)
        {
            Log.d("RingToDataSource", "Cursor is null.");
        }

        else if(cursor_test.getCount() == 0)
        {
            long insertId = database.insert(RingToDatabaseHelper.TABLE_RINGTO, null,
                    values);

            Cursor cursor = database.query(RingToDatabaseHelper.TABLE_RINGTO,
                    allColumns, RingToTable.COLUMN_ID + " = " + insertId, null,
                    null, null, null);

            cursor.moveToFirst();
            newMessage = cursorToMessage(cursor);
            cursor.close();
        }

        else
        {
            Log.d("RingToDataSource", "Already Added.");
        }

        cursor_test.close();

//        return newMessage;
    }

    public void deleteMessage(Message message)
    {
        long id = message.getId();
        System.out.println("Message deleted with id: " + id);
        database.delete(RingToDatabaseHelper.TABLE_RINGTO, RingToDatabaseHelper.TABLE_RINGTO
                + " = " + id, null);
    }

    public List<Message> getChatThread(String to_number, String from_number)
    {
        List<Message> messages = new ArrayList<Message>();

//        Cursor cursor = database.query(RingToDatabaseHelper.TABLE_RINGTO,
//                allColumns, RingToTable.COLUMN_TO + " =? " + " AND " + RingToTable.COLUMN_FROM + " =? ", new String[]{to_number, from_number}, null, null, null);

        Cursor cursor = database.query(RingToDatabaseHelper.TABLE_RINGTO,
                allColumns, RingToTable.COLUMN_TO + " IN (?,?) " + " AND " + RingToTable.COLUMN_FROM + " IN (?,?) ", new String[]{to_number, from_number, to_number, from_number}, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast())
        {
            Message message = cursorToMessage(cursor);

            if(message.getToNumber().equals(message.getFromNumber()))
            {}

            else
            {
                messages.add(message);
            }

            cursor.moveToNext();
        }

        // make sure to close the cursor
        cursor.close();
        return messages;
    }

    public List<Message> getAllMessages()
    {
        List<Message> messages = new ArrayList<Message>();

        Cursor cursor = database.query(RingToDatabaseHelper.TABLE_RINGTO, allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while(!cursor.isAfterLast())
        {
            Message message = cursorToMessage(cursor);
            messages.add(message);
            cursor.moveToNext();
        }

        cursor.close();
        return messages;
    }

    public List<Message> getRecentChatThreads()
    {
        String[] allColumns = {
                RingToTable.COLUMN_ID,
                RingToTable.COLUMN_SERVER_ID,
                RingToTable.COLUMN_FROM,
                RingToTable.COLUMN_TO,
                RingToTable.COLUMN_DIRECTION,
                RingToTable.COLUMN_DATE,
                RingToTable.COLUMN_TEXT,
                RingToTable.COLUMN_STATUS,
                RingToTable.COLUMN_READ,
                RingToTable.COLUMN_LEFT};

        List<Message> messages = new ArrayList<Message>();
        Cursor cursor_inner = null;
//        Cursor cursor = database.query(true, RingToDatabaseHelper.TABLE_RINGTO,
//                allColumns, null, null, RingToTable.COLUMN_FROM, null, RingToTable.COLUMN_FROM + " ASC ", null);

        // Get the phone numbers from all the threads.
        cursor_inner = database.query(true, RingToDatabaseHelper.TABLE_RINGTO, new String[] {RingToTable.COLUMN_FROM}, null, null, null, null, RingToTable.COLUMN_FROM + " ASC", null);
        Message message;
        Cursor m = null;
        Cursor n = null;

        if( cursor_inner.moveToFirst() )
        {
            Log.d("Cursor Info Item: ", cursor_inner.getString(cursor_inner.getColumnIndex(RingToTable.COLUMN_FROM)));
            m = database.query(RingToTable.TABLE_RINGTO, allColumns, RingToTable.COLUMN_FROM + "=? OR " + RingToTable.COLUMN_TO + "=?", new String[]{cursor_inner.getString(cursor_inner.getColumnIndex(RingToTable.COLUMN_FROM)), cursor_inner.getString(cursor_inner.getColumnIndex(RingToTable.COLUMN_FROM))}, null, null, RingToTable.COLUMN_DATE + " DESC", null);

            if( m.moveToFirst() )
            {
                message = cursorToMessage(m);
                message.setFromNumber(cursor_inner.getString(cursor_inner.getColumnIndex(RingToTable.COLUMN_FROM)));
                messages.add(message);
            }
        }

        while(cursor_inner.moveToNext())
        {
            Log.d("Cursor Info Item2: ", cursor_inner.getString(cursor_inner.getColumnIndex(RingToTable.COLUMN_FROM)));
//            m = database.query(RingToTable.TABLE_RINGTO, allColumns, RingToTable.COLUMN_FROM + "=?", new String[]{cursor_inner.getString(cursor_inner.getColumnIndex(RingToTable.COLUMN_FROM))}, null, null, RingToTable.COLUMN_DATE + " DESC", null);
            m = database.query(RingToTable.TABLE_RINGTO, allColumns, RingToTable.COLUMN_FROM + "=? OR " + RingToTable.COLUMN_TO + "=?", new String[]{cursor_inner.getString(cursor_inner.getColumnIndex(RingToTable.COLUMN_FROM)), cursor_inner.getString(cursor_inner.getColumnIndex(RingToTable.COLUMN_FROM))}, null, null, RingToTable.COLUMN_DATE + " DESC", null);

            if( m.moveToFirst() )
            {
                message = cursorToMessage(m);
                message.setFromNumber(cursor_inner.getString(cursor_inner.getColumnIndex(RingToTable.COLUMN_FROM)));
                messages.add(message);
            }
        }

//        while (m.moveToNext())
//        {
//            message = cursorToMessage(m);
//            messages.add(message);
//        }

//        cursor.moveToFirst();
//        while (!cursor.isAfterLast())
//        {
//            Message message = cursorToMessage(cursor);
//            messages.add(message);
////            cursor_inner = database.query(RingToDatabaseHelper.TABLE_RINGTO,
////                    allColumns, RingToTable.COLUMN_ID + " = " + message.get, null, null, null, null);
//            cursor.moveToNext();
//
//        }

        // make sure to close the cursor
//        cursor.close();
        cursor_inner.close();

        if(m != null)
        {
            m.close();
        }

        return messages;
    }
//    public List<Message> getRecentChatList()
//    {
//        List<Message> messages = new ArrayList<Message>();
//        ArrayList<String> numbers = null;
//        Cursor cursor = database.query(true, RingToDatabaseHelper.TABLE_RINGTO, new String[] {RingToTable.COLUMN_FROM}, null, null, null, null, null, null);
//
//        cursor.moveToFirst();
//        while (!cursor.isAfterLast())
//        {
//            numbers.add(cursor.getString(0));
//            cursor.moveToNext();
//        }
//
//        for(int i = 0; i < numbers.size(); i++)
//        {
//            Cursor cursor_recent = database.query(RingToDatabaseHelper.TABLE_RINGTO, allColumns, )
//        }
//
//        return messages;
//    }

    private Message cursorToMessage(Cursor cursor)
    {
        Date date = new Date();

        Message message = new Message();
        boolean value = cursor.getInt(9) > 0;
        message.setLeft(value);
        message.setId(cursor.getLong(0));
        message.setServerId(cursor.getLong(1));
        message.setFromNumber(cursor.getString(2));
        message.setToNumber(cursor.getString(3));
        message.setDirection(cursor.getString(4));
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss a");
//        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        date.setTime(cursor.getLong(5));
        message.setDate(format.format(date));
//        message.setDateRaw(cursor.getString(6));
        message.setText(cursor.getString(6));
//        message.setIncoming(cursor.getString(8));
        message.setStatus(cursor.getString(7));
        message.setRead(cursor.getString(8));
        return message;
    }

    public String[] getChangeDate(String date)
    {
        Log.d("The date", date);
        String[] date_split = date.split("T");

        String[] date_time = date_split[1].split(":");
        int hour = 0;
        String midday = "AM";

        if( (Double.parseDouble(date_time[0]) / 12) > 1)
        {
            hour = (int) Double.parseDouble(date_time[0]) - 12;
            midday = "PM";
        }

        else
        {
            hour = Integer.parseInt(date_time[0]);
        }

        String seconds = date_time[2].split("\\.")[0];
        String final_time = Integer.toString(hour) + ":" + date_time[1] + ":" + seconds + " " + midday;
        String[] final_date = date_split[0].split("-");
        String final_date_string = final_date[0] + "/" + final_date[1] + "/" + final_date[2];
        return new String[] {final_time, final_date_string};
    }
}
