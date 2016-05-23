package com.zelius.webext.async;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.InputStream;

/**
 * Created by Luiz F. Lazzarin on 24/03/2015.
 * Email: lf.lazzarin@gmail.com
 * Github: /luizfelippe
 *
 * Feel free to use it.
 */

public class AsyncImageDownload extends AsyncTask<String, Void, Bitmap> {
    ImageView mImageView;
    String mURL;

    public AsyncImageDownload(ImageView imageView, String url) {
        this.mImageView = imageView;
        this.mURL = url;
    }

    protected Bitmap doInBackground(String... urls) {
        Bitmap mIcon11 = null;
        try {
            InputStream in = new java.net.URL(mURL).openStream();
            mIcon11 = Bitmap.createScaledBitmap(BitmapFactory.decodeStream(in), 120, 120, true);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        return mIcon11;
    }

    protected void onPostExecute(Bitmap result) {
        mImageView.setImageBitmap(result);
    }
}
