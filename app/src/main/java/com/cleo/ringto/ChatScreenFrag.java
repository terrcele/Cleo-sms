package com.cleo.ringto;

import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import org.json.JSONArray;

/**
 * Created by terrin on 4/19/15.
 */
public class ChatScreenFrag extends Fragment
{
    private static final String TAG = "ChatScreen";
    private RingToDataSource datasource = null;
    private ChatArrayAdapter chatArrayAdapter = null;
    private ListView listView;
    private EditText chatText;
    private Button buttonSend;
    private boolean side = false;
    private String token = "";
    private String access_token = "";
    private String messages = "";
    private RingToAPI app = null;
    private JSONArray jobj_messages = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.activity_chat,
                container, false);

        buttonSend = (Button) view.findViewById(R.id.buttonSend);
        listView = (ListView) view.findViewById(R.id.listView1);
        datasource = new RingToDataSource(getActivity());
        datasource.open();

        chatArrayAdapter = new ChatArrayAdapter(getActivity().getApplicationContext(), R.layout.activity_chat_singlemessage);
        listView.setAdapter(chatArrayAdapter);

        chatText = (EditText) view.findViewById(R.id.chatText);
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
        chatArrayAdapter.addListOfMessages(datasource.getAllMessages());
 //
//        datasource.getRecentChatThreads();
//        app.pullMessages(getActivity());
        setRetainInstance(true);
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        if (savedInstanceState == null)
        {

            if (getArguments() == null)
            {
                token = null;
            }

            else
            {
                token = getArguments().getString("token");
                access_token = getArguments().getString("access_token");
//                app = new RingToAPI(token);
                app = new RingToAPI();
                app.setToken(token);
                app.setAccessToken(access_token);
            }
        }
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
        side = false;
        chatArrayAdapter.add(new Message(side, chatText.getText().toString()));
        chatText.setText("");

        return true;
    }
}
