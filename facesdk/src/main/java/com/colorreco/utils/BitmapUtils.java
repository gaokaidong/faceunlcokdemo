package com.colorreco.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.YuvImage;

import com.colorreco.unlock.bean.PictureOrientation;

import org.lasque.tusdk.core.utils.image.BitmapHelper;
import org.lasque.tusdk.core.utils.image.ImageOrientation;

import java.io.ByteArrayOutputStream;
import java.io.File;

/**
 * 图片工具类
 */


public class BitmapUtils
{
    /**
     * 将检测数据保存成图片
     *
     * @param data
     */
    public static Bitmap convertYUVToBitmap(byte[] data, int width, int height, PictureOrientation orientation)
    {
        byte[] rgbByte = BitmapUtils.convertYUVToRGB(data, width, height);
        Bitmap bitmap = BitmapFactory.decodeByteArray(rgbByte, 0, rgbByte.length);

        // 矫正照片的方向
        ImageOrientation imageOrientation = null;
        if (orientation == PictureOrientation.Portrait)
            imageOrientation = ImageOrientation.LeftMirrored;
        else if (orientation == PictureOrientation.Portrait2)
            imageOrientation = ImageOrientation.RightMirrored;
        else if (orientation == PictureOrientation.Landscape)
            imageOrientation = ImageOrientation.RightMirrored;

        Bitmap rotateBitmap = BitmapHelper.imageRotaing(bitmap, imageOrientation);
        return rotateBitmap;
    }


    /**
     * YUV转RGB
     *
     * @param data
     * @param width
     * @param height
     * @return
     */
    public static byte[] convertYUVToRGB(byte[]data, int width, int height)
    {
        YuvImage img = new YuvImage(data, ImageFormat.NV21, width, height, null);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        img.compressToJpeg(new android.graphics.Rect(0, 0, img.getWidth(), img.getHeight()), 50, out);
        return out.toByteArray();
    }

    /**
     * 保存图片到相册
     *
     * @param bitmap
     * @param floderName
     * @return
     */
    public static void savePhotoToAlbum(Bitmap bitmap, String floderName, String liveValue)
    {
        if (bitmap == null) return;

        File mediaFile = FileUtils.getAlbumFile(floderName, liveValue);
        if (mediaFile == null) return;

        BitmapHelper.saveBitmap(mediaFile, bitmap, 100);
    }
}
