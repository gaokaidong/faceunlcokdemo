package com.example.tutu.facedectection.setting.model;

import com.example.tutu.facedectection.R;
import com.example.tutu.facedectection.setting.bean.SettingDialogDataType;
import com.example.tutu.facedectection.utils.DataUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 设置界面二级菜单人脸解锁阈值Dialog数据接口
 */


public class UnlockThresholdModel implements ISettingDialogModel
{
    @Override
    public String getTitle()
    {
        String title = DataUtils.getString(getTitleId());
        return title;
    }

    @Override
    public List<Object> getValueList()
    {
        List<Object> dataList = new ArrayList<>();
        for (Object value : getValues())
        {
            dataList.add(String.valueOf(value));
        }

        return dataList;
    }

    @Override
    public SettingDialogDataType getDataType()
    {
        return SettingDialogDataType.STRING_TYPE;
    }

    /**
     * 所有的阈值
     * @return
     */
    protected Object[] getValues()
    {
        return new Object[]{0.6f, 0.65f, 0.70f, 0.75f, 0.80f, 0.85f};
    }

    public int getTitleId()
    {
        return R.string.lsq_setting_unlock_threshold;
    }
}
