package com.herokuapp.jordan_chau.bakingtime;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.herokuapp.jordan_chau.bakingtime.model.Ingredient;
import com.herokuapp.jordan_chau.bakingtime.model.Step;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeStepDetailActivity extends AppCompatActivity {
    @BindView(R.id.recipe_step_detail_linear_layout) LinearLayout mLayout;
    @BindView(R.id.tv_long_description) TextView mDescription;
    @BindView(R.id.btn_previous_step) Button mPrevious;
    @BindView(R.id.btn_next_step) Button mNext;
    private ArrayList<Step> mSteps;
    private ArrayList<Ingredient> mIngredients;
    private int position;
    private String mVideo;

    private SimpleExoPlayer mExoPlayer;
    @BindView(R.id.playerView) SimpleExoPlayerView mPlayerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_step_detail);

        ButterKnife.bind(this);

        //Creates the back arrow on the top left corner to return to MainActivity, DELETE PARENT?
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Intent intent = getIntent();
        if (intent == null) {
            Log.d("RSDA: ", "intent error");
        }

        setTitle(intent.getStringExtra("recipe_name"));

        if(intent.getParcelableArrayListExtra("ingredients") != null) {
            mIngredients = intent.getParcelableArrayListExtra("ingredients");

            String ingredientsList = "";
            for(int x = 0; x < mIngredients.size(); ++x) {
                Ingredient currentIngredient = mIngredients.get(x);

                ingredientsList +=  x + 1 + ". " +
                        currentIngredient.getQuantity() + " " +
                        currentIngredient.getMeasure() + " " +
                        currentIngredient.getIngredient() + "\n\n";
            }

            mDescription.setText(ingredientsList);
            mPrevious.setVisibility(View.INVISIBLE);
            mNext.setVisibility(View.INVISIBLE);
        } else if (intent.getParcelableArrayListExtra("steps") != null) {
            mSteps = intent.getParcelableArrayListExtra("steps");
            position = intent.getIntExtra("position", 0);
            mVideo = mSteps.get(position).getVideoURL();

            Log.i("RSDA: ", "video url = " + mVideo);

            checkAndSetVideo(mVideo);
            mDescription.setText(mSteps.get(position).getDescription());
            setUpButtons();
        }
    }

    private void previousStep() {
        try {
            Step prevStep = mSteps.get(position - 1);
            String prevVideo = prevStep.getVideoURL();

            position--;

            checkAndSetVideo(prevVideo);
            mDescription.setText(prevStep.getDescription());
        } catch (Exception e) {
            Snackbar sb = Snackbar.make(mLayout, "This is the first step!", Snackbar.LENGTH_LONG);
            sb.show();
        }
    }

    private void nextStep() {
        try {
            Step nextStep = mSteps.get(position + 1);
            String nextVideo = nextStep.getVideoURL();

            position++;

            checkAndSetVideo(nextVideo);
            mDescription.setText(nextStep.getDescription());
        } catch (Exception e) {
            Snackbar sb = Snackbar.make(mLayout, "This is the last step!", Snackbar.LENGTH_LONG);
            sb.show();
        }
    }

    private void setUpButtons() {
        mPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                previousStep();
            }
        });
        mNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextStep();
            }
        });
    }

    private void checkAndSetVideo(String videoUrl){
        //if there is a video, release previous one, initialize and show new one
        if(!videoUrl.equals("")) {
            if(mExoPlayer != null) {
                releasePlayer();
            }
            initializePlayer(Uri.parse(videoUrl));
            mPlayerView.setVisibility(View.VISIBLE);
            //release previous one, and hide player
        } else {
            if(mExoPlayer != null) {
                releasePlayer();
            }
            mPlayerView.setVisibility(View.INVISIBLE);
            Snackbar sb = Snackbar.make(mLayout, "There is no video for this step!", Snackbar.LENGTH_LONG);
            sb.show();
        }
    }

    private void initializePlayer(Uri mediaUri) {
        if (mExoPlayer == null) {
            //Create an instance of the ExoPlayer
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(this, trackSelector, loadControl);
            mPlayerView.setPlayer(mExoPlayer);
            //Prepare the MediaSource
            String userAgent = Util.getUserAgent(this, "BakingTime");
            MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(this, userAgent), new DefaultExtractorsFactory(), null, null);
            mExoPlayer.prepare(mediaSource);
            mExoPlayer.setPlayWhenReady(true);
        }
    }

    private void releasePlayer() {
        mExoPlayer.stop();
        mExoPlayer.release();
        mExoPlayer = null;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;

                default:
                    return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mExoPlayer != null)
            releasePlayer();
    }
}
