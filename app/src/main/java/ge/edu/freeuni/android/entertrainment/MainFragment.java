package ge.edu.freeuni.android.entertrainment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;

public class MainFragment extends Fragment {



    public MainFragment() {
        // Required empty public constructor
    }


    public static MainFragment newInstance() {
        MainFragment fragment = new MainFragment();
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
        View view = inflater.inflate(R.layout.content_main, container, false);

        final MainActivity mainActivity = (MainActivity) getActivity();
        final Menu navMenu = mainActivity.navigationView.getMenu();

        CardView musicButton = (CardView) view.findViewById(R.id.music_button);
        musicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainActivity.onNavigationItemSelected(navMenu.findItem(R.id.nav_music));
            }
        });

        CardView chatButton = (CardView) view.findViewById(R.id.chat_button);
        chatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainActivity.onNavigationItemSelected(navMenu.findItem(R.id.nav_chat));
            }
        });

        CardView mapButton = (CardView) view.findViewById(R.id.map_button);
        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainActivity.onNavigationItemSelected(navMenu.findItem(R.id.nav_map));
            }
        });

        return view;
    }

}
