package ge.edu.freeuni.android.entertrainment.reading;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;

import es.voghdev.pdfviewpager.library.RemotePDFViewPager;
import es.voghdev.pdfviewpager.library.remote.DownloadFile;
import ge.edu.freeuni.android.entertrainment.R;
import ge.edu.freeuni.android.entertrainment.ShareEvent;
import ge.edu.freeuni.android.entertrainment.chat.Utils;

public class ReadingActivity extends AppCompatActivity implements DownloadFile.Listener {
    private String bookUrl;
    private PDFView pdfView;
    private ProgressBar spinner;
    private String filePath;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_reading);

        Intent intent = getIntent();
        bookUrl = intent.getExtras().getString("url");

        pdfView = (PDFView) findViewById(R.id.pdfView);
        spinner = (ProgressBar) findViewById(R.id.book_loading_progress_bar);

        new RemotePDFViewPager(this, bookUrl, this);
    }

    @Override
    public void onSuccess(final String url, String destinationPath) {
        OnPageChangeListener onPageChangeListener = new OnPageChangeListener() {
            @Override
            public void onPageChanged(int page, int pageCount) {
                Utils.saveReadingPage(ReadingActivity.this, url, page);
            }
        };

        filePath = destinationPath;
        File file = new File(destinationPath);
        file.deleteOnExit();

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
        Toast.makeText(ReadingActivity.this, "Page Load failed", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void onProgressUpdate(int progress, int total) {

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (filePath != null) {
            onSuccess(bookUrl, filePath);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ShareEvent event) {
        String data = "Share about reading";
        String title = "Reading a book in train";
        Utils.shareStringData(ReadingActivity.this, data, title);
    }
}
