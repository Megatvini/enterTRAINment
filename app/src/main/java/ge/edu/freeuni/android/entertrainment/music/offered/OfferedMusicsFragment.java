package ge.edu.freeuni.android.entertrainment.music.offered;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import ge.edu.freeuni.android.entertrainment.R;
import ge.edu.freeuni.android.entertrainment.music.data.MusicProvider;
import ge.edu.freeuni.android.entertrainment.music.data.Song;
import ge.edu.freeuni.android.entertrainment.music.listeners.OnListFragmentInteractionListener;

import static ge.edu.freeuni.android.entertrainment.chat.Constants.HOST;

public class OfferedMusicsFragment extends Fragment implements OnListFragmentInteractionListener {

    private static final String PATH = HOST+"/webapi/songs/offered";

    private OnListFragmentInteractionListener mListener;
    private MusicProvider musicProvider;
    private OfferedMusicRecyclerViewAdapter adapter;


    public OfferedMusicsFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initAdapterAndProvider();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View parentView = inflater.inflate(R.layout.offered_music_fragment_item_list, container, false);

        Context context = parentView.getContext();

        RecyclerView recyclerView = (RecyclerView)parentView.findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(adapter);

        initAdapterAndProvider();
        return parentView;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mListener = this;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onResume() {
        initAdapterAndProvider();
        super.onResume();
    }

    @Override
    public void onListFragmentInteraction(Song song) {
        String id = song.getId();
        String uriString1 = HOST + "/webapi/mediastream/audio/" + id;
        System.out.println(uriString1);
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse(uriString1), "audio/mp3");
        startActivity(intent);
    }



    private void initAdapterAndProvider() {
        if (musicProvider == null)
            musicProvider = new MusicProvider(getContext(),PATH);
        if (adapter == null)
            adapter = new OfferedMusicRecyclerViewAdapter(musicProvider.getSongs(), mListener);
        musicProvider.setOfferedAdapter(adapter);
        musicProvider.loadData();
    }
}
