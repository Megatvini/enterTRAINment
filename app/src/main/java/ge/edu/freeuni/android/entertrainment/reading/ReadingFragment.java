package ge.edu.freeuni.android.entertrainment.reading;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ViewFlipper;

import ge.edu.freeuni.android.entertrainment.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReadingFragment extends Fragment {
    private ViewFlipper viewFlipper;
    private float lastX;

    private static int NUM_CHILDREN = 3;


    public ReadingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_reading, container, false);

        viewFlipper = (ViewFlipper) view.findViewById(R.id.reading_view_flipper);
        viewFlipper.setDisplayedChild(0);
        initTouchListener(view);

        return view;
    }

    private void initTouchListener(View view) {
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        lastX = motionEvent.getX();
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        float currentX = motionEvent.getX();

                        if (lastX < currentX) {
                            viewFlipper.setInAnimation(getContext(), R.anim.in_from_left);
                            viewFlipper.setOutAnimation(getContext(), R.anim.out_to_right);
                            viewFlipper.showPrevious();
                        }

                        if (lastX > currentX) {
                            viewFlipper.setInAnimation(getContext(), R.anim.in_from_right);
                            viewFlipper.setOutAnimation(getContext(), R.anim.out_to_left);
                            viewFlipper.showNext();
                        }
                        break;
                    }
                }
                return true;
            }
        });
    }

}
