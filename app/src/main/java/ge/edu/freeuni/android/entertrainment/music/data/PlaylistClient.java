package ge.edu.freeuni.android.entertrainment.music.data;


import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import io.realm.Realm;

public class PlaylistClient {


    public void loadPlaylist(final Realm realm, String path){

        System.out.println(path);
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        asyncHttpClient.get(path,null,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                System.out.println("started loading ");

                int length = response.length();
                if (length >=1) {
                    realm.beginTransaction();
                    realm.where(Song.class).findAll().deleteAllFromRealm();
                    for (int i = 0; i < length; i++) {
                        try {
                            JSONObject musicJson = (JSONObject) response.get(i);
                            Song song = new Song(musicJson.getString("voted"),musicJson.getString("id"), musicJson.getString("name"), musicJson.getInt("rating"), musicJson.getString("imagePath"));
                            Song song1 = realm.createObject(Song.class);
                            song1.setId(song.getId());
                            song1.setVoted(song.isVoted());
                            song1.setImage(song.getImage());
                            song1.setName(song.getName());
                            song1.setRating(song.getRating());
                            System.out.println("new song");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    realm.commitTransaction();
                }
            }
        });

    }

    public void upvote(final Realm realm, String path){
        System.out.println(path);
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        asyncHttpClient.post(path,null,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                System.out.println("started loading ");

                int length = response.length();
                if (length >=1) {

                    realm.beginTransaction();
                    realm.where(Song.class).findAll().deleteAllFromRealm();
                    for (int i = 0; i < length; i++) {
                        try {
                            JSONObject musicJson = (JSONObject) response.get(i);
                            Song song = new Song(musicJson.getString("voted"),musicJson.getString("id"), musicJson.getString("name"), musicJson.getInt("rating"), musicJson.getString("imagePath"));
                            Song song1 = realm.createObject(Song.class);
                            song1.setId(song.getId());
                            song1.setVoted(song.isVoted());
                            song1.setImage(song.getImage());
                            song1.setName(song.getName());
                            song1.setRating(song.getRating());
                            System.out.println("new song");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    realm.commitTransaction();
                }
            }
        });
    }

    public void downvote(String id, String downvote){

    }


}
