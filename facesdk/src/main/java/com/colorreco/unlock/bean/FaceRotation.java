package com.colorreco.unlock.bean;

import com.colorreco.utils.Constants;

/**
 * 脸部旋转角度
 * Created by LiuHang on 12/2/2017.
 */
public enum FaceRotation
{
    /** 正脸*/
    FaceFront,
    /** 左侧*/
    FaceLeft,
    /** 左上侧*/
//    FaceLeftTop,
    /** 上侧*/
    FaceTop,
    /** 右上侧*/
//    FaceRightTop,
    /** 右侧*/
    FaceRight,
    /** 右下侧*/
//    FaceRightBottom,
    /** 下侧*/
    FaceBottom;
    /** 左下侧*/
//    FaceLeftBottom;

    /**
     * 获取脸部旋转角度（180度归一化）
     * @param yaw 仰俯角
     * @param pitch 左右旋转角度
     * @param roll 翻滚角
     * @return 脸部旋转角度
     */
    public static FaceRotation get(float yaw, float pitch, float roll)
    {
        if (Math.abs(yaw) < Constants.MAX_FRONT_FACE_DEGREE
                &&  Math.abs(pitch) < Constants.MAX_FRONT_FACE_DEGREE
                && Math.abs(roll) < Constants.MAX_FRONT_FACE_DEGREE)
        {
            return FaceRotation.FaceFront;
        }

//        if (yaw > 0
//                && pitch > 0
//                && roll > MAX_FRONT_FACE_DEGREE)
//        {
//            return FaceRotation.FaceRightBottom;
//        }
//
//        if (yaw > 0
//                && pitch < 0
//                && roll < -MAX_FRONT_FACE_DEGREE)
//        {
//            return FaceRotation.FaceLeftBottom;
//        }
//
//        if (yaw < 0
//                && pitch < -MAX_FRONT_FACE_DEGREE
//                && roll > MAX_FRONT_FACE_DEGREE)
//        {
//            return FaceRotation.FaceRightTop;
//        }
//
//        if (yaw < 0 && pitch > MAX_FRONT_FACE_DEGREE
//                && roll < -MAX_FRONT_FACE_DEGREE)
//        {
//            return FaceRotation.FaceLeftTop;
//        }

        if (yaw < -Constants.MAX_FRONT_FACE_DEGREE && yaw > -Constants.MAX_OTHER_FACE_DEGREE)
        {
            return FaceRotation.FaceTop;
        }

        if (yaw > Constants.MAX_FRONT_FACE_DEGREE)
        {
            return FaceRotation.FaceBottom;
        }

        if (pitch < -Constants.MAX_FRONT_FACE_DEGREE)
        {
            return FaceRotation.FaceLeft;
        }

        if (pitch > Constants.MAX_FRONT_FACE_DEGREE)
        {
            return FaceRotation.FaceRight;
        }

        return FaceRotation.FaceFront;
    }
}
