package com.molamil.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.molamil.nyboligsalgspris.utils.ImageHelper;
import com.molamil.nyboligsalgspris.utils.Session;

import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by martinschiothdyrby on 19/08/15.
 */
public class DownloadImageManager {

    private static ArrayList<DownloadImageItem> downloads = new ArrayList<>();
    private static boolean loading;

    public static void addItem(ImageView imageView, String image) {
        DownloadImageItem item = new DownloadImageItem();
        item.image = image;
        item.imageView = imageView;
        downloads.add(item);
        if(!loading) {
            loadNext();
        }
    }

    public static void addItem(ImageView imageView, String image, int cornerPixels) {
        DownloadImageItem item = new DownloadImageItem();
        item.image = image;
        item.imageView = imageView;
        item.cornerPixels = cornerPixels;
        item.isRounded = true;
        downloads.add(item);
        if(!loading) {
            loadNext();
        }
    }
    public static void clear() {
        downloads = new ArrayList<>();
        loading = false;
    }

    public static void loadNext() {
        if(downloads.size() == 0) {
            loading = false;
            return;
        }
        loading = true;
        DownloadImageItem item = downloads.remove(0);
        if(!item.isRounded) {
            new DownloadImageTask(item.imageView).execute(item.image);
        } else {
            new DownloadImageTask(item.imageView, item.cornerPixels).execute(item.image);
        }
    }
}

class DownloadImageItem {
    public ImageView imageView;
    public String image;
    public int cornerPixels;
    public boolean isRounded;
}
class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
    int cornerPixels;
    ImageView bmImage;
    boolean isRounded = false;

    public DownloadImageTask(ImageView bmImage, int cornerPixels) {
        this.bmImage = bmImage;
        this.cornerPixels = cornerPixels;
        isRounded = true;
    }
    public DownloadImageTask(ImageView bmImage) {
        this.bmImage = bmImage;
    }

    protected Bitmap doInBackground(String... urls) {
        String urldisplay = urls[0];
        Bitmap mIcon11 = null;
        try {
            InputStream in = new java.net.URL(urldisplay).openStream();
            mIcon11 = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mIcon11;
    }

    protected void onPostExecute(Bitmap result) {
        if(result != null) {
            if (isRounded) {
                bmImage.setImageBitmap(ImageHelper.getRoundedCornerBitmap(result, cornerPixels));
            } else {
                bmImage.setImageBitmap(result);
            }
        }
        DownloadImageManager.loadNext();
    }
}