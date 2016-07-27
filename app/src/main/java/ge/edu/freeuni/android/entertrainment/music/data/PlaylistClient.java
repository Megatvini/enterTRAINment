package ge.edu.freeuni.android.entertrainment.music.data;


import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import cz.msebera.android.httpclient.Header;
import ge.edu.freeuni.android.entertrainment.chat.Utils;

import static ge.edu.freeuni.android.entertrainment.chat.Utils.getRandomId;
import static ge.edu.freeuni.android.entertrainment.chat.Utils.getSongsFromJsonArray;

public class PlaylistClient {

    private MusicProvider musicProvider;

    public PlaylistClient(MusicProvider musicProvider){
        this.musicProvider = musicProvider;
    }


    public void loadPlaylist(String path){
        System.out.println(path);
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add("id", Utils.getRandomId(musicProvider.getContext()));
        asyncHttpClient.get(path,params,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                handleNewPlaylistData(response);
            }
        });

    }

    public void upvote(String path){
        vote(path);
    }

    public void vote(String path){
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        System.out.println(path);
        params.add("id",getRandomId(this.musicProvider.getContext()));
        asyncHttpClient.post(path, params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {

            }
        });
    }

    private void handleNewPlaylistData(JSONArray response) {
        List<Song> songList = getSongsFromJsonArray(response);
        if(songList.size() == 0 )
            return;
        if(songList.size() != 0)
            PlaylistClient.this.musicProvider.onNewPlayListData(songList);
    }

    public void downvote(String path){
        vote(path);
    }


}
