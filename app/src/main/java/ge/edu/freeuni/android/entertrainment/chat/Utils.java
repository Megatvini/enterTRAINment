package ge.edu.freeuni.android.entertrainment.chat;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ge.edu.freeuni.android.entertrainment.music.data.Song;

/**
 * Created by Nika Doghonadze.
 */
public class Utils {
    public static String readUsernameFromPreferences(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE);
        return sharedPreferences.getString(Constants.USERNAME_KEY, null);
    }


    public static void runInMain(Runnable runnable) {
        if (Looper.myLooper() != Looper.getMainLooper()) {
            Handler mainHandler = new Handler(Looper.getMainLooper());
            mainHandler.post(runnable);
        } else {
            runnable.run();
        }
    }

    public static void runInMain2 (Context context, Runnable runnable){
        Handler mainHandler = new Handler(context.getMainLooper());

        mainHandler.post(runnable);
    }

    public static void saveUsernameInSharedPreferences(Context context, JSONObject response) {
        try {
            String username = response.getString("username");
            saveUsernameInSharedPreferences(context, username);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void saveUsernameInSharedPreferences(Context context, String username) {
        SharedPreferences sharedPreferences =
                context.getSharedPreferences(Constants.SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString(Constants.USERNAME_KEY, username);
        edit.commit();
    }


    @NonNull
    public static List<Song> getSongsFromJsonArray(JSONArray response) {
        int length = response.length();
        List<Song> songList = new ArrayList<>();
        if (length >=1) {
            for (int i = 0; i < length; i++) {
                try {
                    JSONObject musicJson = (JSONObject) response.get(i);
                    Song song = new Song(musicJson.getString("voted"),musicJson.getString("id"), musicJson.getString("name"), musicJson.getInt("rating"), musicJson.getString("imagePath"));
                    System.out.println(song.getRating());
                    songList.add(song);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }


        }
        return songList;
    }
}
