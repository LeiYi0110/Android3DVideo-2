package com.tdtm.lei.my3dvideo3;

import android.app.Application;
import android.content.Context;
import android.content.CursorLoader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.provider.MediaStore;
import android.widget.Toast;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ServerSide.DownloadWebpageTask;
import ServerSide.RequestData;
import ServerSide.ResponseData;
import ServerSide.ServerSideAPIKey;
import ServerSide.ServerSideURL;

/**
 * Created by lei on 6/12/16.
 */
public class VideoApplication extends Application implements DownloadWebpageTask.FinishGetDataListener{

    public static List<VideoInfo> sysVideoList = new ArrayList<VideoInfo>();

    @Override
    public void onCreate() {

        super.onCreate();

        initImageLoader(getApplicationContext());

        DownloadWebpageTask task = new DownloadWebpageTask(getApplicationContext());
        RequestData requestData = new RequestData(ServerSideAPIKey.searchKeyValue, ServerSideURL.searchKeyValue());
        task.execute(requestData);

        initLocalVideoThumbnail();
        /*
        PreStoreVideoThumbnailTask storeVideoThumbnailTask = new PreStoreVideoThumbnailTask();
        storeVideoThumbnailTask.context = getApplicationContext();
        storeVideoThumbnailTask.execute("");
        */
        //storeVideoThumbnailTask.cancel(true);

    }
    public boolean didGetData(ResponseData data)
    {
        //ResponseData d = data;
        JSONArray jsonData;
        try
        {
            SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(getApplicationContext().getString(R.string.preference_file_key), Context.MODE_PRIVATE);//_downLoadContext.getPreferences(R.string.preference_file_key,Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            jsonData = data.data.getJSONArray("Data");

            for(int i = 0; i < jsonData.length(); i++)
            {
                //JSONObject object = jsonData.getJSONObject(i);
                JSONObject object;
                try {

                    object = jsonData.getJSONObject(i);

                    editor.putString(object.getString("Name"),object.getString("Url"));


                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }
            }
            editor.commit();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }



        return true;
    }
    public static void initImageLoader(Context context) {
        // This configuration tuning is custom. You can tune every option, you may tune some of them,
        // or you can create default configuration by
        //  ImageLoaderConfiguration.createDefault(this);
        // method.
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
        config.threadPriority(Thread.NORM_PRIORITY - 2);
        config.denyCacheImageMultipleSizesInMemory();
        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
        config.diskCacheSize(50 * 1024 * 1024); // 50 MiB
        config.tasksProcessingOrder(QueueProcessingType.LIFO);
        config.writeDebugLogs(); // Remove for release app

        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config.build());
    }

    public void initLocalVideoThumbnail()
    {

        String[] thumbColumns = { MediaStore.Video.Thumbnails.DATA,
                MediaStore.Video.Thumbnails.VIDEO_ID };


        String[] mediaColumns = { MediaStore.Video.Media._ID,
                MediaStore.Video.Media.DATA, MediaStore.Video.Media.TITLE,
                MediaStore.Video.Media.MIME_TYPE,
                MediaStore.Video.Media.DISPLAY_NAME };


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

                sysVideoList.add(info);
                /*

                GetVideoThumbnailTask task = new GetVideoThumbnailTask();
                task.context = getApplicationContext();

                task.execute(info.Path);
                */





            } while (cursor.moveToNext());
            //Toast.makeText(this, String.valueOf(sysVideoList.size()) , 1).show();
        }
    }
}
