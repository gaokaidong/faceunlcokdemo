package com.colorreco.unlock.bean;

/**
 * 抽取人脸的结果
 */


public class ExtractFaceInfo
{
    /** 抽取人脸特征值状态 */
    public ExtractFeatureState mExtractState;

    /** 人脸特征值 */
    public FaceFeature mFaceFeature;

    public ExtractFaceInfo(ExtractFeatureState extractState, FaceFeature faceFeature)
    {
        this.mExtractState = extractState;
        this.mFaceFeature = faceFeature;
    }
}
