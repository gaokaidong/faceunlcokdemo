package com.colorreco.unlock.utils;

import org.lasque.tusdk.core.utils.TuSdkDate;

/**
 * 计时工具类
 */


public class TimingUtils
{
    /** 创建计时对象 */
    private static TuSdkDate mDate;

    /** 统计总时间 */
    private static TuSdkDate mTotalDate;

    /**
     * 开始计时
     */
    public static void startTiming()
    {
        if (mDate != null) mDate = null;

        mDate = new TuSdkDate();
    }

    /**
     * 结束计时
     *
     * @return
     *      返回计时时间
     */
    public static long endTiming()
    {
        if (mDate == null) return 0;

        return mDate.diffOfMillis();
    }

    /**
     * 开始统计总时间
     */
    public static void startTotalTimestatistics()
    {
        if (mTotalDate != null) mTotalDate = null;

        mTotalDate = new TuSdkDate();
    }

    /**
     * 结束统计总时间
     *
     * @return
     *      返回统计的总时间
     */
    public static long endTotalTimestatistics()
    {
        if (mTotalDate == null) return 0;

        return mTotalDate.diffOfMillis();
    }
}
