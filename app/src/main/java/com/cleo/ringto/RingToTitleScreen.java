package com.cleo.ringto;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.VideoView;

/**
 * Created by terrin on 4/25/15.
 */
public class RingToTitleScreen extends Activity
{
    VideoView videoView = null;
    TextView title = null;
    Button signin = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_screen);

        // Play video in the background on the title screen.
        videoView = (VideoView) findViewById(R.id.VideoView);
        String uri = "android.resource://" + getPackageName() + "/" + R.drawable.dock;
        videoView.setVideoURI(Uri.parse(uri));
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener()
        {
            @Override
            public void onPrepared(MediaPlayer mp)
            {
                mp.setLooping(true);
            }
        });
        videoView.start();

        // Open login screen.
        signin = (Button) findViewById(R.id.signin);
        signin.setOnClickListener( new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(RingToTitleScreen.this, RingToLogin.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onPause()
    {
        // Pause video playing when activity is placed in background.
        videoView.stopPlayback();
        videoView = null;
        Log.d("onPause", "Stopping video.");
        super.onPause();
    }

    @Override
    protected void onResume()
    {
        // Replay the video when the application is resumed.
        videoView = (VideoView) findViewById(R.id.VideoView);
        String uri = "android.resource://" + getPackageName() + "/" + R.drawable.dock;
        videoView.setVideoURI(Uri.parse(uri));
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener()
        {
            @Override
            public void onPrepared(MediaPlayer mp)
            {
                mp.setLooping(true);
            }
        });

        videoView.start();
        title = (TextView) findViewById(R.id.login_title);
        super.onResume();
    }
}
