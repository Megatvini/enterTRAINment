package ge.edu.freeuni.android.entertrainment.music.data;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import ge.edu.freeuni.android.entertrainment.music.OfferedMusicRecyclerViewAdapter;
import ge.edu.freeuni.android.entertrainment.music.SharedMusicRecyclerViewAdapter;
import ge.edu.freeuni.android.entertrainment.music.VoteListener;

public class MusicProvider implements  VoteListener {

    private PlaylistClient playlistClient;
    private String path;

    private SharedMusicRecyclerViewAdapter sharedAdapter;
    private OfferedMusicRecyclerViewAdapter offeredAdapter;

    private List<Song> songs = new ArrayList<>();

    public MusicProvider(Context context, String path){
        this.path = path;
        playlistClient = new PlaylistClient(this);
    }


    public void loadData(){
        playlistClient.loadPlaylist(path);
    }

    public void setSharedAdapter(SharedMusicRecyclerViewAdapter adapter) {
        this.sharedAdapter = adapter;
    }

    public void setOfferedAdapter(OfferedMusicRecyclerViewAdapter offeredAdapter) {
        this.offeredAdapter = offeredAdapter;
    }

    public List<Song> getSongs() {
        return songs;
    }



    @Override
    public void upvote(String id) {
        this.playlistClient.upvote(path+"/"+id+"/upvote");
    }

    @Override
    public void downvote(String id) {
        this.playlistClient.downvote(path+"/"+id+"/downvote");
    }


    public void onNewPlayListData(List<Song> songs){
        if(this.sharedAdapter != null) {
            this.sharedAdapter.setData(songs);
            this.sharedAdapter.notifyDataSetChanged();
        }
        if(this.offeredAdapter != null) {
            this.offeredAdapter.setData(songs);
            this.offeredAdapter.notifyDataSetChanged();
        }
    }


}
