package com.tdtm.lei.my3dvideo3;

import android.graphics.Bitmap;

/**
 * Created by lei on 6/9/16.
 */
public class VideoInfo {
    public String ThumbPath;
    public String Path;
    public String Title;
    public String DisplayName;
    public String MimeType;

    public Bitmap bitmap;

    public void setThumbPath(String ThumbPath)
    {
        this.ThumbPath = ThumbPath;
    }
    public void setPath(String Path)
    {
        this.Path = Path;
    }
    public void setTitle(String Title)
    {
        this.Title = Title;
    }
    public void setDisplayName(String DisplayName)
    {
        this.DisplayName = DisplayName;
    }
    public void setMimeType(String MimeType)
    {
        this.MimeType = MimeType;
    }
    public void setBitmap(Bitmap bitmap)
    {
        this.bitmap = bitmap;
    }
}
