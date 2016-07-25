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

public class MovieFragment extends Fragment {

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
        createProviders();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie_list, container, false);
        createProviders();
        if (view instanceof RecyclerView) {
            Context context = view.getContext();

            recyclerView = (RecyclerView) view;

            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setLayoutManager(new GridLayoutManager(context, 2));

            adapter = new MovieRecyclerViewAdapter(provider.getMovies(), mListener);

            recyclerView.setAdapter(adapter);
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
        super.onResume();
        createProviders();
    }

    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(Movie item);
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


    }


}
