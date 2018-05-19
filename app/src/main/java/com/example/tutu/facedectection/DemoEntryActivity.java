package com.example.tutu.facedectection;

import android.Manifest;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.example.tutu.facedectection.base.BaseFragment;
import com.example.tutu.facedectection.base.FaceDetectionBaseFragment;
import com.example.tutu.facedectection.base.SimpleActivity;
import com.example.tutu.facedectection.camera.CameraManager;
import com.example.tutu.facedectection.fragment.FaceUnLockFragment;
import com.example.tutu.facedectection.fragment.HomePageFragment;
import com.example.tutu.facedectection.utils.FaceToast;
import com.example.tutu.facedectection.utils.PermissionUtils;

import java.util.Stack;

/**
 * 首页
 */
public class DemoEntryActivity extends SimpleActivity
{
    // 相机管理类
    private CameraManager mCameraManager;

    // 使用一个栈记录所有添加的Fragment
    private Stack<BaseFragment> mFragmentStack = new Stack<BaseFragment>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo_entry_activity);

        if (!PermissionUtils.hasRequiredPermissions(this, getRequiredPermissions()))
        {
            PermissionUtils.requestRequiredPermissions(this, getRequiredPermissions());
        }
        else
        {
            // 启动首页Fragment
            chooseFaceUnlock();
        }

        mCameraManager = new CameraManager(this);
        mCameraManager.pause();
    }

    @Override
    protected void onPause()
    {
        super.onPause();

        mCameraManager.pause();
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        mCameraManager.resume();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();

        mCameraManager.destroy();
    }

    /**
     * 检查是否启动人脸解锁
     */
    private void chooseFaceUnlock()
    {
        showFragment(new HomePageFragment());
    }

    public void showFragment(BaseFragment fragment)
    {
        // 添加Fragment前先移除上一个Fragment
        removeFragment();

        if (fragment instanceof  BaseFragment)
        {
            fragment.setFragmentListener(mOnFragmentListener);
        }

        if (fragment instanceof FaceDetectionBaseFragment)
        {
            FaceDetectionBaseFragment newFragment = ((FaceDetectionBaseFragment) fragment);
            mCameraManager.resume();
            newFragment.setCameraManager(mCameraManager);
        }

        // 添加fragment并显示
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.fragment_container, fragment);
        transaction.show(fragment);
        // 使用commitAllowingStateLoss可以解决应用切到后台时调用此方法出现的崩溃问题
        transaction.commitAllowingStateLoss();
        // 记录显示的Fragment
        mFragmentStack.add(fragment);
    }

    /**
     * 移除Fragment
     */
    public void removeFragment()
    {
        if (mFragmentStack == null || mFragmentStack.empty()) return;

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        // 获取显示的Fragment
        Fragment showFragment = mFragmentStack.pop();
        // 使用commitAllowingStateLoss可以解决应用切到后台时调用此方法出现的崩溃问题
        transaction.remove(showFragment).commitAllowingStateLoss();
    }

    /**
     * 实现Fragment的隐藏和显示
     */
    private BaseFragment.OnFragmentListener mOnFragmentListener = new BaseFragment.OnFragmentListener()
    {
        @Override
        public void onShowFragment(BaseFragment fragment)
        {
            showFragment(fragment);
        }
    };

    /**
     * 组件运行需要的权限列表
     *
     * @return
     *            列表数组
     */
    @TargetApi(Build.VERSION_CODES.M)
    protected String[] getRequiredPermissions()
    {
        String[] permissions = new String[]{
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA,
        };

        return permissions;
    }

    /**
     * 处理用户的许可结果
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionUtils.handleRequestPermissionsResult(requestCode, permissions, grantResults, this, mGrantedResultDelgate);
    }

    /**
     * 授予权限的结果，在对话结束后调用
     *
     * @param permissionGranted
     *            true or false, 用户是否授予相应权限
     */
    protected PermissionUtils.GrantedResultDelgate mGrantedResultDelgate = new PermissionUtils.GrantedResultDelgate()
    {
        @Override
        public void onPermissionGrantedResult(boolean permissionGranted)
        {
            if (permissionGranted)
            {
                chooseFaceUnlock();
            }
            else
            {
                FaceToast.makeText(DemoEntryActivity.this, R.string.lsq_deny_permission);
            }
        }
    };

    @Override
    public void onBackPressed()
    {
        if (mFragmentStack == null && mFragmentStack.empty()) return;

        BaseFragment showfragment = mFragmentStack.pop();

        if (showfragment instanceof HomePageFragment || showfragment instanceof FaceUnLockFragment)
        {

            if (showfragment.onBackPressed())
                finish();
            else
                mFragmentStack.add(showfragment);
        }
        else
        {
            removeFragment();
            showFragment(new HomePageFragment());
        }
    }
}
