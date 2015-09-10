package com.cleo.ringto;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * Created by terrin on 5/2/15.
 */
public class RingToContactsFrag extends Fragment
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

    static RingToContactsFrag init()
    {
        RingToContactsFrag temp = new RingToContactsFrag();
        // Supply val input as an argument.
        Bundle args = new Bundle();
        return temp;
    }
}
