package ge.edu.freeuni.android.entertrainment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import ge.edu.freeuni.android.entertrainment.chat.Fragments.ChatFragment;
import ge.edu.freeuni.android.entertrainment.chat.Utils;
import ge.edu.freeuni.android.entertrainment.map.MapFragment;
import ge.edu.freeuni.android.entertrainment.map.NotificationGenerator;
import ge.edu.freeuni.android.entertrainment.movie.MovieFragment;
import ge.edu.freeuni.android.entertrainment.music.MusicFragment;
import ge.edu.freeuni.android.entertrainment.music.data.Song;
import ge.edu.freeuni.android.entertrainment.music.shared.SharedMusicFragment;
import ge.edu.freeuni.android.entertrainment.reading.BookQRFragment;
import ge.edu.freeuni.android.entertrainment.reading.ReadingActivity;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ChatFragment.OnFragmentInteractionListener,
        SharedMusicFragment.OnListFragmentInteractionListener {

    public NavigationView navigationView;
    private DrawerLayout drawer;
    protected int checkedNavigationItemId = -1;
    private Handler handler;

    private Runnable checkDestinationReached = new Runnable() {
        @Override
        public void run() {
            final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
            final String station = prefs.getString("destination", "");
            if (!station.equals("")) {
                String url = "http://entertrainment.herokuapp.com/webapi/map/destReached/" + station;

                AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
                asyncHttpClient.get(url, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, final JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        try {
                            if (response.getString("destination reached").equals("true")){
                                NotificationGenerator.boom(MainActivity.this, station);
                                SharedPreferences.Editor edit = prefs.edit();
                                edit.clear();
                                edit.putString("destination", "");
                                edit.commit();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

                // check if destination is reached in every five minutes
                handler.postDelayed(checkDestinationReached, 300000);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        handler = new Handler();
        handler.post(checkDestinationReached);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_home);

        if (savedInstanceState == null){
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            MainFragment mainFragment = MainFragment.newInstance();
            fragmentTransaction.replace(R.id.fragment_container,mainFragment);
            fragmentTransaction.commit();
            navigationView.getMenu().findItem(R.id.nav_home).setChecked(true);

        }

        Utils.initRandomId(this);

    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
            if (checkedNavigationItemId == -1)
                return;
            navigationView.getMenu().findItem(checkedNavigationItemId).setChecked(false);
            checkedNavigationItemId = -1;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        item.setChecked(true);
        if (checkedNavigationItemId == id)
            return false;

        checkedNavigationItemId = id;

        if (id == R.id.nav_home) {
            replaceFragmentContainer(new MainFragment());
        } else if (id == R.id.nav_chat) {
            replaceFragmentContainer(new ChatFragment());
        } else if (id == R.id.nav_music) {
            replaceFragmentContainer(new MusicFragment());
        } else if (id == R.id.nav_reading) {
            replaceFragmentContainer(new BookQRFragment());
        } else if (id == R.id.nav_map) {
            replaceFragmentContainer(new MapFragment());
        } else if(id == R.id.nav_movie){
            replaceFragmentContainer(new MovieFragment());
        } else if (id == R.id.nav_share) {
            EventBus.getDefault().post(new ShareEvent());
            item.setChecked(false);
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void replaceFragmentContainer(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onListFragmentInteraction(Song song) {

    }

    // Get the results:
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                startReadingActivity(result.getContents());
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


    private void startReadingActivity(String url) {
        Intent i = new Intent(MainActivity.this, ReadingActivity.class);
        i.putExtra("url", url);
        startActivity(i);
    }

    public NavigationView getNavigationView() {
        return navigationView;
    }
}
