package com.colorreco.fire;

import com.colorreco.cutor.CRCutor;
import com.colorreco.fire.delegate.FireDetectionInterface;
import com.colorreco.fire.option.FireDetectionOption;

import org.lasque.tusdk.core.struct.TuSdkSize;

/**
 * Created by LiuHang on 12/16/2017.
 */


public class FireSDK implements FireDetectionInterface
{
    /** 检测火SDK */
    private CRCutor mCRCutor;

    private FireSDKDelegate mDelegate;

    public void setDelegate(FireSDKDelegate delegate)
    {
        this.mDelegate = delegate;
    }

    public FireSDKDelegate getDelegate()
    {
        return mDelegate;
    }

    /** 火检测配置项 */
    private FireDetectionOption mFireDetectionOption;

    /** 人脸检测配置项*/
    public void setComponentOption(FireDetectionOption fireDetectionOption)
    {
        this.mFireDetectionOption = fireDetectionOption;
    }

    public void init()
    {
        if (mCRCutor != null) return;

        mCRCutor = CRCutor.getInstance();
    }

    @Override
    public int[] cutYUV(byte[] data, int width, int height, int degree)
    {
        if (mCRCutor == null) return null;

        return mCRCutor.cutYUV(data, width, height, degree);
    }

    /**
     * 检测是否有火
     *
     * @param data
     */
    public void detectFire(byte[] data)
    {
        if (mFireDetectionOption == null || data == null) return;

        TuSdkSize pitureSize = mFireDetectionOption.getPitureSize();

        if (pitureSize.maxSide() <= 0) return;

        int degree = mFireDetectionOption.getPictureOrientation().getDegree();

        // 存储火检测位置信息
        int[] fireInfo = mCRCutor.cutYUV(data, pitureSize.width, pitureSize.height, degree);

        if (mDelegate == null) return;

        boolean hasFire = fireInfo[0] >= 1 ? true : false;
        int[] firePos = new int[8];
        int index = 0;
        for (int i = 0; i< fireInfo.length; i++)
        {
            if (i == 0 || index >= 8) continue;
            firePos[index++] = fireInfo[i];
        }

        mDelegate.onFireDetectionResult(hasFire, firePos);
    }
}
