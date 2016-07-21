package ge.edu.freeuni.android.entertrainment.music.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MusicProvider {

    public static final List<Song> ITEMS = new ArrayList<Song>();


    public static final Map<String, Song> ITEM_MAP = new HashMap<String, Song>();

    private static final int COUNT = 25;

    static {
        for (int i = 1; i <= COUNT; i++) {
            addItem(createDummyItem(i));
        }
    }

    private static void addItem(Song item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    private static Song createDummyItem(int position) {
        return new Song(String.valueOf(position), "Item " + position, makeDetails(position));
    }

    private static String makeDetails(int position) {
        StringBuilder builder = new StringBuilder();
        builder.append("Details about Item: ").append(position);
        for (int i = 0; i < position; i++) {
            builder.append("\nMore details information here.");
        }
        return builder.toString();
    }

    public static class Song {
        public final String id;
        public final String content;
        public final String details;

        public Song(String id, String content, String details) {
            this.id = id;
            this.content = content;
            this.details = details;
        }

        @Override
        public String toString() {
            return content;
        }
    }
}
