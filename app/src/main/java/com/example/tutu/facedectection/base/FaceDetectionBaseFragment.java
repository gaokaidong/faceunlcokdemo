package com.example.tutu.facedectection.base;

import android.hardware.Camera;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.RelativeLayout;

import com.colorreco.unlock.FaceUnlockSdk;
import com.example.tutu.facedectection.camera.CameraManager;

import org.lasque.tusdk.core.TuSdkContext;
import org.lasque.tusdk.core.struct.TuSdkSize;

/**
 * Created by LiuHang on 1/4/2018.
 */


public class FaceDetectionBaseFragment extends BaseFragment implements SurfaceHolder.Callback, CameraManager.CameraManagerDelegate {
    protected FaceUnlockSdk mFaceUnlock;

    // 相机预览尺寸
    protected TuSdkSize mPreviewSize;

    protected SurfaceView mPreviewSurface;

    protected SurfaceHolder mSurfaceHolder;

    // 相机管理对象
    protected CameraManager mCameraManager;

    @Override
    public void initView(View view, @Nullable Bundle savedInstanceState) {
        super.initView(view, savedInstanceState);
    }

    protected void initFaceSDK()
    {
    }

    /**
     * 设置相机的预览尺寸
     *
     * @param size
     */
    public void setPreviewSize(TuSdkSize size) {
        this.mPreviewSize = size;
    }

    /**
     * 获取相机的预览尺寸
     *
     * @return
     */
    public TuSdkSize getPreviewSize() {
        return mPreviewSize;
    }

    public SurfaceHolder getSurfaceHolder()
    {
        return mSurfaceHolder;
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();

        // 相机暂停重启后再次设置SurfaceHolder才会有画面
        if (mCameraManager != null)
        {
            mCameraManager.setSurfaceHolder(null);
            mCameraManager.unregisterDelegate();
            mCameraManager.setPreviewCallback(null);
            mCameraManager.pause();
        }

        if(mSurfaceHolder != null)
        {
            mSurfaceHolder.removeCallback(this);
            mSurfaceHolder = null;
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder)
    {
        bindHolderToCamera();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
    {
        if (mCameraManager != null)
        {
            // 必须在surfaceChanged中添加setPreviewCallback, 否则有时会收不到回调
            mCameraManager.setPreviewCallback(mCameraManager.mPreviewCallback);
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder)
    {
    }

    public void setCameraManager(CameraManager cameraManager)
    {
        this.mCameraManager = cameraManager;
    }

    public CameraManager getCameraManager()
    {
        return mCameraManager;
    }

    /**
     * 将SurfaceHolder绑定到Camera
     */
    protected void bindHolderToCamera()
    {
        if (mCameraManager == null) return;

        mCameraManager.setSurfaceHolder(mSurfaceHolder);
        mCameraManager.registerDelegate(this);
    }

    /**
     * 接收相机预览数据
     *
     * @param bytes YUV数据
     * @param camera 相机对象
     */
    @Override
    public void onPreviewFrame(byte[] bytes, Camera camera)
    {

    }

    /**
     * 调整相机预览视图大小
     */
    protected void adjustCameraViewSize()
    {
        if (mCameraManager != null)
        {
            mPreviewSize = mCameraManager.getPreviewSize();
        }

        if (mPreviewSurface != null && mPreviewSize != null)
        {
            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) mPreviewSurface.getLayoutParams();
            float ratio = mPreviewSize.width / (float)mPreviewSize.height;
            lp.width = getCameraViewWidth();
            lp.height = (int) (getCameraViewWidth() * ratio);
        }
    }

    /**
     * 获取视频预览区域宽度
     *
     * @return
     */
    protected int getCameraViewWidth()
    {
        return TuSdkContext.getScreenSize().width * 2 / 3;
    }
}
