package ge.edu.freeuni.android.entertrainment.reading;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ge.edu.freeuni.android.entertrainment.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReadingFragment extends Fragment {


    public ReadingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_reading, container, false);
    }

}
