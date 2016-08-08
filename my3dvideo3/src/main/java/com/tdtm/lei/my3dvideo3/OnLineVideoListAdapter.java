package com.tdtm.lei.my3dvideo3;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
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

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

import ServerSide.DownloadWebpageTask;
import ServerSide.RequestData;
import ServerSide.ResponseData;
import ServerSide.ServerSideAPIKey;
import ServerSide.ServerSideURL;

/**
 * Created by lei on 6/1/16.
 */
public class OnLineVideoListAdapter extends BaseAdapter implements View.OnClickListener, DownloadWebpageTask.FinishGetDataListener {

    private Context context;
    private JSONArray dataSource;
    private LayoutInflater listContainer;

    private DisplayImageOptions options;

    private  EditText editText;

    private View selectedView;
    private OnLineVideoListAdapter mAdapter;

    private String mCheckNum;




    public OnLineVideoListAdapter(Context context, JSONArray dataSource) {
        mAdapter = this;
        this.context = context;
        //this.dataSource = dataSource;
        listContainer = LayoutInflater.from(context);

        this.dataSource = dataSource;

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
        return (dataSource.length() + 1) / 2;
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

    @Override
    public View getView(int arg0, View arg1, ViewGroup arg2) {
        // TODO Auto-generated method stub
        //arg1 = listContainer.inflate(R.layout.stock_data_cell_view, null);





        if (arg1 == null)
        {
            arg1 = listContainer.inflate(R.layout.video_list_item, null);
            //return arg1;
        }


        //arg1 = listContainer.inflate(R.layout.video_list_item, null);


        //arg1 = listContainer.inflate(R.layout.video_list_item, null);




        JSONObject leftRowData;// = dataSource.getJSONObject(arg0);
        JSONObject rightRowData;
        ImageView leftImageView = (ImageView)arg1.findViewById(R.id.onLineViewItemLeft);
        ImageView rightImageView = (ImageView)arg1.findViewById(R.id.onLineViewItemRight);

        TextView leftTextView = (TextView)arg1.findViewById(R.id.onLineViewItemLeftName);
        TextView rightTextView = (TextView)arg1.findViewById(R.id.onLineViewItemRightName);
        String leftImageURL = "";
        String rightImageURL = "";
        String leftName = "";
        String rightName = "";

        String leftURL;
        String rightURL;


        try
        {
            leftRowData = dataSource.getJSONObject(2 * arg0);
            leftImageURL = leftRowData.getString("Image");
            leftName = leftRowData.getString("Name");
            leftTextView.setText(leftName);
            leftURL = leftRowData.getString("Url");
            LinearLayout leftLayout = (LinearLayout)arg1.findViewById(R.id.onLineViewItemLeftLayout);
            leftLayout.setTag(leftRowData);
            leftLayout.setOnClickListener(this);
            /*
            if (!leftImageView.getTag().equals(leftImageURL))
            {
                leftImageView.setTag(leftImageURL);
                ImageLoader.getInstance().displayImage(leftImageURL, leftImageView, options, null);

            }
            */
            leftImageView.setTag(leftImageURL);
            //leftImageURL = "content://media/external/images/media/38944";
            ImageLoader.getInstance().displayImage(leftImageURL, leftImageView, options, null);
            if ((2*arg0 + 1) < dataSource.length())
            {
                rightRowData = dataSource.getJSONObject(2 * arg0 + 1);
                rightImageURL = rightRowData.getString("Image");
                rightName = rightRowData.getString("Name");
                rightTextView.setText(rightName);
                rightURL = rightRowData.getString("Url");
                LinearLayout rightLayout = (LinearLayout)arg1.findViewById(R.id.onLineViewItemRightLayout);
                rightLayout.setTag(rightRowData);
                rightLayout.setOnClickListener(this);
                ImageLoader.getInstance().displayImage(rightImageURL, rightImageView, options, null);
            }
            else
            {
                rightImageView.setVisibility(View.GONE);
                rightTextView.setVisibility(View.GONE);
            }



        }
        catch (Exception ex)
        {
            //rowData = null;
        }





        return arg1;
    }



    @Override
    public void onClick(View view)
    {
        selectedView = view;
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









    }
    public void enterPlayInterface()
    {
        JSONObject videoData = (JSONObject)selectedView.getTag();



        Intent intent = new Intent(context, MainActivity.class);
        //intent.putExtra(Constants.Extra.FRAGMENT_INDEX, ImagePagerFragment.INDEX);

        try
        {
            String url = videoData.getString("Url");
            String name = videoData.getString("Name");
            intent.putExtra(HomeActivity.EXTRA_URL,url);
            intent.putExtra(HomeActivity.VIDEO_TITLE,name);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }


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
