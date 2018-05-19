package com.colorreco.unlock.bean;
/**
 * Created by LiuHang on 12/7/2017.
 */

/**
 * 人脸在图片里的朝向
 */
public enum PictureOrientation
{
    /** 竖屏 */
    Portrait(270),

    /** 竖屏2 */
    Portrait2(90),

    /** 横屏 */
    Landscape(0);

    PictureOrientation(int degree)
    {
        this.mDegree = degree;
    }

    /** 人脸在图片里的朝向 */
    private int mDegree;

    /** 人脸在图片里的朝向 */
    public int getDegree()
    {
        return mDegree;
    }

}
