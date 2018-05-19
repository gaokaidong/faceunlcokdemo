package com.colorreco.unlock.bean;

/**
 * 人脸检测状态
 */


public enum FaceDetectionState
{
    // 人脸摆动角度过大
    FACE_OVER_SWING,

    // 检测到人脸
    FACE_DETECTED,

    // 未检测到人脸
    FACE_UNDETECED,

    // 人脸方向不对
    FACE_WRONG_ROTATION,

    // 人脸采集成功
    FACE_DETECTION_SUCCESS;
}
