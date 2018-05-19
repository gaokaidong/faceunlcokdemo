package com.example.tutu.facedectection.setting.delegate;

/**
 * 设置界面Dialog委托事件
 */


public interface DialogValueDelegate
{
    /**
     * 每个item被点击时，会触发
     * 修改对应的参数值
     *
     * @param value
     */
    void onUpdateValue(String value);

    /**
     * 每个item被点击时，会触发
     * 修改设置界面列表显示的当前参数值,
     *
     * @param value
     */
    void onUpdateShownText(String value);
}
