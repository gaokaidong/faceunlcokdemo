package com.example.tutu.facedectection.setting.model;

import com.example.tutu.facedectection.R;
import com.example.tutu.facedectection.setting.bean.LiveModeType;
import com.example.tutu.facedectection.setting.bean.SettingDialogDataType;
import com.example.tutu.facedectection.utils.DataUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 设置界面二级菜单活体模式Dialog数据接口
 */


public class LiveModeModel implements ISettingDialogModel
{
    @Override
    public String getTitle()
    {
        String title = DataUtils.getString(R.string.lsq_live_mode);

        return title;
    }

    @Override
    public List<Object> getValueList()
    {
        List<Object> modeList = new ArrayList<>();

        for (LiveModeType modeType: LiveModeType.values())
        {
            modeList.add(modeType);
        }

        return modeList;
    }

    @Override
    public SettingDialogDataType getDataType()
    {
        return SettingDialogDataType.ENUM_TYPE;
    }
}
