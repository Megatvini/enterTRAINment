package ge.edu.freeuni.android.entertrainment.map;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.klinker.android.sliding.SlidingActivity;

import ge.edu.freeuni.android.entertrainment.R;

public class StationInfoActivity extends SlidingActivity {

    @Override
    public void init(Bundle savedInstanceState) {
        setPrimaryColors(
                getResources().getColor(R.color.colorPrimary),
                getResources().getColor(R.color.colorPrimaryDark)
        );

        setContent(R.layout.activity_station_info);
        super.setImage(R.drawable.miso);

        Bundle myBundle = getIntent().getBundleExtra("Bundle");
        String name = myBundle.getString("Station");
        setTitle(name);
    }
}
