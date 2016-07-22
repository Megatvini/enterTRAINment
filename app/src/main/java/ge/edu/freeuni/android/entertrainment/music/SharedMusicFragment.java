package ge.edu.freeuni.android.entertrainment.music;

import android.app.Application;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import java.io.IOException;
import java.net.URI;

import ge.edu.freeuni.android.entertrainment.MainApplication;
import ge.edu.freeuni.android.entertrainment.R;
import ge.edu.freeuni.android.entertrainment.music.data.MusicProvider;
import ge.edu.freeuni.android.entertrainment.music.data.MusicProvider.Song;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class SharedMusicFragment extends Fragment {

    MediaPlayer mediaPlayer ;

    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public SharedMusicFragment() {
    }

    @SuppressWarnings("unused")
    public static SharedMusicFragment newInstance(int columnCount) {
        SharedMusicFragment fragment = new SharedMusicFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sharedmusic_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            recyclerView.setAdapter(new SharedMusicRecyclerViewAdapter(MusicProvider.ITEMS, mListener));
        }

        final ImageButton playButton = (ImageButton) view.findViewById(R.id.play_button);
        final String realRadio = "http://uk1-pn.webcast-server.net:8698/";
        final String local = "http://192.168.0.100:50000";
        final String local1 = "http://192.168.77.253:8080/song";
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainApplication application = (MainApplication) getActivity().getApplication();
                if (!application.isPlaying()){
                    PlayerService.startActionStart(getActivity(),local1);
                    System.out.println("playing");
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        playButton.setBackground(getContext().getResources().getDrawable(R.drawable.ic_pause_black_24dp,getContext().getTheme()));
                    }else{
                        playButton.setBackground(getContext().getResources().getDrawable(R.drawable.ic_pause_black_24dp));
                    }
                    application.setPlaying(true);
                }else {
                    PlayerService.startActionStop(getActivity());

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        playButton.setBackground(getContext().getResources().getDrawable(R.drawable.ic_play_arrow_black_24dp,getContext().getTheme()));
                    }else{
                        playButton.setBackground(getContext().getResources().getDrawable(R.drawable.ic_play_arrow_black_24dp));
                    }
                    application.setPlaying(false);
                }


            }
        });
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(Song song);
    }
}
