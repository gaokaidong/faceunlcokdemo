package com.example.tutu.facedectection.base;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import org.lasque.tusdk.core.struct.TuSdkSize;

/**
 * Dialog基类
 */


public abstract class BaseDialog
{
    // dialog
    protected AlertDialog mDialog;

    // builder
    protected AlertDialog.Builder mBuilder;

    // 弹出框大小
    protected TuSdkSize mDialogSize;

    protected Context mContext;

    /**
     * 设置布局ID
     *
     * @return
     */
    protected abstract int getLayoutId();

    public BaseDialog(Context context)
    {
        this.mContext = context;

        buildDialog(context);
        create();
    }

    /**
     * 配置Dialog
     *
     * @param context
     */
    public void buildDialog(Context context)
    {
        if (context == null) return;

        mBuilder = new AlertDialog.Builder(context, AlertDialog.THEME_HOLO_LIGHT);

        View customView = initView(context);
        mBuilder.setView(customView);
        //设置按钮是否可以按返回键取消,false则不可以取消
        mBuilder.setCancelable(isCancelable());
    }

    /**
     * 设置按钮是否可以可以按返回键取消,
     * 默认：false
     *
     * @return
     */
    public boolean isCancelable()
    {
        return false;
    }

    /**
     * 设置弹出框失去焦点是否隐藏,即点击屏蔽其它地方是否隐藏
     * 默认：false
     *
     * @return
     */
    public boolean isCanceledOnTouchOutside()
    {
        return false;
    }

    /**
     *
     * @return
     */
    protected View initView(Context context)
    {
        View customView = LayoutInflater.from(context).inflate(getLayoutId(), null);

        return customView;
    }

    /**
     * 创建对话框
     */
    public void create()
    {
        if (mBuilder == null) return;

        mDialog = mBuilder.create();

        //设置弹出框失去焦点是否隐藏,即点击屏蔽其它地方是否隐藏
        mDialog.setCanceledOnTouchOutside(isCanceledOnTouchOutside());
    }

    /**
     *  显示Dialog
     */
    public void show()
    {
        if (mDialog == null) return;

        mDialog.show();

        adjustDialogSize();
    }

    /**
     * 设置Dialog
     */
    public void adjustDialogSize()
    {
    }

    /**
     * 设置Dialog大小
     *
     * @param dialogSize
     */
    public void setDialogSize(TuSdkSize dialogSize)
    {
        this.mDialogSize = dialogSize;
    }

    /**
     * 销毁Dialog
     */
    public void destroy()
    {
        if (mDialog == null) return;

       mDialog.dismiss();
    }
}
