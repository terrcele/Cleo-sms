package com.cleo.ringto;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by terrin on 4/25/15.
 */
public class ChatListThreadAdapter extends BaseAdapter
{
    private LayoutInflater mInflater = null;
    private List<Message> recentMessages = null;
    private Context context = null;

    public ChatListThreadAdapter(Context context, List<Message> recentMessages)
    {
        mInflater = LayoutInflater.from(context);
        this.recentMessages = recentMessages;
        this.context = context;
    }

    @Override
    public int getCount()
    {
        return recentMessages.size();
    }

    @Override
    public Object getItem(int position)
    {
        return recentMessages.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return recentMessages.get(position).getId();
    }

    public long getItemPosition(int position)
    {
        return position;
    }

    public void addMessageThread(Message thread)
    {
        recentMessages.add(thread);
    }

    public void addMessageThreads(ArrayList<Message> threads)
    {
        recentMessages.addAll(threads);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ViewHolder holder = new ViewHolder();

        Message recent_message = recentMessages.get(position);
        holder.message = recent_message;

        if(convertView == null)
        {
            convertView = mInflater.inflate(R.layout.chat_thread_row_layout, parent, false);
            holder.avatar = (ImageView)convertView.findViewById(R.id.avatar);
            holder.name = (TextView)convertView.findViewById(R.id.name);
            holder.latestMessage = (TextView)convertView.findViewById(R.id.latest_message);
            holder.date = (TextView)convertView.findViewById(R.id.date);
            holder.view = (RelativeLayout)convertView.findViewById(R.id.chat_item);
            convertView.setTag(holder);

            holder.view.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    String to_number = "";
                    String from_number = "";

                    ViewHolder temp = (ViewHolder) v.getTag();
                    Log.d("Item position", Integer.toString(temp.position));

                    if(temp.message.getLeft())
                    {
                        to_number = temp.message.getFromNumber();
                        from_number = temp.message.getToNumber();
                    }

                    else
                    {
                        to_number = temp.message.getToNumber();
                        from_number = temp.message.getFromNumber();
                    }

                    Intent intent = new Intent(context, RingToChatScreen.class);
                    intent.putExtra("to_number", to_number);
                    intent.putExtra("from_number", from_number);
                    Log.d("To Number:", temp.message.getToNumber());
                    Log.d("From Number:", temp.message.getFromNumber());
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            });
        }

        else
        {
            holder = (ViewHolder)convertView.getTag();
        }

        holder.avatar.setImageDrawable(context.getResources().getDrawable(R.mipmap.default_user));
        holder.name.setText(recent_message.getFromNumber());
        holder.latestMessage.setText(recent_message.getText());
        holder.message = recent_message;

        if(recent_message.getDate() != null)
        {
            Calendar c = Calendar.getInstance();
            String[] dates = recent_message.getDate().split(" ");
//            String[] dates = recent_message.getDate().split("T");
//            String[] time = dates[1].split(":");
//            String[] dates1 = dates[0].split("-");
//            String final_date = dates1[1] + "/" + dates1[2] + "/" + dates1[0];
            SimpleDateFormat df3 = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss a");
            String date2 = df3.format(c.getTime());
            String[] date2_split = date2.split(" ");

            Log.d("Current Date", date2);
            Log.d("Other dates", dates[0]);

            // Same date
            if( date2.contains(dates[0]) )
            {
//                int hour = 0;
//                String midday = "am";
//
//                if( (Double.parseDouble(time[0]) / 12) > 1)
//                {
//                    hour = (int) Double.parseDouble(time[0]) - 12;
//                    midday = "pm";
//                }
//
//                else
//                {
//                    hour = Integer.parseInt(time[0]);
//                }
//
//                String final_time = Integer.toString(hour) + ":" + time[1] + ":" + time[2].split("\\.")[0] + " " + midday;
////                holder.date.setText(dates[1] + "" + dates[2]);
                holder.date.setText(dates[1] + " " + dates[2]);
            }

            // Yesterday and previous days
            else
            {
//                GregorianCalendar c1 = new GregorianCalendar();
//                GregorianCalendar c2 = new GregorianCalendar();
//                String[] temp1a = date2.split(" ");
//                String[] temp1 = temp1a[0].split("/");
//                String[] temp2 = dates[0].split("/");
//                Log.d("Date1", temp1[0] + temp1[1] + temp1[2]);
//                Log.d("Date2", temp2[0] + temp2[1] + temp2[2]);
//                c2.set(Integer.parseInt("20" + temp1[2]), Integer.parseInt(temp1[0]), Integer.parseInt(temp1[1]));
//                c1.set(Integer.parseInt("20" + temp2[2]), Integer.parseInt(temp2[0]), Integer.parseInt(temp2[1]));
//                long span = c2.getTimeInMillis() - c1.getTimeInMillis();
//                GregorianCalendar c3 = new GregorianCalendar();
//                c3.setTimeInMillis(span);
//                long numberOfMSInADay = 1000*60*60*24;
//                long ms = c3.getTimeInMillis() / numberOfMSInADay;
//                Log.d("MS", Long.toString(ms)); //3653
//
//
//                if(ms >= 1)
//                {
//                    holder.date.setText("Yesterday");
//                }

//                else
//                {
                //String[] temp = recent_message.getDate().split(" ");
                holder.date.setText(dates[0]);
//                holder.date.setText(temp[0]);
//                }
            }
        }

//        holder.view.setOnClickListener(new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            ViewHolder temp = (ViewHolder) v.getTag();
//            Log.d("Item position", Integer.toString(temp.position));
//            Intent intent = new Intent(context, RingToChatScreen.class);
//            intent.putExtra("to_number", temp.message.getToNumber());
//            intent.putExtra("from_number", temp.message.getFromNumber());
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            context.startActivity(intent);
//        }
//    });

        convertView.setTag(holder);
        return convertView;
    }

    private class ViewHolder
    {
        public ImageView avatar;
        public TextView name, latestMessage, date;
        public Message message = null;
        public int position = 0;
        public View view = null;
    }
}
