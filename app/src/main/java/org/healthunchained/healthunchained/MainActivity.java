package org.healthunchained.healthunchained;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private DatabaseReference mDatabase;
    private TextView btm_music_text;
    private ImageView btm_play;
    public static String title,body,url;
    public static MediaPlayer mediaPlayer;
    private LinearLayout linearlay;
    private String data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btm_music_text = (TextView) findViewById(R.id.main_act_text);
        btm_play = (ImageView) findViewById(R.id.main_act_play);
        linearlay = (LinearLayout) findViewById(R.id.linearlay);
        mRecyclerView = (RecyclerView) findViewById(R.id.mainAct_RV);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));


        //Database Ref
        mDatabase = FirebaseDatabase.getInstance().getReference().child("/episodes");

        //Set up Firebase Database Adapter
        FirebaseRecyclerAdapter<EpisodeReference, EpisodeViewHolder> recyclerAdapter = new FirebaseRecyclerAdapter<EpisodeReference, EpisodeViewHolder>
                (EpisodeReference.class, R.layout.episode_layout, EpisodeViewHolder.class, mDatabase)

        //Populating View
        {
            @Override
            protected void populateViewHolder(final EpisodeViewHolder viewHolder, final EpisodeReference model, int position) {
                viewHolder.setBody(model.getBody());
                viewHolder.setTitle(model.getTitle());

                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        title = model.getTitle();
                        body = model.getBody();
                        url = model.getUrl();
                        Uri mUri = Uri.parse(url);

                        btm_music_text.setSelected(true);
                        btm_music_text.setText(title + ": " + body);

                        if (mediaPlayer != null && mediaPlayer.isPlaying()){
                            mediaPlayer.stop();
                            mediaPlayer.release();
                            mediaPlayer = new MediaPlayer();
                            try {
                                mediaPlayer.setDataSource(getApplicationContext(), mUri);
                                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                                mediaPlayer.prepare(); //don't use prepareAsync for mp3 playback
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            mediaPlayer.start();
                            btm_play.setImageDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.selector_pause));
                        } else {
                            mediaPlayer = new MediaPlayer();
                            try {
                                mediaPlayer.setDataSource(getApplicationContext(), mUri);
                                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                                mediaPlayer.prepare(); //don't use prepareAsync for mp3 playback
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            mediaPlayer.start();
                            btm_play.setImageDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.selector_pause));
                        }


                        linearlay.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(getApplicationContext(), MusicPlayerActivity.class);
                                intent.putExtra("TITLE", model.getTitle());
                                intent.putExtra("BODY", model.getBody());
                                intent.putExtra("URL", model.getUrl());
                                startActivity(intent);
                            }
                        });
                        /*Intent intent = new Intent(getApplicationContext(), MusicPlayerActivity.class);
                        intent.putExtra("TITLE", model.getTitle());
                        intent.putExtra("BODY", model.getBody());
                        intent.putExtra("URL", model.getUrl());
                        startActivity(intent);*/
                    }
                });
            }
        };
        mRecyclerView.setAdapter(recyclerAdapter);
        button_change();
    }


    private void button_change(){
        btm_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mediaPlayer.isPlaying() && mediaPlayer != null) {
                    btm_play.setImageDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.selector_play));
                    mediaPlayer.pause();
                } else {
                    mediaPlayer.start();
                    btm_play.setImageDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.selector_pause));
                }
            }
        });
    }

    public static class EpisodeViewHolder extends RecyclerView.ViewHolder {
        View mView;
        TextView tv1;
        TextView tv2;



        public EpisodeViewHolder(View view) {
            super(view);
            mView = view;
            tv1 = (TextView) view.findViewById(R.id.tv1);
            tv2 = (TextView) view.findViewById(R.id.tv2);
        }

        public void setBody(String body) {
            tv2.setText(body);
        }

        public void setTitle(String title) {
            tv1.setText(title);
        }
    }

    public void startService(View v) {
        Intent serviceIntent = new Intent(MainActivity.this, NotificationService.class);
        serviceIntent.setAction(Constants.ACTION.STARTFOREGROUND_ACTION);
        startService(serviceIntent);
    }


}