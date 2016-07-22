package ge.edu.freeuni.android.entertrainment.chat.model;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Nika Doghonadze.
 */
public class GroupChatDataSource {
    private Set<ChatUpdateListener> listeners;

    public GroupChatDataSource() {
        listeners = new HashSet<>();
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

    }

}
