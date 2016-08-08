package com.tdtm.lei.my3dvideo3;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by lei on 6/11/16.
 */
public class VideoThumbnailHelper implements GetVideoThumbnailTask.FinishGetBitmapListener{

    public ImageView imageView;

    public Context context;

    public VideoThumbnailHelper(Context context)
    {
        this.context = context;
    }

    public void loadImage(ImageView imageView, String filePath)
    {
        this.imageView = imageView;
        GetVideoThumbnailTask task = new GetVideoThumbnailTask();
        task.context = this.context;
        task.mCallbac = this;
        GetThumbnailInfo info = new GetThumbnailInfo();
        info.filePath = filePath;
        info.storeImageSoon = false;
        task.execute(info);
    }
    public boolean didGetBitmap(Bitmap bitmap)
    {
        if (bitmap != null)
        {
            this.imageView.setImageBitmap(bitmap);
        }
        else
        {
            this.imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.defual_local_video));
        }

        this.imageView.setVisibility(View.VISIBLE);

        return true;
    }
}
