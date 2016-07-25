package ge.edu.freeuni.android.entertrainment.reading;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

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

        Button button = (Button) view.findViewById(R.id.temp_btn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startReadingActivity();
            }
        });
        return view;
    }

    private void startReadingActivity() {
        String url = "http://litklubi.ge/biblioteka/admin/files/%E1%83%A1%E1%83%90%E1%83%9B%E1%83%9D%E1%83%A1%E1%83%94%E1%83%9A%E1%83%98%20%E1%83%9E%E1%83%98%E1%83%A0%E1%83%95%E1%83%94%E1%83%9A%E1%83%98.pdf";
        Intent i = new Intent(getContext(), ReadingActivity.class);
        i.putExtra("url", url);
        i.putExtra("bookName", "სამოსელი პირველი");
        startActivity(i);
    }

}
