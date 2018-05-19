package com.colorreco.unlock.option;

import android.graphics.Bitmap;

import com.colorreco.unlock.bean.FaceRotation;
import com.colorreco.unlock.bean.PictureOrientation;

import org.lasque.tusdk.core.struct.TuSdkSize;
import org.lasque.tusdk.core.utils.StringHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * 人脸检测配置项
 */

public class FaceDetectionOption
{
    /** 图片或视频流标本的宽高 */
    private TuSdkSize mPitureSize = new TuSdkSize();

    /** 人脸在图片里的朝向, 默认：竖屏 */
    private PictureOrientation mPictureOrientation = PictureOrientation.Portrait;

    /** PersonID, 标记独一无二的Person */
    private String mPersonId;

    /** 人脸解锁阈值， 默认：0.5f */
    private float mThresholdValue = 0.5f;

    /** 开启同个人检测, 默认: false */
    private boolean mEnableSamePersonDetect = false;

    /** 与正脸的匹配值, 默认：0.5f */
    private float mFrontFaceMatchValue = 0.5f;

    /** 数据采集时跳帧数, 默认：10 */
    private int mJumpFrames = 10;

    /** 检测人脸的次数 */
    private int mDetectFaceNum = 0;

    /** 人脸检测成功后，是否将检测数据转成Bitmap, 默认：false */
    private boolean mIsSaveBitmap = false;

    /** 需要检测的人脸角度信息 */
    private List<FaceRotation> mRotationList = new ArrayList<>();

    /** 各个角度人脸Bitmap */
    private List<Bitmap> mFaceBitmapList = new ArrayList<>();

    /** 解锁时保存特征的阈值，默认：0.5f */
    private float mSaveFeatureThresholdValue = 0.5f;

    /** 活体检测的阈值, 默认：0.2f, 0.0f ~ 1.0f */
    private float mLiveThresholdValue = 0.2f;

    /** 开启活体检测, 默认：true */
    private boolean mEnableLiveDetection = true;

    /** 人脸解锁的最小检测人脸， 默认：150 */
    private float mMinFaceSize = 150;

    /** 人脸解锁时间上限， 默认：1000 单位：ms */
    private long mUnlockTimeLimit = 1000;

    /** 设置是否保存解锁成功的图片, 默认：true */
    private boolean mEnableSaveBitmap = true;

    /**
     * 设置图片或视频流标本的宽高
     *
     * @param size
     */
    public void setPictureSize(TuSdkSize size)
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

    /**
     * 标记独一无二的Person
     */
    public void setPersonId(String uuid)
    {
        if (StringHelper.isEmpty(uuid)) return;

        mPersonId = uuid;
    }

    /**
     * 解锁成功后，将大于阈值的特值保存下来
     *
     * @param value
     */
    public void setSaveFeatureThresholdValue(float value)
    {
        if (value < 0.5f) return;

        this.mSaveFeatureThresholdValue = value;
    }


    /**
     * 设置人脸解锁的阈值
     *
     * @param value
     */
    public void setUnlockThresholdValue(float value)
    {
        if (value < 0f) return;

        this.mThresholdValue = value;
    }

    /**
     * 设置活体检测的阈值
     *
     * @param value
     */
    public void setLiveThresholdValue(float value)
    {
        this.mLiveThresholdValue = value;
    }

    /**
     * 设置各侧脸与正脸的相似度
     */
    public void setEnableSamePersonDetect(boolean enableSamePersonDetect)
    {
        this.mEnableSamePersonDetect = enableSamePersonDetect;
    }

    /**
     * 各角度侧脸与正脸的匹配值
     * 该设置只有开启检测侧脸与正脸相似度时有效
     *
     * @param matchValue
     */
    public void setFrontFaceMatchValue(float matchValue)
    {
        if (matchValue < 0.5f) return;

        this.mFrontFaceMatchValue = matchValue;
    }

    /**
     * 设置是否将检测的数据转成Bitmap
     */
    public void setIsSaveBitmap(boolean isSaveBitmap)
    {
        this.mIsSaveBitmap = isSaveBitmap;
    }

    /**
     * 需要检测的人脸角度信息
     */
    public void setFaceRotationList(List<FaceRotation> rotationList)
    {
        this.mRotationList = rotationList;
    }


    /**
     * 开启活体检测
     *
     * @param enableLiveDetection

     */
    public void setEnableLiveDetection(boolean enableLiveDetection)
    {
        this.mEnableLiveDetection = enableLiveDetection;
    }

    /**
     * 人脸解锁的最小检测人脸
     *
     * @param minFaceSize
     */
    public void setMinFaceSize(int minFaceSize)
    {
        this.mMinFaceSize = minFaceSize;
    }

    public float getMinFaceSize()
    {
        return mMinFaceSize;
    }

    public TuSdkSize getPitureSize()
    {
        return mPitureSize;
    }

    public PictureOrientation getPictureOrientation()
    {
        return mPictureOrientation;
    }

    public String getPersonId() 
    {
        return mPersonId;
    }

    public float getThresholdValue()
    {
        return mThresholdValue;
    }

    public boolean isEnableSamePersonDetect() 
    {
        return mEnableSamePersonDetect;
    }

    public float getFrontFaceMatchValue()
    {
        return mFrontFaceMatchValue;
    }

    public void setJumpFrames(int jumpFrames)
    {
        this.mJumpFrames = jumpFrames;
    }

    public int getJumpFrames() 
    {
        return mJumpFrames;
    }

    public boolean isIsSaveBitmap()
    {
        return mIsSaveBitmap;
    }

    public List<FaceRotation> getRotationList() 
    {
        return mRotationList;
    }

    public List<Bitmap> getFaceBitmapList() 
    {
        return mFaceBitmapList;
    }

    public float getSaveFeatureThresholdValue() 
    {
        return mSaveFeatureThresholdValue;
    }

    public float getLiveThresholdValue() 
    {
        return mLiveThresholdValue;
    }

    public boolean isEnableLiveDetection()
    {
        return mEnableLiveDetection;
    }

    /**
     * 解锁时间上限
     *
     * @param unlockTimeLimit
     */
    public void setUnlockTimeLimit(long unlockTimeLimit)
    {
        this.mUnlockTimeLimit = unlockTimeLimit;
    }

    /**
     * 获取解锁时间上限
     */
    public long getUnlockTimeLimit()
    {
        return mUnlockTimeLimit;
    }

    /**
     * 设置是否保存解锁成功的图片
     */
    public void setEnableSaveBitmapAfterUnlocked(boolean enableSaveBitmap)
    {
        this.mEnableSaveBitmap = enableSaveBitmap;
    }

    public boolean getEnableSaveBitmapAfterUnlocked()
    {
        return this.mEnableSaveBitmap;
    }
}
