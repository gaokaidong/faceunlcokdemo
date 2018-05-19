package com.example.tutu.facedectection.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.colorreco.unlock.bean.FaceUnlockInfo;
import com.example.tutu.facedectection.R;
import com.example.tutu.facedectection.base.BaseFragment;
import com.example.tutu.facedectection.utils.UnlockInfoString;

/**
 * 人脸解锁信息的展示页面
 */


public class FaceUnLockInfoFragment extends BaseFragment implements View.OnClickListener {
    private Button mconfirmBtn;

    private TextView mFaceUnlockText;

    private FaceUnlockInfo mFaceUnlockInfo;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int getLayoutId()
    {
        return R.layout.face_unlock_info_fragment;
    }

    @Override
    public void initView(View view, @Nullable Bundle savedInstanceState)
    {
        super.initView(view, savedInstanceState);

        mconfirmBtn = view.findViewById(R.id.lsq_face_unlock_confirm_btn);
        mFaceUnlockText = view.findViewById(R.id.lsq_face_unlock_text);
        mconfirmBtn.setOnClickListener(this);
        mFaceUnlockText.setText(getFaceUnlockInfo(mFaceUnlockInfo));
        ImageView unlockStateIcon = view.findViewById(R.id.lsq_unlock_state_icon);
        TextView unlockStateText = view.findViewById(R.id.lsq_unlock_state_text);

        if (!mFaceUnlockInfo.getUnlockState())
        {
            unlockStateIcon.setBackgroundResource(R.mipmap.lsq_face_unlock_failed);
            unlockStateText.setText(getResources().getString(R.string.lsq_unlock_failed));
            unlockStateText.setTextColor(getResources().getColor(R.color.lsq_color_red));
        }
    }

    @Override
    public void onClick(View v)
    {
        if (v == mconfirmBtn)
        {
            showFragment(new HomePageFragment());
        }
    }

    /**
     * 设置人脸解锁的信息
     *
     * @param faceUnlockInfo
     */
    public void setFaceUnlockInfo(FaceUnlockInfo faceUnlockInfo)
    {
        this.mFaceUnlockInfo = faceUnlockInfo;
    }

    /**
     * 获取人脸解锁信息文本
     *
     * @param faceUnlockInfo
     * @return
     */
    public String getFaceUnlockInfo(FaceUnlockInfo faceUnlockInfo)
    {
        UnlockInfoString unlockInfoString = new UnlockInfoString();
        unlockInfoString.setInfo(faceUnlockInfo);
        return unlockInfoString.toString();
    }
}
