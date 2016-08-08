package com.tdtm.lei.my3dvideo3;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.provider.MediaStore;

import java.io.FileNotFoundException;

/**
 * Created by lei on 7/19/16.
 */
public class StoreImageTask extends AsyncTask<String ,Void,Boolean> {

    public Context context;
    @Override
    protected Boolean doInBackground(String ... filePaths) {

        // params comes from the execute() call: params[0] is the url.
        try {

            try {
                SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.local_video_thumbnail_key),Context.MODE_PRIVATE);//_downLoadContext.getPreferences(R.string.preference_file_key,Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();

                String bitPath = MediaStore.Images.Media.insertImage(context.getContentResolver(), filePaths[0], "", null);

                editor.putString(filePaths[0], bitPath);
                editor.commit();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            return true;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    protected void onPostExecute(Boolean is) {


    }
}
