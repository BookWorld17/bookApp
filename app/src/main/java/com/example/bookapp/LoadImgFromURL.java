package com.example.bookapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.lang.ref.WeakReference;
import java.net.Socket;
import java.net.URL;


public class LoadImgFromURL extends AsyncTask<Object, Void, Bitmap> {
    private final WeakReference<ImageView> vRef;
    Socket socket = null;

    int newWidth = 750;
    int newHieght = 600;

    String ip = "";

    Bitmap bmp = null;

    public LoadImgFromURL(ImageView v, int newWidth, int newHieght) {
        this.newWidth = newWidth;
        this.newHieght = newHieght;
        vRef = new WeakReference<ImageView>(v);
    }



    @Override
    protected Bitmap doInBackground(Object... params) {
        // TODO Auto-generated method stub

        try {
            ip = (String) params[0];
            URL url = null;
            try {
                url = new URL(ip);
                bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return bmp;
    }


    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if (vRef != null && bitmap != null) {
            final ImageView imageView = vRef.get();
            if (imageView != null) {
                if(newHieght == 0 && newWidth ==0)
                    imageView.setImageBitmap(bitmap);
                else
                    imageView.setImageBitmap(getResized(bitmap));

            }
        }
    }

    public Bitmap getResized(Bitmap bm)
    {
        int width= bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth)/ width;
        float scaleHight = ((float) newHieght)/ height;

        Matrix mat = new Matrix();
        mat.postScale(scaleWidth , scaleHight);

        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, mat, false);
        bm.recycle();
        return resizedBitmap;
    }

    @Override
    protected void onPreExecute() {
    }

    @Override
    protected void onProgressUpdate(Void... values) {
    }
}