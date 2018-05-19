package com.example.tutu.facedectection.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import com.example.tutu.facedectection.R;
import com.example.tutu.facedectection.base.BaseFragment;

/**
 * 面部信息采集完成页面
 */


public class FaceDetectCompletedFragment extends BaseFragment implements View.OnClickListener {
    // 面部信息检测完成
    private Button mFaceDetectionFinishBtn;

    @Override
    public int getLayoutId()
    {
        return R.layout.face_detect_completed_fragment;
    }

    @Override
    public void initView(View view, @Nullable Bundle savedInstanceState)
    {
        super.initView(view, savedInstanceState);
        mFaceDetectionFinishBtn = view.findViewById(R.id.lsq_face_dect_finish_btn);
        mFaceDetectionFinishBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        if (v == mFaceDetectionFinishBtn)
        {
            showFragment(new HomePageFragment());
        }
    }
}
