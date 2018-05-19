package com.example.tutu.facedectection.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.tutu.facedectection.R;
import com.example.tutu.facedectection.base.BaseFragment;
import com.example.tutu.facedectection.utils.Constants;
import com.example.tutu.facedectection.utils.DataUtils;
import com.example.tutu.facedectection.utils.FaceToast;

import org.lasque.tusdk.core.utils.StringHelper;

/**
 * 添加密码
 */

public class PasswordSettingFragment extends BaseFragment implements View.OnClickListener
{
    // 密码输入框
    private TextView mInputPassword;

    // 密码确认框
    private TextView mConfirmPassword;

    // 提交按钮
    private Button mSubmitButton;

    @Override
    public int getLayoutId()
    {
        return R.layout.add_password_fragment;
    }

    @Override
    public void initView(View view, @Nullable Bundle savedInstanceState)
    {
        super.initView(view, savedInstanceState);
        if (view == null) return;

        mInputPassword = view.findViewById(R.id.lsq_input_password_text);
        mConfirmPassword = view.findViewById(R.id.lsq_input_password_again_text);
        mSubmitButton = view.findViewById(R.id.lsq_submit_btn);
        mSubmitButton.setOnClickListener(this);
        TextView titleView = view.findViewById(R.id.lsq_title);

        // 动态判断添加和修改密码的文案
        if (StringHelper.isEmpty(DataUtils.getPassword()))
        {
            mInputPassword.setHint(R.string.lsq_input_password);
            mConfirmPassword.setHint(R.string.lsq_input_password_again);
            titleView.setText(R.string.lsq_add_password);
        }
        else
        {
            mInputPassword.setHint(R.string.lsq_input_modify_password);
            mConfirmPassword.setHint(R.string.lsq_input_modify_password_again);
            titleView.setText(R.string.lsq_modify_password);
        }
    }

    @Override
    public void onClick(View v)
    {
        if (v == mSubmitButton)
        {
            submitPassword();
        }
    }

    /**
     * 提交密码
     */
    public void submitPassword()
    {
        if (StringHelper.isEmpty(mInputPassword.getText().toString()) || StringHelper.isEmpty(mConfirmPassword.getText().toString()))
        {
            FaceToast.makeText(getActivity(), R.string.lsq_password_null);
            return;
        }
        else if (!mInputPassword.getText().toString().equals(mConfirmPassword.getText().toString()))
        {
            FaceToast.makeText(getActivity(), R.string.lsq_password_not_same);
            return;
        }
        else if (mInputPassword.getText().length() != Constants.PASSWORD_LENGTH)
        {
            FaceToast.makeText(getActivity(), R.string.lsq_password_length_not_right);
            return;
        }
        else if (!checkPasswordNum(mInputPassword.getText().toString()))
        {
            FaceToast.makeText(getActivity(), R.string.lsq_password_setting_hint);
            return;
        }

        boolean isSuccess = DataUtils.savePassword(mConfirmPassword.getText().toString());

        if (isSuccess)
        {
            FaceToast.makeText(getActivity(), R.string.lsq_password_submit_success);
            showFragment(new HomePageFragment());
        }
        else
        {
            FaceToast.makeText(getActivity(), R.string.lsq_password_submit_failed);
        }
    }

    /**
     * 检查密码是否为全数字
     *
     * @param passwordText
     * @return
     */
     private boolean checkPasswordNum(String passwordText)
     {
        if ("".equals(passwordText)) return false;

        char[] passwordNum = passwordText.toCharArray();

        for (int num : passwordNum)
        {
            if (!Character.isDigit(num))
                return false;
        }

        return true;
     }
}
