package com.colorreco.unlock.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by LiuHang on 12/2/2017.
 */


public class Person implements Serializable
{
    private static final long serialVersionUID = -9206438880237121198L;

    /** 特征值集合 */
    public ArrayList<FaceFeature> featureList;

    /** 上次检测的特征集合 */
    public ArrayList<FaceFeature> lastFeatureList;

    /** 人员ID */
    public String personId;

    /** 登陆密码 */
    public String password;

    /** 存储的Key值 */
    public String username;

    public Person()
    {

    }

    public Person(String uuid)
    {
        this.personId = uuid;
    }

    /** 获取所有人脸特征*/
    public List<FaceFeature> getAllFeatures()
    {
        ArrayList<FaceFeature> list = new ArrayList<FaceFeature>();
        if (featureList != null){
            list.addAll(featureList);
        }

        if (lastFeatureList != null){
            list.addAll(lastFeatureList);
        }

        return list;
    }

    /** 获取采集特征总数 */
    public int getBaseFeatureSize(){
        if (featureList == null){
            featureList = new ArrayList<FaceFeature>();
        }
        return featureList.size();
    }

    /** 添加采集特征 */
    public void addBaseFeature(FaceRotation rotation, float[] features)
    {
        if (rotation == null || features == null) return;

        FaceFeature feature = new FaceFeature(features, rotation);

        if (featureList == null){
            featureList = new ArrayList<FaceFeature>();
        }

        featureList.add(feature);
    }

    public ArrayList<FaceFeature> getBaseFeatureList()
    {
        if (featureList == null){
            featureList = new ArrayList<FaceFeature>();
        }

        return featureList;
    }

    public void setBaseFeatureList(ArrayList<FaceFeature> featureList)
    {
        this.featureList = featureList;
    }
}
