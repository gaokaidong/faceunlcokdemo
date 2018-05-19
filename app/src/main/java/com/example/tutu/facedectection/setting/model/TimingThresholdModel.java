package com.example.tutu.facedectection.setting.model;

import com.example.tutu.facedectection.R;

/**
 * 设置界面二级菜单倒计时Dialog数据接口
 */


public class TimingThresholdModel extends UnlockThresholdModel
{
    @Override
    public int getTitleId()
    {
        return R.string.lsq_setting_timing_threahold;
    }

    @Override
    protected Object[] getValues()
    {
        return new Object[]{1000, 2000, 3000, 4000, 5000};
    }
}
