package com.tdtm.lei.my3dvideo3;

import android.content.CursorLoader;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ServerSide.DownloadWebpageTask;
import ServerSide.ResponseData;

public class LocalVideo2Activity extends AppCompatActivity implements DownloadWebpageTask.FinishGetDataListener {
    private List<VideoInfo> sysVideoList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_video2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        toolbar.setTitleTextColor(Color.BLACK);
        toolbar.setTitle("本地视频");


        try
        {
            initVideoList();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

        ListView listView = (ListView)findViewById(R.id.localVideoList);

        LocalVideoListAdapter adapter = new LocalVideoListAdapter(this,sysVideoList);

        listView.setAdapter(adapter);

        Button back = (Button)findViewById(R.id.local_video_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initVideoList()
    {

        sysVideoList = new ArrayList<VideoInfo>();
        String[] thumbColumns = { MediaStore.Video.Thumbnails.DATA,
                MediaStore.Video.Thumbnails.VIDEO_ID };

        // MediaStore.Video.Media.DATA：视频文件路径；
        // MediaStore.Video.Media.DISPLAY_NAME : 视频文件名，如 testVideo.mp4
        // MediaStore.Video.Media.TITLE: 视频标题 : testVideo
        String[] mediaColumns = { MediaStore.Video.Media._ID,
                MediaStore.Video.Media.DATA, MediaStore.Video.Media.TITLE,
                MediaStore.Video.Media.MIME_TYPE,
                MediaStore.Video.Media.DISPLAY_NAME };

        /*
        Cursor cursor = null;
        try
        {
            cursor = getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, mediaColumns, null, null, null);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        */
        CursorLoader loader = new CursorLoader(this, MediaStore.Video.Media.EXTERNAL_CONTENT_URI, mediaColumns, null, null, null);
        Cursor cursor = loader.loadInBackground();
        //Cursor cursor = getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, mediaColumns, null, null, null);

        if (cursor == null)
        {
            //cursor = getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, mediaColumns, null, null, null);

            Toast.makeText(this, "没有找到可播放视频文件", 1).show();
            return;
        }


        if (cursor.moveToFirst()) {
            do {
                VideoInfo info = new VideoInfo();
                int id = cursor.getInt(cursor
                        .getColumnIndex(MediaStore.Video.Media._ID));
                /*
                Cursor thumbCursor = getContentResolver().query(
                        MediaStore.Video.Thumbnails.EXTERNAL_CONTENT_URI,
                        thumbColumns, MediaStore.Video.Thumbnails.VIDEO_ID
                                + "=" + id, null, null);
                if (thumbCursor.moveToFirst()) {
                    info.setThumbPath(thumbCursor.getString(thumbCursor
                            .getColumnIndex(MediaStore.Video.Thumbnails.DATA)));
                }
                */
                info.setPath(cursor.getString(cursor
                        .getColumnIndexOrThrow(MediaStore.Video.Media.DATA)));
                info.setTitle(cursor.getString(cursor
                        .getColumnIndexOrThrow(MediaStore.Video.Media.TITLE)));

                info.setDisplayName(cursor.getString(cursor
                        .getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME)));
                //LogUtil.log(TAG, "DisplayName:"+info.getDisplayName());
                info.setMimeType(cursor
                        .getString(cursor
                                .getColumnIndexOrThrow(MediaStore.Video.Media.MIME_TYPE)));

                //info.setBitmap(MediaStore.Video.Thumbnails.getThumbnail(contentResolver, id, MediaStore.Images.Thumbnails.MICRO_KIND, options));

                /*
                if (info.ThumbPath != null)
                {
                    sysVideoList.add(info);
                }
                */

                sysVideoList.add(info);
            } while (cursor.moveToNext());
            //Toast.makeText(this, String.valueOf(sysVideoList.size()) , 1).show();
        }



    }
    public boolean didGetData(ResponseData data)
    {
        return true;
    }



}
