package com.example.tutu.facedectection.setting.bean;

/**
 *  人脸解锁设置界面的Item类型
 */


public enum SettingItemType
{
    /** 开关类型 */
    SWITCH_BTN(SettingTypeValue.SWITCH_BTN),

    /** 自定义数值类型 */
    CUSTOM_VALUE(SettingTypeValue.CUSTOM_VALUE),

    /** 用户管理中心 */
    UERR_CENTER(SettingTypeValue.CUSTOM_VALUE),

    /** RecycelerView尾部 */
    REC_VIEW_FOOTER(SettingTypeValue.FOOTER_VALUE);

    /** 类型对应的数值 */
    public int mType;

    SettingItemType(int type)
    {
        this.mType = type;
    }
}
