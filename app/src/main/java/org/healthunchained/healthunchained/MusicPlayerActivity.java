package org.healthunchained.healthunchained;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import org.healthunchained.healthunchained.MainActivity;

import static org.healthunchained.healthunchained.MainActivity.mediaPlayer;

public class MusicPlayerActivity extends AppCompatActivity {

    private ImageView iv_play, iv_next, iv_previous;
    private TextView tv_start_time, tv_end_time, tv_body, tv_title;
    private SeekBar player_seekBar;
    private String song_url, song_title, song_body;

    private double startTime = 0;
    private double finalTime = 0;
    public static int oneTimeOnly = 0;


    private Handler myHandler = new Handler();
    private int forwardtime = 30000;
    private int backTime = 30000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);
        layout_init();

        Intent intent = getIntent();
        song_url = intent.getStringExtra("URL");
        song_title = intent.getStringExtra("TITLE");
        song_body = intent.getStringExtra("BODY");


        tv_title.setText(song_title);
        tv_body.setSelected(true);
        tv_body.setText(song_body);

        startTime = mediaPlayer.getCurrentPosition();
        finalTime = mediaPlayer.getDuration();

        tv_start_time.setText(String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes((long)startTime),
                TimeUnit.MILLISECONDS.toSeconds((long) startTime)- TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long)startTime))));

        tv_end_time.setText(String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes((long)finalTime),
                TimeUnit.MILLISECONDS.toSeconds((long) finalTime)- TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long)finalTime))));


        if (mediaPlayer != null && mediaPlayer.isPlaying()){
            iv_play.setImageDrawable(ContextCompat.getDrawable(MusicPlayerActivity.this, R.drawable.selector_pause));
            if (oneTimeOnly == 0){
                player_seekBar.setMax((int) finalTime);
                oneTimeOnly = 1;
            }
        }

        player_seekBar.setProgress((int)startTime);
        myHandler.postDelayed(UpdateSongTime, 100);

        //media player
        setPlayer_seekBar();
        onPlay();
        fastForward();
        backward();
    }

    private void onPlay(){
        iv_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mediaPlayer.isPlaying() && mediaPlayer != null){
                    iv_play.setImageDrawable(ContextCompat.getDrawable(MusicPlayerActivity.this, R.drawable.selector_play));
                    if (oneTimeOnly == 0) {
                        player_seekBar.setMax((int) finalTime);
                        oneTimeOnly = 1;
                        mediaPlayer.pause();
                    }
                }else{
                    mediaPlayer.start();
                    if (oneTimeOnly == 0){
                        player_seekBar.setMax((int) finalTime);
                        oneTimeOnly = 1;
                    }
                    iv_play.setImageDrawable(ContextCompat.getDrawable(MusicPlayerActivity.this, R.drawable.selector_pause));
                    myHandler.postDelayed(UpdateSongTime, 100);
                }
            }
        });
    }

    private void fastForward(){
        iv_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int temp = (int)startTime;

                if((temp+forwardtime)<=finalTime){
                    startTime = startTime + forwardtime;
                    mediaPlayer.seekTo((int)startTime);
                }
            }
        });
    }

    private void backward(){
        iv_previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int temp = (int)startTime;

                if((temp-backTime)>0){
                    startTime = startTime - backTime;
                    mediaPlayer.seekTo((int)startTime);
                }
            }
        });
    }

    private void setPlayer_seekBar(){
        player_seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {


            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                myHandler.removeCallbacks(UpdateSongTime);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                myHandler.removeCallbacks(UpdateSongTime);
                double val=((double)seekBar.getProgress())/((double)seekBar.getMax());
                int totalDuration = mediaPlayer.getDuration();
                int currentDuration =(int) (val *totalDuration);

                mediaPlayer.seekTo(currentDuration);
                updateProgressBar();
            }
        });
    }

    private void layout_init(){
        iv_play = (ImageView) findViewById(R.id.iv_play);
        iv_next = (ImageView) findViewById(R.id.iv_next);
        iv_previous = (ImageView) findViewById(R.id.iv_previous);
        player_seekBar = (SeekBar) findViewById(R.id.seekbar);
        tv_start_time = (TextView) findViewById(R.id.tv_time_start);
        tv_end_time = (TextView) findViewById(R.id.tv_time_end);
        tv_body = (TextView) findViewById(R.id.tv_music_body);
        tv_title = (TextView) findViewById(R.id.tv_music_title);
    }

    public void updateProgressBar(){
        myHandler.postDelayed(UpdateSongTime,100);
    }

    private Runnable UpdateSongTime = new Runnable() {
        @Override
        public void run() {
            startTime = mediaPlayer.getCurrentPosition();
            tv_start_time.setText(String.format("%02d:%02d",
                    TimeUnit.MILLISECONDS.toMinutes((long)startTime),
                    TimeUnit.MILLISECONDS.toSeconds((long) startTime)- TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long)startTime))));
            player_seekBar.setProgress((int)startTime);
            myHandler.postDelayed(this,100);
        }
    };
}