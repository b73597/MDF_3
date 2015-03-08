// Eddy Davila
// MDF 3
// Full Sail University
// Week 1


package com.edavila.mediaplayback;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import java.util.ArrayList;

public class MusicService extends Service implements MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener,
        MediaPlayer.OnCompletionListener {

    private final IBinder mBinder = new ServiceBinder();
    public MediaPlayer mPlayer;
    public int length = 0;
    public static String sname = "No song";
    //    public int currentposition;//playing position
    public boolean isPasued;
    public ArrayList<String> mp3list;//mp3 list in the assets
    public int index_mp3;//current mp3 index
    public int mp3_num;//all mp3 number

    public MusicService() {
    }

    public class ServiceBinder extends Binder {
        MusicService getService() {
            return MusicService.this;
        }
    }


    @Override
    public IBinder onBind(Intent arg0) {
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //       currentposition = 0;
        index_mp3 = 0;
        length = 0;
        //making mp3 list
        mp3list = new ArrayList<String>();//array init
        mp3list.add("black_banquet");//adding data
        mp3list.add("castlevania_prologue");//adding data
        mp3list.add("young_lust");//adding data
        mp3list.add("barriers");//adding data



        mp3_num = 4;
        mPlayer = new MediaPlayer();
        //       mp3_play();
        mPlayer.setOnPreparedListener(this);
        mPlayer.setOnCompletionListener(this);
        mPlayer.setOnErrorListener(this);
        //       mp3_play(index_mp3);
        initMusicPlayer();

    }

    public void setList(ArrayList<String> list, int pos) {
        //    	mp3list= list;
        //    	mp3_play(pos);
        //    	startMusic();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //         mPlayer.start();
        //         createNotification();
        //    	 mp3_play();
        //    	 mPlayer.start();
        return START_STICKY;
    }

    public void initMusicPlayer() {
        //set player properties
        if (mPlayer != null) {
            mPlayer.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
            mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mPlayer.setVolume(100, 100);
        }
    }

    public void mp3_play(int pos) {
        //mPlayer.reset();
        index_mp3 = pos;
        //For playing , init process
        int index = pos;
        if (index == 0) {
            mPlayer = MediaPlayer.create(this, Uri.parse("android.resource://" + getPackageName() + "/raw/mother"));
            sname = "mother";
            startMusic();
       }
        if (index == 1) {
            mPlayer = MediaPlayer.create(this, Uri.parse("android.resource://" + getPackageName() + "/raw/castlevania_prologue"));
            sname = "castlevania_prologue";
            startMusic();
        }
        if (index == 2) {
            mPlayer = MediaPlayer.create(this, Uri.parse("android.resource://" + getPackageName() + "/raw/young_lust"));
            sname = "young_lust";
            startMusic();
        }
        if (index == 3) {
            mPlayer = MediaPlayer.create(this, Uri.parse("android.resource://" + getPackageName() + "/raw/barriers"));
            sname = "barriers";
            startMusic();
        }

//        mPlayer.setLooping(true);
        //startMusic();
        //		mPlayer.setDataSource(this, Uri.parse("android.resource://" + "com.edavila.mediaplayback" + "/raw/"+mp3list.get(pos)));
        //		mPlayer.prepare();//For playing, ready!
    }

    public void startMusic() {
        if (mPlayer != null)
            mPlayer.start();//Play
        createNotification();
        //	if(mPlayer.getCurrentPosition() == mPlayer.getDuration()){
        //		nextSong();
        //	}
        //		}
    }

    public void pauseMusic() {
        if (mPlayer.isPlaying()) {
            mPlayer.pause();
            isPasued = true;
            length = mPlayer.getCurrentPosition();

        }
    }

    public void resumeMusic() {
        if (mPlayer.isPlaying() == false && isPasued) {
            mPlayer.start();
            isPasued = false;
        }
    }

    /*public void nextSong(){
        if(mPlayer.getCurrentPosition() == mPlayer.getDuration() && index_mp3< mp3list.size()){
            index_mp3 ++;
            mp3_play(index_mp3);
        }
        else if(mPlayer.getCurrentPosition() == mPlayer.getDuration() && index_mp3== mp3list.size()-1){
            index_mp3=0;
            mp3_play(index_mp3);
        }

    }*/
    public void stopMusic() {
        mPlayer.stop();
        length = 0;

    }

    public void createNotification() {

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle("Playing...")
                        .setContentText(sname)
                        .setAutoCancel(true);

        Intent resultIntent = new Intent(this, PlaybackUi.class);

        // Because clicking the notification opens a new ("special") activity, there's
        // no need to create an artificial back stack.
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        this,
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        int mNotificationId = 001;
        // Gets an instance of the NotificationManager service
        NotificationManager mNotifyMgr =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        // Builds the notification and issues it.
        mNotifyMgr.notify(mNotificationId, mBuilder.build());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPlayer != null) {
            try {
                mPlayer.stop();
                mPlayer.release();
            } finally {
                mPlayer = null;
            }
        }
    }

    public boolean onError(MediaPlayer mp, int what, int extra) {

        Toast.makeText(this, "music player failed", Toast.LENGTH_SHORT).show();
        if (mPlayer != null) {
            try {
                mPlayer.stop();
                mPlayer.release();
            } finally {
                mPlayer = null;
            }
        }
        return false;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {

    }

    @Override
    public void onPrepared(MediaPlayer mp) {

    }
}
