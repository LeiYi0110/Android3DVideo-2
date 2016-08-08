package com.tdtm.lei.my3dvideo3;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import ServerSide.DownloadWebpageTask;
import ServerSide.RequestData;
import ServerSide.ResponseData;
import ServerSide.ServerSideAPIKey;
import ServerSide.ServerSideURL;

/**
 * Created by lei on 6/9/16.
 */
public class LocalVideoListAdapter extends BaseAdapter implements View.OnClickListener, DownloadWebpageTask.FinishGetDataListener {

    private Context context;
    private List<VideoInfo> dataSource;
    private LayoutInflater listContainer;

    private  final SharedPreferences sharedPref;// = this.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);

    private DisplayImageOptions options;

    private VideoInfo videoInfo;

    private  EditText editText;

    private String mCheckNum;

    private LocalVideoListAdapter mAdapter;

    public LocalVideoListAdapter(Context context, List<VideoInfo> dataSource)
    {
        this.mAdapter = this;
        this.context = context;
        this.dataSource = dataSource;

        listContainer = LayoutInflater.from(context);
        sharedPref = this.context.getSharedPreferences(this.context.getString(R.string.local_video_thumbnail_key), Context.MODE_PRIVATE);
        options = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.drawable.ic_empty)
                .showImageOnFail(R.drawable.ic_error)
                .resetViewBeforeLoading(true)
                .cacheOnDisk(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .considerExifParams(true)
                .displayer(new FadeInBitmapDisplayer(1000))
                        //.displayer(new FadeInBitmapDisplayer(300))
                .build();


    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub

        if (dataSource.size() == 0)
            return 0;
        return (dataSource.size() - 1)/ 3 + 1;
    }

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return 0;
    }
    private void setImage(ImageView imageView,int dataIndex)
    {





        VideoThumbnailHelper helper = new VideoThumbnailHelper(context);
        VideoInfo videoInfo = dataSource.get(dataIndex);
        imageView.setTag(videoInfo);
        imageView.setOnClickListener(this);

        final SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.local_video_thumbnail_key), Context.MODE_PRIVATE);
        String fileURL = sharedPref.getString(videoInfo.Path,"");
        if (fileURL.length() > 0)
        {
            ImageLoader.getInstance().displayImage(fileURL, imageView, options, null);
        }
        /*
        else
        {
            //imageView.setVisibility(View.GONE);
            helper.loadImage(imageView,videoInfo.Path);
        }
        */


    }
    @Override
    public View getView(int arg0, View arg1, ViewGroup arg2) {
        // TODO Auto-generated method stub
        //arg1 = listContainer.inflate(R.layout.stock_data_cell_view, null);


        /*
        if (arg1 == null)
        {
            arg1 = listContainer.inflate(R.layout.local_video_list_item, null);
        }
        */


        arg1 = listContainer.inflate(R.layout.local_video_list_item, null);



        //VideoThumbnailHelper helper = new VideoThumbnailHelper(context);
        //VideoInfo videoInfo = dataSource.get(3*arg0);

        ImageView leftImageView = (ImageView)arg1.findViewById(R.id.localViewItemLeft);
        //leftImageView.setVisibility(View.GONE);
        setImage(leftImageView, 3 * arg0);

        int indexMid = 3*arg0 + 1;
        ImageView midImageView = (ImageView)arg1.findViewById(R.id.localViewItemMid);
        if (indexMid < dataSource.size())
        {
            //ImageView midImageView = (ImageView)arg1.findViewById(R.id.localViewItemMid);
            setImage(midImageView,indexMid);

            int indexRight = 3*arg0 + 2;
            ImageView rightImageView = (ImageView)arg1.findViewById(R.id.localViewItemRight);
            if (indexRight < dataSource.size())
            {
                //ImageView rightImageView = (ImageView)arg1.findViewById(R.id.localViewItemRight);
                setImage(rightImageView,indexRight);
            }
            else
            {
                rightImageView.setVisibility(View.GONE);
            }
        }
        else
        {
            midImageView.setVisibility(View.GONE);
        }

        //helper.loadImage(leftImageView,videoInfo.Path);

        //ImageView midImageView = (ImageView)arg1.findViewById(R.id.localViewItemMid);
       // ImageView rightImageView = (ImageView)arg1.findViewById(R.id.localViewItemRight);


        //String thubString = "file://" + videoInfo.ThumbPath;

        //leftImageView.setImageBitmap(getVideoThumbnail(videoInfo.Path));

        return arg1;
    }
    @Override
    public void onClick(View view)
    {
        videoInfo = (VideoInfo)view.getTag();

        enterPlayInterface();


        /*
        editText = new EditText(context);
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();

        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.check_num), Context.MODE_PRIVATE);//_downLoadContext.getPreferences(R.string.preference_file_key,Context.MODE_PRIVATE);
        mCheckNum = sharedPref.getString("checkNum", "");

        if (mCheckNum.length() == 0) {

            new AlertDialog.Builder(context,AlertDialog.THEME_HOLO_LIGHT)
                    .setTitle("请输入序列号")
                    .setView(editText)
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {


                            //editText.setText("f52d-8c1d-09ab-01c6");
                            mCheckNum = editText.getText().toString();
                            DownloadWebpageTask task = new DownloadWebpageTask(context);
                            task.mCallbac = mAdapter;
                            RequestData requestData = new RequestData(ServerSideAPIKey.setCheckNum, ServerSideURL.setCheckNum(mCheckNum));
                            task.execute(requestData);


                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //dialog.dismiss();

                        }
                    })
                    .show();

        }
        else
        {
            enterPlayInterface();
        }


        Timer timer = new Timer();
        timer.schedule(new TimerTask() {

            public void run() {
                InputMethodManager inputManager = (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.showSoftInput(editText, 0);
            }

        }, 500);
        */





        /*
        Intent intent = new Intent(context, MainActivity.class);
        //intent.putExtra(Constants.Extra.FRAGMENT_INDEX, ImagePagerFragment.INDEX);
        intent.putExtra(HomeActivity.EXTRA_URL,videoInfo.Path);
        intent.putExtra(HomeActivity.VIDEO_TITLE,videoInfo.DisplayName);
        context.startActivity(intent);
        */

    }

    public void enterPlayInterface()
    {
        Intent intent = new Intent(context, MainActivity.class);
        //intent.putExtra(Constants.Extra.FRAGMENT_INDEX, ImagePagerFragment.INDEX);
        intent.putExtra(HomeActivity.EXTRA_URL,videoInfo.Path);
        intent.putExtra(HomeActivity.VIDEO_TITLE,videoInfo.DisplayName);
        context.startActivity(intent);
    }

    public boolean didGetData(ResponseData data)
    {
        try
        {
            if (data.data.getInt("Status") == 1)
            {
                SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.check_num), Context.MODE_PRIVATE);//_downLoadContext.getPreferences(R.string.preference_file_key,Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();

                //String check_num = editText.getText().toString();

                editor.putString("checkNum",mCheckNum);
                editor.commit();

                enterPlayInterface();

            }
            else
            {
                Toast toast=Toast.makeText(context, "序列号无效", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

        return true;

    }

}
