package com.tdtm.lei.my3dvideo3;

import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.widget.Toast;

/**
 * Created by lei on 7/19/16.
 */
public class PreStoreVideoThumbnailTask extends AsyncTask<String ,Void,Boolean> {
    public Context context;
    @Override
    protected Boolean doInBackground(String ... filePaths) {

        // params comes from the execute() call: params[0] is the url.
        try {

            initLocalVideoThumbnail();
            return true;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    protected void onPostExecute(Boolean is) {


    }
    private void initLocalVideoThumbnail()
    {

        String[] thumbColumns = { MediaStore.Video.Thumbnails.DATA,
                MediaStore.Video.Thumbnails.VIDEO_ID };


        String[] mediaColumns = { MediaStore.Video.Media._ID,
                MediaStore.Video.Media.DATA, MediaStore.Video.Media.TITLE,
                MediaStore.Video.Media.MIME_TYPE,
                MediaStore.Video.Media.DISPLAY_NAME };


        CursorLoader loader = new CursorLoader(context, MediaStore.Video.Media.EXTERNAL_CONTENT_URI, mediaColumns, null, null, null);
        Cursor cursor = loader.loadInBackground();
        //Cursor cursor = getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, mediaColumns, null, null, null);




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
                /*
                GetVideoThumbnailTask task = new GetVideoThumbnailTask();
                task.context = context;//getApplicationContext();

                task.execute(info.Path);
                */




            } while (cursor.moveToNext());
            //Toast.makeText(this, String.valueOf(sysVideoList.size()) , 1).show();
        }
    }
}
