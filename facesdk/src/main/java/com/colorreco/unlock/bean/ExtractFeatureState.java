package com.colorreco.unlock.bean;

/**
 * 抽取特征值状态
 */


public enum  ExtractFeatureState
{
    /** 成功 */
    EXTRACT_SUCCESS(256),

    /** 人脸框错误 */
    INCORRECT_FACE_POSITON(-2),

    /** 抽取特征失败 */
    EXTRACT_FAILURE(-3),

    /** 图片过大 */
    PICTURE_SIZE_TOO_LARGE(-4),

    /** 授权失败 */
    AUTHORIZATION_FAILED(-5),

    /** 未知状态 */
    UNKNOWN(0);

    ExtractFeatureState(int state)
    {
        this.mState = state;
    }

    /** 抽取特征值状态 */
    private int mState;

    /** 抽取特征值状态 */
    public int getState()
    {
        return mState;
    }

    /**
     * 获取抽取特征值的状态
     *
     * @param state
     */
    public static ExtractFeatureState getExtractFeatureState(int state)
    {
        for (ExtractFeatureState item : ExtractFeatureState.values())
        {
            if (item.getState() == state)
            {
                return item;
            }
        }

        return ExtractFeatureState.UNKNOWN;
    }
}
