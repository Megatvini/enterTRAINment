package ge.edu.freeuni.android.entertrainment.map;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.klinker.android.sliding.SlidingActivity;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import ge.edu.freeuni.android.entertrainment.R;

public class StationInfoActivity extends SlidingActivity {

    private Handler handler = new Handler();
    private String station = "";

    private TextView estimatedTime;
    private TextView distance;
    private TextView temperature;
    private TextView description;
    private ImageView weatherIcon;

    private Runnable updateInfo = new Runnable() {
        @Override
        public void run() {
            String url = "http://entertrainment.herokuapp.com/webapi/map/station/" + station;

            AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
            asyncHttpClient.get(url, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, final JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                    updateView(response);
                }
            });

            // Repeat this the same runnable code block again another 2 seconds
            handler.postDelayed(updateInfo, 2000);
        }
    };

    private void updateView(JSONObject response) {
        System.out.println(response);
        try {
            estimatedTime.setText(response.getString("time"));
            distance.setText(response.getString("distance"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void init(Bundle savedInstanceState) {
        setPrimaryColors(
                R.color.colorPrimary,
                R.color.colorPrimaryDark
        );

        setContent(R.layout.activity_station_info);

        Bundle myBundle = getIntent().getBundleExtra("Bundle");
        String name = myBundle.getString("Station");
        if (name != null) {
            station = name;
            setTitle(name);
            setImage(getImage());
            getWeather();
        }

        initViews();
        setListeners();

        handler.post(updateInfo);
    }

    private void initViews() {
        estimatedTime = (TextView) findViewById(R.id.time_left);
        distance = (TextView) findViewById(R.id.distance);
        temperature = (TextView) findViewById(R.id.temperature);
        description = (TextView) findViewById(R.id.description);
        weatherIcon = (ImageView) findViewById(R.id.weather_icon);
    }

    private void setListeners() {
        findViewById(R.id.alarm_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(StationInfoActivity.this);
                SharedPreferences.Editor edit = prefs.edit();
                edit.clear();
                edit.putString("destination", station);
                edit.commit();

                String message = "Destination station set to: " + station;
                Toast toast = Toast.makeText(StationInfoActivity.this, message, Toast.LENGTH_LONG);
                toast.show();
            }
        });
    }

    private void getWeather() {
        String url = "http://api.openweathermap.org/data/2.5/weather?q=" + station + ",GE&APPID=ad52a3bc9c21e3c709f91782f30a92bd";

        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        asyncHttpClient.get(url, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, final JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                setWeather(response);
                System.out.println(response);
            }
        });
    }

    private void setWeather(JSONObject jObj) {

        if (jObj != null) {
            JSONArray jArr;
            try {
                jArr = jObj.getJSONArray("weather");
                JSONObject JSONWeather = jArr.getJSONObject(0);

                description.setText(JSONWeather.getString("main"));
                String icon = JSONWeather.getString("icon");
                weatherIcon.setBackgroundResource(getWeatherIcon(icon));

                JSONObject mainObj = jObj.getJSONObject("main");
                int temp = toCelsius(mainObj.getInt("temp"));
                temperature.setText(Integer.toString(temp) + " \u2103");

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private static int toCelsius(int kelvin) {
        return kelvin - 273;
    }

    private int getImage() {
        switch (station) {
            case "Tbilisi":
                return R.drawable.city_tbilisi;
            case "Rustavi":
                return R.drawable.city_rustavi;
            case "Gori":
                return R.drawable.city_gori;
            case "Batumi":
                return R.drawable.city_batumi;
            case "Zestafoni":
                return R.drawable.city_zestafoni;
            case "Khashuri":
                return R.drawable.city_khashuri;
            case "Samtredia":
                return R.drawable.city_samtredia;
            case "Kutaisi":
                return R.drawable.city_kutaisi;
            case "Kobuleti":
                return R.drawable.city_kobuleti;
            case "Poti":
                return R.drawable.city_poti;
        }

        return 0;
    }

    public static int getWeatherIcon(String icon) {
        switch (icon) {
            case "01d":
                return R.drawable.icon_01d;
            case "01n":
                return R.drawable.icon_01n;
            case "02d":
                return R.drawable.icon_02d;
            case "02n":
                return R.drawable.icon_02n;
            case "03d":
                return R.drawable.icon_03d;
            case "03n":
                return R.drawable.icon_03n;
            case "04d":
                return R.drawable.icon_04d;
            case "04n":
                return R.drawable.icon_04n;
            case "09d":
                return R.drawable.icon_09d;
            case "09n":
                return R.drawable.icon_09n;
            case "10d":
                return R.drawable.icon_10d;
            case "10n":
                return R.drawable.icon_10n;
            case "11d":
                return R.drawable.icon_11d;
            case "11n":
                return R.drawable.icon_11n;
            case "13d":
                return R.drawable.icon_13d;
            case "13n":
                return R.drawable.icon_13n;
            case "50d":
                return R.drawable.icon_50d;
            case "50n":
                return R.drawable.icon_50n;
        }
        return R.drawable.na;
    }
}
