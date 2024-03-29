package ge.edu.freeuni.android.entertrainment.music.shared;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import ge.edu.freeuni.android.entertrainment.MainApplication;
import ge.edu.freeuni.android.entertrainment.R;
import ge.edu.freeuni.android.entertrainment.chat.Utils;
import ge.edu.freeuni.android.entertrainment.events.ShareEvent;
import ge.edu.freeuni.android.entertrainment.music.PlayerService;
import ge.edu.freeuni.android.entertrainment.music.data.MusicProvider;
import ge.edu.freeuni.android.entertrainment.music.data.Song;

import static ge.edu.freeuni.android.entertrainment.chat.Constants.HOST;

public class SharedMusicFragment extends Fragment {


    public static final String PLAYLIST_ENDPOINT = HOST+"/webapi/songs/shared";
    private MusicProvider musicProvider;

    private OnListFragmentInteractionListener mListener;
    private SharedMusicRecyclerViewAdapter adapter;
    private SharedMusicService service;


    public SharedMusicFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        updateUI();
        initAdapterAndProvider();

    }

    private void initAdapterAndProvider() {
        if (musicProvider == null)
            musicProvider = new MusicProvider(getContext(),PLAYLIST_ENDPOINT);
        if (service == null) {
            service = new SharedMusicService(musicProvider);
            service.setListener(this);
        }
        if (adapter == null)
            adapter = new SharedMusicRecyclerViewAdapter(musicProvider.getSongs(), mListener);
        musicProvider.setSharedAdapter(adapter);
        adapter.setVoteListener(musicProvider);
        musicProvider.loadData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        initAdapterAndProvider();

        View parentView = inflater.inflate(R.layout.fragment_sharedmusic_list, container, false);

        View view = parentView.findViewById(R.id.shared_list);
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(adapter);
        }


        final ImageButton playButton = (ImageButton) parentView.findViewById(R.id.play_button);
        final String local1 = HOST + "/song";
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainApplication application = (MainApplication) getActivity().getApplication();
                if (!application.isPlaying()){
                    List<Song> songs = musicProvider.getSongs();
                    if (songs.size() != 0) {
                        Song song = songs.get(0);
                        PlayerService.startActionStart(getActivity(), local1, song.getName());
                    }
                }else {
                    PlayerService.startActionStop(getActivity());
                }
            }
        });

        EventBus.getDefault().register(this);

        updateUI();
        return parentView;
    }


    @Override
    public void onAttach(Context context) {
        updateUI();
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onResume() {
        IntentFilter statusIntentFilter = new IntentFilter();
        statusIntentFilter.addAction(PlayerService.UPDATE_PLAYING_KEY);
        getActivity().registerReceiver(this.updateStatusBroadcastReceiver, statusIntentFilter);
        initAdapterAndProvider();
        updateUI();
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        this.getActivity().unregisterReceiver(this.updateStatusBroadcastReceiver);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(Song song);
    }

    private BroadcastReceiver updateStatusBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            SharedMusicFragment.this.updateUI();
        }

    };

    public void updateData(){
        this.musicProvider.loadData();
    }

    public void connectionClosed(){
        this.service = new SharedMusicService(musicProvider);
        service.setListener(this);
    }


    protected void updateUI() {
        final ImageButton playButton = (ImageButton) getActivity().findViewById(R.id.play_button);
        if (playButton == null){
            return;
        }
        MainApplication application = (MainApplication) getActivity().getApplication();
        if (application.isPlaying()){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                playButton.setBackground(getContext().getResources().getDrawable(R.drawable.ic_pause_black_24dp,getContext().getTheme()));
            }else{
                playButton.setBackground(getContext().getResources().getDrawable(R.drawable.ic_pause_black_24dp));
            }
        }else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                playButton.setBackground(getContext().getResources().getDrawable(R.drawable.ic_play_arrow_black_24dp,getContext().getTheme()));
            }else{
                playButton.setBackground(getContext().getResources().getDrawable(R.drawable.ic_play_arrow_black_24dp));
            }
        }

    }



    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ShareEvent event) {
        if (isVisible()) {
            List<Song> songs = musicProvider.getSongs();
            if (songs.size()!=0){
                PlayerService.NameInfo nameInfo = new PlayerService.NameInfo(songs.get(0).getName());
                String data = String.format("%s \n %s", nameInfo.getArtist(), nameInfo.getTitle());
                String title = "Listening to awesome song";
                Utils.shareStringData(getContext(), title, data);
            }

        }
    }

    @Override
    public void onDestroyView() {
        EventBus.getDefault().unregister(this);
        super.onDestroyView();
    }
}
