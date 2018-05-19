package com.example.tutu.facedectection.setting.model;

import java.util.List;

/**
 * Created by LiuHang on 1/15/2018.
 */


public class BaseDataModel
{
    private List<Object> mDataList;

    public void setData(List<Object> dataList)
    {
        this.mDataList = dataList;
    }

    /**
     * 获取数据数量
     *
     * @return
     */
    public int getDataSize()
    {
        if (mDataList == null) return 0;

        return mDataList.size();
    }
}
