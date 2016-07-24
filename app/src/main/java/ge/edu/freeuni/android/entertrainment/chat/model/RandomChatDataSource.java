package ge.edu.freeuni.android.entertrainment.chat.model;

import android.util.Log;

import com.koushikdutta.async.callback.CompletedCallback;
import com.koushikdutta.async.http.AsyncHttpClient;
import com.koushikdutta.async.http.WebSocket;

import ge.edu.freeuni.android.entertrainment.chat.Constants;
import ge.edu.freeuni.android.entertrainment.chat.Utils;

/**
 * Created by Nika Doghonadze.
 */
public class RandomChatDataSource extends ChatDataSource {
    public RandomChatDataSource() {
        super();
    }

    public void initWebSocketConnection() {
        String url = "ws://" + Constants.SERVER_ADDRESS + "/webapi/randomchat";
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
                                        if (s.equals(Constants.PAIR_FOUND_CODE)) {
                                            notifyListenersPairFound();
                                        } else {
                                            notifyListeners(s);
                                        }
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
                                Utils.runInMain(new Runnable() {
                                    @Override
                                    public void run() {
                                        notifyListenersConnectionClosed();
                                    }
                                });
                            }
                        });
                    }
                });
    }

    private void notifyListenersPairFound() {
        for (ChatUpdateListener listener : listeners) {
            listener.connectionFound();
        }
    }
}
