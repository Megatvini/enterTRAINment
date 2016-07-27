package ge.edu.freeuni.android.entertrainment.chat.model;

import android.util.Log;

import com.koushikdutta.async.future.Future;
import com.koushikdutta.async.http.WebSocket;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Nika Doghonadze.
 */
public class ChatDataSource {
    protected Future<WebSocket> webSocketFuture;
    protected Set<ChatUpdateListener> listeners;

    public ChatDataSource() {
        this.listeners = new HashSet<>();
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

    protected void notifyListenersConnectionClosed() {
        for (ChatUpdateListener listener : listeners) {
            listener.connectionClosed();
        }
    }

    protected void notifyListeners(String s) {
        ChatEntry chatEntry = ChatEntry.fromJson(s);
        for (ChatUpdateListener listener : listeners) {
            listener.messageReceived(chatEntry);
        }
    }

    protected void notifyListeners() {
        for (ChatUpdateListener listener : listeners) {
            listener.noMessages();
        }
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
        if (webSocketFuture == null)
            return;
        WebSocket webSocket = webSocketFuture.tryGet();
        if (webSocket != null)
            webSocket.close();
    }

    public WebSocket getWebSocket() {
        if (webSocketFuture == null)
            return null;
        return webSocketFuture.tryGet();
    }
}
