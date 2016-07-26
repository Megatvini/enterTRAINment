package ge.edu.freeuni.android.entertrainment.music;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.session.MediaController;
import android.media.session.MediaSession;
import android.media.session.MediaSessionManager;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.NotificationCompat;

import java.io.IOException;

import ge.edu.freeuni.android.entertrainment.MainActivity;
import ge.edu.freeuni.android.entertrainment.MainApplication;
import ge.edu.freeuni.android.entertrainment.R;


public class PlayerService extends Service  implements MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener, AudioManager.OnAudioFocusChangeListener {
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_START = "ge.edu.freeuni.android.entertrainment.music.action.START";
    private static final String ACTION_STOP = "ge.edu.freeuni.android.entertrainment.music.action.STOP";
    private static final String ACTION_PAUSE = "ge.edu.freeuni.android.entertrainment.music.action.PAUSE";

    private static final String PATH_KEY = "ge.edu.freeuni.android.entertrainment.music.action.path";
    public static final String UPDATE_PLAYING_KEY = "ge.edu.freeuni.android.entertrainment.music.action.update";
    public static final String SONG_NAME = "ge.edu.freeuni.android.entertrainment.music.action.song.name";
    private String songName;
    private final int NOTIFICATION_ID = 1000;
    private String path;

    private MediaPlayer mMediaPlayer;
    private MediaSessionManager mManager;
    private MediaSession mSession;
    private MediaController mController;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        System.out.println("service bind");
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mMediaPlayer = new MediaPlayer();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mManager = (MediaSessionManager) getSystemService(Context.MEDIA_SESSION_SERVICE);
            mSession = new MediaSession(getApplicationContext(), "Tag");
            mController = mSession.getController();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent == null || intent.getAction() == null)
            return 1;

        path = intent.getStringExtra(PATH_KEY);
        songName = intent.getStringExtra(SONG_NAME);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mSession.setCallback(new MediaSession.Callback() {
                @Override
                public void onPlay() {
                    super.onPlay();
                    buildNotification(generateAction(android.R.drawable.ic_media_pause, "Pause", ACTION_PAUSE, path, songName));
                }

                @Override
                public void onPause() {
                    super.onPause();
                    buildNotification(generateAction(android.R.drawable.ic_media_play, "Play", ACTION_START, path, songName));
                }

                @Override
                public void onStop() {
                    super.onStop();
                    buildNotification(generateAction(android.R.drawable.ic_media_play, "Play", ACTION_START, path,songName));
                }
            });
        }
        String action = intent.getAction();

        if (action.equalsIgnoreCase(ACTION_START)) {
            handleActionStart(path, songName);
        } else if (action.equalsIgnoreCase(ACTION_STOP)) {
            stop();
        }
        else if (action.equalsIgnoreCase(ACTION_PAUSE)) {
            pause();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private void buildNotification(Notification.Action action ) {
        Notification.MediaStyle style = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            style = new Notification.MediaStyle();
            Intent intent = new Intent(getApplicationContext(), PlayerService.class);
            intent.setAction(ACTION_STOP);
            PendingIntent pendingIntent = PendingIntent.getService(getApplicationContext(), 1, intent, 0);
            String[] split = songName.split("-");
            String title;
            String artist = "unknown";
            if(split.length > 1){
                title = split[0];
                artist = split[1];
            }else {
                title = songName;
            }
            Notification.Builder builder = new Notification.Builder(this)
                    .setSmallIcon(R.drawable.ic_audiotrack_black_24dp)
                    .setVisibility(Notification.VISIBILITY_PUBLIC)
                    .setContentTitle(title)
                    .setContentText(artist)
                    .setDeleteIntent(pendingIntent)
                    .setStyle(style.setShowActionsInCompactView(0));

            builder.addAction( action );

            NotificationManager notificationManager = (NotificationManager) getSystemService( Context.NOTIFICATION_SERVICE );
            notificationManager.notify( NOTIFICATION_ID, builder.build() );
        }else {
            startForegroundWrapper();
        }


    }


    private Notification.Action generateAction(int icon, String title, String intentAction, String path, String songName) {
        Intent intent = new Intent( getApplicationContext(), PlayerService.class );
        intent.setAction( intentAction );
        if(path != null){
            intent.putExtra(PATH_KEY,path);
        }
        if(songName!=null){
            intent.putExtra(SONG_NAME,songName);
        }
        PendingIntent pendingIntent = PendingIntent.getService(getApplicationContext(), 1, intent, 0);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
            return new Notification.Action.Builder( icon, title, pendingIntent )
                    .build();
        }
        return null;
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

    private MediaPlayer initPlayer(){
        MainApplication mainApplication = (MainApplication) getApplication();
        MediaPlayer mediaPlayer = mainApplication.getMediaPlayer();
        if (mediaPlayer==null){
            mediaPlayer = new MediaPlayer();
            mainApplication.setMediaPlayer(mediaPlayer);
            mediaPlayer.setOnPreparedListener(this);
            mediaPlayer.setOnErrorListener(this);
            mediaPlayer.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
        }
        return mediaPlayer;
    }

    /**
     * @param songName
     * @param path
     */
    private void handleActionStart(String path, String songName) {
        this.path = path;
        MainApplication mainApplication = (MainApplication) getApplication();
        MediaPlayer mediaPlayer = initPlayer();

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
    private void stop() {
        stopPlaying();
        NotificationManager notificationManager = (NotificationManager) getSystemService( Context.NOTIFICATION_SERVICE );
        notificationManager.cancel(NOTIFICATION_ID);
    }

    private void pause(){
        stopPlaying();
        pauseNotification();
    }


    private void pauseNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mController.getTransportControls().stop();
        }
    }

    private void stopPlaying() {
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

        WifiManager.WifiLock wifiLock = mainApplication.getWifiLock();
        if (wifiLock.isHeld())
            wifiLock.release();
        stopForeground(true);
        updateActivity(false);
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {

        System.out.println("onpreapared");
        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        int result = audioManager.requestAudioFocus(this, AudioManager.STREAM_MUSIC,
                AudioManager.AUDIOFOCUS_GAIN);

            mediaPlayer.start();
            //startForegroundWrapper();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mController.getTransportControls().play();
            }
            updateActivity(true);

    }


    private void startForegroundWrapper() {
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
                    handleActionStart(path,songName);
                }
                mMediaPlayer.setVolume(1.0f, 1.0f);
                break;

            case AudioManager.AUDIOFOCUS_LOSS:
                pause();

            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                if (mMediaPlayer.isPlaying()) mMediaPlayer.setVolume(0.1f, 0.1f);
                break;

            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:

                if (mMediaPlayer.isPlaying()) mMediaPlayer.setVolume(0.1f, 0.1f);
                break;
        }
    }


    @Override
    public boolean onUnbind(Intent intent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mSession.release();
        }
        return super.onUnbind(intent);
    }
}
