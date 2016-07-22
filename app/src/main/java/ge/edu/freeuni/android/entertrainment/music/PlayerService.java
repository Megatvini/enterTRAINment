package ge.edu.freeuni.android.entertrainment.music;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.Nullable;

import java.io.IOException;

import ge.edu.freeuni.android.entertrainment.MainApplication;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * helper methods.
 */
public class PlayerService extends Service  implements MediaPlayer.OnPreparedListener{
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_START = "ge.edu.freeuni.android.entertrainment.music.action.START";
    private static final String ACTION_STOP = "ge.edu.freeuni.android.entertrainment.music.action.STOP";

    private static final String PATH_KEY = "ge.edu.freeuni.android.entertrainment.music.action.path";



    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        System.out.println("service bind");
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        System.out.println("service started");
        if(ACTION_START.equals(intent.getAction())){
            String path = intent.getStringExtra(PATH_KEY);
            handleActionStart(path);
        }else if(ACTION_STOP.equals(intent.getAction())){
            handleActionStop();
        }
        return super.onStartCommand(intent,flags,startId);
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    public static void startActionStart(Context context, String path) {
        Intent intent = new Intent(context, PlayerService.class);
        intent.setAction(ACTION_START);
        intent.putExtra(PATH_KEY,path);
        System.out.println("starting service");
        context.startService(intent);
    }

    /**
     * Starts this service to perform action Baz with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    public static void startActionStop(Context context) {
        Intent intent = new Intent(context, PlayerService.class);
        intent.setAction(ACTION_STOP);
        context.startService(intent);
    }


    /**
     * @param path
     */
    private void handleActionStart(String path) {
        System.out.println("onstart called");
        MainApplication mainApplication = (MainApplication) getApplication();
        MediaPlayer mediaPlayer = mainApplication.getMediaPlayer();
        if (mediaPlayer==null){
            mediaPlayer = new MediaPlayer();
            mainApplication.setMediaPlayer(mediaPlayer);
            mediaPlayer.setOnPreparedListener(this);
            mediaPlayer.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);

        }


        mainApplication.getWifiLock().acquire();
        try {
            mediaPlayer.setDataSource(path);
            System.out.println(path);
            mediaPlayer.prepareAsync();
            System.out.println("prepare called");
        } catch (IOException e) {
            e.printStackTrace();
            mainApplication.getWifiLock().acquire();
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

    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        System.out.println("on prepared");
        mediaPlayer.start();
    }
}
