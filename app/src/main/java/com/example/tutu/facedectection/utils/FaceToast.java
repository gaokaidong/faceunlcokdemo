package com.example.tutu.facedectection.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by LiuHang on 12/2/2017.
 */


public class FaceToast
{
    public static void makeText(Context context, int strId)
    {
        if (context == null) return;

        Toast.makeText(context, strId, Toast.LENGTH_SHORT).show();
    }

    public static void makeText(Context context, String str)
    {
        if (context == null) return;

        Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
    }
}
