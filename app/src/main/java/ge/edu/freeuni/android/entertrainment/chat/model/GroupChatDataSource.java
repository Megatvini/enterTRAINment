package ge.edu.freeuni.android.entertrainment.chat.model;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.koushikdutta.async.future.Future;
import com.koushikdutta.async.http.AsyncHttpClient;
import com.koushikdutta.async.http.WebSocket;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Nika Doghonadze.
 */
public class GroupChatDataSource {
    private Set<ChatUpdateListener> listeners;
    Future<WebSocket> webSocketFuture;

    public GroupChatDataSource() {
        listeners = new HashSet<>();

        String url = "ws://entertrainment.herokuapp.com/webapi/groupchat";
//        url = "ws://192.168.76.224:8080/webapi/groupchat";
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
                        notifyListenersFromMainThread(s);
                    }
                });
            }
        });

    }

    private void notifyListenersFromMainThread(final String s) {
        if (Looper.myLooper() != Looper.getMainLooper()) {
            Handler mainHandler = new Handler(Looper.getMainLooper());
            mainHandler.post(new Runnable() {
                @Override
                public void run() {
                    notifyListeners(s);
                }
            });
        } else {
            notifyListeners(s);
        }
    }

    private void notifyListeners(String s) {
        ChatEntry chatEntry = ChatEntry.fromJson(s);
        for (ChatUpdateListener listener : listeners) {
            listener.messageReceived(chatEntry);
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
