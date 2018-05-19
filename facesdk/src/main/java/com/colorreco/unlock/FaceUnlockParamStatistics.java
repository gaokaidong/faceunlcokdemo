package com.colorreco.unlock;

import com.colorreco.unlock.bean.FaceUnlockInfo;
import com.colorreco.unlock.bean.Person;

import java.util.ArrayList;

/**
 * 人脸解锁参数统计
 */


public class FaceUnlockParamStatistics
{
    /** 人脸解锁时间上限， 默认：1000 单位：ms */
    private long mUnlockTimeLimit = 1000;

    /** 倒计时线程实例 */
    private TimingThread mTimingThread;

    /** 保存解锁过程的信息 */
    private FaceUnlockInfo mFaceUnlockInfo;

    /** 解锁参数 */
    private FaceUnlockParameters mParameters;

    /**  匹配更新后的人员信息 */
    private ArrayList<Person> mPersons;

    /**  标记解锁状态, 默认：false (失败)*/
    private boolean mIsSuccess = false;

    /**  标记解锁信息统计是否结束, 默认：false (未结束)*/
    private boolean mIsFinished = false;

    public interface ParamStatisticsDelegate
    {
        /**
         * 参数统计结束
         */
        void onFinish(ArrayList<Person> mPersons, FaceUnlockInfo faceUnlockInfo);
    }

    private ParamStatisticsDelegate mDelagte;

    public void setDelagte(ParamStatisticsDelegate delagte)
    {
        this.mDelagte = delagte;
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
     * 开始统计
     */
    public void start()
    {
        if (mTimingThread == null)
            mTimingThread = new TimingThread();

        mTimingThread.start();

        // 检测
        if (mFaceUnlockInfo == null)
        {
            mFaceUnlockInfo = new FaceUnlockInfo();
            mFaceUnlockInfo.startTotalTimestatistics();
        }
    }

    /**
     * 开始单个项目统计
     *
     * @param parameters
     */
    public void singleStatiStart(FaceUnlockParameters parameters)
    {
        if (mFaceUnlockInfo == null) return;

        mFaceUnlockInfo.startTiming();
        this.mParameters = parameters;
    }

    /**
     * 开始单个项目统计
     */
    public void singleStatiEnd()
    {
        if (mFaceUnlockInfo == null) return;

        mFaceUnlockInfo.endTiming(mParameters);
    }

    /**
     * 人脸解锁的次数统计
     */
    public void addFaceDetectNum()
    {
        if (mFaceUnlockInfo == null) return;

    }

    /**
     * 活体检测次数统计
     */
    public void addLiveDetectNum()
    {
        if (mFaceUnlockInfo == null) return;

        mFaceUnlockInfo.addLiveDetectNum();
    }


    /**
     * 计算检测人脸失败的次数
     */
    public void addInvalidFaceNum()
    {
        if (mFaceUnlockInfo == null) return;

        mFaceUnlockInfo.addInvalidFaceNum();
    }

    /**
     * 计算抽取特征值失败的次数
     */
    public void addExtractFailedNum()
    {
        if (mFaceUnlockInfo == null) return;

        mFaceUnlockInfo.addExtractFailedNum();
    }

    /**
     * 计算检测到无效人脸大小的次数
     */
    public void addInvalideFaceSizeNum()
    {
        if (mFaceUnlockInfo == null) return;

        mFaceUnlockInfo.addInvalideFaceSizeNum();
    }

    /**
     * 计算检测的总时间
     */
    public void addDetectTime()
    {
        if (mFaceUnlockInfo == null) return;

        mFaceUnlockInfo.addDetectTime();
    }

    /**
     * 统计结束
     */
    public void finish()
    {
        mFaceUnlockInfo.endTotalTimestatistics();

        if (mDelagte != null)
        {
            mDelagte.onFinish(mPersons, mFaceUnlockInfo);
        }

        // 标记统计结束
        mIsFinished = true;
    }

    /**
     * 标记统计是否已经结束
     *
     * @return
     */
    public boolean isFinished()
    {
        return mIsFinished;
    }

    /**
     * 保存匹配的用户名
     *
     * @param userName
     */
    public void saveUserName(String userName)
    {
        if (mFaceUnlockInfo == null) return;

        mFaceUnlockInfo.saveUserName(userName);
    }

    /**
     *  保存匹配值
     *
     * @param matchValue
     */
    public void saveMatchValue(float matchValue)
    {
        if (mFaceUnlockInfo == null) return;

        mFaceUnlockInfo.saveMatchValue(matchValue);
    }

    /**
     * 保存成功解锁单次的时间
     */
    public void saveSuccessUnlockTime()
    {
        if (mFaceUnlockInfo == null) return;

        mFaceUnlockInfo.saveSuccessUnlockTime();
    }

    /**
     * 返回人脸解锁统计的信息
     *
     * @return
     */
    public FaceUnlockInfo getFaceUnlockInfo()
    {
        return mFaceUnlockInfo;
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

        if (mFaceUnlockInfo == null) return;
        mFaceUnlockInfo.setUnlockState(mIsSuccess);
    }

    /**
     * 保存匹配后的人员信息
     *
     * @param persons
     */
    public void saveMatchedPerson(ArrayList<Person> persons)
    {
        this.mPersons = persons;
    }

    /**
     * 倒计时线程
     */
    private class TimingThread extends Thread
    {
        /** 倒计时线程休眠时间 */
        private final long SLEEP_TIME = 1000;

        /** 倒计时线程开关, 默认：true 开启 */
        private boolean mIsContinue = true;

        /** 倒计时线程开关, 默认：true 开启 */
        private boolean mIsLiveThread = true;

        @Override
        public void run()
        {
            super.run();

            while (mIsContinue)
            {
                try
                {
                    Thread.sleep(SLEEP_TIME);
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }

                mUnlockTimeLimit -= SLEEP_TIME;

                // 解锁成功后, 结束倒计时流程
                if (mIsSuccess)
                {
                    mFaceUnlockInfo.setUnlockState(mIsSuccess);
                    return;
                }

                if (mUnlockTimeLimit <= 0 && mIsLiveThread)
                {
                    stopThread();
                    finish();
                }
            }
        }

        /**
         * 停止线程
         */
        public void stopThread()
        {
            mIsContinue = false;
            mIsLiveThread = false;
        }
    }

    /**
     *
     * 销毁
     */
    public void destroy()
    {
        if (mTimingThread != null)
        {
            mTimingThread.stopThread();
        }
    }
}
