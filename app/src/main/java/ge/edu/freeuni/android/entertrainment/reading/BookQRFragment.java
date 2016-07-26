package ge.edu.freeuni.android.entertrainment.reading;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.zxing.integration.android.IntentIntegrator;

import ge.edu.freeuni.android.entertrainment.MainActivity;
import ge.edu.freeuni.android.entertrainment.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class BookQRFragment extends Fragment {
    public BookQRFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_book_qr, container, false);


        ImageView button = (ImageView) view.findViewById(R.id.scan_barcode_image);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new IntentIntegrator(getActivity()).initiateScan();
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        MainActivity activity = (MainActivity) getActivity();
        activity.getNavigationView().getMenu().findItem(R.id.nav_reading).setChecked(true);
    }
}
