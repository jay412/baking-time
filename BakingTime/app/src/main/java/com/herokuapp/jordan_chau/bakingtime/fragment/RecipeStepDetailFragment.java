package com.herokuapp.jordan_chau.bakingtime.fragment;

import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
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
    @BindView(R.id.recipe_step_detail_linear_layout) LinearLayout mLayout;
    @BindView(R.id.ll_step_buttons) LinearLayout mButtonLayout;
    @BindView(R.id.exo_player_container) FrameLayout mPlayerContainer;
    @BindView(R.id.tv_long_description) TextView mDescription;
    @BindView(R.id.btn_previous_step) Button mPrevious;
    @BindView(R.id.btn_next_step) Button mNext;
    @BindView(R.id.playerView) SimpleExoPlayerView mPlayerView;
    private SimpleExoPlayer mExoPlayer;

    private ArrayList<Step> mSteps;
    private int position;
    private String mVideo;
    private Boolean hideButtons = false;
    private String option;

    public RecipeStepDetailFragment() {}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recipe_step_detail, container, false);

        ButterKnife.bind(this, rootView);

        if(savedInstanceState != null) {
            option = savedInstanceState.getString("option");

            if(option.equals("ingredients")) {
                mDescription.setText(savedInstanceState.getString("ingredients"));
                hideButtons = true;
            } else {
                mSteps = savedInstanceState.getParcelableArrayList("steps");
                position = savedInstanceState.getInt("position");
                mVideo = mSteps.get(position).getVideoURL();

                checkAndSetVideo(mVideo);
                //restores where the player left off
                mExoPlayer.seekTo(savedInstanceState.getInt("currentWindow"), savedInstanceState.getLong("playbackPosition"));

                mDescription.setText(mSteps.get(position).getDescription());
                setUpButtons();
            }

        } else {

        Bundle b = getArguments();
        if (b == null) {
            //put another error message here later

        } else {

            if (b.getParcelableArrayList("ingredients") != null) {
                ArrayList<Ingredient> mIngredients = b.getParcelableArrayList("ingredients");

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

                option = "ingredients";

            } else if (b.getParcelableArrayList("steps") != null) {
                mSteps = b.getParcelableArrayList("steps");
                position = b.getInt("position", 0);
                mVideo = mSteps.get(position).getVideoURL();

                checkAndSetVideo(mVideo);
                mDescription.setText(mSteps.get(position).getDescription());
                setUpButtons();

                option = "steps";
            }
        }
        }

        if(hideButtons) {
            mPrevious.setVisibility(View.GONE);
            mNext.setVisibility(View.GONE);
        }

        return rootView;
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
            mPlayerContainer.setVisibility(View.VISIBLE);
            //release previous one, and hide player
        } else {
            if(mExoPlayer != null) {
                releasePlayer();
            }
            mPlayerContainer.setVisibility(View.INVISIBLE);
            //if snackbar uses activity layout, it may return null
                Snackbar sb = Snackbar.make(getActivity().findViewById(android.R.id.content), "There is no video for this step!", Snackbar.LENGTH_LONG);
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
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            //hide objects
            mButtonLayout.setVisibility(View.GONE);
            mDescription.setVisibility(View.GONE);

            //hide action bar
            if(((AppCompatActivity)getActivity()).getSupportActionBar() != null) {
                ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
            }

            //change layout container for exoplayer view
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) mPlayerView.getLayoutParams();
            params.width =  params.MATCH_PARENT;
            params.height = params.MATCH_PARENT;
            mPlayerView.setLayoutParams(params);

            //make full screen and hide status bar
            getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE);

        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            //unhide objects
            mButtonLayout.setVisibility(View.VISIBLE);
            mDescription.setVisibility(View.VISIBLE);

            //show action bar
            if(((AppCompatActivity)getActivity()).getSupportActionBar() != null) {
                ((AppCompatActivity)getActivity()).getSupportActionBar().show();
            }

            //set layout params to be original dimensions
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) mPlayerView.getLayoutParams();
            params.width =  params.MATCH_PARENT;
            params.height = 350;
            mPlayerView.setLayoutParams(params);

            //show status bar
            getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        if(option != null) {
            if (option.equals("ingredients")) {
                outState.putString("ingredients", mDescription.getText().toString());
            } else {
                outState.putParcelableArrayList("steps", mSteps);
                outState.putInt("position", position);
                outState.putString("video", mVideo);

                outState.putInt("currentWindow", mExoPlayer.getCurrentWindowIndex());
                outState.putLong("playbackPosition", mExoPlayer.getCurrentPosition());
            }

            outState.putString("option", option);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mExoPlayer != null)
            releasePlayer();
    }
}
