package com.colorreco.unlock.bean;

import org.lasque.tusdk.core.struct.TuSdkSize;

/**
 * 人脸检测的数据
 */


public class FaceDetectInfo
{
    /**
     * 数组第一位（下标0）表示检测到多少个人脸。如果没有检测到人脸则这个值等于0
     * 数组第二位起（下标为1）分别为：人脸框左上角坐标X，左上角坐标Y，人脸框宽度，人脸框高度，人脸区域图片质量
     */
    public int [] mFaceInfo;

    /**
     * 人脸角度信息,分别为仰角／俯角（yaw），左右旋转角度（pitch），翻滚角（roll）
     */
    public float[] mFaceRotationInfo;

    public FaceDetectInfo(int [] faceInfo, float[] faceRotationInfo)
    {
        this.mFaceInfo = faceInfo;
        this.mFaceRotationInfo = faceRotationInfo;
    }

    /**
     * 是否有人脸
     *
     * @return
     */
    public boolean hasFace()
    {
        if (mFaceInfo == null || mFaceInfo.length < 1) return false;

        boolean hasFace = mFaceInfo[0] == 1 ? true : false;
        return hasFace;
    }

    /**
     * 人脸宽高
     * 人脸为正方形，宽高一致
     *
     * @return
     */
    public TuSdkSize getFaceSize()
    {
        TuSdkSize size = new TuSdkSize();
        if (mFaceInfo == null || mFaceInfo.length < 5) return size;

        size.width = mFaceInfo[3];
        size.height = mFaceInfo[4];
        return size;
    }
}
