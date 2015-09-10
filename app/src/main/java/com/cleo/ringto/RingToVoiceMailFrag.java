package com.cleo.ringto;

import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by terrin on 5/2/15.
 */
public class RingToVoiceMailFrag extends Fragment
{
    Context context = null;
    private RingToAPI app = null;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        context = getActivity().getApplicationContext();

        if(app == null)
        {
            app = new RingToAPI();
        }
    }

    static RingToVoiceMailFrag init()
    {
        RingToVoiceMailFrag temp = new RingToVoiceMailFrag();
        // Supply val input as an argument.
        Bundle args = new Bundle();
        return temp;
    }
}
