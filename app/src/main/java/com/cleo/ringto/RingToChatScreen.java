package com.cleo.ringto;

import android.app.Activity;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class RingToChatScreen extends ActionBarActivity
{
    private static final String TAG = "ChatScreen";
    RingToDataSource datasource = null;
    private ChatArrayAdapter chatArrayAdapter = null;
    private ArrayList<Message> message_list = null;
    private ListView listView;
    private EditText chatText;
    private Button buttonSend;
    private boolean side = false;
    private String token = "";
    private String to_number = "";
    private String from_number = "";
    private String messages = "";
    private RingToAPI app = null;
    private JSONArray jobj_messages = null;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        buttonSend = (Button) findViewById(R.id.buttonSend);
        listView = (ListView) findViewById(R.id.listView1);
        datasource = new RingToDataSource(this);
        datasource.open();

        chatArrayAdapter = new ChatArrayAdapter(this.getApplicationContext(), R.layout.activity_chat_singlemessage);
        listView.setAdapter(chatArrayAdapter);
        listView.setDivider(null);
        chatText = (EditText) findViewById(R.id.chatText);
        chatText.setOnKeyListener(new View.OnKeyListener()
        {
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER))
                {
                    return sendChatMessage();
                }

                return false;
            }
        });

        buttonSend.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View arg0)
            {
                sendChatMessage();
            }
        });


//        else
//        {
//            message_list = new ArrayList<Message>(datasource.getAllMessages());
//            chatArrayAdapter.addListOfMessages(message_list);
//        }

//        FragmentManager managerFragment = getSupportFragmentManager();
//        app = (RingToAPI) managerFragment.findFragmentByTag(String.valueOf(0));
//
//        if(app == null)
//        {
//            app = new RingToAPI();
            to_number = getIntent().getExtras().getString("to_number");        
            from_number = getIntent().getExtras().getString("from_number");

//            app.setAPI(this, token);
//            managerFragment.beginTransaction().add(app, String.valueOf(0)).commit();
//        }

        listView.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        listView.setAdapter(chatArrayAdapter);

        //to scroll the list view to bottom on data change
        chatArrayAdapter.registerDataSetObserver(new DataSetObserver()
        {
            @Override
            public void onChanged()
            {
                super.onChanged();
                listView.setSelection(chatArrayAdapter.getCount() - 1);
            }
        });

        chatArrayAdapter.addListOfMessages(datasource.getChatThread(to_number, from_number));

//        if (savedInstanceState != null)
//        {
//            RingToApplication singleton = (RingToApplication) getApplicationContext();
//            token = singleton.getToken();
//            app = new RingToAPI();
//            app.setToken(token);
//        }
//
//        else
//        {
//            // Takes the previous saved list of messages on screen and appends them to
//            // the current message list on screen.
////            message_list = savedInstanceState.getParcelableArrayList("here");
////            chatArrayAdapter.addListOfMessages(message_list);
//        }

//        Toast.makeText(getApplicationContext(), Integer.toString(messages.length()), Toast.LENGTH_LONG);
//        app.pullMessages(getActivity());

    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState)
    {
        //savedInstanceState.putParcelableArrayList("here", chatArrayAdapter.getMessages());
        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }

//    public boolean sendChatMessage(boolean left, long server_id, String from_number, String to_number, String direction,
//                                   String date, String date_raw, String text, String incoming, String status,
//                                   String read)
//    {
//        datasource.createMessage(left, server_id, from_number,to_number, direction,
//                date, date_raw, text, incoming, status, read);
//        chatArrayAdapter.add(new Message(left, server_id, from_number,to_number, direction,
//                date, date_raw, text, incoming, status, read));
//        return true;
//    }

    public boolean sendChatMessage()
    {
        app = new RingToAPI();
        ArrayList<String> to_num = new ArrayList<String>();
        to_num.add(to_number.replace('+', ' ').trim());
        RingToApplication singleton = (RingToApplication) getApplicationContext();
        String access_token = singleton.getAccessToken();
        token = singleton.getToken();

        app.setAPI(this, token, access_token, from_number.replace('+', ' ').trim(), new ArrayList<String>(), chatText.getText().toString(), to_num);
        side = false;
        chatArrayAdapter.add(new Message(side, chatText.getText().toString()));
        chatText.setText("");

        return true;
    }

    public boolean storeChatMessage(boolean left, long message_id, String from_number, String to_number, String direction,
                                    String time, String text, String status, String read)
    {
        datasource.createMessage(left, message_id, from_number, to_number, direction, time, text, status, read);
        return true;
    }

    @Override
    protected void onResume()
    {
        datasource.open();
        super.onResume();
    }

    @Override
    protected void onPause()
    {
        datasource.close();
        super.onPause();
    }

    public void printStuff(ArrayList<Message> temp)
    {
        for(int i = 0; i < temp.size(); i++)
        {
            Log.d("Message", temp.get(i).getFromNumber() + " | " + temp.get(i).getText() + " | " + Long.toString(temp.get(i).getId()) + " | " + temp.get(i).getDateRaw());
        }
    }
}
