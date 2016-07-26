package ge.edu.freeuni.android.entertrainment.movie;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.MediaController;
import android.widget.VideoView;

import ge.edu.freeuni.android.entertrainment.R;

public class Player extends AppCompatActivity {
    private MediaPlayer mediaPlayer;

    private int position = 0;


    private MediaController mediaControls;
    private String path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        Intent intent = getIntent();

        path = intent.getStringExtra("path");

        if(savedInstanceState != null){
            position = savedInstanceState.getInt("position");
            path = savedInstanceState.getString("path");

        }


        if (mediaControls == null) {
            mediaControls = new MediaController(Player.this);
        }

        //initialize the VideoView
        final VideoView videoView = (VideoView) findViewById(R.id.video_view);

        try {
            //set the media controller in the VideoView
            videoView.setMediaController(mediaControls);
            videoView.setVideoPath(path);

        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }

        videoView.requestFocus();

        //we also set an setOnPreparedListener in order to know when the video file is ready for playback
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

            public void onPrepared(MediaPlayer mediaPlayer) {
                Player.this.mediaPlayer = mediaPlayer;
                System.out.println(position);
                Player.this.mediaPlayer = mediaPlayer;

                videoView.seekTo(position);
                videoView.start();

                mediaPlayer.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
                    @Override
                    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {

                        // Re-Set the videoView that acts as the anchor for the MediaController
                        mediaControls.setAnchorView(videoView);
                    }
                });
            }
        });


    }
    public static void start(Context context, String path) {
        Intent starter = new Intent(context, Player.class);
        starter.putExtra("path",path);
        context.startActivity(starter);
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        System.out.println("on save state");
        super.onSaveInstanceState(outState);
        outState.putInt("position",mediaPlayer.getCurrentPosition());
        outState.putString("path",path);
    }



    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}
