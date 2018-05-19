package com.example.tutu.facedectection.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.tutu.facedectection.R;
import com.example.tutu.facedectection.adapter.DialKeyAdapter;
import com.example.tutu.facedectection.adapter.SpeceItemDecoration;
import com.example.tutu.facedectection.base.BaseFragment;
import com.example.tutu.facedectection.utils.Constants;
import com.example.tutu.facedectection.utils.DataUtils;
import com.example.tutu.facedectection.utils.FaceToast;

/**
 * 密码解锁的页面
 */


public class PasswordUnlockFragment extends BaseFragment implements View.OnClickListener {
    // 数字键列表
    private RecyclerView mNumListView;

    // 显示密码文本
    private TextView mShowPasswordText;

    private Button mFaceDetectButton;

    // 返回按钮
    private Button mBackButton;

    @Override
    public int getLayoutId()
    {
        return R.layout.confirm_password_fragment;
    }

    @Override
    public void initView(View view, @Nullable Bundle savedInstanceState)
    {
        super.initView(view, savedInstanceState);

        mShowPasswordText = view.findViewById(R.id.lsq_show_password_text);
        mShowPasswordText.addTextChangedListener(mTextWatcher);
        mNumListView = view.findViewById(R.id.lsq_num_list_view);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 3);
        mNumListView.addItemDecoration(new SpeceItemDecoration(Constants.DIAL_KEY_SPACE_LEFT, Constants.DIAL_KEY_SPACE_LEFT,
                Constants.DIAL_KEY_SPACE_LEFT, Constants.DIAL_KEY_SPACE_LEFT));
        mNumListView.setLayoutManager(gridLayoutManager);
        DialKeyAdapter adapter = new DialKeyAdapter(getActivity());
        adapter.setOnDialKeyClickListener(mOnDialKeyClickListener);
        mNumListView.setAdapter(adapter);

        mFaceDetectButton = view.findViewById(R.id.lsq_face_detect_again);
        mFaceDetectButton.setOnClickListener(this);

        mBackButton = view.findViewById(R.id.lsq_back_btn);
        mBackButton.setOnClickListener(this);
    }

    /**
     * 监听密码显示框变化
     */
    private TextWatcher mTextWatcher = new TextWatcher()
    {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after)
        {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count)
        {
        }

        @Override
        public void afterTextChanged(Editable s)
        {
            // 密码输入完成后验证密码
            if (s.length() == Constants.PASSWORD_LENGTH)
            {
                if (DataUtils.getPassword().equals(s.toString()))
                {
                    FaceToast.makeText(getActivity(), R.string.lsq_password_right);
                    showFragment(new HomePageFragment());
                }
                else
                {
                    mShowPasswordText.setText("");
                    showFragment(new HomePageFragment());
                }
            }
        }
    };

    private DialKeyAdapter.OnDialKeyClickListener mOnDialKeyClickListener = new DialKeyAdapter.OnDialKeyClickListener()
    {
        @Override
        public void onClick(String value)
        {
            mShowPasswordText.setText(mShowPasswordText.getText() + value);
        }
    };

    @Override
    public void onClick(View v)
    {
        if (v == mFaceDetectButton)
        {
            showFragment(new FaceUnLockFragment());
        }
        else if (v == mBackButton)
        {
            showFragment(new HomePageFragment());
        }
    }


}
