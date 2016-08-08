package com.tdtm.lei.my3dvideo3;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class SearchActivity extends AppCompatActivity {
    private EditText mSearchEditText;
    private Context mContext;
    private ListView mListView;
    private List<OnLineVideo> mResultList;

    private Button mCancelButton;

    private void initSearchPrefix()
    {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_search);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mContext = this;

        mSearchEditText = (EditText) findViewById(R.id.searchEditText);

        Drawable drawableFind = getResources().getDrawable(R.drawable.search_left_view);
        drawableFind.setBounds(30, 0, 69, 39);

        //Drawable drawableClean = getResources().getDrawable(R.drawable.clean);
        //drawableClean.setBounds(-10, 0, 35, 45);
        mSearchEditText.setCompoundDrawables(drawableFind, null, null, null);

       //mSearchEditText.setCompoundDrawables(drawable1, null, drawable1, null);



        mSearchEditText.setFocusable(true);
        mSearchEditText.setFocusableInTouchMode(true);
        mSearchEditText.requestFocus();

        mCancelButton = (Button)findViewById(R.id.cancel_search);
        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        Timer timer = new Timer();
        timer.schedule(new TimerTask() {

            public void run() {
                InputMethodManager inputManager = (InputMethodManager) mSearchEditText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.showSoftInput(mSearchEditText, 0);
            }

        }, 500);

        //initSearchPrefix();
        final SharedPreferences sharedPref = this.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);

        mResultList = new ArrayList();
        mListView = (ListView) findViewById(R.id.listViewSearchVideo);
        mContext = this;
        mSearchEditText.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {

                String prefix = mSearchEditText.getText().toString();

                mResultList.clear();

                Map<String, ?> keys = sharedPref.getAll();

                if (prefix.length() != 0) {
                    Drawable drawableFind = getResources().getDrawable(R.drawable.search_left_view);
                    drawableFind.setBounds(30, 0, 69, 39);
                    Drawable drawableClean = getResources().getDrawable(R.drawable.clean);
                    drawableClean.setBounds(-10,0,35,45);
                    mSearchEditText.setCompoundDrawables(drawableFind, null, drawableClean, null);

                    for (Map.Entry<String, ?> entry : keys.entrySet()) {
                        //entry.getKey().toLowerCase().contains(prefix.toLowerCase())
                        if (entry.getKey().toLowerCase().contains(prefix.toLowerCase())) {
                            //System.out.println(entry.getKey());
                            //mResultList.add(entry.getKey());
                            try {


                                OnLineVideo onLineVideo = new OnLineVideo(entry.getKey().toString(), entry.getValue().toString(), null);
                                mResultList.add(onLineVideo);
                            } catch (Exception ex) {

                            }
                        }

                    }
                }

                //sharedPref.getAll()


                SearchListAdapter listAdapter = new SearchListAdapter(mContext, mResultList);
                //mListView.setAdapter(new ArrayAdapter<String>(mContext,android.R.layout.simple_list_item_1,mResultList));
                mListView.setAdapter(listAdapter);
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {


                Intent intent = new Intent(mContext, MainActivity.class);
                String url = mResultList.get(arg2).url;
                String title = mResultList.get(arg2).name;
                //intent.putExtra(Constants.Extra.FRAGMENT_INDEX, ImagePagerFragment.INDEX);
                intent.putExtra(HomeActivity.EXTRA_URL, url);
                intent.putExtra(HomeActivity.VIDEO_TITLE,title);
                mContext.startActivity(intent);

                /*
                System.out.println(mResultList.get(arg2));
                SharedPreferences sharedPref = mContext.getSharedPreferences(mContext.getString(R.string.mySelectedKey), Context.MODE_PRIVATE);//_downLoadContext.getPreferences(R.string.preference_file_key,Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                Stock stockItem = (Stock) mResultList.get(arg2);
                editor.putString(stockItem.stockCode, stockItem.stockName);
                editor.commit();
                //List selectedList = (List)sharedPref.ge

                Intent intent = new Intent(SearchStockActivity.this, MainActivity.class);

                intent.putExtra(MainActivity.EXTRA_MESSAGE, mResultList.get(arg2).toString());

                startActivity(intent);
                finish();
                */
            }

        });
    }

}
