package com.colorreco.fire.option;

import com.colorreco.unlock.bean.PictureOrientation;

import org.lasque.tusdk.core.struct.TuSdkSize;

/**
 * 火检测配置项
 */


public class FireDetectionOption
{
    /** 图片或视频流标本的宽高 */
    private TuSdkSize mPitureSize = new TuSdkSize();

    /** 人脸在图片里的朝向, 默认：竖屏 */
    private PictureOrientation mPictureOrientation = PictureOrientation.Portrait;

    /**
     * 设置图片或视频流标本的宽高
     *
     * @param size
     */
    protected void setPictureSize(TuSdkSize size)
    {
        this.mPitureSize = size;
    }

    /**
     * 人脸在图片里的朝向
     *
     * @param orientation
     */
    public void setOrientation(PictureOrientation orientation)
    {
        this.mPictureOrientation = orientation;
    }

    public TuSdkSize getPitureSize()
    {
        return mPitureSize;
    }

    public PictureOrientation getPictureOrientation()
    {
        return mPictureOrientation;
    }
}
