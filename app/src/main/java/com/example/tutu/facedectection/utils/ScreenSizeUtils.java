package com.example.tutu.facedectection.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import com.example.tutu.facedectection.FaceUnlockApplication;

/**
 * Created by LiuHang on 11/29/2017.
 */


public class ScreenSizeUtils
{
    public static int getWidth()
    {
        return getMetrics().widthPixels;
    }

    public static int getHeight()
    {
        return getMetrics().heightPixels;
    }

    public static DisplayMetrics getMetrics()
    {
        WindowManager wm = (WindowManager) FaceUnlockApplication.getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics dm = new DisplayMetrics();
        display.getRealMetrics(dm);
        return dm;
    }
}
