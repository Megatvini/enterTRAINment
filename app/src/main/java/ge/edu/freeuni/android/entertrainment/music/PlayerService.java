package ge.edu.freeuni.android.entertrainment.music;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;

import java.io.IOException;

import ge.edu.freeuni.android.entertrainment.MainActivity;
import ge.edu.freeuni.android.entertrainment.MainApplication;
import ge.edu.freeuni.android.entertrainment.R;


public class PlayerService extends Service  implements MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener, AudioManager.OnAudioFocusChangeListener {
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_START = "ge.edu.freeuni.android.entertrainment.music.action.START";
    private static final String ACTION_STOP = "ge.edu.freeuni.android.entertrainment.music.action.STOP";

    private static final String PATH_KEY = "ge.edu.freeuni.android.entertrainment.music.action.path";
    public static final String UPDATE_PLAYING_KEY = "ge.edu.freeuni.android.entertrainment.music.action.update";
    public static final String SONG_NAME = "ge.edu.freeuni.android.entertrainment.music.action.song.name";
    private String songName;
    private final int NOTIFICATION_ID = 1000;
    private String path;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        System.out.println("service bind");
        return null;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        System.out.println("service started");
        if(ACTION_START.equals(intent.getAction())){
            String path = intent.getStringExtra(PATH_KEY);
            String songName = intent.getStringExtra(SONG_NAME);
            handleActionStart(path,songName);
        }else if(ACTION_STOP.equals(intent.getAction())){
            handleActionStop();
        }

        return super.onStartCommand(intent,flags,startId);
    }


    public static void startActionStart(Context context, String path, String name) {
        Intent intent = new Intent(context, PlayerService.class);
        intent.setAction(ACTION_START);
        intent.putExtra(PATH_KEY,path);
        intent.putExtra(SONG_NAME,name);
        context.startService(intent);
    }

    public static void startActionStop(Context context) {
        Intent intent = new Intent(context, PlayerService.class);
        intent.setAction(ACTION_STOP);
        context.startService(intent);

    }


    /**
     * @param songName
     * @param path
     */
    private void handleActionStart(String path, String songName) {
        this.path = path;
        MainApplication mainApplication = (MainApplication) getApplication();
        MediaPlayer mediaPlayer = mainApplication.getMediaPlayer();
        if (mediaPlayer==null){
            mediaPlayer = new MediaPlayer();
            mainApplication.setMediaPlayer(mediaPlayer);
            mediaPlayer.setOnPreparedListener(this);
            mediaPlayer.setOnErrorListener(this);
            mediaPlayer.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);

        }
        mainApplication.getWifiLock().acquire();
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(path);
            System.out.println(path);
            this.songName = songName;
            mediaPlayer.prepareAsync();
            System.out.println("prepare called");
        } catch (IOException e) {
            e.printStackTrace();
            mainApplication.getWifiLock().release();
        }
        catch (IllegalStateException e){
            e.printStackTrace();
            mediaPlayer.reset();
        }


    }
    private void handleActionStop() {
        MainApplication mainApplication = (MainApplication) getApplication();
        MediaPlayer mediaPlayer = mainApplication.getMediaPlayer();
        if (mediaPlayer==null){
            mediaPlayer = new MediaPlayer();
            mainApplication.setMediaPlayer(mediaPlayer);
        }
        if (mediaPlayer.isPlaying()){
            mediaPlayer.stop();
            mediaPlayer.reset();
        }
        mainApplication.getWifiLock().release();
        stopForeground(true);
        updateActivity(false);

    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        int result = audioManager.requestAudioFocus(this, AudioManager.STREAM_MUSIC,
                AudioManager.AUDIOFOCUS_GAIN);
        if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            mediaPlayer.start();
            startForegroundWrapper();
            updateActivity(true);
        }
    }

    private void startForegroundWrapper() {
        String songName = this.songName;
        PendingIntent pi = PendingIntent.getActivity(getApplicationContext(), 0,
                new Intent(getApplicationContext(), MainActivity.class),
                PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.icon_music)
                .setContentTitle("Playing")
                .setContentText(this.songName)
                .setContentIntent(pi).build();

        startForeground(NOTIFICATION_ID, notification);
    }

    private void updateActivity(boolean playing){
        Intent intent = new Intent(UPDATE_PLAYING_KEY);
        ((MainApplication) getApplication()).setPlaying(playing);
        getApplicationContext().sendBroadcast(intent);
    }

    @Override
    public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
        System.out.println("error");
        stopForeground(true);
        updateActivity(false);
        return false;
    }

    public void onAudioFocusChange(int focusChange) {
        MainApplication mainApplication = (MainApplication) getApplication();
        MediaPlayer mMediaPlayer = mainApplication.getMediaPlayer();

        switch (focusChange) {
            case AudioManager.AUDIOFOCUS_GAIN:

                if (mMediaPlayer == null) {
                    mMediaPlayer = new MediaPlayer();
                    mainApplication.setMediaPlayer(mMediaPlayer);
                } else if (!mMediaPlayer.isPlaying()) {
                    mMediaPlayer.reset();
                    try {
                        mMediaPlayer.setDataSource(path);
                        mMediaPlayer.prepareAsync();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                mMediaPlayer.setVolume(1.0f, 1.0f);
                break;

            case AudioManager.AUDIOFOCUS_LOSS:
                if (mMediaPlayer.isPlaying()) mMediaPlayer.stop();
                mMediaPlayer.release();
                mMediaPlayer = null;
                break;

            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                if (mMediaPlayer.isPlaying()) mMediaPlayer.setVolume(0.1f, 0.1f);
                break;

            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:

                if (mMediaPlayer.isPlaying()) mMediaPlayer.setVolume(0.1f, 0.1f);
                break;
        }
    }
}
