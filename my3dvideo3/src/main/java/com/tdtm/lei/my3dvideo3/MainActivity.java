package com.tdtm.lei.my3dvideo3;

import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.media.AudioManager;
import android.media.MediaCodec;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.google.android.exoplayer.ExoPlaybackException;
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

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity  implements TextureView.SurfaceTextureListener,
        SeekBar.OnSeekBarChangeListener  {
    private TextureView mTextureView;
    private ExoPlayer mediaPlayer;
    //private Camera mCamera;

    //private TextureSurfaceRenderer videoRenderer;

    private VideoTextureSurfaceRenderer videoRenderer;
    private int surfaceWidth;
    private int surfaceHeight;

    private String mVideoURL;

    private SeekBar mSeekBar;

    private ImageView mPlayImageView;
    private Button mBackButton;

    private TextView mCurrentTimeTextView;
    private TextView mVideoDurationTextView;
    private RelativeLayout mTopView;
    private LinearLayout mBottomView;

    private boolean mCloseViewFinished = true;


    private static final int BUFFER_SEGMENT_SIZE = 2*128*1024;//64 * 1024;
    private static final int BUFFER_SEGMENT_COUNT = 2*512;//256;

    private int playerType = 0;

    private MediaPlayer generalPlayer;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);




        /*
        mTextureView = new TextureView(this);
        mTextureView.setSurfaceTextureListener(this);

        setContentView(mTextureView);

        */



        Intent intent = getIntent();

        mVideoURL = intent.getStringExtra(HomeActivity.EXTRA_URL); //"/storage/emulated/0/3D视频/IMG_6949.MP4";//intent.getStringExtra(HomeActivity.EXTRA_URL);

        /*
        if (mVideoURL.endsWith("xyj.mp4"))
        {
            playerType = 1;
        }
        else
        {
            playerType = 0;
        }
        */


        setContentView(R.layout.activity_main);


        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);


        TextView videoTitleTextView = (TextView)findViewById(R.id.video_title_text_view);

        //videoTitleTextView.setText("abc");
        videoTitleTextView.setText(intent.getStringExtra(HomeActivity.VIDEO_TITLE));

        mTextureView = (TextureView) findViewById(R.id.textureView);
        mTextureView.setSurfaceTextureListener(this);

        mSeekBar = (SeekBar) findViewById(R.id.currentTimeSeekBar);
        mSeekBar.setOnSeekBarChangeListener(this);
        mSeekBar.getProgressDrawable().setColorFilter(Color.WHITE, PorterDuff.Mode.DST_IN);
        //mSeekBar.setProgressTintList(ColorStateList.valueOf(Color.BLUE));

        //mSeekBar.setMax(100);
        //mSeekBar.setProgress(35);

        mPlayImageView = (ImageView)findViewById(R.id.playImageView);
        mPlayImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                int tag = Integer.parseInt((String)arg0.getTag());
                ImageView imageView = (ImageView)arg0;
                if (tag == 0)
                {
                    if (playerType == 1)
                    {
                        generalPlayer.stop();
                    }
                    else
                    {
                        mediaPlayer.setPlayWhenReady(false);
                    }
                    //mediaPlayer.stop();
                    //mediaPlayer.setPlayWhenReady(false);
                    imageView.setImageResource(R.drawable.play);
                    imageView.setTag("1");


                }
                else
                {
                    if (playerType == 1)
                    {
                        try {
                            generalPlayer.prepare();
                            generalPlayer.start();

                        }
                        catch (Exception ex)
                        {
                            ex.printStackTrace();
                        }

                    }
                    else
                    {
                        mediaPlayer.setPlayWhenReady(true);
                    }

                    imageView.setImageResource(R.drawable.stop);
                    imageView.setTag("0");
                }


            }
        });
        mBackButton = (Button)findViewById(R.id.videoBackButton);
        mBackButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View arg0)
            {
                finish();
            }
        });
        mCurrentTimeTextView = (TextView)findViewById(R.id.currentTimeTextView);
        mVideoDurationTextView = (TextView)findViewById(R.id.videoDurationTextView);

        mTopView = (RelativeLayout)findViewById(R.id.TopView);
        mBottomView = (LinearLayout)findViewById(R.id.BottomView);

        mTextureView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTopView.getVisibility() == View.VISIBLE) {
                    mTopView.setVisibility(View.GONE);
                } else {
                    mTopView.setVisibility(View.VISIBLE);

                }
                mBottomView.setVisibility(mTopView.getVisibility());

                if (mBottomView.getVisibility() == View.VISIBLE) {
                    prepareCloseView();

                }


            }
        });


        prepareCloseView();


        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);






    }
    @Override
    public void onPause()
    {
        super.onPause();
        if (playerType == 1)
        {
            generalPlayer.pause();
            /*
            try {
                generalPlayer.release();
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }
            */

        }
        else
        {
            mediaPlayer.stop();
        }

        //getWindow().addFlags(WindowManager.LayoutParams.F);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        //mediaPlayer.release();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /*
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        //return;

        Surface surface1 = new Surface(surface);

        String url = "http://www.xiangyouji.com.cn:3000/7.MP4"; // your URL here
        //String url = "http://www.xiangyouji.com.cn:3000/12.mp4";
        mediaPlayer = new MediaPlayer();
        //mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        mediaPlayer.setSurface(surface1);
        mediaPlayer.setLooping(true);


        try
        {
            mediaPlayer.setDataSource(url);
            mediaPlayer.prepare();
            mediaPlayer.start();
        }
        catch (IllegalStateException ex)
        {
            String message = ex.getMessage();

            int a = 0;

        }
        catch (IOException ex)
        {
            String message = ex.getMessage();

            int a = 0;
        }



        // don't forget to call MediaPlayer.prepareAsync() method when you use constructor for
        // creating MediaPlayer


        //mediaPlayer.prepareAsync();

        // Play video when the media source is ready for playback.
        //mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
          //  public void onPrepared(MediaPlayer mediaPlayer) {
            //    mediaPlayer.start();
            //}
        //});





    }
*/


    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
        // Ignored, Camera does all the work for us

    }

    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {

        return true;
    }

    public void onSurfaceTextureUpdated(SurfaceTexture surface) {
        // Invoked every time there's a new Camera preview frame
        String currentTime;
        int position;
        if (playerType == 1)
        {
            currentTime = convertMillisecondsToString(generalPlayer.getCurrentPosition());
            position = generalPlayer.getCurrentPosition();
        }
        else {
            currentTime = convertMillisecondsToString((int)mediaPlayer.getCurrentPosition());
            position = (int) mediaPlayer.getCurrentPosition();
        }
        //String currentTime = convertMillisecondsToString((int)mediaPlayer.getCurrentPosition());//String.valueOf(mediaPlayer.getCurrentPosition()) ;
        mCurrentTimeTextView.setText(currentTime);
        mSeekBar.setProgress(position);
    }
    private String convertMillisecondsToString(int milliseconds)
    {
        int seconds = milliseconds/1000;
        String strSeconds = String.valueOf(seconds%60);
        String strMinute = String.valueOf(seconds%3600/60);
        String strHour = String.valueOf(seconds / 3600);

        if (seconds < 3600)
        {
            return addZeroWithStr(strMinute) + ":" + addZeroWithStr(strSeconds);
        }
        return addZeroWithStr(strHour) + ":" + addZeroWithStr(strMinute) + ":" + addZeroWithStr(strSeconds);


    }
    private String addZeroWithStr(String str)
    {
        if (str.length() < 2)
        {
            str = "0" + str;
        }
        return str;
    }
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        //Log.v( TAG, "GLViewMediaActivity::onSurfaceTextureAvailable()"+ " tName:" + Thread.currentThread().getName() + "  tid:");

        surfaceWidth = width;
        surfaceHeight = height;
        playVideo(surface);
    }
    private void playVideo(SurfaceTexture surfaceTexture) {
        videoRenderer = new VideoTextureSurfaceRenderer(this, surfaceTexture, surfaceWidth, surfaceHeight);
        initMediaPlayer();
    }

    private void initMediaPlayer() {
        try {
            /*
            MediaPlayerInstance.getInstance().player.reset();
            this.mediaPlayer = MediaPlayerInstance.getInstance().player;      //new MediaPlayer();
            this.mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    finish();
                }
            });

            mediaPlayer.setScreenOnWhilePlaying(true);
            mediaPlayer.setWakeMode(this, PowerManager.FULL_WAKE_LOCK);

            WindowManager wm = this.getWindowManager();

            int width = wm.getDefaultDisplay().getWidth();
            //int height = wm.getDefaultDisplay().getHeight();
            */

            while (videoRenderer.getVideoTexture() == null) {
                try {
                    //Thread.sleep(100);
                    Thread.sleep(1);
                    //Thread.sleep(500);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }






            String url = mVideoURL;

            if (playerType == 1)
            {
                MediaPlayerInstance.getInstance().generalPlayer = new MediaPlayer();
                generalPlayer = MediaPlayerInstance.getInstance().generalPlayer;
                //generalPlayer.reset();

                generalPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        finish();
                    }
                });

                generalPlayer.setDataSource(url);


                generalPlayer.prepareAsync();
                generalPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        Surface surface = new Surface(videoRenderer.getVideoTexture());
                        generalPlayer.setSurface(surface);

                        surface.release();
                        generalPlayer.start();
                        mSeekBar.setMax(generalPlayer.getDuration());
                        mVideoDurationTextView.setText(convertMillisecondsToString(generalPlayer.getDuration()));
                        //prepareCloseView();
                    }
                });

                generalPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                    @Override
                    public boolean onError(MediaPlayer mp, int what, int extra) {

                        //mp.seekTo(what + 10);
                        return true;
                    }
                });



            }
            else
            {
                this.mediaPlayer = MediaPlayerInstance.getInstance().player;
                this.mediaPlayer.seekTo(0);
                Allocator allocator = new DefaultAllocator(BUFFER_SEGMENT_SIZE);
                DataSource dataSource = new DefaultUriDataSource(this, null, Util.getUserAgent(this, "My3DVideo"));
                ExtractorSampleSource sampleSource = new ExtractorSampleSource(
                        Uri.parse(url), dataSource, allocator, BUFFER_SEGMENT_COUNT * BUFFER_SEGMENT_SIZE);
                MediaCodecVideoTrackRenderer videoRendererPlayer = new MediaCodecVideoTrackRenderer(
                        this, sampleSource, MediaCodecSelector.DEFAULT, MediaCodec.VIDEO_SCALING_MODE_SCALE_TO_FIT);
                MediaCodecAudioTrackRenderer audioRenderer = new MediaCodecAudioTrackRenderer(
                        sampleSource, MediaCodecSelector.DEFAULT);
// 3. Inject the renderers through prepare.
                mediaPlayer.prepare(videoRendererPlayer, audioRenderer);

                Surface surface = new Surface(videoRenderer.getVideoTexture());
                //mediaPlayer.setSurface(surface);
                mediaPlayer.sendMessage(videoRendererPlayer, MediaCodecVideoTrackRenderer.MSG_SET_SURFACE, surface);
// 5. Start playback.

                mediaPlayer.setPlayWhenReady(true);



                mediaPlayer.addListener(new ExoPlayer.Listener() {
                    @Override
                    public void onPlayerError(ExoPlaybackException error) {

                    }

                    @Override
                    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                        if (playWhenReady)
                        {
                            mSeekBar.setMax((int) mediaPlayer.getDuration());
                            mVideoDurationTextView.setText(convertMillisecondsToString((int) mediaPlayer.getDuration()));
                        }

                        if (playbackState == ExoPlayer.STATE_ENDED)
                        {
                            finish();
                        }
                    }

                    @Override
                    public void onPlayWhenReadyCommitted() {

                    }

                });
            }











            /*
            mediaPlayer.setDataSource(url);


            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    Surface surface = new Surface(videoRenderer.getVideoTexture());
                    mediaPlayer.setSurface(surface);

                    surface.release();
                    mediaPlayer.start();
                    mSeekBar.setMax(mediaPlayer.getDuration());
                    mVideoDurationTextView.setText(convertMillisecondsToString(mediaPlayer.getDuration()));
                    //prepareCloseView();
                }
            });
            */

        } catch (IllegalArgumentException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } catch (SecurityException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } catch (IllegalStateException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();

        }
    }
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
    {
        if (fromUser)
        {
            if (playerType == 1)
            {
                generalPlayer.seekTo(progress);
            }
            else
            {
                mediaPlayer.seekTo(progress);
            }

        }


    }
    @Override
    public void onStartTrackingTouch(SeekBar seekBar)
    {

    }
    @Override
    public void onStopTrackingTouch(SeekBar seekBar)
    {

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {

            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);









        }
    }

    class CloseViewTask extends TimerTask {

        @Override
        public void run() {
            //System.out.println("dddd");
            //mTopView.setVisibility(View.GONE);

            try
            {
                if (mTopView.getVisibility() == View.VISIBLE)
                {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mTopView.setVisibility(View.GONE);
                            mBottomView.setVisibility(View.GONE);
//stuff that updates ui

                        }
                    });

                }


            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }
            finally {
                mCloseViewFinished = true;
            }




        }

    }

    private void prepareCloseView()
    {


        if ( mCloseViewFinished == false)
            return;
        int time = 5000;
        //mCloseBottomViewFinished = false;
        mCloseViewFinished = false;
        Timer timer = new Timer();
        timer.schedule(new CloseViewTask(),time);
        //timer.schedule(new CloseBottomViewTask(), time);



    }

}
