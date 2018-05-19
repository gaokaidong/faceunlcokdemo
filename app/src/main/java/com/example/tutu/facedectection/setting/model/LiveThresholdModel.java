package com.example.tutu.facedectection.setting.model;

import com.example.tutu.facedectection.R;
import com.example.tutu.facedectection.utils.DataUtils;

/**
 * 设置界面二级菜单倒计时Dialog数据接口
 */


public class LiveThresholdModel extends UnlockThresholdModel
{
    @Override
    public String getTitle()
    {
        String title = DataUtils.getString(R.string.lsq_setting_live_threahold);

        return title;
    }

    protected Object[] getValues()
    {
        return new Object[]{0.0f, 0.05f, 0.1f, 0.15f, 0.2f, 0.25f};
    }
}
