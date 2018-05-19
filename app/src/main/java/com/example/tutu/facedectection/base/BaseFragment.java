package com.example.tutu.facedectection.base;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.tutu.facedectection.R;
import com.example.tutu.facedectection.fragment.HomePageFragment;
import com.example.tutu.facedectection.utils.PermissionUtils;

/**
 * Created by LiuHang on 11/27/2017.
 */

/**
 * 视图组件基类
 */
public class BaseFragment extends Fragment
{
    // 布局Id
    private int mLayoutId;

    // 返回按钮
    private Button mBackButton;

    /**
     * Fragment监听事件：实现隐藏和显示Fragment
     */
    public interface OnFragmentListener
    {
        void onShowFragment(BaseFragment fragment);
    }

    public OnFragmentListener mOnFragmentListener;

    public void setFragmentListener(OnFragmentListener onFragmentListener)
    {
        this.mOnFragmentListener = onFragmentListener;
    }

    /**
     * 获取布局Id
     * @return
     */
    public int getLayoutId()
    {
        return mLayoutId;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(getLayoutId(), null);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        initView(view, savedInstanceState);
    }

    public void initView(View view, @Nullable Bundle savedInstanceState)
    {
        mBackButton = view.findViewById(R.id.lsq_back_btn);
        if (mBackButton != null)
            mBackButton.setOnClickListener(mOnClickListener);
    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            showFragment(new HomePageFragment());
        }
    };

    public void showFragment(BaseFragment fragment)
    {
        if (mOnFragmentListener != null)
            mOnFragmentListener.onShowFragment(fragment);
    }

    @Override
    public void onPause()
    {
        super.onPause();
    }

    @Override
    public void onResume()
    {
        super.onResume();

        // 是否有权限访问相机
        // Android SDK 23 +，运行时请求权限
        if (PermissionUtils.hasRequiredPermissions(getActivity(), getRequiredPermissions()))
        {
//            prepareStartCamera();
        }
        else
        {
            PermissionUtils.requestRequiredPermissions(getActivity(), getRequiredPermissions());
        }
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
    }


    /**
     * 处理用户的许可结果
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        PermissionUtils.handleRequestPermissionsResult(requestCode, permissions, grantResults, getActivity(), mGrantedResultDelgate);
    }

    private PermissionUtils.GrantedResultDelgate mGrantedResultDelgate = new PermissionUtils.GrantedResultDelgate()
    {
        @Override
        public void onPermissionGrantedResult(boolean permissionGranted)
        {

            if (permissionGranted)
            {
//                prepareStartCamera();
            }
        }
    };

    private String[] getRequiredPermissions()
    {
        String[] permissions = new String[]{
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
        };

        return permissions;
    }

    public boolean onBackPressed()
    {
        return true;
    }
}
