package ge.edu.freeuni.android.entertrainment;

import android.app.Application;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.wifi.WifiManager;


public class MainApplication extends Application {

    private MediaPlayer mediaPlayer;

    private boolean isPlaying = false;

    private WifiManager.WifiLock wifiLock = null;

    public boolean isPlaying() {

        return isPlaying;
    }

    public void setPlaying(boolean playing) {
        isPlaying = playing;
    }

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    public void setMediaPlayer(MediaPlayer mediaPlayer) {
        this.mediaPlayer = mediaPlayer;
    }

    public WifiManager.WifiLock getWifiLock() {
        if (wifiLock == null){
            wifiLock = ((WifiManager) getSystemService(Context.WIFI_SERVICE))
                    .createWifiLock(WifiManager.WIFI_MODE_FULL, "mylock");
        }
        return wifiLock;
    }

    public void setWifiLock(WifiManager.WifiLock wifiLock) {
        this.wifiLock = wifiLock;
    }
}
