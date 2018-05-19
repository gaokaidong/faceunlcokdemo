package com.colorreco.unlock;

/**
 * Created by LiuHang on 12/27/2017.
 */


public enum FaceUnlockParameters
{
    /** 检测的时间 */
    DETECT_TIME(true, false, "ms", "Detect Time"),

    /** 抽取特征值时间 */
    EXTRACT_FEATURE_TIME (true, false, "ms", "Extract Time"),

    /** 比对的时间 */
    MATCH_TIME(true, false, "ms", "Match Time"),

    /** 活体检测的时间 */
    HACKER_TIME(true, false, "ms", "Hacker Time"),

    /** 匹配值 */
    MATCH_VALUE(true, false, "", "Match Value"),

    /** 检测人脸的次数 */
    DETECT_NUM(true, true, "", "Detect Num"),

    /** 活体检测的次数 */
    LIVE_DETECT_NUM(true, true, "", "Live Detect Num"),

    /** 解锁的总时间 */
    UNLOCK_TOTAL_TIME(true, true, "ms", "Unlock Total Time"),

    /** 匹配的用户名 */
    MATCH_USER_NAME(false, false, "", "Match User Name"),

    /** 成功解锁单次的时间 */
    SUCCESS_UNLOCK_TIME(true, false, "ms", "Success Unlock Time"),

    /** 未检测到人脸的次数 */
    INVALIDE_FACE_NUM(true, true, "", "Invalide Face Num"),

    /** 抽取特征值成功的次数 */
    EXTRACT_FAILED_NUM(true, true, "", "Extract Failed Num"),

    /** 检测到无效人脸的次数 */
    INVALID_FACE_SIZE_NUM(true, true, "", "Invalide Face Size Num"),

    /** 保存图片的时间 */
    SAVE_BITMAP_TIME(true, true, "ms", "Save Bitmap Time"),

    /** 检测的总时间 */
    DETECT_TOTAL_TIME(true, true, "ms", "Detect Total Time");

    /** 参数是否需要打印 */
    public boolean isTestFiled;

    /** 字段名称 */
    public String field;

    /** 单位 */
    public String unit;

    public boolean isPrinted;

    FaceUnlockParameters(boolean isPrinted, boolean isTestFiled, String unit, String field)
    {
        this.isPrinted = isPrinted;
        this.isTestFiled = isTestFiled;
        this.unit = unit;
        this.field = field;
    }
}
