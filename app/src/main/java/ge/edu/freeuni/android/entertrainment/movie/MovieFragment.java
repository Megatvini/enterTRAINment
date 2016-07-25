package ge.edu.freeuni.android.entertrainment.movie;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ge.edu.freeuni.android.entertrainment.R;
import ge.edu.freeuni.android.entertrainment.music.data.Song;

import static ge.edu.freeuni.android.entertrainment.chat.Constants.HOST;

public class MovieFragment extends Fragment implements OnListFragmentInteractionListener{

    private OnListFragmentInteractionListener mListener;
    private MovieRecyclerViewAdapter adapter;
    private MovieProvider provider;
    private RecyclerView recyclerView;

    public MovieFragment() {
    }

    @SuppressWarnings("unused")
    public static MovieFragment newInstance(int columnCount) {
        MovieFragment fragment = new MovieFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie_list, container, false);
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new GridLayoutManager(context, 2));
        }
        createProviders();
        return view;
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
        super.onResume();

    }



    public void createProviders(){

        if(provider == null) {
            provider = new MovieProvider();
        }
        if (adapter == null){
            adapter = new MovieRecyclerViewAdapter(provider.getMovies(),mListener);
            provider.setRecyclerViewAdapter(adapter);
            if (recyclerView!= null)
                recyclerView.setAdapter(adapter);
        }

        provider.loadData();
    }

    @Override
    public void onListFragmentInteraction(Song media) {
        String url = HOST +"/webapi/mediastream/video/"+media.getId();
        Player.start(getContext(),url);
    }
}
