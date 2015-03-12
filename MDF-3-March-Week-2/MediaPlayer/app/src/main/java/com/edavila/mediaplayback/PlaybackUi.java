// Eddy Davila
// MDF 3
// Full Sail University
// Week 2


package com.edavila.mediaplayback;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.edavila.mediaplayback.MusicService.ServiceBinder;


import java.util.Timer;
import java.util.TimerTask;

public class PlaybackUi extends Activity implements OnClickListener {
    private MusicService musicSrv;
    private boolean musicBound = false;
    Button playbutton;//play button
    Button pausebutton;//pause button
    Button stopbutton;//stop button
    Button prevbutton;//prev button
    Button nextbutton;//next button
    TimerTask timerTask;
    TextView text;
    Button loopbutton;//loop button
    public SeekBar m_Progbar;
    int t_number;
    Timer timer;
    Boolean loopStart = false;

    private static final String TAG = "Activity State: ";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.playbackui);
        t_number = 0;
        m_Progbar = (SeekBar) findViewById(R.id.seekBar1);
        loopStart = true;
        timerTask = new TimerTask() {
            @Override
            public void run() {
                if (musicSrv != null) {
                    if (musicSrv.mPlayer.isPlaying()) {
                        int currentPosition = musicSrv.mPlayer.getCurrentPosition();//Getting playing position
                        int duration = musicSrv.mPlayer.getDuration();//Getting duration
                        int seekposition = currentPosition * 100 / duration;//caclulation seekbar position
                        m_Progbar.setProgress(seekposition);//position set
                    } else {
                        m_Progbar.setProgress(0);//seekbar position init
                    }
                } else {
                    m_Progbar.setProgress(0);//seekbar position init
                }
            }


        };


        text = (TextView) findViewById(R.id.textView1);
        text.setText(MusicService.sname + " is playing");

        playbutton = (Button) findViewById(R.id.button1);
        playbutton.setOnClickListener(this);

        //pause button
        pausebutton = (Button) findViewById(R.id.button2);
        pausebutton.setOnClickListener(this);

        //stop button
        stopbutton = (Button) findViewById(R.id.button3);
        stopbutton.setOnClickListener(this);

//        //Prev button
        prevbutton = (Button) findViewById(R.id.button4);
        prevbutton.setOnClickListener(this);

//        //Next button
        nextbutton = (Button) findViewById(R.id.button5);
        nextbutton.setOnClickListener(this);

//        //loop button
        loopbutton = (Button) findViewById(R.id.button6);
        loopbutton.setOnClickListener(this);

        //pause,stop button disable
        pausebutton.setEnabled(false);
        stopbutton.setEnabled(false);
//        prevbutton.setEnabled(false);
//        nextbutton.setEnabled(false);


    }

    //connect to the service
    private ServiceConnection musicConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            ServiceBinder binder = (ServiceBinder) service;
            //get service
            musicSrv = binder.getService();
            //pass list

            t_number = 0;

            if (loopStart == true) {
                // loop

            }

            musicSrv.mp3_play(t_number);
            text.setText(MusicService.sname + " is playing");
            if (timer == null) {
                timer = new Timer();
                timer.schedule(timerTask, 1, 1);

            }
//				musicSrv.setList(songList,0);

            musicBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            musicBound = false;
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
//		mServ.pauseMusic();
//        musicSrv.resumeMusic();
//		mServ.stopMusic();
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }

    // function handles button clicks
    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            //play button
            case R.id.button1:
                if (!musicBound) {
                    Intent playmusic = new Intent(PlaybackUi.this, MusicService.class);
                    bindService(playmusic, musicConnection, Context.BIND_AUTO_CREATE);

                    //Timer start
//					startService(playmusic);
                } else {
                    musicSrv.resumeMusic();
                }

//				}

                //button enable,disabel set
                text.setText(MusicService.sname + " is playing");
                playbutton.setEnabled(false);
                playbutton.setText("Playing");
                pausebutton.setEnabled(true);
                stopbutton.setEnabled(true);
                prevbutton.setEnabled(true);
                nextbutton.setEnabled(true);
                loopbutton.setEnabled(true);
                break;

            //pause button
            case R.id.button2:
                if (musicBound) {

                    musicSrv.pauseMusic();
                    text.setText(MusicService.sname + " is playing");
                    m_Progbar.setProgress(m_Progbar.getProgress());

                }

                playbutton.setEnabled(true);
                playbutton.setText("Play");
                pausebutton.setEnabled(false);
                stopbutton.setEnabled(true);
                prevbutton.setEnabled(true);
                nextbutton.setEnabled(true);
                loopbutton.setEnabled(true);
                break;

            //stop button
            case R.id.button3:
                if (musicBound) {
                    musicSrv.stopMusic();
                    unbindService(musicConnection);
                    text.setText(MusicService.sname + " is playing");
                    timer.cancel();
                    m_Progbar.setProgress(0);

//						timer =new Timer();
//						 timer.schedule(timerTask, 1, 1);
                    musicBound = false;
                }
                //button enable,disable set
                playbutton.setEnabled(true);
                playbutton.setText("Play");
                pausebutton.setEnabled(true);
                stopbutton.setEnabled(false);
                prevbutton.setEnabled(true);
                nextbutton.setEnabled(true);
                loopbutton.setEnabled(true);
                break;

//        //Prev button
            case R.id.button4:
                if (musicBound) {

                    if (t_number > 0) {
                        musicSrv.stopMusic();
                        t_number = t_number - 1;

                        musicSrv.mp3_play(t_number);
                        text.setText(MusicService.sname + " is playing");
                    } else {
                        Toast.makeText(PlaybackUi.this,
                                "No previous mp3!", Toast.LENGTH_LONG).show();
                    }
                }
                break;
            case R.id.button5:
                if (musicBound) {
                    if (t_number < 3) {
                        musicSrv.stopMusic();
                        t_number = t_number + 1;

                        musicSrv.mp3_play(t_number);
                        text.setText(MusicService.sname + " is playing");
                    } else {
                        Toast.makeText(PlaybackUi.this,
                                "No next mp3!", Toast.LENGTH_LONG).show();
                    }
                }
                break;
            //loop button
            case R.id.button6:
                if (loopStart == false) {
                    loopStart = true;//start playing loop
                    loopbutton.setText("Loop On");
                    return;
                }

                if (loopStart == true) {
                    loopStart = false;//No playing loop
                    loopbutton.setText("Loop Off");
                    return;
                }
                break;

        }
    }
    // Landscape State Configuration
    @Override
    public void onConfigurationChanged(Configuration newConfigChange) {
        super.onConfigurationChanged(newConfigChange);


    }
}