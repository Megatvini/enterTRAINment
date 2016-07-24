package ge.edu.freeuni.android.entertrainment.music.data;


import android.support.annotation.NonNull;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

import static ge.edu.freeuni.android.entertrainment.chat.Utils.getSongsFromJsonArray;

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
        List<Song> songList = getSongsFromJsonArray(response);
        if(songList.size() != 0)
            PlaylistClient.this.musicProvider.onNewPlayListData(songList);
    }

    public void downvote(String id, String downvote){

    }


}
