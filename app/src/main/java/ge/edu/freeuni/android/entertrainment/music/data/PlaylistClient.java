package ge.edu.freeuni.android.entertrainment.music.data;


import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;

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
        vote(path);
    }
    public void vote(String path){
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        asyncHttpClient.post(path, null, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                handleNewPlaylistData(response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }

    private void handleNewPlaylistData(JSONArray response) {
        List<Song> songList = getSongsFromJsonArray(response);
        if(songList.size() == 0 )
            return;
        String voted = songList.get(0).getVoted();
        System.out.println("souted : "+ voted);
        if(songList.size() != 0)
            PlaylistClient.this.musicProvider.onNewPlayListData(songList);
    }

    public void downvote(String path){
        vote(path);
    }


}
