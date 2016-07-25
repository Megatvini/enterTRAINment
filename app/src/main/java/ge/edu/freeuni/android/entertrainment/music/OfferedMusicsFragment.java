package ge.edu.freeuni.android.entertrainment.music;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ge.edu.freeuni.android.entertrainment.R;
import ge.edu.freeuni.android.entertrainment.music.data.MusicProvider;
import ge.edu.freeuni.android.entertrainment.music.data.Song;

import static ge.edu.freeuni.android.entertrainment.chat.Constants.HOST;

public class OfferedMusicsFragment extends Fragment {

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
        initAdapterAndProvider();
        View view = inflater.inflate(R.layout.offered_music_fragment_item_list, container, false);

        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(new OfferedMusicRecyclerViewAdapter(musicProvider.getSongs(), mListener));
        }
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

    @Override
    public void onResume() {
        initAdapterAndProvider();
        super.onResume();
    }

    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(Song item);
    }

    private void initAdapterAndProvider() {

        musicProvider = new MusicProvider(getContext(),PATH);
        adapter = new OfferedMusicRecyclerViewAdapter(musicProvider.getSongs(), mListener);
        musicProvider.setOfferedAdapter(adapter);
        musicProvider.loadData();
    }
}
