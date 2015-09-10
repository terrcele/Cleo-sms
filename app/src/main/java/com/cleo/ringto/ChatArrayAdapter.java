package com.cleo.ringto;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ChatArrayAdapter extends ArrayAdapter<Message>
{
	private TextView chatText;
	private List<Message> chatMessageList = new ArrayList<Message>();
	private LinearLayout singleMessageContainer;
	private LinearLayout singleMessageContent;

	@Override
	public void add(Message object)
    {
		chatMessageList.add(object);
		super.add(object);
	}

    public ArrayList<Message> getMessages()
    {
        return new ArrayList<Message>(chatMessageList);
    }

    public void addListOfMessages(List<Message> object)
    {
        chatMessageList.addAll(object);
    }

	public ChatArrayAdapter(Context context, int textViewResourceId)
    {
		super(context, textViewResourceId);
	}

	public int getCount()
    {
		return this.chatMessageList.size();
	}

	public Message getItem(int index)
    {
		return this.chatMessageList.get(index);
	}

	public View getView(int position, View convertView, ViewGroup parent)
    {
		View row = convertView;

        if (row == null)
        {
			LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row = inflater.inflate(R.layout.activity_chat_singlemessage, parent, false);
		}

		singleMessageContainer = (LinearLayout) row.findViewById(R.id.wholeContainer);
		singleMessageContent = (LinearLayout) row.findViewById(R.id.singleMessageContent);
		Message chatMessageObj = getItem(position);
		chatText = (TextView) row.findViewById(R.id.singleMessage);
		chatText.setText(chatMessageObj.getText());
//		chatText.setBackgroundResource(chatMessageObj.left ? R.drawable.bubble_b : R.drawable.bubble_a_old);

		chatText = (TextView) row.findViewById(R.id.singleMessageTime);
//		getChangeDate(chatMessageObj.getDate());

		chatText.setText(chatMessageObj.getDate().split(" ")[1] + " " + chatMessageObj.getDate().split(" ")[2]); //getChangeDate(chatMessageObj.getDate())[0]);

		chatText = (TextView) row.findViewById(R.id.singleMessageDate);
//		getChangeDate(chatMessageObj.getDate());
		chatText.setText(chatMessageObj.getDate().split(" ")[0]); //getChangeDate(chatMessageObj.getDate())[1]);

		singleMessageContent.setBackgroundResource(chatMessageObj.left ? R.drawable.bubble_b : R.drawable.bubble_a_old);
		singleMessageContainer.setGravity(chatMessageObj.left ? Gravity.LEFT : Gravity.RIGHT);
		return row;
	}

	public Bitmap decodeToBitmap(byte[] decodedByte)
    {
		return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
	}

	public String[] getChangeDate(String date)
	{
		Log.d("The date", date);
		String[] date_split = date.split("T");
		String[] date_time = date_split[1].split(":");
		int hour = 0;
		String midday = "am";

		if( (Double.parseDouble(date_time[0]) / 12) > 1)
		{
			hour = (int) Double.parseDouble(date_time[0]) - 12;
			midday = "pm";
		}

		else
		{
			hour = Integer.parseInt(date_time[0]);
		}

		String seconds = date_time[2].split("\\.")[0];
		String final_time = Integer.toString(hour) + ":" + date_time[1] + ":" + seconds + " " + midday;
		String[] final_date = date_split[0].split("-");
		String final_date_string = final_date[1] + "/" + final_date[2] + "/" + final_date[0];
		return new String[] {final_time, final_date_string};
	}
}