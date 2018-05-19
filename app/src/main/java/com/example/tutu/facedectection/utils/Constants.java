package com.example.tutu.facedectection.utils;

/**
 * Created by LiuHang on 11/27/2017.
 */


public class Constants
{
    /** SharedPreferences存储密码的key */
    public final static String SHARE_PASSWORD_KEY = "password";

    /** 存储人脸特征值key*/
    public final static String SHARE_FACE_FEATURE_KEY = "mFaceFeature";

    /**拨号键的左间距*/
    public final static int DIAL_KEY_SPACE_LEFT = 15;

    /** 密码长度**/
    public final static int PASSWORD_LENGTH = 6;

    /** 人脸解锁的阈值*/
    public static float  FACE_UNLOCK_VALUE = 0.6f;

    /** 最少正脸检测次数 */
    public final static int MIN_DETECT_FRONT_FACE_NUM = 0;

    /** 检测时与正脸的相似度 */
    public final static float SIMIRAR_FRONT_FACE = 0.6f;

    /** 人脸解锁成功后保存人脸信息的阈值 */
    public final static float MIN_FACE_INFO_SAVE_VALUE = 0.7f;

    /** 活体检测阈值 */
    public final static float Live_Threshold_Value = 0.1f;

    /** 人脸解锁时间上限, 单位：ms */
    public final static long UNLOCK_TIME_LIMIT = 2000;

    /** 是否开启活体检测，默认: true */
    public static boolean ENABLE_LIVE_DETECTION = true;

    /** 是否保存解锁时图片，默认: true */
    public static boolean ENABLE_SAVE_PICTURE = true;

    /* 活体模式,有两种模式：严格模式(0)，非严格模式(1)，默认: 0 */
    public static int LIVE_MODE = 0;
}
