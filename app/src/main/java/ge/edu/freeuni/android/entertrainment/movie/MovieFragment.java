package ge.edu.freeuni.android.entertrainment.movie;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import ge.edu.freeuni.android.entertrainment.MainActivity;
import ge.edu.freeuni.android.entertrainment.R;
import ge.edu.freeuni.android.entertrainment.events.ShareEvent;
import ge.edu.freeuni.android.entertrainment.chat.Utils;
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
        View parentView = inflater.inflate(R.layout.fragment_movie_list, container, false);

        RecyclerView recyclerView = (RecyclerView) parentView.findViewById(R.id.list);

        Context context = parentView.getContext();
        recyclerView.setLayoutManager(new GridLayoutManager(context, 2));
        provider = new MovieProvider();
        adapter = new MovieRecyclerViewAdapter(provider.getMovies(),mListener);
        recyclerView.setAdapter(adapter);
        provider.setRecyclerViewAdapter(adapter);



        EventBus.getDefault().register(this);
        return parentView;
    }

    @Override
    public void onDestroyView() {
        EventBus.getDefault().unregister(this);
        super.onDestroyView();
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
        createProviders();
        MainActivity activity = (MainActivity) getActivity();
        activity.getNavigationView().getMenu().findItem(R.id.nav_movie).setChecked(true);

    }



    public void createProviders(){

        provider.loadData();
    }

    @Override
    public void onListFragmentInteraction(Song media) {
        String url = HOST +"/webapi/mediastream/video/"+media.getId();
//        System.out.println(url);
//        Player.start(getContext(),url);

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        intent.setDataAndType(Uri.parse(url), "video/mp4");
        startActivity(intent);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ShareEvent event) {
        if (isVisible()) {
            String data = "Share about movie";
            String title = "Watching a movie in train";
            Utils.shareStringData(getContext(), title, data);
        }
    }
}
