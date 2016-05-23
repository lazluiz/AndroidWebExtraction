package com.zelius.webext.adapter;

/**********************************************
 * Created by Luiz F. Lazzarin on 23/03/2015.
 * Email: lf.lazzarin@gmail.com
 * Github: /luizfelippe
 *
 ***************************Feel free to use it.
 */

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.zelius.webext.R;
import com.zelius.webext.model.SectionModel;

import java.util.ArrayList;

public class SectionsMenuAdapter extends ArrayAdapter<SectionModel> {

    private Activity mContext;
    private ArrayList<SectionModel> mData;

    public SectionsMenuAdapter(Context context, int textViewResourceId, ArrayList<SectionModel> data) {
        super(context, textViewResourceId, data);

        mContext = (Activity) context;
        mData = data;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = mContext.getLayoutInflater().inflate(R.layout.item_main_menu, parent, false);
        }

        TextView txtMenuLabel = (TextView) convertView.findViewById(android.R.id.text1);

        txtMenuLabel.setText(mData.get(position).getLabel());
        convertView.setTag(mData.get(position).getUrl());

        return convertView;
    }
}
