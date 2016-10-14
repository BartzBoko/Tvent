package teamsmartphone1.com.tvent.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

import teamsmartphone1.com.tvent.R;
import teamsmartphone1.com.tvent.Tweet;
import teamsmartphone1.com.tvent.adapters.TweetsAdapter;

public class TweetsActivity extends AppCompatActivity {
    private static ArrayList<Tweet> tweets;
    private TweetsAdapter mTweetsAdapter;

    private RecyclerView mRecyclerMovies;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweets);
        mRecyclerMovies = (RecyclerView) findViewById(R.id.listTweets);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        tweets = new ArrayList<>();
        mRecyclerMovies.setLayoutManager(layoutManager);
        Intent tweetData = getIntent();
        tweets = (ArrayList<Tweet>) tweetData.getSerializableExtra("Tweets");
        // creating a fake tweet. Incase there's none in the server
        Tweet tweet = new Tweet("Team smartphone rocks");
        tweets.add(tweet);
        mTweetsAdapter = new TweetsAdapter(this, tweets);
        mRecyclerMovies.setAdapter(mTweetsAdapter);
    }
}
