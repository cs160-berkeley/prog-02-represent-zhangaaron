package com.cs160.joleary.catnip;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import io.fabric.sdk.android.Fabric;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.*;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.services.StatusesService;
import com.twitter.sdk.android.tweetui.*;


import java.util.List;

/**
 * Created by aaron on 3/1/16.
 */
public class RepDataAdapter extends RecyclerView.Adapter<RepDataAdapter.RepViewHolder>  {
    public List<Representative> repList;
    Context app_context;
    public RepDataAdapter (List<Representative> repList, Context app_context) {
        this.repList = repList;
        this.app_context = app_context;
    }
    public static class RepViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView website;
        TextView email;
        TextView name;
        ImageView rep_photo;
        Button button;
        TweetView tweet;

        RepViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            website = (TextView)itemView.findViewById(R.id.website_text);
            name = (TextView)itemView.findViewById(R.id.representative_name);
            email = (TextView)itemView.findViewById(R.id.email_text);
            rep_photo = (ImageView)itemView.findViewById(R.id.rep_photo);
            button = (Button)itemView.findViewById(R.id.button);
            tweet = (TweetView)itemView.findViewById(R.id.tweet_view);
        }
    }

    @Override
    public int getItemCount() {
        return repList.size();
    }

    @Override
    public RepViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_view, viewGroup, false);
        RepViewHolder rep = new RepViewHolder(v);
        return rep;
    }

    @Override
    public void onBindViewHolder(RepViewHolder repView, int i) {
        Representative rep = repList.get(i);
        repView.website.setText(rep.website);
        repView.name.setText(rep.name);
        repView.email.setText(rep.email);
        //repView.rep_photo.setImageResource(rep.photoID);
        repView.button.setOnClickListener(new ButtonListener(i, app_context));
        final RepViewHolder view_to_add_tweet = repView;
        StatusesService s = TwitterCore.getInstance().getApiClient().getStatusesService();
        Log.wtf(this.getClass().toString(), "TWITTER ID IS: " + rep.twitterID);
        s.userTimeline(null, rep.twitterID, 0, null, null, false, true, false, false, new Callback<List<Tweet>>() {
            @Override
            public void success(Result<List<Tweet>> result) {
                Tweet t = result.data.get(0);
                view_to_add_tweet.tweet.setTweet(result.data.get(0));
            }

            @Override
            public void failure(TwitterException exception) {
                Log.d("TwitterKit", "Load Tweet failure", exception);
            }
        });  // https://dev.twitter.com/rest/reference/get/statuses/user_timeline

    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public class ButtonListener implements View.OnClickListener {
        private int id;
        private Context context;
        ButtonListener(int id, Context context) {
            this.id = id;
            this.context = context;
        }
        @Override
        public void onClick(View v) {
            Intent detailed_view = new Intent(context, DetailedView.class);
            detailed_view.putExtra("name", repList.get(id).name);
            detailed_view.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(detailed_view);
        }
    }

}
