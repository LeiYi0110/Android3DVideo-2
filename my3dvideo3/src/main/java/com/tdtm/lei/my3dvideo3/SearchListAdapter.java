package com.tdtm.lei.my3dvideo3;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 *
 * Created by lei on 6/11/16.
 */
public class SearchListAdapter extends BaseAdapter {

    private Context mContext;
    private List<OnLineVideo> mResultList;
    private LayoutInflater listContainer;
    public SearchListAdapter(Context context, List<OnLineVideo> list)
    {
        this.mContext = context;
        this.mResultList = list;
        listContainer = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mResultList.size();
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

    public View getView(int arg0, View arg1, ViewGroup arg2) {
        // TODO Auto-generated method stub
        //arg1 = listContainer.inflate(R.layout.stock_data_cell_view, null);
        arg1 = listContainer.inflate(R.layout.search_video_item, null);

        TextView textView = (TextView)arg1.findViewById(R.id.search_video_text_view);
        textView.setText(mResultList.get(arg0).name);






        return arg1;
    }
}
