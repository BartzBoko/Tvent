package teamsmartphone1.com.tvent.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashSet;

import teamsmartphone1.com.tvent.R;
import teamsmartphone1.com.tvent.Tweet;
import teamsmartphone1.com.tvent.adapters.TweetsAdapter;

/**
 * This class represents a TweetsActivity object
 *
 * @author Harmeet S. Bindra
 * @version 1.0
 */
public class TweetsActivity extends AppCompatActivity {
    private static HashSet<Tweet> tweets;
    private TweetsAdapter mTweetsAdapter;

    private RecyclerView mRecyclerMovies;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweets);
        mRecyclerMovies = (RecyclerView) findViewById(R.id.listTweets);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerMovies.setLayoutManager(layoutManager);
        Intent tweetData = getIntent();
        tweets = (HashSet<Tweet>) tweetData.getSerializableExtra("Tweets");
        mTweetsAdapter = new TweetsAdapter(this, tweets);
        mRecyclerMovies.setAdapter(mTweetsAdapter);
    }
}
