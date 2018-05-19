package com.example.tutu.facedectection.utils;

import com.colorreco.unlock.FaceUnlockParameters;
import com.colorreco.unlock.bean.FaceUnlockInfo;

import java.util.Map;

/**
 * Created by LiuHang on 12/27/2017.
 */


public class UnlockInfoString
{
    private StringBuilder mStringBuilder = new StringBuilder();;

    /** 人脸解锁的信息 */
    private FaceUnlockInfo mFaceUnlockInfo;

    public void setInfo(FaceUnlockInfo faceUnlockInfo)
    {
        this.mFaceUnlockInfo = faceUnlockInfo;
    }

    public String toString()
    {
        if (mFaceUnlockInfo == null) return "";

        Map<FaceUnlockParameters, Object> unlockInfo = mFaceUnlockInfo.getUnlockInfo();

        for (Map.Entry<FaceUnlockParameters, Object> entry : unlockInfo.entrySet())
        {
            if (!entry.getKey().isPrinted) continue;

            FaceUnlockParameters parameters = entry.getKey();
            String flag = "";
            if (parameters.isTestFiled) flag = "//";

            mStringBuilder.append(parameters.field + ": " + entry.getValue() + parameters.unit + flag + "\n");
        }

        return mStringBuilder.toString();
    }
}
