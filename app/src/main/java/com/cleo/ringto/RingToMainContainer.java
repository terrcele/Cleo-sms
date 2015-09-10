package com.cleo.ringto;

import android.app.ActionBar;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.support.v7.app.ActionBarDrawerToggle;

//import com.ikimuhendis.ldrawer.ActionBarDrawerToggle;
//import com.ikimuhendis.ldrawer.DrawerArrowDrawable;

import java.util.ArrayList;

/**
 * Created by terrin on 4/23/15.
 */
public class RingToMainContainer extends ActionBarActivity
{
    private RingToDataSource datasource = null;
    static final int NUM_ITEMS = 3;
    MainActivityFragmentAdapter mAdapter = null;
    ViewPager mPager = null;
    private RingToAPI app = null;
    private String token = "";
    private String access_token = "";
    private String jb_token = "";
    private Context context = null;
    Bundle savedInstanceState = null;
    private ChatListThreadAdapter list = null;
    DrawerLayout mDrawerLayout;
    ListView mDrawerList;
    ActionBarDrawerToggle mDrawerToggle;
//    DrawerArrowDrawable drawerArrow;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.savedInstanceState = savedInstanceState;
        setContentView(R.layout.main_activity_fragment_pager);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        mTitle = mDrawerTitle = getTitle();

        mAdapter = new MainActivityFragmentAdapter(getSupportFragmentManager());
        mPager = (ViewPager)findViewById(R.id.pager);
        mPager.setAdapter(mAdapter);

        datasource = new RingToDataSource(this);
        datasource.open();

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.navdrawer);

//        drawerArrow = new DrawerArrowDrawable(this)
//        {
//            @Override
//            public boolean isLayoutRtl()
//            {
//                return false;
//            }
//        };


        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.string.drawer_open,
                R.string.drawer_close)
        {

            public void onDrawerClosed(View view)
            {
                super.onDrawerClosed(view);
                supportInvalidateOptionsMenu();
                getSupportActionBar().setTitle(mTitle);
            }

            public void onDrawerOpened(View drawerView)
            {
                super.onDrawerOpened(drawerView);
                supportInvalidateOptionsMenu();
                getSupportActionBar().setTitle(mDrawerTitle);
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);

        String[] values = new String[]
                {
                "Stop Animation (Back icon)",
                "Stop Animation (Home icon)",
                "Start Animation",
                "Change Color",
                "GitHub Page",
                "Share",
                "Rate"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, values);
        mDrawerList.setAdapter(adapter);
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id)
            {
                switch (position)
                {
                    case 0:
//                        mDrawerToggle.setAnimateEnabled(false);
//                        drawerArrow.setProgress(1f);
                        break;
                }
            }
        });

//        mDrawerToggle.setAnimateEnabled(true);
//        if(savedInstanceState == null)
//        {
//            RingToApplication singleton = (RingToApplication) getApplicationContext();
//            token = singleton.getToken();
//            app = new RingToAPI();
//            app.setAPI(this, token);
////            app.setToken(token);
////            app.pullMessages(this, token);
//        }
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener
    {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id)
        {
            selectItem(position);
        }
    }

    /** Swaps fragments in the main content view */
    private void selectItem(int position)
    {}

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState)
    {
        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }

    /* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu)
    {
        // If the nav drawer is open, hide action items related to the content view
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (item.getItemId() == android.R.id.home)
        {
            if (mDrawerLayout.isDrawerOpen(mDrawerList))
            {
                mDrawerLayout.closeDrawer(mDrawerList);
            }

            else
            {
                mDrawerLayout.openDrawer(mDrawerList);
            }
        }
        return super.onOptionsItemSelected(item);
    }
//    @Override
//    public void onSaveInstanceState(Bundle savedInstanceState)
//    {
//        getFragmentManager().putFragment(savedInstanceState,"myfragment",this);
//        super.onSaveInstanceState(savedInstanceState);
//    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState)
    {
//        this = ((RingToChatListThreadFrag) getFragmentManager().getFragment(savedInstanceState, "myfragment"));
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_ring_to_login, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public static class MainActivityFragmentAdapter extends FragmentStatePagerAdapter
    {
        int SLIDES = 3;

        public MainActivityFragmentAdapter(FragmentManager fragmentManager)
        {
            super(fragmentManager);
        }

        @Override
        public int getCount()
        {
            return NUM_ITEMS;
        }

        @Override
        public Fragment getItem(int position)
        {
            switch (position)
            {
                case 0: // Fragment # 0 - This will show image
                    return RingToChatListThreadFrag.init();
                case 1:
                    return RingToVoiceMailFrag.init();
                case 2:
                    return RingToContactsFrag.init();
                default:
                    return new Fragment();
            }
        }
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        RingToApplication singleton = (RingToApplication) getApplicationContext();

        if((savedInstanceState == null) && (!singleton.isInitialPullMessage()))
        {
            Log.d("Here", "Resume");
            token = singleton.getToken();
            access_token = singleton.getAccessToken();
            jb_token = singleton.getJBToken();
            app = new RingToAPI();
            app.setAPI(this, token, access_token, jb_token);
//            app.setToken(token);
//            app.pullMessages(this, token);
        }

        else
        {
            Log.d("Here", "Resume2");
            app = new RingToAPI();
            app.setToken(singleton.getToken());
            app.setAccessToken(singleton.getAccessToken());
            app.setActivity(this);
        }
    }

    @Override
    protected void onPause()
    {
        super.onPause();
    }

//    public boolean storeChatMessage(boolean left, long server_id, String from_number, String to_number, String direction,
//                                   String date, String date_raw, String text, String incoming, String status,
//                                   String read)
    public boolean storeChatMessage(boolean left, long message_id, String from_number, String to_number, String direction,
                    String time, String text, String status, String read)
    {
        datasource.createMessage(left, message_id, from_number, to_number, direction,
                time, text, status, read);
        return true;
    }

    public RingToDataSource getDataSource()
    {
        return datasource;
    }

    public void setChatList(ChatListThreadAdapter list)
    {
        app.setList(list);
    }

    public void printStuff(ArrayList<Message> temp)
    {
        for(int i = 0; i < temp.size(); i++)
        {
            Log.d("Message", temp.get(i).getFromNumber() + " | " + temp.get(i).getText() + " | " + Long.toString(temp.get(i).getId()) + " | " + temp.get(i).getDateRaw());
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState)
    {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }
}
