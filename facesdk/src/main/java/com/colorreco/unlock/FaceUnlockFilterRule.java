package com.colorreco.unlock;

import com.colorreco.unlock.bean.ExtractFaceInfo;

import org.lasque.tusdk.core.struct.TuSdkSize;

/**
 * 人脸解锁的过滤规则
 */


public class FaceUnlockFilterRule
{
    /** 人脸占图片最大比例 */
    private static final float MAX_FACE_RATIO = 0.8f;

    /** 人脸占图片最小比例 */
    private static final float MIN_FACE_RATIO = 0.2f;

    /**
     * 过滤偏大或偏小的人脸
     */
    public static boolean filterFaceSize(TuSdkSize personSize, TuSdkSize imageSize)
    {
        int personMaxSide = personSize.maxSide();

        int imageMinSide = imageSize.minSide();

        float ratio = personMaxSide / (float)imageMinSide;

        if (ratio > MAX_FACE_RATIO || ratio < MIN_FACE_RATIO)
            return true;
        else
            return false;
    }

    /**
     * 过滤假体
     *
     * @param isLiveValue
     * @param liveThresholdValue
     * @return
     */
    public static boolean filterUnLive(float isLiveValue, float liveThresholdValue)
    {
        if (isLiveValue >= liveThresholdValue) return true;

        return false;
    }

    /**
     * 过滤不匹配的数值
     *
     * @param matchValue
     * @param thresholdValue
     * @return
     */
    public static boolean filterUnMatchedValue(float matchValue, float thresholdValue)
    {
        if (matchValue <= thresholdValue) return true;

        return false;
    }

    /**
     * 过滤无效的特征值
     * 
     * @param extractFaceInfo
     * @return
     */
    public static boolean filterInvalideFaceFeature(ExtractFaceInfo extractFaceInfo)
    {
        if (extractFaceInfo == null) return true;

        return false;
    }
}
