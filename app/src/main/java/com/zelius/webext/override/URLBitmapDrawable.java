package com.zelius.webext.override;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;

/**
 * Created by Zelius on 29/03/2015.
 */
public class URLBitmapDrawable extends BitmapDrawable {

    private Drawable drawable;
    private Context context;
    private DisplayMetrics metrics;

    public URLBitmapDrawable(Context context) {
        this.context = context;
        this.metrics = context.getResources().getDisplayMetrics();
        //setDrawable(context.getResources().getDrawable(R.drawable.loading_post_image));
    }

    public void setDrawable(Drawable drawable) {
        this.drawable = drawable;

        this.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        this.drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
    }

    @Override
    public void draw(Canvas canvas) {

        if (drawable != null) {
            drawable.draw(canvas);
        }
    }
}