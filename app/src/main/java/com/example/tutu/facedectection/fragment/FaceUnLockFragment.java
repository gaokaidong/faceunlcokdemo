package com.example.tutu.facedectection.fragment;

import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.colorreco.unlock.FaceUnlockParameters;
import com.colorreco.unlock.FaceUnlockSdk;
import com.colorreco.unlock.bean.FaceInitialState;
import com.colorreco.unlock.bean.FaceUnlockInfo;
import com.colorreco.unlock.bean.Person;
import com.colorreco.unlock.bean.PictureOrientation;
import com.colorreco.unlock.delegate.FaceUnlockDelegate;
import com.colorreco.unlock.option.FaceDetectionOption;
import com.example.tutu.facedectection.R;
import com.example.tutu.facedectection.setting.bean.SettingItemData;
import com.example.tutu.facedectection.base.FaceDetectionBaseFragment;
import com.example.tutu.facedectection.utils.Constants;
import com.example.tutu.facedectection.utils.DataUtils;
import com.example.tutu.facedectection.utils.FaceToast;

import org.lasque.tusdk.core.TuSdkContext;
import org.lasque.tusdk.core.utils.ThreadHelper;

import java.util.ArrayList;
import java.util.Map;

/**
 * 人脸识别开锁
 */


public class FaceUnLockFragment extends FaceDetectionBaseFragment implements FaceUnlockDelegate {

    private ArrayList<Person> mPersons;

    // 是否解锁成功
    private boolean isFaceUnlockSuccess;

    // 标记人脸检测是否完成
    private boolean isFaceDetectCompleted = false;

    @Override
    public int getLayoutId()
    {
        return R.layout.face_unlock_fragment;
    }

    @Override
    public void initView(View view, @Nullable Bundle savedInstanceState)
    {
        super.initView(view, savedInstanceState);

        mPreviewSurface = view.findViewById(R.id.previewSurface);
        mSurfaceHolder = mPreviewSurface.getHolder();
        // translucent半透明 transparent透明
        mSurfaceHolder.setFormat(PixelFormat.TRANSLUCENT);
        mSurfaceHolder.addCallback(this);
        mPreviewSize = mCameraManager.getPreviewSize();
        adjustCameraViewSize();

        initFaceSDK();
        mPersons = TuSdkContext.sharedPreferences().loadSharedCacheObject(Constants.SHARE_FACE_FEATURE_KEY);

        TextView titleView = view.findViewById(R.id.lsq_title);
        titleView.setText(R.string.lsq_face_unlock);
    }

    @Override
    protected int getCameraViewWidth()
    {
        return TuSdkContext.getScreenSize().width;
    }

    protected void initFaceSDK()
    {
        FaceDetectionOption option = new FaceDetectionOption();
        // 设置图片或者视频流的大小(最大720P)
        option.setPictureSize(mPreviewSize);

        // 设置检测的图片或视频流朝向(横屏：Landscape, 竖屏：Portrait)
        // 默认：Portrait
        if(mCameraManager.getCameraInfo().orientation == 90)
            option.setOrientation(PictureOrientation.Portrait2);
        else
            option.setOrientation(PictureOrientation.Portrait);

        // 设置人脸解锁的阈值,默认：0.5f (0 ~ 1.0f)
        option.setUnlockThresholdValue((Float) SettingItemData.UNLOCK_THRESHOLD.mDefaultValue);

        // 设置人脸解锁成功后,保存人脸特征的阈值，默认：0.5f (0.5f ~ 1.0f)
        option.setSaveFeatureThresholdValue(Constants.MIN_FACE_INFO_SAVE_VALUE);

        // 是否开启活体检测, 默认：true
        option.setEnableLiveDetection((Boolean) SettingItemData.LIVE_DETECTION.mDefaultValue);

        // 活体检测的阈值, 默认：0.2f, 0.0f ~ 1.0f
        // 值越大，假体可能性越大
        option.setLiveThresholdValue((Float) SettingItemData.LIVE_THRESHOLD.mDefaultValue);

        // 人脸解锁时间上限， 默认：1000 单位：ms
        option.setUnlockTimeLimit((Long) SettingItemData.TIMING_LIMIT.mDefaultValue);

        // 设置是否保存解锁成功的图片, 默认：true
        option.setEnableSaveBitmapAfterUnlocked((Boolean) SettingItemData.SAVE_IMAGE.mDefaultValue);

        mFaceUnlock = new FaceUnlockSdk();

        mFaceUnlock.setComponentOption(option);
        // 设置回调，接收检测的结果以及状态信息
        // 需要放在init方法前,不然初始化状态无法获取
        mFaceUnlock.setFaceUnlockDelegate(this);

        // 使用前必须先初始化
        // 没有初始化或者初始化失败都无法正常使用功能
        mFaceUnlock.init();
    }

    @Override
    public void initState(FaceInitialState state)
    {

    }

    @Override
    public void onFaceUnlockSuccess(final ArrayList<Person> persons, final FaceUnlockInfo faceUnlockInfo)
    {
        ThreadHelper.post(new Runnable()
        {
            @Override
            public void run()
            {
                isFaceUnlockSuccess = true;
                // 将最新的Person对象重新保存起来
                DataUtils.savePersons(persons);
                Map<FaceUnlockParameters, Object> unlockInfo = faceUnlockInfo.getUnlockInfo();

                String successHint;
                if (getActivity() != null)
                {
                    successHint = unlockInfo.get(FaceUnlockParameters.MATCH_USER_NAME) + getString(R.string.lsq_password_right);
                    FaceToast.makeText(getActivity(), successHint);
                }

                intentToInfoFragment(faceUnlockInfo);
            }
        });

    }

    @Override
    public void onFaceUnlockFailed(final FaceUnlockInfo faceUnlockInfo)
    {
        ThreadHelper.post(new Runnable()
        {
            @Override
            public void run()
            {
                intentToInfoFragment(faceUnlockInfo);

                // ToDO 等之后活体完善后再添加
//                unlockHint(faceUnlockInfo);
            }
        });
    }

    /**
     * 解锁提示
     */
    public void unlockHint(FaceUnlockInfo faceUnlockInfo)
    {
        // 未检测到人脸：提示未检测到人脸(抽取特征值时间为空, 说明一直未检测到人脸)
        Map<FaceUnlockParameters, Object> unlockInfo = faceUnlockInfo.getUnlockInfo();

        if (!unlockInfo.containsKey(FaceUnlockParameters.EXTRACT_FEATURE_TIME))
        {
            FaceToast.makeText(getActivity(), R.string.lsq_no_face);
        }
        // 活体攻击提示：检测到为图片或视频
       else if (unlockInfo.containsKey(FaceUnlockParameters.LIVE_DETECT_NUM) && (int)unlockInfo.get(FaceUnlockParameters.LIVE_DETECT_NUM) > 1)
        {
            FaceToast.makeText(getActivity(), R.string.lsq_live_attack);
        }
        // 不是本人：未注册人脸
        else
        {
            FaceToast.makeText(getActivity(), R.string.lsq_not_same_person);
        }
    }

    /**
     * 跳转到解锁信息展示页面
     *
     * @param faceUnlockInfo
     */
    public void intentToInfoFragment(FaceUnlockInfo faceUnlockInfo)
    {
        FaceUnLockInfoFragment fragment = new FaceUnLockInfoFragment();
        fragment.setFaceUnlockInfo(faceUnlockInfo);

        showFragment(fragment);
    }

    @Override
    public void onPreviewFrame(final byte[] data, Camera camera)
    {
        if (mPersons == null || mPersons.size() < 1 || isFaceUnlockSuccess) return;

        // 要采用异步，不然解锁时间会很长
        ThreadHelper.runThread(new Runnable()
        {
            @Override
            public void run()
            {
                if (isFaceDetectCompleted) return;

                isFaceDetectCompleted = true;
                byte[] copyData = DataUtils.copyBytes(data);
                mFaceUnlock.faceUnlock(copyData, mPersons);
                isFaceDetectCompleted = false;
            }
        });
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();

        mFaceUnlock.destroy();
    }
}
