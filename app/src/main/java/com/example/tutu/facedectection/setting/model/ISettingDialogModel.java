package com.example.tutu.facedectection.setting.model;

import com.example.tutu.facedectection.setting.bean.SettingDialogDataType;

import java.util.List;

/**
 * 设置界面二级菜单Dialog数据接口
 */


public interface ISettingDialogModel
{
    /** 标题 */
    String getTitle();

    /** 数据集合 */
    List getValueList();

    /** 数据类型 */
    SettingDialogDataType getDataType();
}
