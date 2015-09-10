package com.cleo.ringto;

import android.app.Application;

/**
 * Created by terrin on 4/19/15.
 */
public class RingToApplication extends Application
{
    private String token = "";
    private String access_token = "";
    private boolean pull_messages_on_startup = false;
    private String jb_token = "";

    public void setToken(String token)
    {
        this.token = token;
    }

    public void setAccessToken(String access_token)
    {
        this.access_token = access_token;
    }

    public void setJBToken(String jb_token)
    {
        this.jb_token = jb_token;
    }

    public String getJBToken()
    {
        return jb_token;
    }

    public String getToken()
    {
        return token;
    }

    public String getAccessToken()
    {
        return access_token;
    }

    public void setInitialPullMessage(boolean pull)
    {
        this.pull_messages_on_startup = pull;
    }

    public boolean isInitialPullMessage()
    {
        return pull_messages_on_startup;
    }
}
