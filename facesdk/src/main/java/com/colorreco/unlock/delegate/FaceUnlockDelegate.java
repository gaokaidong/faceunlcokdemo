package com.colorreco.unlock.delegate;

        import com.colorreco.unlock.bean.FaceUnlockInfo;
        import com.colorreco.unlock.bean.Person;

        import java.util.ArrayList;

/**
 * 人脸解锁委托
 */


public interface FaceUnlockDelegate extends FaceDetectionBaseDelegate
{
    /**
     * 人脸解锁成功
     *
     * @param persons 人员的信息
     * @param faceUnlockInfo 人脸解锁的信息
     */
    void onFaceUnlockSuccess(ArrayList<Person> persons, FaceUnlockInfo faceUnlockInfo);

    /**
     * 人脸解锁失败
     *
     * @param faceUnlockInfo 人脸解锁的信息
     */
    void onFaceUnlockFailed(FaceUnlockInfo faceUnlockInfo);
}
