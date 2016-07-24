package ge.edu.freeuni.android.entertrainment.music.data;


import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import io.realm.Realm;

public class PlaylistClient {

    private MusicProvider musicProvider;

    public PlaylistClient(MusicProvider musicProvider){

        this.musicProvider = musicProvider;
    }


    public void loadPlaylist(String path){
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        asyncHttpClient.get(path,null,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                handleNewPlaylistData(response);
            }
        });

    }

    public void upvote(String path){
        System.out.println(path);
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        asyncHttpClient.post(path,null,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                handleNewPlaylistData(response);
            }
        });
    }

    private void handleNewPlaylistData(JSONArray response) {
        int length = response.length();
        if (length >=1) {
            List<Song> songList = new ArrayList<>();
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
            PlaylistClient.this.musicProvider.onNewPlayListData(songList);

        }
    }

    public void downvote(String id, String downvote){

    }


}
