package com.example.tutu.facedectection.setting.view;

import com.example.tutu.facedectection.setting.bean.SettingDialogDataType;

import java.util.List;

/**
 * 设置界面二级菜单Dialog View类
 */


public interface SettingDialogView
{
    /** 设置标题 */
    void setTitle(String title);

    /** 绑定数据 */
    void bindData(List dataList, SettingDialogDataType dataType);
}
