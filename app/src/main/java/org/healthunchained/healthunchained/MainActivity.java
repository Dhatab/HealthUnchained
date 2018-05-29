package org.healthunchained.healthunchained;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
                        Intent intent = new Intent(getApplicationContext(), MusicPlayerActivity.class);
                        intent.putExtra("TITLE", model.getTitle());
                        intent.putExtra("BODY", model.getBody());
                        intent.putExtra("URL", model.getUrl());
                        startActivity(intent);
                    }
                });
            }
        };



        mRecyclerView.setAdapter(recyclerAdapter);
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
}