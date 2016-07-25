package ge.edu.freeuni.android.entertrainment.movie;

import java.util.ArrayList;
import java.util.List;

import ge.edu.freeuni.android.entertrainment.music.data.Song;

import static ge.edu.freeuni.android.entertrainment.chat.Constants.HOST;


public class MovieProvider {

    private static final String PATH = HOST+"/webapi/movies";

    private MovieRecyclerViewAdapter recyclerViewAdapter;
    private MovieClient client;
    private List<Song> songs;

    public MovieProvider(){
        this.songs = new ArrayList<>();
    }

    public List<Song> getMovies() {
        return songs;
    }

    public void setRecyclerViewAdapter(MovieRecyclerViewAdapter recyclerViewAdapter) {
        this.recyclerViewAdapter = recyclerViewAdapter;
        client = new MovieClient(this);
    }

    public void loadData(){
        this.client.loadPlaylist(PATH);
    }

    public void onNewPlayListData(List<Song> songList) {
        recyclerViewAdapter.setData(songList);
        recyclerViewAdapter.notifyDataSetChanged();
    }
}
