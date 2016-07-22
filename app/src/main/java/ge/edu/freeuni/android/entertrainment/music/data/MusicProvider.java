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
        return new Song(position+"",position+"",0,"");
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
        public final String name;
        public final int rating;
        public final String image;

        public Song(String id, String name, int rating, String image) {
            this.id = id;
            this.name = name;
            this.rating = rating;
            this.image = image;
        }
    }
}
