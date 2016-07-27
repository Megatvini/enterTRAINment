package ge.edu.freeuni.android.entertrainment.chat;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import ge.edu.freeuni.android.entertrainment.music.data.Song;

import static ge.edu.freeuni.android.entertrainment.chat.Constants.RANDOM_ID_KEY;

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

    public static void initRandomId(Context context){
        if(getRandomId(context)== null){
            saveRandomId(context);
        }
    }

    @SuppressLint("CommitPrefEdits")
    public static void saveRandomId(Context context) {
        SharedPreferences sharedPreferences =
                context.getSharedPreferences(Constants.SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString(Constants.RANDOM_ID_KEY, UUID.randomUUID().toString());
        edit.commit();
    }

    public static String getRandomId(Context context) {
        SharedPreferences sharedPreferences =
                context.getSharedPreferences(Constants.SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE);
        return sharedPreferences.getString(RANDOM_ID_KEY, null);
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
        try {
            parseJson(response, length, songList);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return songList;
    }

    private static void parseJson(JSONArray response, int length, List<Song> songList) throws JSONException {
        for (int i = 0; i < length; i++) {
            JSONObject musicJson = (JSONObject) response.get(i);
            Song song = new Song(musicJson.getString("voted"),musicJson.getString("id"), musicJson.getString("name"), musicJson.getInt("rating"), musicJson.getString("imagePath"));
            songList.add(song);
        }
    }

    public static void saveReadingPage(Context context, String url, int page) {
        SharedPreferences sharedPreferences =
                context.getSharedPreferences(Constants.SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putInt(Constants.READING_PAGE_KEY + url, page);
        edit.apply();
    }


    public static int readReadingPage(Context context, String url) {
        if (context == null)
            return 1;

        SharedPreferences sharedPreferences =
                context.getSharedPreferences(Constants.SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(Constants.READING_PAGE_KEY + url, 1);
    }

    public static void shareStringData(Context context, String data, String title) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, data);
        sendIntent.setType("text/plain");
        context.startActivity(Intent.createChooser(sendIntent, title));
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }


    public static void animateViewScale(final ImageView imageView) {
        imageView.clearAnimation();
        imageView.animate()
                .scaleX(0.3f)
                .scaleY(0.3f)
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {
                        imageView.animate()
                                .scaleX(1f)
                                .scaleY(1f)
                                .start();
                    }

                    @Override
                    public void onAnimationCancel(Animator animator) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animator) {

                    }
                })
                .start();
    }
}
