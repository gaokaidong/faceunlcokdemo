package com.colorreco.unlock.bean;

import java.io.Serializable;

/**
 * Created by LiuHang on 12/2/2017.
 */


public class FaceFeature implements Serializable
{
    private static final long serialVersionUID = -7345236179721481625L;

    /** 特征值 */
    public float[] mFeatures;

    /** 特征角度 */
    public FaceRotation mRotation;

    public FaceFeature(float[] features, FaceRotation rotation)
    {
        this.mFeatures = features;
        this.mRotation = rotation;
    }
}
