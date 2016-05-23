package com.zelius.webext.async;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.text.Html;
import android.widget.TextView;

import com.zelius.webext.override.URLBitmapDrawable;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.InputStream;

/**
 * Created by Luiz F. Lazzarin on 29/03/2015.
 * Email: lf.lazzarin@gmail.com
 * Github: /luizfelippe
 *
 * Feel free to use it.
 */
public class URLImageParser implements Html.ImageGetter {

    Context mContext;
    TextView mContainer;

    /**
     * Construct the URLImageParser which will execute AsyncTask and refresh the container
     *
     * @param container
     * @param context
     */
    public URLImageParser(TextView container, Context context) {
        this.mContext = context;
        this.mContainer = container;
    }

    public Drawable getDrawable(String source) {
        URLBitmapDrawable urlDrawable = new URLBitmapDrawable(mContext);

        ImageGetterAsyncTask asyncTask = new ImageGetterAsyncTask(urlDrawable);
        asyncTask.execute(source);

        return urlDrawable;
    }

    public class ImageGetterAsyncTask extends AsyncTask<String, Void, Drawable> {

        URLBitmapDrawable mURLDrawable;

        public ImageGetterAsyncTask(URLBitmapDrawable d) {
            this.mURLDrawable = d;
        }

        @Override
        protected Drawable doInBackground(String... params) {
            try {

                DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpGet request = new HttpGet(params[0]);
                HttpResponse response = httpClient.execute(request);
                InputStream is = response.getEntity().getContent();

                Drawable drawable = Drawable.createFromStream(is, "src");

                //drawable.setBounds(0, 0, 0 + drawable.getIntrinsicWidth(), 0 + drawable.getIntrinsicHeight());
                return drawable;
            } catch (Exception e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(Drawable result) {
            if(result != null){
                // set the correct bound according to the result from HTTP call
                mURLDrawable.setBounds(0, 0, result.getIntrinsicWidth(), result.getIntrinsicHeight());

                // change the reference of the current drawable to the result
                // from the HTTP call
                mURLDrawable.setDrawable(result);

                // redraw the image by invalidating the container
                URLImageParser.this.mContainer.invalidate();

                // For ICS
                URLImageParser.this.mContainer.setHeight((URLImageParser.this.mContainer.getHeight() + result.getIntrinsicHeight()));

                // Pre ICS
                URLImageParser.this.mContainer.setEllipsize(null);
            }
        }
    }
}