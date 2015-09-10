package com.cleo.ringto;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by terrin on 4/7/15.
 */
public class Message implements Parcelable
{
    private long id;
    private long server_id;
    private String from_number;
    private String to_number;
    private String direction;
    private String date;
    private String date_raw;
    private String text;
    private String incoming;
    private String status;
    private String read;
    public boolean left;

    public Message()
    {
        super();
    }

    public Message(boolean left, String message)
    {
        super();
        this.left = left;
        this.text = message;
        this.date = getDateAndTime();
    }

    public Message(boolean left, long server_id, String from_number, String to_number,
                   String direction, String date, String date_raw, String message,
                   String incoming, String status, String read)
    {
        super();
        this.left = left;
        this.server_id = server_id;
        this.from_number = from_number;
        this.to_number = to_number;
        this.direction = direction;
        this.date = date;
        this.date_raw = date_raw;
        this.text = message;
        this.incoming = incoming;
        this.status = status;
        this.read = read;
    }

    public boolean getLeft()
    {
        return left;
    }

    public void setLeft(boolean left)
    {
        this.left = left;
    }

    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    public long getServerId()
    {
        return server_id;
    }

    public void setServerId(long server_id)
    {
        this.server_id = server_id;
    }

    public String getFromNumber()
    {
        return from_number;
    }

    public void setFromNumber(String from_number)
    {
        this.from_number = from_number;
    }

    public String getToNumber()
    {
        return to_number;
    }

    public void setToNumber(String to_number)
    {
        this.to_number = to_number;
    }

    public String getDirection()
    {
        return direction;
    }

    public void setDirection(String direction)
    {
        this.direction = direction;
    }

    public String getDate()
    {
        return date;
    }

    public void setDate(String date)
    {
        this.date = date;
    }

    public String getDateRaw()
    {
        return date_raw;
    }

    public void setDateRaw(String date_raw)
    {
        this.date_raw = date_raw;
    }

    public String getText()
    {
        return text;
    }

    public void setText(String text)
    {
        this.text = text;
    }

    public String getIncoming()
    {
        return incoming;
    }

    public void setIncoming(String incoming)
    {
        this.incoming = incoming;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getRead()
    {
        return read;
    }

    public void setRead(String read)
    {
        this.read = read;
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeLong(id);
        dest.writeLong(server_id);
        dest.writeString(from_number);
        dest.writeString(to_number);
        dest.writeString(direction);
        dest.writeString(date);
        dest.writeString(date_raw);
        dest.writeString(text);
        dest.writeString(incoming);
        dest.writeString(status);
        dest.writeString(read);
    }

    static public String getDateAndTime()
    {
        Calendar c = Calendar.getInstance();
        Date date = c.getTime();
        String string_date = Integer.toString(c.get(Calendar.YEAR)) + "-" + Integer.toString(c.get(Calendar.MONTH))
                + "-" + Integer.toString(c.get(Calendar.DATE)) + "T" + Integer.toString(c.get(Calendar.HOUR))
                + ":" + Integer.toString(c.get(Calendar.MINUTE)) + ":" + Integer.toString(c.get(Calendar.SECOND))
                + ".000Z";
        Log.d("Message", string_date);

        return string_date;
    }
}
