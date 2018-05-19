package com.example.tutu.facedectection.setting;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.tutu.facedectection.R;
import com.example.tutu.facedectection.base.BaseDialog;
import com.example.tutu.facedectection.setting.adapter.BaseAdapter;
import com.example.tutu.facedectection.setting.adapter.StringValueAdapter;
import com.example.tutu.facedectection.setting.adapter.EnumValueAdapter;
import com.example.tutu.facedectection.setting.bean.SettingDialogDataType;
import com.example.tutu.facedectection.setting.delegate.DialogValueDelegate;
import com.example.tutu.facedectection.setting.model.ISettingDialogModel;
import com.example.tutu.facedectection.setting.presenter.SettingDialogPresenter;
import com.example.tutu.facedectection.setting.view.SettingDialogView;

import java.util.List;

/**
 * 设置界面的调节参数Dialog
 */


public class SettingDialog extends BaseDialog implements SettingDialogView, DialogValueDelegate
{
    protected RecyclerView mRecyView;
    // 标题
    protected TextView mTitleView;

    protected SettingDialogPresenter mPresenter;

    private DialogValueDelegate mDelegate;

    // 数据模型
    private ISettingDialogModel mDialogModel;

    public void setDelegate(DialogValueDelegate delegate)
    {
        this.mDelegate = delegate;
    }

    public SettingDialog(Context context)
    {
        super(context);
    }

    @Override
    protected int getLayoutId()
    {
        return R.layout.setting_dialog_view;
    }

    @Override
    protected View initView(Context context)
    {
        View view = super.initView(context);
        mRecyView = view.findViewById(R.id.lsq_setting_recy_view);
        mTitleView = view.findViewById(R.id.lsq_title);

        return view;
    }

    /**
     * 设置数据模型
     *
     * @param dialogModel
     */
    public void setDialogModel(ISettingDialogModel dialogModel)
    {
        this.mDialogModel = dialogModel;
    }

    @Override
    public boolean isCancelable()
    {
        return true;
    }

    @Override
    public boolean isCanceledOnTouchOutside()
    {
        return true;
    }

    @Override
    public void show()
    {
        super.show();

        mPresenter = new SettingDialogPresenter(this, mDialogModel);
        mPresenter.bindData();
    }

    @Override
    public void setTitle(String title)
    {
        if (mTitleView == null) return;

        mTitleView.setText(title);
    }

    @Override
    public void bindData(List dataList, SettingDialogDataType dataType)
    {
        if (mContext == null || dataType == null) return;

        BaseAdapter adapter = null;
        if (dataType == SettingDialogDataType.STRING_TYPE)
        {
            adapter = new StringValueAdapter(mContext, dataList);
        }
        else if (dataType == SettingDialogDataType.ENUM_TYPE)
        {
            adapter = new EnumValueAdapter(mContext, dataList);
        }

        adapter.setDelegate(this);
        mRecyView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyView.setAdapter(adapter);
    }

    @Override
    public void onUpdateValue(String value)
    {
        if (mDelegate == null) return;

        mDelegate.onUpdateValue(value);
    }

    @Override
    public void onUpdateShownText(String text)
    {
        if (mDelegate == null) return;

        mDelegate.onUpdateShownText(text);
    }
}
