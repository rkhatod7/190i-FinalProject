package com.example.radhika.finalproject;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * Created by jacob on 6/10/2017.
 */

public class ImageAdapter extends BaseAdapter {

    public static ArrayList<Bitmap> imageList = new ArrayList<Bitmap>();
    ImageView imageView;
    Context ctx;

    public ImageAdapter(Context context)
    {
        this.ctx = context;
    }

    @Override
    public int getCount()
    {
        return imageList.size();
    }

    @Override
    public View getView(int pos, View view, ViewGroup container)
    {
        if(view == null)
        {
            imageView = new ImageView(ctx);
            imageView.setLayoutParams(new GridView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,200));
            imageView.setPadding(30,30,30,30);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }
        else {
            imageView = (ImageView) view;
        }
        imageView.setImageBitmap(getItem(pos));
        return imageView;
    }

    @Override
    public Bitmap getItem(int position)
    {
        return imageList.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }


}
