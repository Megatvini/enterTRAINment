package ge.edu.freeuni.android.entertrainment.movie;


import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import cz.msebera.android.httpclient.Header;
import ge.edu.freeuni.android.entertrainment.music.data.Song;

import static ge.edu.freeuni.android.entertrainment.chat.Utils.getSongsFromJsonArray;

public class MovieClient {

    private MovieProvider provider;

    public MovieClient(MovieProvider provider){

        this.provider = provider;
    }
    public void loadPlaylist(String path){
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        asyncHttpClient.get(path,null,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                handleNewPlaylistData(response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);

            }

        });

    }


    private void handleNewPlaylistData(JSONArray response) {
        List<Song> songList = getSongsFromJsonArray(response);
        if(songList.size() == 0 )
            return;
        if(songList.size() != 0)
            MovieClient.this.provider.onNewPlayListData(songList);
    }
}
