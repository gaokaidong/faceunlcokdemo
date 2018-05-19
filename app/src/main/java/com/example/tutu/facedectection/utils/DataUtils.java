package com.example.tutu.facedectection.utils;


import com.colorreco.unlock.bean.FaceRotation;
import com.colorreco.unlock.bean.Person;

import org.lasque.tusdk.core.TuSdkContext;
import org.lasque.tusdk.core.utils.FileHelper;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by LiuHang on 11/27/2017.
 */

/**
 * 实现数据存储、读取等操作
 */
public class DataUtils
{
    // 标记是否成功保存人脸特征
    public static boolean isSavedFaceFeature = false;

    /**
     * 密码保存在SharedPreferences
     *
     * @param password
     */
    public static boolean savePassword(String password)
    {
        if (password == null) return false;
        TuSdkContext.sharedPreferences().saveSharedCache(Constants.SHARE_PASSWORD_KEY, password);
        return true;
    }

    /**
     * 读取保存在本地的密码
     *
     * @return
     */
    public static String getPassword()
    {
        return TuSdkContext.sharedPreferences().loadSharedCache(Constants.SHARE_PASSWORD_KEY);
    }


    /**
     * 保存人脸特征值
     *
     * @param persons
     * @return
     */
    public static boolean savePersons(ArrayList<Person> persons)
    {
        if (persons == null) return false;

        TuSdkContext.sharedPreferences().saveSharedCacheObject(Constants.SHARE_FACE_FEATURE_KEY, persons);
        return true;
    }

    /**
     * 读取人脸特征值
     *
     * @return
     */
    public static ArrayList<Person> getPersons()
    {
        return TuSdkContext.sharedPreferences().loadSharedCacheObject(Constants.SHARE_FACE_FEATURE_KEY);
    }

    public static void savePerson(Person person)
    {
        ArrayList<Person> cache = getPersons();
        if (cache != null)
        {
            for (Person oPerson : cache)
            {
                clearOldPerson(oPerson);
            }
        }
        else
        {
            cache = new ArrayList<Person>();
        }

        cache.clear();
        cache.add(person);
        savePersons(cache);
    }

    public static void clearOldPerson(Person oPerson)
    {
        if (oPerson == null) return;

        File tempPath = getTempPath(oPerson.personId);
        if (tempPath.exists())
        {
            FileHelper.deleteSubs(tempPath);
        }
    }


    public static File getTempPath(String personId)
    {
        String tempPath = "TuSDK/temp/" + personId + "/";
        File path = TuSdkContext.getAppCacheDir(tempPath, false);

        return path;
    }

    public static File getTempFile(FaceRotation faceRotation, String personId)
    {
        File path = DataUtils.getTempPath(personId);
        path = new File(path.getPath(), faceRotation+".dat");
        return path;
    }

    /**
     * 将float[]转成String
     *
     * @param floatValue
     * @return
     */
    public static String floatToString(float [] floatValue)
    {
        if (floatValue == null || floatValue.length == 0) return null;

        StringBuilder builder = new StringBuilder();

        for (float value : floatValue)
        {
            builder.append(value+",");
        }

        return builder.toString();
    }

    /**
     * String转成float[]
     *
     * @param string
     * @return
     */
    public static float[] stringToFloat(String string)
    {
        if (string == null) return null;

        String[] splitStr = string.split(",");
        float[] floatValue = new float[splitStr.length];
        int index = 0;
        for (String str : splitStr)
        {
            floatValue[index++] = Float.parseFloat(str);
        }
        return floatValue;
    }

    /**
     * 复制float[]到另一个对象
     *
     * @param sourceFloat
     * @param denstiFloat
     */
    public static void copyFloat(float[] sourceFloat, float[] denstiFloat)
    {
        for (int i = 0; i < sourceFloat.length; i++)
        {
            denstiFloat[i] = sourceFloat[i];
        }
    }

    /**
     * 打印Int[]
     *
     * @param intValue
     * @return
     */
    public static String printInt(int [] intValue)
    {
        StringBuilder builder = new StringBuilder();
        builder.append("{");
        for (int value : intValue)
        {
            builder.append(value+",");
        }
        builder.append("}");

        return builder.toString();
    }

    /**
     * 打印float[]
     *
     * @param floatValue
     * @return
     */
    public static String printFloat(float[] floatValue)
    {
        if (floatValue == null) return  null;
        StringBuilder builder = new StringBuilder();
        builder.append("{");
        for (float value : floatValue)
        {
            builder.append(value+",");
        }
        builder.append("}");

        return builder.toString();
    }

    public static byte[] copyBytes(byte[] data)
    {
        if (data == null) return null;
        byte[] copyData = new byte[data.length];


        for (int i = 0; i < data.length; i++)
        {
            copyData[i] = data[i];
        }

        return copyData;
    }

    /**
     * 获取String
     *
     * @param resId
     * @return
     */
    public static String getString(int resId)
    {
        String title = TuSdkContext.getString(resId);

        return title;
    }
}
