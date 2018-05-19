package com.example.tutu.facedectection.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.colorreco.unlock.bean.Person;
import com.example.tutu.facedectection.R;
import com.example.tutu.facedectection.base.BaseFragment;
import com.example.tutu.facedectection.utils.DataUtils;
import com.example.tutu.facedectection.utils.FaceToast;

import org.lasque.tusdk.core.utils.StringHelper;

import java.util.ArrayList;

/**
 * 登陆首页
 */

public class HomePageFragment extends BaseFragment implements View.OnClickListener
{
    // 添加密码按钮
    private RelativeLayout mAddPasswordBtn;

    // 添加脸部信息按钮
    private RelativeLayout mAddFaceInfoBtn;

    // 人脸解锁按钮
    private RelativeLayout mFaceUnlockBtn;

    // 添加密码文本
    private TextView mAddPasswordText;

    // 添加人脸信息文本
    private TextView mAddFaceInfoText;

    private ArrayList<Person> mPersons;

    // 处理返回操作
    private Handler mBackHandler;

    // 是否即将退出程序
    private boolean mIsWillExit = false;

    // 设置按钮
    private ImageView mSettingBtn;

    @Override
    public int getLayoutId()
    {
        return R.layout.home_page_fragment;
    }

    @Override
    public void initView(View view, @Nullable Bundle savedInstanceState)
    {
        super.initView(view, savedInstanceState);

        mAddPasswordBtn = view.findViewById(R.id.lsq_add_password);
        mAddFaceInfoBtn = view.findViewById(R.id.lsq_add_face_info);
        mAddPasswordBtn.setOnClickListener(this);
        mAddFaceInfoBtn.setOnClickListener(this);
        mFaceUnlockBtn = view.findViewById(R.id.lsq_face_unlock_btn);
        mFaceUnlockBtn.setOnClickListener(this);
        mAddFaceInfoText = view.findViewById(R.id.lsq_add_face_text);
        mAddPasswordText = view.findViewById(R.id.lsq_add_password_text);
        mSettingBtn = view.findViewById(R.id.lsq_setting_btn);
        mSettingBtn.setOnClickListener(this);

        if (StringHelper.isEmpty(DataUtils.getPassword()))
            changeAddPasswordButtonText(R.string.lsq_add_password);
        else
            changeAddPasswordButtonText(R.string.lsq_modify_password);

        mPersons = DataUtils.getPersons();

        if (mPersons == null || mPersons.size() < 1)
            changeFaceDetectButtonText(R.string.lsq_add_face_info);
        else
            changeFaceDetectButtonText(R.string.lsq_modify_face_info);

        mBackHandler = new Handler(new Handler.Callback()
        {
            @Override
            public boolean handleMessage(Message msg)
            {
                mIsWillExit = false;
                return false;
            }
        });
    }

    private void changeFaceDetectButtonText(int strId)
    {
        mAddFaceInfoText.setText(strId);
    }

    @Override
    public void onClick(View v)
    {
        if (v == mAddFaceInfoBtn)
        {
            showFragment(new FaceDetectionHintFragment());
        }
        else if (v == mFaceUnlockBtn)
        {
            if (mPersons == null || mPersons.size() < 1)
            {
                FaceToast.makeText(getActivity(), R.string.lsq_face_info_lack);
                return;
            }

            showFragment(new FaceUnLockFragment());
        }
        else if (v == mSettingBtn)
        {
            showFragment(new FaceSettingFragment());
        }
    }

    /**
     * 修改添加密码按钮的文案
     *
     * @param id
     */
    public void changeAddPasswordButtonText(int id)
    {
        if (mAddPasswordText != null)
            mAddPasswordText.setText(id);
    }

    @Override
    public boolean onBackPressed()
    {
        if (!mIsWillExit)
        {
            mIsWillExit = true;
            FaceToast.makeText(getActivity(), R.string.lsq_exit_info);
            this.mBackHandler.sendEmptyMessageDelayed(0, 2000);
            return false;
        }

        return true;
    }
}
