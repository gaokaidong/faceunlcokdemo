package com.example.tutu.facedectection.setting.bean;

import com.example.tutu.facedectection.R;
import com.example.tutu.facedectection.setting.model.ISettingDialogModel;
import com.example.tutu.facedectection.setting.model.LiveThresholdModel;
import com.example.tutu.facedectection.setting.model.TimingThresholdModel;
import com.example.tutu.facedectection.setting.model.UnlockThresholdModel;
import com.example.tutu.facedectection.utils.Constants;
import com.example.tutu.facedectection.utils.DataUtils;


/**
 * 人脸解锁设置界面的Item数据对象
 */


public enum SettingItemData
{
    /** 用户管理中心 */
    USER_CENTER(DataUtils.getString(R.string.lsq_setting_user_center), SettingItemType.UERR_CENTER, 1, "", null),

    /** 倒计时时间 */
    TIMING_LIMIT(DataUtils.getString(R.string.lsq_setting_timing_threahold), SettingItemType.CUSTOM_VALUE, Constants.UNLOCK_TIME_LIMIT, "", new TimingThresholdModel()),

    /** 人脸解锁阈值 */
    UNLOCK_THRESHOLD(DataUtils.getString(R.string.lsq_setting_unlock_threshold), SettingItemType.CUSTOM_VALUE, Constants.FACE_UNLOCK_VALUE, "", new UnlockThresholdModel()),

    /** 活体阈值 */
    LIVE_THRESHOLD(DataUtils.getString(R.string.lsq_setting_live_threahold), SettingItemType.CUSTOM_VALUE, Constants.Live_Threshold_Value, DataUtils.getString(R.string.lsq_live_threahold_hint), new LiveThresholdModel()),

    /** 活体检测 */
    LIVE_DETECTION(DataUtils.getString(R.string.lsq_setting_live_detection), SettingItemType.SWITCH_BTN, Constants.ENABLE_LIVE_DETECTION, "", null),

    /** 保存图片 */
    SAVE_IMAGE(DataUtils.getString(R.string.lsq_setting_save_unlock_picture), SettingItemType.SWITCH_BTN, Constants.ENABLE_SAVE_PICTURE, DataUtils.getString(R.string.lsq_setting_save_piture_hint), null),

    /** 活体模式 */
//    LIVE_MODE(DataUtils.getString(R.string.lsq_live_mode), SettingItemType.CUSTOM_VALUE, Constants.LIVE_MODE, "", new LiveModeModel()),

    /** 版本号 */
    VERSION_CODE(DataUtils.getString(R.string.lsq_version_code), SettingItemType.REC_VIEW_FOOTER, "", "", null);

    /** 项目名称 */
    public String mItemName;

    /** 项目类型 */
    public SettingItemType mItemType;

    /** 提示内容 */
    public String mHintText;

    /** 默认值 */
    public Object mDefaultValue;

    /** 具有Dialog类型的数据 */
    public ISettingDialogModel mModel;

    SettingItemData(String mItemName, SettingItemType mItemType, Object mDefaultValue, String mHintText, ISettingDialogModel mModel)
    {
        this.mItemName = mItemName;
        this.mItemType = mItemType;
        this.mDefaultValue = mDefaultValue;
        this.mHintText = mHintText;
        this.mModel = mModel;
    }
}
