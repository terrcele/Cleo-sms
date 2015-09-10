package com.cleo.ringto;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by terrin on 4/23/15.
 */
public class RingToChatListThreadFrag extends Fragment
{
    public ArrayList<Message> messageList = null;
    private RingToDataSource datasource = null;
    Context context = null;
    ChatListThreadAdapter list = null;
    ListView chat_threads = null;
    private Activity main_activity = null;
    private RingToAPI app = null;
    private String token = "";

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        messageList = new ArrayList<Message>();
        context = getActivity().getApplicationContext();
        datasource = new RingToDataSource(context);
        datasource.open();
        List<Message> temp = datasource.getRecentChatThreads();
        list = new ChatListThreadAdapter(main_activity.getApplicationContext(), temp);
        ((RingToMainContainer) main_activity).setChatList(list);
        RingToApplication singleton = (RingToApplication) context;
        datasource.close();
        // Create the RingTo API using token.
        // Obtain all the messages from RingTo and add to the database.
//        if( (app == null) && (!singleton.isInitialPullMessage()))
//        {
//            app = new RingToAPI();
//            Bundle args = getActivity().getIntent().getExtras();
//            token = args.getString("token");
//            app.setAPI((ActionBarActivity) main_activity, token, list);
//        }
//
//        else
//        {
//            Log.d("ThreadFrag", "Creation did not start.");
//        }

        notifyThread();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState)
    {
        getFragmentManager().putFragment(savedInstanceState,"myfragment",this);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.chat_thread_list, container, false);
        chat_threads = (ListView) v.findViewById(R.id.chat_threads);
        chat_threads.setAdapter(list);

        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        if(savedInstanceState != null)
        {
            datasource = new RingToDataSource(context);
            datasource.open();
            List<Message> temp = datasource.getRecentChatThreads();
            list = new ChatListThreadAdapter(getActivity(), temp);
            chat_threads.setAdapter(list);
        }
    }

    static RingToChatListThreadFrag init()
    {
        RingToChatListThreadFrag temp = new RingToChatListThreadFrag();
        return temp;
    }

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        main_activity = activity;
    }

    public void notifyThread()
    {
        list.notifyDataSetChanged();
    }

    @Override
    public void onDetach()
    {
        super.onDetach();
        list = null;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        notifyThread();
    }
}
