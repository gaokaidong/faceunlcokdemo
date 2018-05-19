package com.example.tutu.facedectection.adapter;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by LiuHang on 12/3/2017.
 */


public class SpeceItemDecoration extends RecyclerView.ItemDecoration
{
    private int mLeft;
    private int mRight;
    private int mTop;
    private int mBottom;

    public SpeceItemDecoration(int left, int top, int right, int bottom)
    {
        this.mLeft = left;
        this.mRight = right;
        this.mTop = top;
        this.mBottom = bottom;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state)
    {
        super.getItemOffsets(outRect, view, parent, state);

        outRect.left = mLeft;
        outRect.top = mTop;
        outRect.right = mRight;
        outRect.bottom = mBottom;
    }
}
