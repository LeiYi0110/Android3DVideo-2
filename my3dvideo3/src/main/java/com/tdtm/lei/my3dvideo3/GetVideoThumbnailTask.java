package com.tdtm.lei.my3dvideo3;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import ServerSide.RequestData;
import ServerSide.ResponseData;

/**
 * Created by lei on 6/11/16.
 */
public class GetVideoThumbnailTask extends AsyncTask<GetThumbnailInfo ,Void,Bitmap> {
    public FinishGetBitmapListener mCallbac;

    public Context context;

    public interface FinishGetBitmapListener {
        public boolean didGetBitmap(Bitmap bitmap);
    }

    @Override
    protected Bitmap doInBackground(GetThumbnailInfo ... infos) {

        // params comes from the execute() call: params[0] is the url.
        try {

            /*
            final SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.local_video_thumbnail_key), Context.MODE_PRIVATE);
            String filePath = sharedPref.getString(filePaths[0],"");
            if (filePath.length() == 0)
            {
                //return getVideoThumbnailFromImagePath(filePath);
                return getVideoThumbnail(filePaths[0]);
            }
            */


            return getVideoThumbnail(infos[0]);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        //textView.setText(result);
        //ResponseData data = result;
        if (mCallbac != null)
        {
            mCallbac.didGetBitmap(bitmap);
        }

    }

    private Bitmap getVideoThumbnailFromImagePath(String filePath) {

        try
        {
            FileInputStream fs = new FileInputStream(filePath);
            BufferedInputStream bs = new BufferedInputStream(fs);
            Bitmap btp = BitmapFactory.decodeStream(bs);
            //imageView.setImageBitmap(btp);
            bs.close();
            fs.close();
            //btp = null;
            return btp;
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            return null;
        }


    }


    private Bitmap getVideoThumbnail(GetThumbnailInfo info) {
        Bitmap bitmap = null;
        String filePath = info.filePath;
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            retriever.setDataSource(filePath);
            bitmap = retriever.getFrameAtTime();




            if (bitmap != null)
            {
                String fileName = String.valueOf((int)(1+Math.random()*(10000))) + ".jpg";//filePath + ".jpg";//String.valueOf((int)(1+Math.random()*(10000))) + ".jpg";
                OutputStream fOut = null;
                File file = new File(context.getFilesDir(), fileName); // the File to save to





                //fileName = file.getAbsolutePath();
                try
                {
                    fOut = new FileOutputStream(file);


                    bitmap.compress(Bitmap.CompressFormat.JPEG, 10, fOut); // saving the Bitmap to a file compressed as a JPEG with 85% compression rate

                    SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.local_video_thumbnail_key),Context.MODE_PRIVATE);//_downLoadContext.getPreferences(R.string.preference_file_key,Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();

                    //editor.putString(filePath, file.getAbsolutePath());
                    //editor.commit();


                    bitmap = null;
                    fOut.flush();
                    fOut.close();


                    if (info.storeImageSoon)
                    {
                        try {
                            String bitPath = MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), fileName, null);

                            editor.putString(filePath, bitPath);
                            editor.commit();

                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }
                    /*
                    else
                    {
                        StoreImageTask storeImageTask = new StoreImageTask();
                        storeImageTask.context = this.context;
                        storeImageTask.execute(file.getAbsolutePath());
                    }
                    */



                    /*
                    try {
                        String bitPath = MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), fileName, null);

                        editor.putString(filePath, bitPath);
                        editor.commit();

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    */



                    //8022.jpg
                    ///data/data/com.tdtm.lei.my3dvideo3/files/8022.jpg
                    //content://media/external/images/media/38912

                    /*
                    StoreImageTask storeImageTask = new StoreImageTask();
                    storeImageTask.context = this.context;
                    storeImageTask.execute(file.getAbsolutePath());
                    */

                    return getVideoThumbnailFromImagePath(file.getAbsolutePath());

                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }





            }



        }
        catch(IllegalArgumentException e) {
            e.printStackTrace();
        }
        catch (RuntimeException e) {
            e.printStackTrace();
        }
        finally {
            try {
                retriever.release();
            }
            catch (RuntimeException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

}
