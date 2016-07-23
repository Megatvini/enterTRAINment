package ge.edu.freeuni.android.entertrainment.chat;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;

import org.json.JSONException;
import org.json.JSONObject;

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
        edit.apply();
    }
}
