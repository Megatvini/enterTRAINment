package ge.edu.freeuni.android.entertrainment.chat.model;

import android.icu.util.Calendar;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Nika Doghonadze.
 */
public class ChatEntry {
    private String username;
    private String text;
    private long timestamp;

    public ChatEntry(String username, String text, long timestamp) {
        this.username = username;
        this.text = text;
        this.timestamp = timestamp;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getTimeText() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:SS");
        Date date = new Date(timestamp);
        return simpleDateFormat.format(date);
    }
}
