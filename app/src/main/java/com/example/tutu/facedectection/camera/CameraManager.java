package com.example.tutu.facedectection.camera;

import android.app.Activity;
import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.view.Surface;
import android.view.SurfaceHolder;

import org.lasque.tusdk.core.struct.TuSdkSize;
import org.lasque.tusdk.core.utils.hardware.CameraHelper;

import java.io.IOException;

/**
 * Created by LiuHang on 1/4/2018.
 */


public class CameraManager
{
    // 前置摄像头
    private final int CAMERA_FRONT = 1;

    // 设置的预览分辨率宽
    private final int PREVIEW_WIDTH = 640;

    // 设置的预览分辨率高
    private final int PREVIEW_HEIGHT = 480;

    private Camera mCamera;

    // 相机预览大小
    protected TuSdkSize mPreviewSize = new TuSdkSize();

    // 相机是否已启动
    private boolean mCameraStarted;

    // 页面是否暂停
    private boolean mCapturePaused;

    // 上下文
    private Context mContext;

    public interface CameraManagerDelegate
    {
        /**
         * 接收相机预览数据
         *
         * @param bytes YUV数据
         * @param camera 相机对象
         */
        void onPreviewFrame(byte[] bytes, Camera camera);
    }

    private CameraManagerDelegate mDelegate;

    /**
     * 注册回调
     *
     * @param delegate
     */
    public void registerDelegate(CameraManagerDelegate delegate)
    {
        this.mDelegate = delegate;
    }

    /**
     * 解绑回调
     */
    public void unregisterDelegate()
    {
        this.mDelegate = null;
    }

    public CameraManager(Context context)
    {
        this.mContext = context;

        startCamera();
    }

    protected void initCamera() {
        if (mCamera != null) {
            return;
        }


        mCamera = Camera.open(CAMERA_FRONT);

        if (mCamera == null) {
            throw new RuntimeException("Unable to open camera");
        }

        Camera.Parameters params = mCamera.getParameters();

        // 设置预览尺寸
        TuSdkSize screenSize = new TuSdkSize(PREVIEW_WIDTH, PREVIEW_HEIGHT);

        // 选择和屏幕比例适配的预览尺寸
        CameraHelper.setPreviewSize(this.mContext, params, screenSize.maxSide(), 0.75f, screenSize.getRatioFloat());

        CameraHelper.setFocusMode(params, CameraHelper.focusModes);

        //CameraHelper.logParameters(params);

        // leave the frame rate set to default

        mCamera.setParameters(params);
//        mCamera.setDisplayOrientation(90);
        setCameraDisplayOrientation((Activity) mContext,CAMERA_FRONT,mCamera);
        Camera.Size previewSize = mCamera.getParameters().getPreviewSize();
        mPreviewSize.width = previewSize.width;
        mPreviewSize.height = previewSize.height;
    }
    private CameraInfo info;
    public CameraInfo getCameraInfo()
    {
        return info;
    }
    /**
     * 设置摄像头旋转角度
     * @param activity
     * @param cameraId 1前置
     * @param camera
     */
    private void setCameraDisplayOrientation(Activity activity,
                                                   int cameraId, android.hardware.Camera camera) {
        info = new CameraInfo();
        android.hardware.Camera.getCameraInfo(cameraId, info);
        int rotation = activity.getWindowManager().getDefaultDisplay()
                .getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0: degrees = 0; break;
            case Surface.ROTATION_90: degrees = 90; break;
            case Surface.ROTATION_180: degrees = 180; break;
            case Surface.ROTATION_270: degrees = 270; break;
        }

        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;  // compensate the mirror
        } else {  // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }
        camera.setDisplayOrientation(result);
    }

    /**
     * 获取相机的预览分辨率
     *
     * @return
     */
    public TuSdkSize getPreviewSize()
    {
        return mPreviewSize;
    }

    protected void stopCamera()
    {
        if (mCamera != null) {
            mCamera.stopPreview();
            setPreviewCallback(null);
            mCamera.release();
            mCamera = null;
        }

        mCameraStarted = false;
    }

    protected void startCamera()
    {
        if (mCamera == null)
            initCamera();

        // 开始相机预览
        tryStartPreview();
    }


    protected void tryStartPreview()
    {
        if (mCameraStarted) return;

        mCapturePaused = false;

        if (mCamera != null)
        {
            mCamera.startPreview();

            mCameraStarted = true;
        }
    }

    protected void pauseCamera()
    {
        if (mCamera != null)
        {
            mCamera.stopPreview();
        }

        mCapturePaused = true;
    }

    protected void resumeCamera()
    {
        if (mCamera != null)
        {
            mCamera.startPreview();
        }

        mCapturePaused = false;
    }


    public void pause()
    {
        pauseCamera();
    }

    public void resume()
    {
        if (mCapturePaused)
        {
            resumeCamera();
            return;
        }
    }

    public void destroy()
    {
        stopCamera();
    }

    public void setPreviewCallback(Camera.PreviewCallback callback)
    {
        if (mCamera == null) return;

        mCamera.setPreviewCallback(callback);
    }

    //--------------------------------- Preview callback -----------------------------------------

    public void setSurfaceHolder(SurfaceHolder surfaceHolder)
    {
        // 设置预览渲染容器， 注意：SurfaceView 内部渲染的是原始画面
        try
        {
            mCamera.setPreviewDisplay(surfaceHolder);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public Camera.PreviewCallback mPreviewCallback = new Camera.PreviewCallback()
    {
        @Override
        public void onPreviewFrame(byte[] data, Camera camera)
        {
            // 必须添加
            camera.addCallbackBuffer(data);

            if (mDelegate != null)
            {
                mDelegate.onPreviewFrame(data, camera);
            }
        }
    };
}
