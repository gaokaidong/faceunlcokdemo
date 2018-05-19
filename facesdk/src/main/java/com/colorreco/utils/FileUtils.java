package com.colorreco.utils;

import org.lasque.tusdk.core.TuSdkContext;
import org.lasque.tusdk.core.utils.StringHelper;
import org.lasque.tusdk.core.utils.image.AlbumHelper;

import java.io.File;
import java.util.UUID;

/**
 * 文件工具类
 */


public class FileUtils
{
    /**
     * 获取缓存目录
     *
     * @param fileName
     * @return
     */
    public static File getTempPath(String fileName)
    {
        String tempPath = "TuSDK/temp/" + fileName + "/";
        File path = TuSdkContext.getAppCacheDir(tempPath, false);

        return path;
    }

    /**
     * 获取缓存文件名
     *
     * @return
     */
    public static String getTempFileName(String liveValue)
    {
        String timeStamp = StringHelper.timeStampString();
        String uuId = UUID.randomUUID().toString();
        return liveValue + "_" + timeStamp + "_" + uuId+".dat";
    }

    /**
     * 获取缓存文件
     *
     * @param liveValue
     * @param fileName
     * @return
     */
    public static File getTempFile(String liveValue, String fileName)
    {
        File path = getTempPath(fileName);
        if (path == null) return null;

        path = new File(path.getPath(), getTempFileName(liveValue));
        return path;
    }

    /**
     * 获取系统相册文件名
     *
     * @return
     */
    public static String getAlbumFileName(String liveValue)
    {
        String timeStamp = StringHelper.timeStampString();
        return "LSQ_" + "isLive_" + liveValue+ "_" + timeStamp + ".jpg";
    }

    /**
     * 获取系统相册文件包装
     *
     * @param floderName
     * @return
     */
    public static File getAlbumFile(String floderName, String liveValue)
    {
        File ablumPath = AlbumHelper.getAblumPath(floderName);
        if (ablumPath == null) return null;
        File mediaFile = new File(ablumPath.getPath() + File.separator
                + getAlbumFileName(liveValue));
        return mediaFile;
    }
}
