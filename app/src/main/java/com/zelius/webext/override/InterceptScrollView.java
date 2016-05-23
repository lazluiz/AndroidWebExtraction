package com.zelius.webext.override;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * Created by Zelius on 01/04/2015.
 */


public class InterceptScrollView extends ScrollView {

    public InterceptScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
//
//    @Override
//    public boolean onInterceptTouchEvent(MotionEvent ev) {
//        return false;
//    }

}