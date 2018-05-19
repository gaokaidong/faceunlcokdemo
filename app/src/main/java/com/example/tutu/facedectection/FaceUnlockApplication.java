package com.example.tutu.facedectection;

import android.app.Application;
import android.content.Context;

import org.lasque.tusdk.core.TuSdk;

/**
 * 人脸解锁Application
 */


public class FaceUnlockApplication extends Application
{
    public static Context mContext;

    public FaceUnlockApplication()
    {
        this.mContext = this;
    }

    public static Context getContext()
    {
        return mContext;
    }

    @Override
    public void onCreate()
    {
        super.onCreate();

        TuSdk.enableDebugLog(true);
        TuSdk.setResourcePackageClazz(com.example.tutu.facedectection.R.class);
        /**
         *  初始化SDK，应用密钥是您的应用在 TuSDK 的唯一标识符。每个应用的包名(Bundle Identifier)、密钥、资源包(滤镜、贴纸等)三者需要匹配，否则将会报错。
         *
         *  @param appkey 应用秘钥 (请前往 http://tusdk.com 申请秘钥)
         */
        TuSdk.init(this.getApplicationContext(), "d13a7d5c451768f4-04-ewdjn1");
    }
}
