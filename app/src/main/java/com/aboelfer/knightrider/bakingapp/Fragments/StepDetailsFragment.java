package com.aboelfer.knightrider.bakingapp.Fragments;

import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aboelfer.knightrider.bakingapp.R;
import com.google.android.exoplayer2.C;
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
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepDetailsFragment extends Fragment {

    private String stepId;
    private String shortDescription;
    private String fullDescription;
    private String videoUrl;
    private String thumbnailURL;
    private SimpleExoPlayer exoPlayer;
    static boolean playerIsPlaying ;
    private boolean twoPane;

    private final static String PLAYER_POSITION = "playerPosition";
    private final static String PLAYER_STATE = "playerState";
    private long mPlayerPosition;

    @BindView(R.id.stepDescriptionEv)
    TextView stepFullDescription;

    @BindView(R.id.stepDescriptionTv)
    TextView stepDescription_tv;

    @BindView(R.id.noVideoIv)
    ImageView noVideoImage;

    @BindView(R.id.playerView)
    SimpleExoPlayerView exoPlayerView;

    public StepDetailsFragment(){

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_step_detail, container, false);

        ButterKnife.bind(this, rootView);

        Bundle bundle = getArguments();

        if (bundle != null) {
            stepId = bundle.getString(getString(R.string.STEP_ID_BUNDLES_KEY));
            shortDescription = bundle.getString(getString(R.string.STEP_SHORT_DESCRIPTION_BUNDLES_KEY));
            fullDescription = bundle.getString(getString(R.string.STEP_FULL_DESCRIPTION_BUNDLES_KEY));
            videoUrl = bundle.getString(getString(R.string.STEP_VIDEO_URL_BUNDLES_KEY));
            twoPane = bundle.getBoolean(getString(R.string.TWO_PANE_SITUATION));
            thumbnailURL = bundle.getString(getString(R.string.STEP_THUMBNAIL_URL_BUNDLES_KEY));

        }

        if (!TextUtils.isEmpty(thumbnailURL)) {

            Picasso.with(getContext()).load(thumbnailURL).into(noVideoImage,
                    new Callback.EmptyCallback() {

                        @Override
                        public void onError() {

                            noVideoImage.setImageResource(R.drawable.no_video);
                        }
                    });
        }

        if (!twoPane) {

            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE && TextUtils.isEmpty(videoUrl)) {
                exoPlayerView.setVisibility(View.GONE);
                noVideoImage.setVisibility(View.VISIBLE);
                stepFullDescription.setVisibility(View.VISIBLE);

            } else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE && !(TextUtils.isEmpty(videoUrl))) {
                exoPlayerView.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;
                exoPlayerView.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
                exoPlayerView.setVisibility(View.VISIBLE);
                noVideoImage.setVisibility(View.GONE);
                stepFullDescription.setVisibility(View.GONE);
                stepDescription_tv.setVisibility(View.GONE);

            } else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT && TextUtils.isEmpty(videoUrl)) {
                exoPlayerView.setVisibility(View.GONE);
                noVideoImage.setVisibility(View.VISIBLE);
                stepFullDescription.setVisibility(View.VISIBLE);

            } else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT && !(TextUtils.isEmpty(videoUrl))) {
                exoPlayerView.setVisibility(View.VISIBLE);
                noVideoImage.setVisibility(View.GONE);
                stepFullDescription.setVisibility(View.VISIBLE);
            }

        } else {

            if (TextUtils.isEmpty(videoUrl)) {
                exoPlayerView.setVisibility(View.GONE);
                noVideoImage.setVisibility(View.VISIBLE);
                stepFullDescription.setVisibility(View.VISIBLE);

            } else if (!(TextUtils.isEmpty(videoUrl))) {
                exoPlayerView.setVisibility(View.VISIBLE);
                noVideoImage.setVisibility(View.GONE);
                stepFullDescription.setVisibility(View.VISIBLE);
            }

        }





        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null) {
            // Since both fragments are visible on a tablet, the recipe step must be intialized to 0
            // on fragment creation. When fragment is recreated we need to restore the correct step.
            mPlayerPosition = savedInstanceState.getLong(PLAYER_POSITION, C.TIME_UNSET);

            playerIsPlaying = savedInstanceState.getBoolean(PLAYER_STATE, true);

        } else {
            mPlayerPosition = C.TIME_UNSET; // ExoPlayer constant for unknown time
            playerIsPlaying = true;
        }

        if(videoUrl != null){

            initializePlayer();
            stepFullDescription.setText(fullDescription);
        }
    }

    private void initializePlayer() {
        if (exoPlayer == null) {
            // Create an instance of the ExoPlayer.
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            exoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector, loadControl);
            exoPlayerView.setPlayer(exoPlayer);

            // Prepare the MediaSource.
            String userAgent = Util.getUserAgent(getContext(), "Baking Application");
            MediaSource mediaSource = new ExtractorMediaSource(Uri.parse(videoUrl), new DefaultDataSourceFactory(
                    getContext(), userAgent), new DefaultExtractorsFactory(), null, null);

            exoPlayer.prepare(mediaSource);

            // Seek to last known paused position, set play by default and hide controls
            if (mPlayerPosition != C.TIME_UNSET) exoPlayer.seekTo(mPlayerPosition);

            exoPlayer.setPlayWhenReady(true);
        }
    }

    /**
     * Release ExoPlayer.
     */
    private void releasePlayer() {
        if (exoPlayer != null) {
            exoPlayer.stop();
            exoPlayer.release();
            exoPlayer = null;
        }
    }

    /**
     * Release the player when the activity is destroyed.
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        releasePlayer();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (exoPlayer != null) {
            mPlayerPosition = exoPlayer.getCurrentPosition();
            playerIsPlaying = exoPlayer.getPlayWhenReady();
            releasePlayer();
        }

    }


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(PLAYER_POSITION, mPlayerPosition);
        outState.putBoolean(PLAYER_STATE, playerIsPlaying);

    }

    @Override
    public void onStop() {
        super.onStop();
        releasePlayer();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(videoUrl != null){

            initializePlayer();
        }


    }

}
