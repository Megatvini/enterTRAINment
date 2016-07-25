package ge.edu.freeuni.android.entertrainment;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import ge.edu.freeuni.android.entertrainment.chat.Fragments.ChatFragment;
import ge.edu.freeuni.android.entertrainment.map.MapFragment;
import ge.edu.freeuni.android.entertrainment.music.MusicFragment;
import ge.edu.freeuni.android.entertrainment.music.OfferedMusicsFragment;
import ge.edu.freeuni.android.entertrainment.music.SharedMusicFragment;
import ge.edu.freeuni.android.entertrainment.music.data.Song;
import ge.edu.freeuni.android.entertrainment.reading.BookQRFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ChatFragment.OnFragmentInteractionListener,
        SharedMusicFragment.OnListFragmentInteractionListener,OfferedMusicsFragment.OnListFragmentInteractionListener {

    public NavigationView navigationView;
    private DrawerLayout drawer;
    protected int checkedNavigationItemId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        MainFragment mainFragment = MainFragment.newInstance();
        fragmentTransaction.replace(R.id.fragment_container,mainFragment);
        fragmentTransaction.commit();
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
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

        if (id == R.id.nav_chat) {
            replaceFragmentContainer(new ChatFragment());
        } else if (id == R.id.nav_music) {
            replaceFragmentContainer(new MusicFragment());
        } else if (id == R.id.nav_reading) {
            replaceFragmentContainer(new BookQRFragment());
        } else if (id == R.id.nav_map) {
            replaceFragmentContainer(new MapFragment());
        } else if (id == R.id.nav_share) {
            return false;
        } else if (id == R.id.nav_send) {
            return false;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
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
}
