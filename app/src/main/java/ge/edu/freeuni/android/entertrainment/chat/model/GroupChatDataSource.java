package ge.edu.freeuni.android.entertrainment.chat.model;

import android.util.Log;

import com.koushikdutta.async.callback.CompletedCallback;
import com.koushikdutta.async.future.Future;
import com.koushikdutta.async.http.AsyncHttpClient;
import com.koushikdutta.async.http.WebSocket;

import java.util.HashSet;
import java.util.Set;

import ge.edu.freeuni.android.entertrainment.chat.Constants;
import ge.edu.freeuni.android.entertrainment.chat.Utils;

/**
 * Created by Nika Doghonadze.
 */
public class GroupChatDataSource {
    private Set<ChatUpdateListener> listeners;
    Future<WebSocket> webSocketFuture;

    public GroupChatDataSource() {
        listeners = new HashSet<>();
        initWebsocketConnection();
    }

    private void initWebsocketConnection() {
        String url = "ws://" + Constants.SERVER_ADDRESS + "/webapi/groupchat";
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
                                if (!s.equals("no data")) {
                                    notifyListeners(s);
                                } else {
                                    notifyListeners();
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
                        notifyListenersConnectionClosed();
                    }
                });
            }
        });
    }

    private void notifyListenersConnectionClosed() {
        for (ChatUpdateListener listener : listeners) {
            listener.connectionClosed();
        }
    }

    private void notifyListeners(String s) {
        ChatEntry chatEntry = ChatEntry.fromJson(s);
        for (ChatUpdateListener listener : listeners) {
            listener.messageReceived(chatEntry);
        }
    }

    private void notifyListeners() {
        for (ChatUpdateListener listener : listeners) {
            listener.noMessages();
        }
    }

    public void registerListener(ChatUpdateListener listener) {
        listeners.add(listener);
    }

    public void removeListener(ChatUpdateListener listener) {
        listeners.remove(listener);
    }

    public void clearListeners() {
        listeners.clear();
    }

    public void sendMessage(ChatEntry entry) {
        WebSocket webSocket = webSocketFuture.tryGet();
        if (webSocket != null) {
            webSocket.send(entry.toString());
        } else {
            Log.d("WebSocket", "is null");
        }
    }


    public void closeConnection() {
        WebSocket webSocket = webSocketFuture.tryGet();
        if (webSocket != null)
            webSocket.close();
    }
}
