package com.example.tutu.facedectection.setting.presenter;

import com.example.tutu.facedectection.setting.model.ISettingDialogModel;
import com.example.tutu.facedectection.setting.view.SettingDialogView;

/**
 * 设置界面二级菜单Dialog Presenter类
 */


public class SettingDialogPresenter
{
    protected SettingDialogView mView;

    protected ISettingDialogModel mDataModel;

    public SettingDialogPresenter(SettingDialogView view, ISettingDialogModel dialogModel)
    {
        this.mView = view;

        mDataModel = dialogModel;
    }

    public void bindData()
    {
        if (mView == null || mDataModel == null) return;

        mView.setTitle(mDataModel.getTitle());
        mView.bindData(mDataModel.getValueList(), mDataModel.getDataType());
    }
}
