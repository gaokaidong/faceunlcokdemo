package com.colorreco.unlock.delegate;

import com.colorreco.unlock.bean.FaceInitialState;

/**
 * 人脸检测委托基类
 */


public interface FaceDetectionBaseDelegate
{
    /**
     * 初始化状态
     */
    void initState(FaceInitialState state);
}
