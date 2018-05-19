package com.example.tutu.facedectection.setting.bean;

import com.example.tutu.facedectection.R;
import com.example.tutu.facedectection.utils.DataUtils;

/**
 * 活体检测模式
 */


public enum LiveModeType
{
    /** 严格模式 */
    STRICT_MODE(0, DataUtils.getString(R.string.lsq_strict_mode)),

    /** 非严格模式 */
    UNSTRICT_MODE(1, DataUtils.getString(R.string.lsq_unstrict_mode));

    /** 类型对应的数值 */
    public int mType;

    /** 类型对应的数值 */
    public String mModeName;

    LiveModeType(int type, String modeName)
    {
        this.mType = type;
        this.mModeName = modeName;
    }
}
