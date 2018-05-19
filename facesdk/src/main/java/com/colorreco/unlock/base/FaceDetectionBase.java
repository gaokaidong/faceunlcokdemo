package com.colorreco.unlock.base;

import com.colorreco.libface.CRFace;
import com.colorreco.unlock.bean.FaceInitialState;

/**
 * Created by LiuHang on 12/10/2017.
 */


public class FaceDetectionBase implements FaceDetectionInterface
{
    /** 人脸解锁SDK对象 */
    private CRFace mCRFace;

    /** 人脸识别SDK初始化状态 */
    protected FaceInitialState mInitialState = FaceInitialState.IntialFailed;

    /**
     * 初始化
     */
    public void init()
    {
        mCRFace = CRFace.getInstance();
        mCRFace.loadLibrarySys("crface");

        // 人脸模型已在so库中，初始化模型路径可以为任意值
        int intialResult = mCRFace.initSDK("/sdcard/colorreco");

        FaceInitialState state = FaceInitialState.IntialFailed;
        switch (intialResult)
        {
            case 1:
                state = FaceInitialState.IntialSuccess;
                break;
            default:
                state = FaceInitialState.IntialFailed;
                break;
        }
        mInitialState = state;
    }

    @Override
    public int[] detect(byte[] data, int width, int height, int isMaxface, float[] pose, int degree)
    {
        if (mCRFace == null) return null;

        return mCRFace.detectYUV(data, width, height, isMaxface, pose, degree);
    }

    /**
     * 活体检测
     *
     * @return 0.0f ~ 1.0f,值越大假体可能性越大
     */
    public float getLive()
    {
        if (mCRFace == null) return 1f;

        float isLive = mCRFace.getLive();
        return isLive;
    }


    @Override
    public int extractYUV(byte[] data, int width, int height, int[] facebox, float[] feature, int degree)
    {
        if (mCRFace == null) return 0;

        return mCRFace.extractYUV(data, width,height, facebox, feature, degree);
    }

    /**
     * 人脸特征比对
     */
    @Override
    public float match(float[] featureA, float[] featureB)
    {
        if (mCRFace == null || featureA == null || featureB == null) return -1.f;

        return mCRFace.match(featureA, featureB);
    }
}
