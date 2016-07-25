package ge.edu.freeuni.android.entertrainment.reading;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import ge.edu.freeuni.android.entertrainment.R;

public class ReadingActivity extends AppCompatActivity {
    private String bookUrl;
    private String bookName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        bookUrl = intent.getExtras().getString("url");
        bookName = intent.getExtras().getString("bookName");


//        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.setContentView(R.layout.activity_reading);
    }
}
