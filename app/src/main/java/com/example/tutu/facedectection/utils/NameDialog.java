package com.example.tutu.facedectection.utils;

import android.content.Context;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.example.tutu.facedectection.R;
import com.example.tutu.facedectection.base.BaseDialog;

/**
 * 姓名输入框
 */


public class NameDialog extends BaseDialog
{
    // 取消按钮
    private Button mCancelBtn;

    // 确认按钮
    private Button mConfirmBtn;

    // 文本输入框
    private EditText mEditText;

    public NameDialog(Context context)
    {
        super(context);
    }

    public String getEditText()
    {
        if (mEditText == null) return null;

        return mEditText.getText().toString();
    }

    public interface DialogDelegate
    {
        /**
         * EditText输入的内容
         *
         * @param content
         */
        void onEditTextContent(String content);
    }

    private DialogDelegate mDelegate;

    public void setDelegate(DialogDelegate delegate)
    {
        this.mDelegate = delegate;
    }

    @Override
    protected int getLayoutId()
    {
        return R.layout.name_dialog_view;
    }

    /**
     *
     * @return
     */
    public View initView(Context context)
    {
        View customView = super.initView(context);

        if (customView == null) return customView;

        mEditText = customView.findViewById(R.id.lsq_edit_text);
        mCancelBtn = customView.findViewById(R.id.lsq_cancel_btn);
        mConfirmBtn =customView.findViewById(R.id.lsq_confirm_btn);

        return customView;
    }


    @Override
    public void show()
    {
        super.show();

        // 点击事件不能放在getView里
        mCancelBtn.setOnClickListener(mOnClickListener);
        mConfirmBtn.setOnClickListener(mOnClickListener);
    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            if (mConfirmBtn == v)
            {
                if (mDelegate != null && mEditText != null)
                    mDelegate.onEditTextContent(getEditText());
            }

            mDialog.dismiss();
        }
    };

    /**
     * 设置Dialog
     */
    public void adjustDialogSize()
    {
        if (mDialog == null) return;

        WindowManager.LayoutParams params =
                mDialog.getWindow().getAttributes();
        params.width = mDialogSize.width;
        params.height = mDialogSize.height;
        mDialog.getWindow().setAttributes(params);
    }
}
