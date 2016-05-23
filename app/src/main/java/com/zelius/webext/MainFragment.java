package com.zelius.webext;

import android.app.Fragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zelius.webext.adapter.PostItemAdapter;
import com.zelius.webext.model.PostData;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Luiz F. Lazzarin on 17/05/2016.
 * Email: lf.lazzarin@gmail.com
 * Github: /luizfelippe
 *
 * Feel free to use it.
 */

public class MainFragment extends Fragment {

    public static final String EXTRA_SECTION_URL = "section_url";
    public static final String EXTRA_SECTION_TITLE = "section_title";

    SwipeRefreshLayout mSwipeView;
    LinearLayout mNoConnPanelView;
    RelativeLayout mLoadingPanelView;
    ListView mListView;

    public MainFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        mNoConnPanelView = (LinearLayout) rootView.findViewById(R.id.no_conn_panel);
        mLoadingPanelView = (RelativeLayout) rootView.findViewById(R.id.loading_panel);
        mListView = (ListView) rootView.findViewById(R.id.list_view);
        mSwipeView = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_container);


        mNoConnPanelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadData();
            }
        });

        setupSwipeRefresh();
        setupFragmentList(container);

        return rootView;
    }

    private void setupSwipeRefresh() {
        mSwipeView.setColorScheme(R.color.theme, R.color.theme_light, R.color.theme_dark, R.color.black);
        mSwipeView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();
            }
        });
    }

    private void setupFragmentList(ViewGroup root) {
        View header = getActivity().getLayoutInflater().inflate(R.layout.header_list_feed, null, false);

        TextView headerTitle = (TextView) header.findViewById(R.id.txtTitle);
        headerTitle.setText(getArguments().getString(EXTRA_SECTION_TITLE));

        mListView.addHeaderView(header, null, false);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PostData item = (PostData) mListView.getItemAtPosition(position);

                Intent intent = new Intent(getActivity(), DetailActivity.class);
                intent.putExtra(DetailActivity.EXTRA_POST_LINK, item.link);
                getActivity().startActivity(intent);
            }
        });
        loadData();
    }


    public void loadData() {
        try {
            mNoConnPanelView.setVisibility(View.GONE);
            mListView.setAdapter(null);

            TaskReadRSSData rssTest = new TaskReadRSSData();
            rssTest.execute(new URL(getArguments().getString(EXTRA_SECTION_URL)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void afterReadRSSData(ArrayList<PostData> output) {
        if (output.isEmpty()) {
            mNoConnPanelView.setVisibility(View.VISIBLE);
        } else {
            mListView.setAdapter(new PostItemAdapter(getActivity(), R.layout.item_feed, output));
            mLoadingPanelView.setVisibility(View.GONE);
            mSwipeView.setRefreshing(false);
        }
    }

    private class TaskReadRSSData extends AsyncTask<URL, Integer, ArrayList<PostData>> {

        static final int RSS_IGNORE_TAG = 0;
        static final int RSS_TITLE = 1;
        static final int RSS_DATE = 2;
        static final int RSS_LINK = 3;

        @Override
        protected ArrayList<PostData> doInBackground(URL... urls) {

            InputStream inputStream;
            ArrayList<PostData> postDataList = new ArrayList<PostData>();
            try {
                HttpURLConnection connection = (HttpURLConnection) urls[0].openConnection();
                connection.setReadTimeout(10 * 1000);
                connection.setConnectTimeout(10 * 1000);
                connection.setRequestMethod("GET");
                connection.setDoInput(true);
                connection.connect();

                inputStream = connection.getInputStream();

                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(true);
                XmlPullParser xpp = factory.newPullParser();
                xpp.setInput(inputStream, null);

                int eventType = xpp.getEventType();
                PostData postData = null;
                SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z", Locale.US);
                SimpleDateFormat newDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
                SimpleDateFormat newTimeFormat = new SimpleDateFormat("HH:mm", Locale.US);

                int currentTag = RSS_IGNORE_TAG;
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    switch (eventType){
                        case XmlPullParser.START_DOCUMENT: break;

                        case XmlPullParser.START_TAG: {
                            if (xpp.getName().equals("item")) {
                                postData = new PostData();
                                currentTag = RSS_IGNORE_TAG;
                            } else if (xpp.getName().equals("title")) {
                                currentTag = RSS_TITLE;
                            } else if (xpp.getName().equals("link")) {
                                currentTag = RSS_LINK;
                            } else if (xpp.getName().equals("pubDate")) {
                                currentTag = RSS_DATE;
                            }
                            break;
                        }

                        case XmlPullParser.END_TAG: {
                            if (xpp.getName().equals("item")) {
                                Date postDate = dateFormat.parse(postData.date); //pdData.date;//
                                postData.date = newDateFormat.format(postDate);
                                postData.time = newTimeFormat.format(postDate);
                                postDataList.add(postData);
                            } else {
                                currentTag = RSS_IGNORE_TAG;
                            }
                            break;
                        }

                        case XmlPullParser.TEXT: {
                            String content = xpp.getText();
                            content = content.trim();
                            if (postData != null) {
                                switch (currentTag) {
                                    case RSS_TITLE:
                                        if (content.length() != 0) {
                                            if (postData.title != null) {
                                                postData.title += content;
                                            } else {
                                                postData.title = content;
                                            }
                                        }
                                        break;
                                    case RSS_LINK:
                                        if (content.length() != 0) {
                                            if (postData.link != null) {
                                                postData.link += content;
                                            } else {
                                                postData.link = content;
                                            }
                                        }
                                        break;
                                    case RSS_DATE:
                                        if (content.length() != 0) {
                                            if (postData.date != null) {
                                                postData.date += content;
                                            } else {
                                                postData.date = content;
                                            }
                                            if (postData.time != null) {
                                                postData.time += content;
                                            } else {
                                                postData.time = content;
                                            }
                                        }
                                        break;
                                    default:
                                        break;
                                }
                            }
                            break;
                        }
                    }
                    eventType = xpp.next();
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }

            return postDataList;
        }

        @Override
        protected void onPostExecute(ArrayList<PostData> result) {
            if (result != null) {
                afterReadRSSData(result);
            }
        }
    }
}
