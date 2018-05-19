package com.colorreco.unlock.delegate;

import android.graphics.Bitmap;

import com.colorreco.unlock.bean.FaceDetectionState;
import com.colorreco.unlock.bean.FaceFeature;
import com.colorreco.unlock.bean.Person;

import java.util.List;

/**
 * 人脸检测委托
 */


public interface FaceCollectionDelegate extends FaceDetectionBaseDelegate
{
    /**
     * 各个角度特征值抽取完成
     *
     * @param person 存储用户所有的特征值信息
     * @param bitmapList 所有角度照片
     */
    void onExtractFeatureListCompeleted(Person person, List<Bitmap> bitmapList);

    /**
     * 单个角度抽取特征值完成
     *
     * @param faceFeature
     */
    void onExtractFeatureSuccess(FaceFeature faceFeature, Bitmap bitmap);

    /**
     * 人脸采集的状态
     *
     * @param state
     */
    void onFaceDetectionState(FaceDetectionState state);
}
