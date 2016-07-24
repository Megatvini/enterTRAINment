package ge.edu.freeuni.android.entertrainment.music;


import android.util.Log;

import com.koushikdutta.async.callback.CompletedCallback;
import com.koushikdutta.async.future.Future;
import com.koushikdutta.async.http.AsyncHttpClient;
import com.koushikdutta.async.http.WebSocket;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.List;

import ge.edu.freeuni.android.entertrainment.chat.Constants;
import ge.edu.freeuni.android.entertrainment.chat.Utils;
import ge.edu.freeuni.android.entertrainment.music.data.MusicProvider;
import ge.edu.freeuni.android.entertrainment.music.data.Song;

public class SharedMusicService  {
    protected Future<WebSocket> webSocketFuture;
    protected SharedMusicFragment listener;
    private MusicProvider musicProvider;


    public SharedMusicService(MusicProvider musicProvider){
        this.musicProvider = musicProvider;
        initWebsocketConnection();
    }

    private void initWebsocketConnection() {
        String url = "ws://" + Constants.HOST_IP + "/webapi/song_socket";
        webSocketFuture = AsyncHttpClient.getDefaultInstance()
                .websocket(url, "my-protocol", new AsyncHttpClient.WebSocketConnectCallback() {
                    @Override
                    public void onCompleted(Exception ex, WebSocket webSocket) {
                        if (ex != null) {
                            ex.printStackTrace();
                            return;
                        }
                        webSocket.setStringCallback(new WebSocket.StringCallback() {
                            @Override
                            public void onStringAvailable(final String s) {
                                Utils.runInMain(new Runnable() {
                                    @Override
                                    public void run() {
                                        notifyListeners(s);
                                    }
                                });
                            }
                        });

                        webSocket.setClosedCallback(new CompletedCallback() {
                            @Override
                            public void onCompleted(Exception ex) {
                                if (ex != null)
                                    ex.printStackTrace();
                                Log.d("Websocket", "has closed");
                                notifyListenersConnectionClosed();
                            }
                        });
                    }
                });
    }

    private void notifyListenersConnectionClosed() {
        listener.connectionClosed();
    }

    public void notifyListeners(String s) {
        try {
            JSONArray  jsonArray= new JSONArray(s);
            List<Song> songs = Utils.getSongsFromJsonArray(jsonArray);
            musicProvider.onNewPlayListData(songs);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void setListener(SharedMusicFragment listener) {
        this.listener = listener;
    }
}
