package ge.edu.freeuni.android.entertrainment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import ge.edu.freeuni.android.entertrainment.chat.ChatFragment;
import ge.edu.freeuni.android.entertrainment.music.MusicFragment;

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
        Button musicButton = (Button) view.findViewById(R.id.music_button);
        musicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                MusicFragment musicFragment = new MusicFragment();
                fragmentTransaction.replace(R.id.fragment_container,musicFragment);
                fragmentTransaction.commit();
            }
        });

        Button chatButton = (Button) view.findViewById(R.id.chat_button);
        chatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                ChatFragment chatFragment = new ChatFragment();
                fragmentTransaction.replace(R.id.fragment_container, chatFragment);
                fragmentTransaction.commit();
            }
        });

        return view;
    }

}
