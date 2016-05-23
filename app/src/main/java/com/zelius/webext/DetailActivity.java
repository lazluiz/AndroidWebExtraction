package com.zelius.webext;

/**
 * Created by Luiz Lazzarin, aka Zelius on 27/03/2015.
 * Email: lf.lazzarin@gmail.com
 * Github: /luizfelippe
 *
 * Feel free to use it.
 */

import android.app.ActionBar;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zelius.webext.async.URLImageParser;
import com.zelius.webext.data.DataWebsite;
import com.zelius.webext.model.ContentModel;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public class DetailActivity extends Activity {

    public static final String EXTRA_POST_LINK = "post_link";

    RelativeLayout mLoadingPanel;
    TextView mTxtActionBarTitle;

//    GestureDetector mGestureDetector;
//    ScaleGestureDetector mScaleGestureDetector;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.MyTheme);
        setContentView(R.layout.activity_read_post);


        mLoadingPanel = (RelativeLayout) findViewById(R.id.loadingPanel);


//        mGestureDetector = new GestureDetector(this, new GestureListener());
//        mScaleGestureDetector = new ScaleGestureDetector(this, new ScaleGestureListener());

        View actionBarView = getLayoutInflater().inflate(R.layout.actionbar_post, null);
        mTxtActionBarTitle = (TextView) actionBarView.findViewById(R.id.txtTitle);

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setCustomView(actionBarView);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);


        loadData();
    }


    void loadData() {
        mLoadingPanel.setVisibility(View.VISIBLE);

        String url = getIntent().getExtras().getString(EXTRA_POST_LINK);

        TaskReadPostData taskReadPostData = new TaskReadPostData();
        taskReadPostData.execute(url);
    }

    public void afterReadPostData(Map<DataWebsite.EnumContents, String> output) {
        TextView txtHeaderDate = (TextView) findViewById(R.id.txtHeaderDate);
        TextView txtTitle = (TextView) findViewById(R.id.txtTitle);
        TextView txtSubtitle = (TextView) findViewById(R.id.txtSubtitle);
        TextView txtPicture = (TextView) findViewById(R.id.txtPicture);
        TextView txtBody = (TextView) findViewById(R.id.txtBody);
        TextView txtFooter = (TextView) findViewById(R.id.txtFooter);

        try {
            for (Map.Entry<DataWebsite.EnumContents, String> content : output.entrySet()) {
                switch (content.getKey()) {
                    case HEADER_CATEGORY:
                        mTxtActionBarTitle.setText(String.format(
                                getResources().getString(R.string.activity_read_title_format), Html.fromHtml(content.getValue())
                        ));
                        break;
                    case HEADER_DATE:
                        txtHeaderDate.setText(Html.fromHtml(content.getValue()));
                        break;
                    case TITLE:
                        txtTitle.setText(Html.fromHtml(content.getValue()));
                        break;
                    case SUBTITLE:
                        txtSubtitle.setText(Html.fromHtml(content.getValue()));
                        txtSubtitle.setVisibility(content.getValue().isEmpty() ? View.GONE : View.VISIBLE);
                        break;
                    case PICTURE:
                        URLImageParser urlParsePicture = new URLImageParser(txtPicture, this);
                        txtPicture.setText(Html.fromHtml(content.getValue(), urlParsePicture, null));
                        txtPicture.setVisibility(content.getValue().isEmpty() ? View.GONE : View.VISIBLE);
                        break;
                    case BODY:
                        URLImageParser urlParseBody = new URLImageParser(txtBody, this);
                        txtBody.setText(Html.fromHtml(content.getValue(), urlParseBody, null));
                        txtBody.setMovementMethod(LinkMovementMethod.getInstance());
                        break;
                    case FOOTER:
                        txtFooter.setText(Html.fromHtml(content.getValue()));
                        break;
                }

                mLoadingPanel.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class TaskReadPostData extends AsyncTask<String, Integer, Map<DataWebsite.EnumContents, String>> {

        @Override
        protected Map<DataWebsite.EnumContents, String> doInBackground(String... urls) {
            Map<DataWebsite.EnumContents, String> contentList = new LinkedHashMap<DataWebsite.EnumContents, String>();
            try {
                HttpGet get = new HttpGet(urls[0]);

                DefaultHttpClient client = new DefaultHttpClient();
                HttpResponse resp = client.execute(get);

                String content = EntityUtils.toString(resp.getEntity());
                Document doc = Jsoup.parse(content);

                for (ContentModel item : DataWebsite.dataContentSelector) {
                    String value;
                    if (item.getContent() == DataWebsite.EnumContents.PICTURE) {
                        value = doc.select(item.getSelector()).toString();
                    } else if (item.getContent() == DataWebsite.EnumContents.BODY) {
                        value = doc.select(item.getSelector()).toString();
//                    Document bodyContent = Jsoup.parse(value);
//
//                    Elements eleTest = bodyContent.select("img");
                    } else {
                        value = doc.select(item.getSelector()).text();
                    }
                    contentList.put(item.getContent(), value);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return contentList;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);

            Log.i("BUSCANDO CONTEUDO...", "Valor: " + String.valueOf(values));
        }

        @Override
        protected void onPostExecute(Map<DataWebsite.EnumContents, String> result) {
            if (result != null) {
                afterReadPostData(result);
            }
        }
    }

    public void backButtonOnClick(View v) {
        finish();
    }

//    @Override
//    public boolean dispatchTouchEvent(MotionEvent event) {
//        super.dispatchTouchEvent(event);
//        mScaleGestureDetector.onTouchEvent(event);
//        mGestureDetector.onTouchEvent(event);
//        return mGestureDetector.onTouchEvent(event);
//    }

//    private class GestureListener extends GestureDetector.SimpleOnGestureListener {
//        @Override
//        public boolean onDown(MotionEvent e) {
//            return true;
//        }
//        // event when double tap occurs
//        @Override
//        public boolean onDoubleTap(MotionEvent e) {
//            // double tap fired.
//            return true;
//        }
//    }
//
//    public class ScaleGestureListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
//
//        @Override
//        public boolean onScale(ScaleGestureDetector detector) {
//            float size = mTxtBody.getTextSize();
//            Log.d("TextSizeStart", String.valueOf(size));
//
//            float factor = detector.getScaleFactor();
//            Log.d("Factor", String.valueOf(factor));
//
//
//            float product = size * factor;
//            Log.d("TextSize", String.valueOf(product));
//            mTxtBody.setTextSize(TypedValue.COMPLEX_UNIT_PX, product);
//
//            size = mTxtBody.getTextSize();
//            Log.d("TextSizeEnd", String.valueOf(size));
//            return true;
//        }
//    }
}


