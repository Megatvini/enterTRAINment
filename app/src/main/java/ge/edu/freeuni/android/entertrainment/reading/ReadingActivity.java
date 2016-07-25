package ge.edu.freeuni.android.entertrainment.reading;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;

import java.io.File;

import es.voghdev.pdfviewpager.library.RemotePDFViewPager;
import es.voghdev.pdfviewpager.library.remote.DownloadFile;
import ge.edu.freeuni.android.entertrainment.R;
import ge.edu.freeuni.android.entertrainment.chat.Utils;

public class ReadingActivity extends AppCompatActivity implements DownloadFile.Listener {
    private String bookUrl;
    private String bookName;
    private PDFView pdfView;
    private File file;
    private ProgressBar spinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_reading);

        Intent intent = getIntent();
        bookUrl = intent.getExtras().getString("url");
        bookName = intent.getExtras().getString("bookName");


        new RemotePDFViewPager(this, bookUrl, this);

        pdfView = (PDFView) findViewById(R.id.pdfView);
        spinner = (ProgressBar) findViewById(R.id.book_loading_progress_bar);
    }

    @Override
    public void onSuccess(final String url, String destinationPath) {
        OnPageChangeListener onPageChangeListener = new OnPageChangeListener() {
            @Override
            public void onPageChanged(int page, int pageCount) {
                Utils.saveReadingPage(ReadingActivity.this, url, page);
            }
        };

        file = new File(destinationPath);

        pdfView.fromFile(file)
                .enableSwipe(true)
                .enableDoubletap(true)
                .swipeVertical(false)
                .defaultPage(Utils.readReadingPage(ReadingActivity.this, url))
                .showMinimap(false)
//                .onDraw(onDrawListener)
//                .onLoad(onLoadCompleteListener)
                .onPageChange(onPageChangeListener)
//                .onError(onErrorListener)
                .enableAnnotationRendering(false)
                .password(null)
                .load();

        spinner.setVisibility(View.GONE);
        pdfView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onFailure(Exception e) {

    }

    @Override
    public void onProgressUpdate(int progress, int total) {

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
