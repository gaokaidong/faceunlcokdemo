package com.colorreco.fire.delegate;

/**
 * Created by LiuHang on 12/22/2017.
 */


public interface FireDetectionInterface
{
    public interface FireSDKDelegate
    {
        /**
         * 火的检测结果
         *
         * @param hasFire 是否有火
         * @param firPos 火的坐标
         */
        void onFireDetectionResult(boolean hasFire, int[] firPos);
    }

    /**
     * 初始化检测功能
     */
   void init();

    /**
     * 检测是否有火
     * @param data 视频帧数据。格式YUV420SP.
     * @param width 图片宽，单位像素，最大支持1280。
     * @param height 图片高，单位像素，最大支持720。
     * @param degree 人脸在图片里的朝向。Android平台，横屏：0，竖屏：270.
     *
     * return >= 1代表有火，< 1 代表没火
     */
    int[] cutYUV(byte[] data, int width, int height, int degree);
}
