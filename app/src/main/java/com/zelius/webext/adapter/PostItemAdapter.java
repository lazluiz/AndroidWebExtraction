package com.zelius.webext.adapter;

/**
 * Created by Luiz F. Lazzarin on 24/03/2015.
 * Email: lf.lazzarin@gmail.com
 * Github: /luizfelippe
 *
 * Feel free to use it.
 */

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.zelius.webext.R;
import com.zelius.webext.model.PostData;

import java.util.ArrayList;

public class PostItemAdapter extends ArrayAdapter<PostData> {

    private Activity mContext;
    private ArrayList<PostData> mData;

    public PostItemAdapter(Context context, int textViewResourceId, ArrayList<PostData> data) {
        super(context, textViewResourceId, data);

        mContext = (Activity) context;
        mData = data;
    }

    static class ViewHolder {
        TextView txtPostTitle;
        TextView txtPostDate;
        TextView txtPostTime;

        public ViewHolder(View view){
            txtPostTitle = (TextView) view.findViewById(R.id.txtTitle);
            txtPostDate = (TextView) view.findViewById(R.id.txtDate);
            txtPostTime = (TextView) view.findViewById(R.id.txtTime);
        }
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = mContext.getLayoutInflater().inflate(R.layout.item_feed, parent, false);
        }

        ViewHolder viewHolder;
        if(convertView.getTag() == null){
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        PostData item = mData.get(position);
        viewHolder.txtPostTitle.setText(item.title);
        viewHolder.txtPostDate.setText(item.date);
        viewHolder.txtPostTime.setText(item.time);

        return convertView;
    }
}
