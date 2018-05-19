package com.colorreco.libface;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.colorreco.libface.ActivatorCallback;
import com.colorreco.libface.CRFace;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Activator extends AsyncTask<Boolean,Integer,Integer>{
    private static Activator mIns = null;

    private ActivatorCallback mCb;
    private String appKey;
    private String modelPath;
    private Context mCtx;
    private String mMac;
    private boolean mIslaunch;
    private boolean mIsIMEI;
    public Activator(Context ctx,String appkey,String model,ActivatorCallback cb,String mac){
        mCtx = ctx;
        appKey = appkey;
        modelPath = model;
        mCb = cb;
        mMac = mac;
    }
    /**
     * 创建Activator单例。
     * @param ctx Activity上下文。
     * @param appkey 用户appkey。
     * @param model 模型文件在硬盘上 的路径。比如："/sdcard/colorreco"，不要当上最后一个反斜杠'/'。
     * @param cb 激活状态回调接口，由用户实现。
     * @param mac 预留的字段。
     * @param isLaunch 激活后是否初始化SDK。True：尝试激活并初始化SDK。False：仅仅激活而不初始化SDK。
     * @param isIMEI 是否采用IMEI值作为唯一码生成依据。
     * @return 创建的Activator单例。
     */
    public static Activator CreateActivator(Context ctx,String appkey,String model,ActivatorCallback cb,String mac,boolean isLaunch,boolean isIMEI){
        if(mIns == null){
            mIns = new Activator(ctx,appkey,model,cb,mac);
            mIns.mIslaunch = isLaunch;
            mIns.mIsIMEI = isIMEI;
        }
        return mIns;
    }
    /**
     * 发起SDK的激活线程。
     */
    public static void initSDK(){
        if( mIns != null){
            mIns.execute(mIns.mIslaunch);
        }
    }
    @Override
    protected Integer doInBackground(Boolean... arg0) {
        int check = 1;
        if(mIsIMEI){
            check = localIMEI(mCtx);
        }
        if(check != 1){
            return -5001;
        }
        check = CRFace.getInstance().localChecker(modelPath);
        if(check == 1){
            if(arg0[0]){
                return CRFace.getInstance().initSDK(modelPath);
            }else{
                return 1;
            }
        }else if(check != 0){
            return check;
        }
        String uid = CRFace.getInstance().getLocalMarker(mMac);
        if(uid.isEmpty()){
            return -2001; //无法获取本地唯一码。
        }
        String figerprinter = "cr_cmlanche_911";
        String md5ed = md5(figerprinter + appKey + uid);
        String url = "http://120.77.13.79/keyauth/exe";
        Map<String, String> p = new HashMap<>();
        p.put("appkey", appKey);
        p.put("auth", md5ed);
        p.put("uid", uid);
        p.put("packagename", mCtx.getPackageName());
        HttpPost httpost = new HttpPost(url);
        List<NameValuePair> nvps = new ArrayList<>();
        Set<String> keySet = p.keySet();
        for(String k : keySet) {
            nvps.add(new BasicNameValuePair(k, p.get(k)));
        }
        try {
            httpost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        HttpResponse response = null;
        DefaultHttpClient httpclient = new DefaultHttpClient();
        // 请求超时
        httpclient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 5000);
        // 读取超时
        httpclient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 5000);
        try {
            response = httpclient.execute(httpost);
            HttpEntity entity = response.getEntity();
            String body = EntityUtils.toString(entity);
            JSONObject obj = new JSONObject(body);
            int code = obj.getInt("Code");
            String msg = obj.getString("Message");
            String authkey =  obj.getString("AuthKey");
            String md5 = obj.getString("AuthData");
            String leftcount = obj.getString("LeftCount");
            Log.v("TAG", "msg from server:" + msg + "\tleftcount:" + leftcount);
            if(code == 1){
                int ac = CRFace.getInstance().activate(authkey, md5);
                if(ac == 1){
                    if(arg0[0]){
                        return CRFace.getInstance().initSDK(modelPath);
                    }else{
                        return 1;
                    }
                }else{
                    return -2002;//无法进行本地授权.
                }
            }else{
                return -3001;//服务端相应错误。
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return -4001;//网络错误.
    }

    @Override
    protected void onPostExecute(Integer state){
        mCb.notifyMsg(state);
    }

    private static String md5(String string) {
        byte[] hash;
        try {
            hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Huh, MD5 should be supported?", e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Huh, UTF-8 should be supported?", e);
        }

        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10) hex.append("0");
            hex.append(Integer.toHexString(b & 0xFF));
        }
        return hex.toString();
    }
    protected static native int localIMEI(Context ctx);

}
