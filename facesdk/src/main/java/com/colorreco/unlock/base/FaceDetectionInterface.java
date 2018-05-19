package com.colorreco.unlock.base;

/**
 * Created by LiuHang on 12/22/2017.
 */


public interface FaceDetectionInterface
{
    /**
     * 初始化检测功能
     */
    void init();

    /**
     * 检测数据
     *
     * @param yuv 视频帧数据。格式YUV420SP.
     * @param width 图片宽，单位像素，最大支持1280。
     * @param height 图片高，单位像素，最大支持720。
     * @param isMaxface 大于0表示当检测出多张人脸时处理最大人脸。
     * @param pose 人脸角度信息，内存需要提前申请。预留接口，目前可传null。
     * @param degree 人脸在图片里的朝向。Android平台，横屏：0，竖屏：270.
     * @return
     */
    int [] detect(byte[] yuv, int width, int height, int isMaxface,float[]pose,int degree);

    /**
     * 视频流特征抽取函数。
     * @param yuv 视频帧数据。格式YUV420SP.
     * @param width 图片宽，单位像素，最大支持1280。
     * @param height 图片高，单位像素，最大支持720。
     * @param facebox 需要处理的人脸框。长度为4的数组，分别表示：左上角X坐标，左上角Y坐标，人脸框宽，人脸框高。如果输入视频帧数据是竖屏，人脸框坐标需要用detectYUV接口获取。
     * @param feature 长度为256的数组，用于存放抽取到的人脸特征。
     * @param degree 人脸在图片里的朝向。Android平台，横屏：0，竖屏：270.
     * @return 256：正确；-2：人脸框错误；-3：抽取特征失败；-4：图片过大；-5：授权问题
     */
     int extractYUV(byte[] yuv, int width, int height, int[] facebox, float[] feature,int degree);


    /**
     * 特征比对函数。
     * @param featureA 抽取到的人脸特征，长度为256的数组。
     * @param featureB 抽取到的人脸特征，长度为256的数组。
     * @return 相似度。-1.000--1.000.值越大表示相似度越高。
     */
    float match(float[] featureA, float[] featureB);

    /**
     * 检测是否活体
     *
     * @return
     *      0.0f ~ 1.0f,值越大假体可能性越大
     */
    float getLive();
}
