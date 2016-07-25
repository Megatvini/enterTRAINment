package ge.edu.freeuni.android.entertrainment.reading;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;

import java.io.File;

import es.voghdev.pdfviewpager.library.RemotePDFViewPager;
import es.voghdev.pdfviewpager.library.remote.DownloadFile;
import ge.edu.freeuni.android.entertrainment.R;
import ge.edu.freeuni.android.entertrainment.chat.Utils;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReadingFragment extends Fragment implements DownloadFile.Listener {
    private PDFView pdfView;
    private String bookName;
    private String bookUrl;

    public static ReadingFragment newInstance(String url, String bookName) {
        ReadingFragment f = new ReadingFragment();
        Bundle args = new Bundle();
        args.putString("url", url);
        args.putString("bookName", bookName);
        f.setArguments(args);
        return f;
    }

    public ReadingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_reading, container, false);

        Bundle arguments = getArguments();
        String bookUrl = arguments.getString("url");
        assert bookUrl != null;
        bookName = arguments.getString("bookName");

        new RemotePDFViewPager(getContext(), bookUrl, this);

        pdfView = (PDFView) view.findViewById(R.id.pdfView);
        return view;
    }


    @Override
    public void onSuccess(final String url, String destinationPath) {
        OnPageChangeListener onPageChangeListener = new OnPageChangeListener() {
            @Override
            public void onPageChanged(int page, int pageCount) {
                Utils.saveReadingPage(getContext(), url, page);
            }
        };


        pdfView.fromFile(new File(destinationPath))
                .enableSwipe(true)
                .enableDoubletap(true)
                .swipeVertical(false)
                .defaultPage(Utils.readReadingPage(getContext(), url))
                .showMinimap(false)
//                .onDraw(onDrawListener)
//                .onLoad(onLoadCompleteListener)
                .onPageChange(onPageChangeListener)
//                .onError(onErrorListener)
                .enableAnnotationRendering(false)
                .password(null)
                .load();
    }

    @Override
    public void onFailure(Exception e) {

    }

    @Override
    public void onProgressUpdate(int progress, int total) {

    }
}
