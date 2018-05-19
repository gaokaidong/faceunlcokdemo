package com.example.tutu.facedectection.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.tutu.facedectection.R;

import java.util.ArrayList;

/**
 * Created by LiuHang on 12/3/2017.
 */


public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageHolder>
{
    private ArrayList<Bitmap> mBitmaps;

    private Context mContext;

    public ImageAdapter(ArrayList<Bitmap> bitmaps, Context context)
    {
        this.mBitmaps = bitmaps;
        this.mContext = context;
    }

    @Override
    public ImageHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        if (mContext == null) return  null;

        View view = LayoutInflater.from(mContext).inflate(R.layout.image_cell_view, null);

        if (view == null) return null;

        ImageHolder holder = new ImageHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ImageHolder holder, int position)
    {
        if (holder == null || mBitmaps == null || mBitmaps.size() < 1) return;

        Bitmap bitmap = mBitmaps.get(position);
        holder.imageView.setImageBitmap(bitmap);
    }

    @Override
    public int getItemCount()
    {
        if (mBitmaps == null || mBitmaps.size() < 1) return 0;

        return mBitmaps.size();
    }

    public class ImageHolder  extends RecyclerView.ViewHolder
    {
        private ImageView imageView;

        public ImageHolder(View itemView)
        {
            super(itemView);
            imageView = itemView.findViewById(R.id.lsq_image_cell_view);
        }
    }
}
