package com.example.tutu.facedectection.fragment;

import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.colorreco.unlock.FaceUnlockSdk;
import com.colorreco.unlock.bean.FaceDetectionState;
import com.colorreco.unlock.bean.FaceFeature;
import com.colorreco.unlock.bean.FaceInitialState;
import com.colorreco.unlock.bean.FaceRotation;
import com.colorreco.unlock.bean.Person;
import com.colorreco.unlock.bean.PictureOrientation;
import com.colorreco.unlock.delegate.FaceCollectionDelegate;
import com.colorreco.unlock.option.FaceDetectionOption;
import com.example.tutu.facedectection.R;
import com.example.tutu.facedectection.base.FaceDetectionBaseFragment;
import com.example.tutu.facedectection.utils.Constants;
import com.example.tutu.facedectection.utils.DataUtils;
import com.example.tutu.facedectection.utils.NameDialog;

import org.lasque.tusdk.core.TuSdkContext;
import org.lasque.tusdk.core.struct.TuSdkSize;
import org.lasque.tusdk.core.utils.StringHelper;
import org.lasque.tusdk.core.utils.ThreadHelper;
import org.lasque.tusdk.core.utils.image.BitmapHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 人脸检测
 */


public class FaceDetectionFragment extends FaceDetectionBaseFragment implements FaceCollectionDelegate {

    /** 需要检测的人脸角度 */
    private final FaceRotation[] mRotations = new FaceRotation[]{FaceRotation.FaceFront, FaceRotation.FaceLeft, FaceRotation.FaceRight,
           };

    /** 需要检测的人脸角度集合 */
    private List<FaceRotation> mRotationList = new ArrayList<FaceRotation>();

    // 标记人脸检测是否完成
    private boolean isFaceDetectCompleted = false;

   // 人脸采集步骤文案
   private TextView mFaceDetectStep;

   // 人脸检测状态文本
   private TextView mFaceDetectState;

    // 人脸检测指示图标
   private ImageView mLeftArrow;
   private ImageView mRightArrow;

   // 采集人脸的对象
   private Person mPerson;

   private String mPersonId;

    @Override
    public int getLayoutId() {
        return R.layout.face_detect_fragment;
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
        // 需要放在mPreviewSurface创建完成后调用
        adjustCameraViewSize();

        TextView titleView = view.findViewById(R.id.lsq_title);
        titleView.setText(R.string.lsq_face_detect_title);
        mFaceDetectStep = view.findViewById(R.id.lsq_face_detect_step);
        mFaceDetectStep.setText(R.string.lsq_detect_front_face);
        mFaceDetectState = view.findViewById(R.id.lsq_face_dect_state_text);
        mLeftArrow = view.findViewById(R.id.lsq_arrow_left);
        mRightArrow = view.findViewById(R.id.lsq_arrow_right);
        initFaceSDK();
    }

    /**
     * 初始化人脸检测SDK
     */
    protected void initFaceSDK()
    {
        FaceDetectionOption option =  new FaceDetectionOption();

        // 设置图片或者视频流的大小(最大720P)
        option.setPictureSize(getPreviewSize());

        // 设置检测的图片或视频流朝向(横屏：Landscape, 竖屏：Portrait)
        // 默认：Portrait
        if(mCameraManager.getCameraInfo().orientation == 90)
            option.setOrientation(PictureOrientation.Portrait2);
        else
            option.setOrientation(PictureOrientation.Portrait);

        // 开启同个人检测，默认：false
        option.setEnableSamePersonDetect(false);

        // 设置其余角度脸部信息与正脸信息相似度，只有在setEnableSamePersonDetect为true时生效
        // 默认：0.5f (0.5f~1.0f)
        option.setFrontFaceMatchValue(Constants.SIMIRAR_FRONT_FACE);

        // 人脸检测成功后，是否将检测数据转成Bitmap, 默认：false
        option.setIsSaveBitmap(true);

        // 为了保证采集数据的可靠性，采取跳帧的方式，数据稳定后再开始采集, 默认：10
        option.setJumpFrames(Constants.MIN_DETECT_FRONT_FACE_NUM);

        // 设置需要采集的人脸角度信息
        option.setFaceRotationList(getRotationList());

        // 标记独一无二的Person
        mPersonId = StringHelper.uuid();
        option.setPersonId(mPersonId);
        mFaceUnlock = new FaceUnlockSdk();

        mFaceUnlock.setComponentOption(option);

        // 设置回调，接收检测的结果以及状态信息
        // 需要放在init方法前,不然初始化状态无法获取
        mFaceUnlock.setFaceColletionDelegate(this);

        // 使用前必须先初始化
        // 没有初始化或者初始化失败都无法正常使用功能
        mFaceUnlock.init();
    }

    public List<FaceRotation> getRotationList()
    {
        if (mRotationList.size() > 1) return mRotationList;

        for (FaceRotation rotation : mRotations)
        {
            mRotationList.add(rotation);
        }

        return mRotationList;
    }

    @Override
    public void initState(FaceInitialState state)
    {
    }

    @Override
    public void onExtractFeatureListCompeleted(Person person, List<Bitmap> bitmapList)
    {
        this.mPerson = person;

        ThreadHelper.post(new Runnable()
        {
            @Override
            public void run()
            {
                NameDialog nameDialog = new NameDialog(getActivity());
                nameDialog.setDialogSize(getDialogSize());
                nameDialog.setDelegate(mDialogDelegate);
                nameDialog.show();
            }
        });

    }

    @Override
    public void onExtractFeatureSuccess(final FaceFeature faceFeature, Bitmap bitmap)
    {
        final FaceRotation rotation = faceFeature.mRotation;

        ThreadHelper.post(new Runnable()
        {
            @Override
            public void run()
            {
                updateFaceRotationHint(rotation);
            }
        });

        if (bitmap != null)
            saveBitmap(bitmap, rotation);
    }

    @Override
    public void onFaceDetectionState(final FaceDetectionState state)
    {
        ThreadHelper.post(new Runnable()
        {
            @Override
            public void run()
            {
                updateFaceDetectionState(state);
            }
        });
    }

    /**
     * 获取Dialog大小
     *
     * @return
     */
    public TuSdkSize getDialogSize()
    {
        TuSdkSize size = new TuSdkSize();
        size.width = TuSdkContext.getScreenSize().width;
        size.height = TuSdkContext.getScreenSize().height / 3;

        return size;
    }

    private NameDialog.DialogDelegate mDialogDelegate = new NameDialog.DialogDelegate()
    {
        @Override
        public void onEditTextContent(String content)
        {
            if (mPerson == null) return;

            // 为Person设置姓名
            mPerson.username = content;
            DataUtils.savePerson(mPerson);

            showFragment(new FaceDetectCompletedFragment());
        }
    };

    /**
     * 更新采集的状态
     *
     * @param state
     */
    public void updateFaceDetectionState(FaceDetectionState state)
    {
        int textId = 0;
        int textColorId = 0;
        if (state == FaceDetectionState.FACE_UNDETECED)
        {
            textId = R.string.lsq_face_dect_no_one;
            textColorId = R.color.lsq_color_red;
        }
        else if (state == FaceDetectionState.FACE_WRONG_ROTATION)
        {
            textId = R.string.lsq_face_wrong_rotation;
            textColorId = R.color.lsq_color_red;
        }
        else if (state == FaceDetectionState.FACE_DETECTED)
        {
            textId = R.string.lsq_face_detected;
            textColorId = R.color.lsq_color_green;
        }
        else if (state == FaceDetectionState.FACE_DETECTION_SUCCESS)
        {
            textId = R.string.lsq_face_detection_success;
            textColorId = R.color.lsq_color_green;
        }

        if (textId != 0 && textColorId != 0)
        {
            // 避免页面销毁,获取资源失败
            if (getActivity() == null) return;
            mFaceDetectState.setTextColor(getResources().getColor(textColorId));
            mFaceDetectState.setText(textId);
        }
    }

    @Override
    public void onPreviewFrame(final byte[] data, Camera camera)
    {
        ThreadHelper.runThread(new Runnable()
        {
            @Override
            public void run()
            {
                if (isFaceDetectCompleted) return;

                isFaceDetectCompleted = true;
                byte[] copyData = DataUtils.copyBytes(data);
                mFaceUnlock.detectFaceList(copyData);

                isFaceDetectCompleted = false;
            }
        });
    }

    /**
     * 更新人脸角度提示
     *
     * @param rotation
     */
    private void updateFaceRotationHint(FaceRotation rotation)
    {
        updateFaceRotationText(rotation);
        updateFaceRotationArrow(rotation);
    }

    /**
     * 更新人脸角度方向文案
     *
     * @param rotation
     */
    public void updateFaceRotationText(FaceRotation rotation)
    {
        int textDetectId = 0;

        if (rotation == FaceRotation.FaceFront)
        {
            textDetectId = R.string.lsq_detect_left_face;
        }
        else if (rotation == FaceRotation.FaceLeft)
        {
            textDetectId = R.string.lsq_detect_right_face;
        }
        else if (rotation ==  FaceRotation.FaceRight)
        {
            textDetectId = R.string.lsq_detect_front_face;
        }

        if (textDetectId == 0) return;

        mFaceDetectStep.setText(textDetectId);
    }

    /**
     * 更新人脸角度指示图标
     */
    private void updateFaceRotationArrow(FaceRotation faceRotation)
    {
        mLeftArrow.setVisibility(View.INVISIBLE);
        mRightArrow.setVisibility(View.INVISIBLE);

        switch (faceRotation)
        {
            case FaceFront:
                mLeftArrow.setVisibility(View.VISIBLE);
                break;
            case FaceLeft:
                mRightArrow.setVisibility(View.VISIBLE);
                break;
            case FaceRight:
                break;
            default:break;
        }
    }

    /**
     * 将检测数据保存成图片
     * @param bitmap
     * @param faceRotation
     */
    protected void saveBitmap(Bitmap bitmap, FaceRotation faceRotation)
    {
        File tempFile = DataUtils.getTempFile(faceRotation, mPersonId);
        BitmapHelper.saveBitmap(tempFile, bitmap, mCameraManager.getCameraInfo().orientation);
    }
}
