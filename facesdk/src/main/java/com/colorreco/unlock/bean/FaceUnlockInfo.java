package com.colorreco.unlock.bean;

import android.graphics.Bitmap;

import com.colorreco.unlock.FaceUnlockParameters;
import com.colorreco.unlock.utils.TimingUtils;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 人脸解锁的信息
 */


public class FaceUnlockInfo
{
    /** 人脸检测的次数 */
    private int mFaceDetectNum;

    /** 活体检测次数 */
    private int mLiveDetectNum;

    /** 检测人脸失败的次数 */
    private int mInvalideFaceNum;

    /** 抽取特征值失败的次数 */
    private int mExtractFailedNum;

    /** 检测到无效人脸大小的次数 */
    private int mInvalideFaceSizeNum;

    /**  检测的总时间 */
    private int mDetectTotalTime;

    /**  标记解锁状态, 默认：false (失败)*/
    private boolean mIsSuccess = false;

    /** 解锁成功的Bitmap */
    private Bitmap mUnlockBitmap;

    /** 保存人脸解锁的信息 */
    private Map<FaceUnlockParameters, Object> mInfoMap = new LinkedHashMap();

    public FaceUnlockInfo()
    {
    }

    /**
     * 人脸解锁的次数统计
     */
    public void addFaceDetectNum()
    {
        mFaceDetectNum++;
        mInfoMap.put(FaceUnlockParameters.DETECT_NUM, mFaceDetectNum);
    }

    /**
     * 活体检测次数统计
     */
    public void addLiveDetectNum()
    {
        mLiveDetectNum++;

        mInfoMap.put(FaceUnlockParameters.LIVE_DETECT_NUM, mLiveDetectNum);
    }

    /**
     * 开始计时
     */
    public void startTiming()
    {
        TimingUtils.startTiming();
    }

    /**
     * 结束计时
     *
     * @param parameters
     *          根据指定的参数保存数值
     */
    public void endTiming(FaceUnlockParameters parameters)
    {
        long savedTime = TimingUtils.endTiming();
        mInfoMap.put(parameters, savedTime);
    }

    /**
     * 开始统计总时间
     */
    public void startTotalTimestatistics()
    {
        TimingUtils.startTotalTimestatistics();
    }

    /**
     * 结束统计总时间
     *
     */
    public void endTotalTimestatistics()
    {
        long savedTime = TimingUtils.endTotalTimestatistics();

        mInfoMap.put(FaceUnlockParameters.UNLOCK_TOTAL_TIME, savedTime);
    }

    /**
     * 保存匹配的用户名
     *
     * @param userName
     */
    public void saveUserName(String userName)
    {
        mInfoMap.put(FaceUnlockParameters.MATCH_USER_NAME, userName);
    }

    /**
     *  保存匹配值
     *
     * @param matchValue
     */


    public void saveMatchValue(float matchValue)
    {
        mInfoMap.put(FaceUnlockParameters.MATCH_VALUE, matchValue);
    }

    /**
     * 获取人脸解锁的信息集合
     *
     * @return
     */
    public Map<FaceUnlockParameters, Object> getUnlockInfo()
    {
        return mInfoMap;
    }

    /**
     * 保存成功解锁单次的时间
     */
    public void saveSuccessUnlockTime()
    {
        if (!mInfoMap.containsKey(FaceUnlockParameters.DETECT_TIME) || !mInfoMap.containsKey(FaceUnlockParameters.EXTRACT_FEATURE_TIME)
                || !mInfoMap.containsKey(FaceUnlockParameters.HACKER_TIME) || !mInfoMap.containsKey(FaceUnlockParameters.MATCH_TIME)) return;

        long detectTime = (long) mInfoMap.get(FaceUnlockParameters.DETECT_TIME);
        long extractTime = (long) mInfoMap.get(FaceUnlockParameters.EXTRACT_FEATURE_TIME);
        long liveTime = (long) mInfoMap.get(FaceUnlockParameters.HACKER_TIME);
        long matchTime = (long) mInfoMap.get(FaceUnlockParameters.MATCH_TIME);
        long successUnlockTime = detectTime + extractTime + liveTime + matchTime;
        mInfoMap.put(FaceUnlockParameters.SUCCESS_UNLOCK_TIME, successUnlockTime);
    }

    /**
     * 计算检测人脸失败的次数
     */
    public void addInvalidFaceNum()
    {
        mInvalideFaceNum++;

        mInfoMap.put(FaceUnlockParameters.INVALIDE_FACE_NUM, mInvalideFaceNum);
    }

    /**
     * 计算抽取特征值失败的次数
     */
    public void addExtractFailedNum()
    {
        mExtractFailedNum++;

        mInfoMap.put(FaceUnlockParameters.EXTRACT_FAILED_NUM, mExtractFailedNum);
    }

    /**
     * 计算检测到无效人脸大小的次数
     */
    public void addInvalideFaceSizeNum()
    {
        mInvalideFaceSizeNum++;

        mInfoMap.put(FaceUnlockParameters.INVALID_FACE_SIZE_NUM, mInvalideFaceSizeNum);
    }

    /**
     * 计算检测的总时间
     */
    public void addDetectTime()
    {
        mDetectTotalTime += TimingUtils.endTiming();

        mInfoMap.put(FaceUnlockParameters.DETECT_TOTAL_TIME, mDetectTotalTime);
    }

    /**
     * 设置解锁状态
     *
     * @param isSuccess
     *          true 为成功, false 为失败
     */
    public void setUnlockState(boolean isSuccess)
    {
        this.mIsSuccess = isSuccess;
    }

    /**
     * 获取解锁状态
     */
    public boolean getUnlockState()
    {
        return mIsSuccess;
    }

}
