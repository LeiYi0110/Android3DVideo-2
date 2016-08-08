package com.tdtm.lei.my3dvideo3;

import android.content.Context;
import android.media.MediaCodec;
import android.media.MediaPlayer;
import android.net.Uri;
import android.view.Surface;

import com.google.android.exoplayer.ExoPlayer;
import com.google.android.exoplayer.MediaCodecAudioTrackRenderer;
import com.google.android.exoplayer.MediaCodecSelector;
import com.google.android.exoplayer.MediaCodecVideoTrackRenderer;
import com.google.android.exoplayer.extractor.ExtractorSampleSource;
import com.google.android.exoplayer.upstream.Allocator;
import com.google.android.exoplayer.upstream.DataSource;
import com.google.android.exoplayer.upstream.DefaultAllocator;
import com.google.android.exoplayer.upstream.DefaultUriDataSource;
import com.google.android.exoplayer.util.Util;

/**
 * Created by lei on 6/23/16.
 */


public class MyPlayer {
    /*
    public int playerType = 0;

    private MediaPlayer mediaPlayer;
    private ExoPlayer exoPlayer;
    private Context context;
    private static final int BUFFER_SEGMENT_SIZE = 2*128*1024;//64 * 1024;
    private static final int BUFFER_SEGMENT_COUNT = 2*512;//256;

    private MediaCodecVideoTrackRenderer videoRendererPlayer;

    public MyPlayer(Context context)
    {
        this.context = context;
        if (android.os.Build.MODEL.equals("HUAWEI NXT-AL10")){

            playerType = 1;

        }
        initPlayer();


    }
    public void initPlayer()
    {
        if (playerType == 1)
        {
            exoPlayer = MediaPlayerInstance.getInstance().player;//ExoPlayer.Factory.newInstance(2);

            //exoPlayer = MediaPlayerInstance.getInstance().player;
            exoPlayer.seekTo(0);

        }
        else
        {
            mediaPlayer = new MediaPlayer();
        }
    }

    public void setPlayerType(int playerType)
    {
        this.playerType = playerType;
        initPlayer();
    }

    public void setURL(String url)
    {
        if (playerType == 1)
        {
            Allocator allocator = new DefaultAllocator(BUFFER_SEGMENT_SIZE);
            DataSource dataSource = new DefaultUriDataSource(context, null, Util.getUserAgent(context, "My3DVideo"));
            ExtractorSampleSource sampleSource = new ExtractorSampleSource(
                    Uri.parse(url), dataSource, allocator, BUFFER_SEGMENT_COUNT * BUFFER_SEGMENT_SIZE);
            this.videoRendererPlayer = new MediaCodecVideoTrackRenderer(
                    context, sampleSource, MediaCodecSelector.DEFAULT, MediaCodec.VIDEO_SCALING_MODE_SCALE_TO_FIT);
            MediaCodecAudioTrackRenderer audioRenderer = new MediaCodecAudioTrackRenderer(
                    sampleSource, MediaCodecSelector.DEFAULT);

            exoPlayer.prepare(videoRendererPlayer, audioRenderer);
        }

    }
    public void play()
    {
        if (playerType == 1)
        {
            exoPlayer.setPlayWhenReady(true);
        }
        else
        {
            mediaPlayer.start();
        }

    }
    public void pause()
    {
        if (playerType == 1)
        {
            exoPlayer.setPlayWhenReady(false);
        }
        else
        {
            mediaPlayer.pause();
        }

    }

    public void seekTo(int time)
    {
        if (playerType == 1)
        {
            exoPlayer.seekTo(time);
        }
        else {
            mediaPlayer.seekTo(time);
        }

    }

    public void setSurface(Surface surface)
    {
        if (playerType == 1)
        {
            exoPlayer.sendMessage(this.videoRendererPlayer, MediaCodecVideoTrackRenderer.MSG_SET_SURFACE, surface);

        }
        else
        {
            mediaPlayer.setSurface(surface);
        }


    }
    */



}
