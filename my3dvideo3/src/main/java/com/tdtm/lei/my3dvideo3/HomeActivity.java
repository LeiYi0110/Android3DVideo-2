package com.tdtm.lei.my3dvideo3;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import org.json.JSONArray;

import ServerSide.DownloadWebpageTask;
import ServerSide.RequestData;
import ServerSide.ResponseData;
import ServerSide.ServerSideAPIKey;
import ServerSide.ServerSideURL;

public class HomeActivity extends AppCompatActivity implements DownloadWebpageTask.FinishGetDataListener {

    private ListView mOnLineVideoList;

    private static final int SELECT_VIDEO = 1;

    private String selectedImagePath;

    private  EditText mSearchEditText;

    public final static String EXTRA_URL = "com.lei.my3dVideo.video.url";
    public final static String VIDEO_TITLE = "com.lei.my3dVideo.video.title";

    private Context mContext;

    //private ImageView mCheckImageView;
    //private ImageView mLocalVideoImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mContext = this;

        mOnLineVideoList = (ListView)findViewById(R.id.onLineVideoList);

        DownloadWebpageTask task = new DownloadWebpageTask(this);
        RequestData requestData = new RequestData(ServerSideAPIKey.videoList, ServerSideURL.videoList());
        task.execute(requestData);

        //initImageLoader();
        mSearchEditText = (EditText)findViewById(R.id.searchEditText);

        Drawable drawable1 = getResources().getDrawable(R.drawable.search_left_view);
        drawable1.setBounds(30, 0, 69, 39);
        mSearchEditText.setCompoundDrawables(drawable1, null, null, null);
        //mSearchEditText.OnClickListener
        mSearchEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext, SearchActivity.class);
                startActivity(intent);
            }
        });

        Button checkImageButton = (Button)findViewById(R.id.check_image_button);
        checkImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(mContext,Main2Activity.class);
                startActivity(intent);

            }
        });

        String modeString = android.os.Build.MODEL;

        if (modeString.equals("HUAWEI NXT-AL10") )
        {
            String modeString2 = android.os.Build.MODEL;
        }
        else
        {
            String modeString3 = android.os.Build.MODEL;
        }

        Button localVideoButton = (Button)findViewById(R.id.local_video_image_button);
        //HUAWEI MT7-TL10
        //HUAWEI NXT-AL10

        localVideoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(mContext,LocalVideo2Activity.class);
                startActivity(intent);

            }
        });
        /*
        PreStoreVideoThumbnailTask storeVideoThumbnailTask = new PreStoreVideoThumbnailTask();
        storeVideoThumbnailTask.context = this;
        storeVideoThumbnailTask.execute("");
        */
        final SharedPreferences sharedPref = this.getSharedPreferences(this.getString(R.string.local_video_thumbnail_key), Context.MODE_PRIVATE);
        for (VideoInfo item: VideoApplication.sysVideoList)
        {

            String filePath = sharedPref.getString(item.Path,"");
            if (filePath.length() == 0)
            {
                GetVideoThumbnailTask imageTask = new GetVideoThumbnailTask();
                imageTask.context = getApplicationContext();

                GetThumbnailInfo info = new GetThumbnailInfo();
                info.filePath = item.Path;
                info.storeImageSoon = true;

                imageTask.execute(info);
            }

        }




    }
    /*
    private  void initImageLoader()
    {
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(this);
        config.threadPriority(Thread.NORM_PRIORITY - 2);
        config.denyCacheImageMultipleSizesInMemory();
        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
        config.diskCacheSize(50 * 1024 * 1024); // 50 MiB
        config.tasksProcessingOrder(QueueProcessingType.LIFO);
        config.writeDebugLogs(); // Remove for release app

        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config.build());
    }
    */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            /*
            case R.id.local_video:






                //Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
                //startActivityForResult(i, SELECT_VIDEO);


                Intent intent = new Intent(this, LocalVideoActivity.class);
                startActivity(intent);


                return true;

            case R.id.check_image:
                // User chose the "Favorite" action, mark the current item
                // as a favorite...
                return true;
                */


            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_VIDEO && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };
            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            Intent intent = new Intent(this, MainActivity.class);
            //intent.putExtra(Constants.Extra.FRAGMENT_INDEX, ImagePagerFragment.INDEX);
            intent.putExtra(HomeActivity.EXTRA_URL,picturePath);
            startActivity(intent);
            //ImageView imageView = (ImageView) findViewById(R.id.onLineViewItemLeft);
            //imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));


        }
    }
    /*
    // UPDATED!
    public String getPath(Uri uri) {
        //String[] projection = { MediaStore.Images.Media.DATA };
        String[] projection = {MediaStore.Video.Media.DATA };

        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        //Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        if (cursor != null) {
            // HERE YOU WILL GET A NULLPOINTER IF CURSOR IS NULL
            // THIS CAN BE, IF YOU USED OI FILE MANAGER FOR PICKING THE MEDIA
            //int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
            cursor.moveToFirst();
            String a =  cursor.getString(column_index);
            return a;
        }

        return null;
    }
    */

    public boolean didGetData(ResponseData data)
    {
        //ResponseData d = data;
        JSONArray jsonData;
        try
        {
            jsonData = data.data.getJSONArray("Data");
        }
        catch (Exception ex)
        {
            jsonData = null;
        }
        OnLineVideoListAdapter onLineVideoListAdapter = new OnLineVideoListAdapter(this,jsonData);
        mOnLineVideoList.setAdapter(onLineVideoListAdapter);
        return true;
    }



}
