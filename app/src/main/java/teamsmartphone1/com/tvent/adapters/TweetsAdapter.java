package teamsmartphone1.com.tvent.adapters;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;

import teamsmartphone1.com.tvent.R;
import teamsmartphone1.com.tvent.Tweet;

/**
 * This class represents a TweetAdapter object
 *
 * @author Harmeet S. Bindra
 * @version 1.0
 */
public class TweetsAdapter extends RecyclerView.Adapter<TweetsAdapter.ViewHolderTweets> {
    private Context context;
    // list of tweets
    private ArrayList<Tweet> mTweets = new ArrayList<>();
    private LayoutInflater mInflater;

    public Context getContext() {
        return context;
    }

    /**
     * makes a TweetsAdapter object
     *
     * @param context is the context of the app
     */
    public TweetsAdapter(Context context, ArrayList<Tweet> list) {
        mInflater = LayoutInflater.from(context);
        this.context = context;
        mTweets = list;

    }

    @Override
    public ViewHolderTweets onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.tweetlayout, parent, false);
        ViewHolderTweets viewHolder = new ViewHolderTweets(view);
        return viewHolder;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(ViewHolderTweets holder, final int position) {
        final String currentTweet = mTweets.get(position).getMessage();

        holder.tweet.setText(currentTweet);
        Drawable myDrawable = context.getDrawable(R.mipmap.twitter);
        holder.imageView.setImageDrawable(myDrawable);
        holder.tweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // on click
                Toast.makeText(context, currentTweet, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mTweets.size();
    }

    /**
     * This class represents a ViewHolderTweets object
     *
     * @author Harmeet S. Bindra
     * @version 1.0
     */
    class ViewHolderTweets extends RecyclerView.ViewHolder {

        private TextView tweet;
        private ImageView imageView;

        public TextView getTweet() {
            return tweet;
        }

        /**
         * Makes viewHolder object
         *
         * @param itemView is the number of items to display
         */
        public ViewHolderTweets(View itemView) {
            super(itemView);
            tweet = (TextView) itemView.findViewById(R.id.tweet);
            imageView = (ImageView) itemView.findViewById(R.id.image);

        }
    }
}