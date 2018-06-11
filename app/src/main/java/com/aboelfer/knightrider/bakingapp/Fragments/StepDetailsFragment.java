package com.aboelfer.knightrider.bakingapp.Fragments;


import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aboelfer.knightrider.bakingapp.Activities.StepDetailsActivity;
import com.aboelfer.knightrider.bakingapp.R;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepDetailsFragment extends Fragment implements View.OnClickListener, ExoPlayer.EventListener {

    private String stepId;
    private String shortDescription;
    private String fullDescription;
    private String videoUrl;
    private SimpleExoPlayer exoPlayer;
    private static MediaSessionCompat mediaSessionCompat;
    private PlaybackStateCompat.Builder stateBuilder;
    static boolean playerIsPlaying = false;
    private boolean twoPane;
    private static final String TAG = StepDetailsActivity.class.getSimpleName();

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
        initializeMediaSession();
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

        }

        if (!twoPane) {

            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE && Objects.equals(videoUrl, "")) {
                exoPlayerView.setVisibility(View.GONE);
                noVideoImage.setVisibility(View.VISIBLE);
                stepFullDescription.setVisibility(View.VISIBLE);

            } else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE && !(Objects.equals(videoUrl, ""))) {
                exoPlayerView.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;
                exoPlayerView.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
                exoPlayerView.setVisibility(View.VISIBLE);
                noVideoImage.setVisibility(View.GONE);
                stepFullDescription.setVisibility(View.GONE);
                stepDescription_tv.setVisibility(View.GONE);

            } else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT && Objects.equals(videoUrl, "")) {
                exoPlayerView.setVisibility(View.GONE);
                noVideoImage.setVisibility(View.VISIBLE);
                stepFullDescription.setVisibility(View.VISIBLE);

            } else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT && !(Objects.equals(videoUrl, ""))) {
                exoPlayerView.setVisibility(View.VISIBLE);
                noVideoImage.setVisibility(View.GONE);
                stepFullDescription.setVisibility(View.VISIBLE);
            }

        } else {

            if (Objects.equals(videoUrl, "")) {
                exoPlayerView.setVisibility(View.GONE);
                noVideoImage.setVisibility(View.VISIBLE);
                stepFullDescription.setVisibility(View.VISIBLE);

            } else if (!(Objects.equals(videoUrl, ""))) {
                exoPlayerView.setVisibility(View.VISIBLE);
                noVideoImage.setVisibility(View.GONE);
                stepFullDescription.setVisibility(View.VISIBLE);
            }

        }

        if(videoUrl != null){

            initializePlayer(Uri.parse(videoUrl));
            stepFullDescription.setText(fullDescription);
        }

        return rootView;
    }

    /**
     * Initializes the Media Session to be enabled with media buttons, transport controls, callbacks
     * and media controller.
     */
    private void initializeMediaSession() {

        // Create a MediaSessionCompat.
        mediaSessionCompat = new MediaSessionCompat(getContext(), TAG);

        // Enable callbacks from MediaButtons and TransportControls.
        mediaSessionCompat.setFlags(
                MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                        MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);

        // Do not let MediaButtons restart the player when the app is not visible.
        mediaSessionCompat.setMediaButtonReceiver(null);

        // Set an initial PlaybackState with ACTION_PLAY, so media buttons can start the player.
        stateBuilder = new PlaybackStateCompat.Builder()
                .setActions(
                        PlaybackStateCompat.ACTION_PLAY |
                                PlaybackStateCompat.ACTION_PAUSE |
                                PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS |
                                PlaybackStateCompat.ACTION_PLAY_PAUSE);

        mediaSessionCompat.setPlaybackState(stateBuilder.build());


        // StepSessionCallback has methods that handle callbacks from a media controller.
        mediaSessionCompat.setCallback(new StepSessionCallback());

        // Start the Media Session since the activity is active.
        mediaSessionCompat.setActive(true);

    }

    /**
     * Initialize ExoPlayer.
     * @param mediaUri The URI of the sample to play.
     */
    private void initializePlayer(Uri mediaUri) {
        if (exoPlayer == null) {
            // Create an instance of the ExoPlayer.
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            exoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector, loadControl);
            exoPlayerView.setPlayer(exoPlayer);

            // Set the ExoPlayer.EventListener to this activity.
            exoPlayer.addListener(this);

            // Prepare the MediaSource.
            String userAgent = Util.getUserAgent(getContext(), "Baking Application");
            MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(
                    getContext(), userAgent), new DefaultExtractorsFactory(), null, null);
            exoPlayer.prepare(mediaSource);
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
        mediaSessionCompat.setActive(false);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {

        if((playbackState == ExoPlayer.STATE_READY) && playWhenReady){
            stateBuilder.setState(PlaybackStateCompat.STATE_PLAYING,
                    exoPlayer.getCurrentPosition(), 1f);
            playerIsPlaying = true;
        } else if((playbackState == ExoPlayer.STATE_READY)){
            stateBuilder.setState(PlaybackStateCompat.STATE_PAUSED,
                    exoPlayer.getCurrentPosition(), 1f);
            playerIsPlaying = false;
        }

        mediaSessionCompat.setPlaybackState(stateBuilder.build());

    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity() {

    }

    /**
     * Media Session Callbacks, where all external clients control the player.
     */
    private class StepSessionCallback extends MediaSessionCompat.Callback {
        @Override
        public void onPlay() {
            exoPlayer.setPlayWhenReady(true);
        }

        @Override
        public void onPause() {
            exoPlayer.setPlayWhenReady(false);
        }

        @Override
        public void onSkipToPrevious() {
            exoPlayer.seekTo(0);
        }
    }


}
