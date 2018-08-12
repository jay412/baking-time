package com.herokuapp.jordan_chau.bakingtime.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.herokuapp.jordan_chau.bakingtime.R;
import com.herokuapp.jordan_chau.bakingtime.model.Ingredient;
import com.herokuapp.jordan_chau.bakingtime.model.Step;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeStepDetailFragment extends Fragment {
    private LinearLayout mLayout;
    private TextView mDescription;
    private Button mNext, mPrevious;
    private SimpleExoPlayerView mPlayerView;
    private SimpleExoPlayer mExoPlayer;

    private ArrayList<Step> mSteps;
    private ArrayList<Ingredient> mIngredients;
    private int position;
    private String mVideo;
    private Boolean hideButtons = false;

    public RecipeStepDetailFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recipe_step_detail, container, false);

        //Butterknife binding does not work for fragments
        mLayout = rootView.findViewById(R.id.recipe_step_detail_linear_layout);
        mDescription = rootView.findViewById(R.id.tv_long_description);
        mNext = rootView.findViewById(R.id.btn_next_step);
        mPrevious = rootView.findViewById(R.id.btn_previous_step);
        mPlayerView = rootView.findViewById(R.id.playerView);

        Bundle b = getArguments();
        if (b == null) {
            //put another error message here later
            Log.d("RSFrag: ", "bundle is null");

        } else {

            if (b.getParcelableArrayList("ingredients") != null) {
                mIngredients = b.getParcelableArrayList("ingredients");

                String ingredientsList = "";
                for (int x = 0; x < mIngredients.size(); ++x) {
                    Ingredient currentIngredient = mIngredients.get(x);

                    ingredientsList += x + 1 + ". " +
                            currentIngredient.getQuantity() + " " +
                            currentIngredient.getMeasure() + " " +
                            currentIngredient.getIngredient() + "\n\n";
                }

                mDescription.setText(ingredientsList);
                hideButtons = true;

            } else if (b.getParcelableArrayList("steps") != null) {
                mSteps = b.getParcelableArrayList("steps");
                position = b.getInt("position", 0);
                mVideo = mSteps.get(position).getVideoURL();

                checkAndSetVideo(mVideo);
                mDescription.setText(mSteps.get(position).getDescription());
                setUpButtons();
            }
        }

        if(hideButtons) {
            mPrevious.setVisibility(View.GONE);
            mNext.setVisibility(View.GONE);
        }

        return rootView;
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

    public void hideButtons() {
       hideButtons = true;
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
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector, loadControl);
            mPlayerView.setPlayer(mExoPlayer);
            //Prepare the MediaSource
            String userAgent = Util.getUserAgent(getContext(), "BakingTime");
            MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(getContext(), userAgent), new DefaultExtractorsFactory(), null, null);
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
    public void onDestroy() {
        super.onDestroy();
        if(mExoPlayer != null)
            releasePlayer();
    }
}
