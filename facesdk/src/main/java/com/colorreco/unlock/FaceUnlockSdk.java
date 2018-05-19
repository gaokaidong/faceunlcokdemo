package com.colorreco.unlock;

import android.graphics.Bitmap;

import com.colorreco.unlock.base.FaceDetectionBase;
import com.colorreco.unlock.bean.ExtractFaceInfo;
import com.colorreco.unlock.bean.ExtractFeatureState;
import com.colorreco.unlock.bean.FaceDetectInfo;
import com.colorreco.unlock.bean.FaceDetectionState;
import com.colorreco.unlock.bean.FaceFeature;
import com.colorreco.unlock.bean.FaceInitialState;
import com.colorreco.unlock.bean.FaceRotation;
import com.colorreco.unlock.bean.FaceUnlockInfo;
import com.colorreco.unlock.bean.Person;
import com.colorreco.unlock.bean.PictureOrientation;
import com.colorreco.unlock.delegate.FaceCollectionDelegate;
import com.colorreco.unlock.delegate.FaceUnlockDelegate;
import com.colorreco.unlock.option.FaceDetectionOption;
import com.colorreco.utils.BitmapUtils;
import com.colorreco.utils.Constants;
import com.colorreco.utils.FileUtils;

import org.lasque.tusdk.core.struct.TuSdkSize;
import org.lasque.tusdk.core.utils.FileHelper;
import org.lasque.tusdk.core.utils.TLog;
import org.lasque.tusdk.core.utils.ThreadHelper;
import org.lasque.tusdk.core.utils.image.BitmapHelper;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

/**
 * 人脸解锁包装类
 */


public class FaceUnlockSdk extends FaceDetectionBase
{
    /**
     * sdk版本号
     */
    public static final String SDK_VERSION = "1.0.0";

    /**
     * sdk版本代号
     */
    public static final int SDK_CODE = 1;

    /** 图片文件夹名称 */
    private static final String IMG_DERECTORY_NAME = "lh";

    /** 各个角度人脸Bitmap */
    private List<Bitmap> mFaceBitmapList = new ArrayList<>();

    /** 检测人脸的次数 */
    private int mDetectFaceNum;

    /** 人脸信息采集委托 */
    private FaceCollectionDelegate mFaceCollectionDelegate;

    /** 人脸解锁委托 */
    private FaceUnlockDelegate mFaceUnlockDelegate;

    /**　解锁时匹配的人员　*/
    private Person mBestPerson;

    /** 人脸检测配置项 */
    private FaceDetectionOption mFaceDetectionOption;

    /** 保存每次检测的人脸特征 */
    private ArrayList<FaceFeature> mFaceFeatureList = new ArrayList<>();
    
    /** 统计解锁过程的时间信息 */
    private FaceUnlockParamStatistics mParamStatis;

    /**
     *
     * @param delegate the delegate to set
     */
    public void setFaceColletionDelegate(FaceCollectionDelegate delegate)
    {
        this.mFaceCollectionDelegate = delegate;
    }

    /**
     *
     * @return the mFaceCollectionDelegate
     */
    public FaceCollectionDelegate getFaceColletionDelegate()
    {
        return mFaceCollectionDelegate;
    }

    /**
     *
     * @param delegate the delegate to set
     */
    public void setFaceUnlockDelegate(FaceUnlockDelegate delegate)
    {
        this.mFaceUnlockDelegate = delegate;
    }

    /**
     *
     * @return the mFaceUnlockDelegate
     */
    public FaceUnlockDelegate getFaceUnlockDelegate()
    {
        return mFaceUnlockDelegate;
    }

    /** 人脸检测配置项*/
    public void setComponentOption(FaceDetectionOption faceDetectionOption)
    {
        this.mFaceDetectionOption = faceDetectionOption;
    }

    @Override
    public void init()
    {
        super.init();

        TLog.i("Face Sdk initState: "+mInitialState);

        // 上传图片到服务器
        //UpLoadImage.handleImageUploading();

        if (mFaceCollectionDelegate == null) return;

        mFaceCollectionDelegate.initState(mInitialState);
    }

    /**
     * 检测是否有人脸
     *
     * @param data
     * @return
     */
    protected FaceDetectInfo detectFace(byte[] data)
    {
        if (mFaceDetectionOption == null || data == null || mInitialState == FaceInitialState.IntialFailed) return null;

        TuSdkSize pitureSize = mFaceDetectionOption.getPitureSize();
        if (pitureSize == null) pitureSize = new TuSdkSize();

        int degree = mFaceDetectionOption.getPictureOrientation().getDegree();

        // 存储人脸检测位置信息
        float[] facePos = new float[256];

        // 预览尺寸最大支持720P
        int [] faceInfo = detect(data, pitureSize.width, pitureSize.height, 1, facePos, degree);

        FaceDetectInfo faceDetectInfo = new FaceDetectInfo(faceInfo, facePos);
        if (!faceDetectInfo.hasFace()) return null;

        return faceDetectInfo;
    }

    /**
     *  抽取人脸信息特征值
     */
    protected ExtractFaceInfo extractFaceFeature(byte[] data, int[] faceInfo, FaceRotation rotation)
    {
        if (mFaceDetectionOption == null || data == null || faceInfo == null
                || rotation == null || mInitialState == FaceInitialState.IntialFailed) return null;

        // 抽取2-5位数据作为人脸坐标
        int index = 0;
        for (int i =0; i< faceInfo.length; i++)
        {
            if (i == 0 || index >= 4) continue;

            faceInfo[index++] = faceInfo[i];
        }

        TuSdkSize pitureSize = mFaceDetectionOption.getPitureSize();
        int degree = mFaceDetectionOption.getPictureOrientation().getDegree();

        float[] feature =  new float[256];
        int extractState = extractYUV(data, pitureSize.width, pitureSize.height, faceInfo, feature, degree);

        ExtractFeatureState state = ExtractFeatureState.getExtractFeatureState(extractState);
        if (state != ExtractFeatureState.EXTRACT_SUCCESS) return null;

        FaceFeature faceFeature = new FaceFeature(feature, rotation);
        ExtractFaceInfo extractFaceInfo = new ExtractFaceInfo(state, faceFeature);
        return extractFaceInfo;
    }

    /**
     * 检测指定角度的人脸
     *
     * @param data
     * @param rotation
     */
    public ExtractFaceInfo detectSpecifiedFace(byte[] data, FaceRotation rotation)
    {
        if (data == null || mFaceDetectionOption == null || rotation == null) return null;

        // 判断是否是需要的人脸角度信息
        FaceDetectInfo faceDetectInfo = detectFace(data);
        if (faceDetectInfo == null)
        {
            if (mFaceCollectionDelegate != null)
                mFaceCollectionDelegate.onFaceDetectionState(FaceDetectionState.FACE_UNDETECED);
            return null;
        }

        float[] facePos = faceDetectInfo.mFaceRotationInfo;

        FaceRotation faceRotation = getFaceRotation(facePos);
        if (faceRotation == null || faceRotation != rotation)
        {
            if (mFaceCollectionDelegate != null)
                mFaceCollectionDelegate.onFaceDetectionState(FaceDetectionState.FACE_WRONG_ROTATION);
            return null;
        }

        if (mFaceCollectionDelegate != null)
            mFaceCollectionDelegate.onFaceDetectionState(FaceDetectionState.FACE_DETECTED);

        // 跳帧处理，等数据稳定后再抽取特征值
        if (mDetectFaceNum < mFaceDetectionOption.getJumpFrames())
        {
            mDetectFaceNum++;
            return null;
        }

        ExtractFaceInfo extractFaceInfo = extractFaceFeature(data, faceDetectInfo.mFaceInfo, rotation);
        if (extractFaceInfo.mExtractState != ExtractFeatureState.EXTRACT_SUCCESS) return null;

        if (mFaceCollectionDelegate != null)
            mFaceCollectionDelegate.onFaceDetectionState(FaceDetectionState.FACE_DETECTION_SUCCESS);

        mDetectFaceNum = 0;
        return extractFaceInfo;
    }

    /**
     * 检测各角度的人脸
     *
     * @param data
     */
    public void detectFaceList(byte[] data)
    {
        if (mFaceDetectionOption == null || mFaceFeatureList == null) return;

        List<FaceRotation> rotationList = mFaceDetectionOption.getRotationList();

        if (rotationList.size() < 1|| mFaceFeatureList.size() == rotationList.size()) return;

        // 按照人脸角度集合顺序检测
        int index = mFaceFeatureList.size();
        ExtractFaceInfo extractInfo = detectSpecifiedFace(data, rotationList.get(index));
        if (extractInfo == null) return;

        // 以采集的第一个角度特征值为基准（建议第一个为正脸），
        // 采集的其余角度特征值与第一个角度特征值小于mFrontFaceMatchValue,
        // 则认为不属于同一个人，该角度信息忽略不计。
        if (mFaceDetectionOption.isEnableSamePersonDetect() && mFaceFeatureList.size() > 0)
        {
           float matchValue =  match(mFaceFeatureList.get(0).mFeatures, extractInfo.mFaceFeature.mFeatures);
           if (matchValue <= mFaceDetectionOption.getFrontFaceMatchValue()) return;
        }

        FaceFeature currentFeature = new FaceFeature(extractInfo.mFaceFeature.mFeatures, extractInfo.mFaceFeature.mRotation);
        mFaceFeatureList.add(currentFeature);

        // 保存图片
        Bitmap bitmap = null;
        if (mFaceDetectionOption.isIsSaveBitmap())
        {
            TuSdkSize pitureSize = mFaceDetectionOption.getPitureSize();
            PictureOrientation pictureOrientation = mFaceDetectionOption.getPictureOrientation();

            bitmap = BitmapUtils.convertYUVToBitmap(data, pitureSize.width, pitureSize.height, pictureOrientation);
            mFaceBitmapList.add(bitmap);
        }
        if (mFaceCollectionDelegate == null) return;
        // 抽取特征值结果回调
        mFaceCollectionDelegate.onExtractFeatureSuccess(extractInfo.mFaceFeature, bitmap);

        if (mFaceFeatureList.size()== mFaceDetectionOption.getRotationList().size())
        {
            Person person = new Person();
            person.personId = mFaceDetectionOption.getPersonId();
            person.setBaseFeatureList(mFaceFeatureList);
            mFaceCollectionDelegate.onExtractFeatureListCompeleted(person, mFaceBitmapList);
        }
    }

    /**
     * 获取脸部角度
     *
     * @return
     */
    private FaceRotation getFaceRotation(float[] facePos)
    {
        if (facePos == null) return  null;

        TLog.i("yaw = " + facePos[0] + ",pitch = " + facePos[1] + ",roll = " + facePos[1]);
        return FaceRotation.get(facePos[0], facePos[1], facePos[2]);
    }

    /**
     * 人脸解锁
     *
     * @param data
     */
    public void faceUnlock(byte[] data, ArrayList<Person> persons)
    {
        if(mFaceDetectionOption == null) return;

        // 检测
        if (mParamStatis == null)
        {
            mParamStatis = new FaceUnlockParamStatistics();
            mParamStatis.setDelagte(mParamStatisticsDelegate);
            mParamStatis.setUnlockTimeLimit(mFaceDetectionOption.getUnlockTimeLimit());
            mParamStatis.start();
        }

        mParamStatis.singleStatiStart(FaceUnlockParameters.DETECT_TIME);
        FaceDetectInfo faceDetectInfo = detectFace(data);
        mParamStatis.singleStatiEnd();
        mParamStatis.addDetectTime();
        mParamStatis.addFaceDetectNum();

        if (faceDetectInfo == null)
        {
            mParamStatis.addInvalidFaceNum();
            return;
        }

        // 过滤偏大或偏小的人脸
        TuSdkSize personSize = faceDetectInfo.getFaceSize();
        TuSdkSize imageSize = new TuSdkSize(mFaceDetectionOption.getPitureSize().width, mFaceDetectionOption.getPitureSize().height);
        if (FaceUnlockFilterRule.filterFaceSize(personSize, imageSize))
        {
            mParamStatis.addInvalideFaceSizeNum();
            return;
        }

        // 抽取特征值
        FaceRotation faceRotation = getFaceRotation(faceDetectInfo.mFaceRotationInfo);
        mParamStatis.singleStatiStart(FaceUnlockParameters.EXTRACT_FEATURE_TIME);
        ExtractFaceInfo extractFaceInfo = extractFaceFeature(data,faceDetectInfo.mFaceInfo, faceRotation);
        mParamStatis.singleStatiEnd();

        // 过滤无效的特征值
        if (FaceUnlockFilterRule.filterInvalideFaceFeature(extractFaceInfo))
        {
            mParamStatis.addExtractFailedNum();
            return;
        }


        // 活体检测
        float isLive = 0.0f;
        if (mFaceDetectionOption.isEnableLiveDetection() )
        {
            mParamStatis.singleStatiStart(FaceUnlockParameters.HACKER_TIME);
            isLive = getLive();
            TLog.i("isLive : " + isLive);
            mParamStatis.singleStatiEnd();
            mParamStatis.addLiveDetectNum();
        }

        // 过滤假体
        if (FaceUnlockFilterRule.filterUnLive(isLive, mFaceDetectionOption.getLiveThresholdValue())) return;

        // 比对
        mParamStatis.singleStatiStart(FaceUnlockParameters.MATCH_TIME);
        float matchValue = matchPerson(extractFaceInfo.mFaceFeature.mFeatures, extractFaceInfo.mFaceFeature.mRotation, persons);
        mParamStatis.singleStatiEnd();
        mParamStatis.saveMatchValue(matchValue);

        // 过滤不匹配的数值
        if (FaceUnlockFilterRule.filterUnMatchedValue(matchValue, mFaceDetectionOption.getThresholdValue())) return;

        if (mBestPerson != null)
            mParamStatis.saveUserName(mBestPerson.username);

        // 添加!mParamStatis.isFinished()判, 避免倒计时结束逻辑和检测成功逻辑同时发生
        if (mFaceUnlockDelegate != null && !mParamStatis.isFinished())
        {
            mParamStatis.saveSuccessUnlockTime();
            mParamStatis.setUnlockState(true);
            mParamStatis.finish();

            Bitmap bitmap = BitmapUtils.convertYUVToBitmap(data, mFaceDetectionOption.getPitureSize().width,
                    mFaceDetectionOption.getPitureSize().height, mFaceDetectionOption.getPictureOrientation());

            File tempFile = FileUtils.getTempFile(String.valueOf(isLive), UpLoadImage.UPLOAD_IMG_DERECTORY_NAME);
            BitmapHelper.saveBitmap(tempFile, bitmap, 100);

            // 判断是否需要保存图片
            if (!mFaceDetectionOption.getEnableSaveBitmapAfterUnlocked()) return;

            BitmapUtils.savePhotoToAlbum(bitmap, IMG_DERECTORY_NAME, String.valueOf(isLive));
        }
    }

    /**
     * 统计的委托事件
     */
    private FaceUnlockParamStatistics.ParamStatisticsDelegate mParamStatisticsDelegate = new FaceUnlockParamStatistics.ParamStatisticsDelegate()
    {

        @Override
        public void onFinish(ArrayList<Person> persons, FaceUnlockInfo faceUnlockInfo)
        {
            if (faceUnlockInfo.getUnlockState())
                mFaceUnlockDelegate.onFaceUnlockSuccess(persons, faceUnlockInfo);
            else
                mFaceUnlockDelegate.onFaceUnlockFailed(faceUnlockInfo);
        }
    };

    /**
     * 匹配人脸特征
     * @param orginFeature 比对特征
     * @param persons 所有人脸特征
     * @return 相识度
     */
    private float matchPerson(float[] orginFeature, FaceRotation rotation, ArrayList<Person> persons)
    {
        if (mFaceDetectionOption == null || orginFeature == null || persons == null) return 0f;

        float match = 0f;
        // 匹配到的最相识对象
        Person bestPerson = null;

        for (Person person : persons)
        {
            List<FaceFeature> faceFeatures = person.getAllFeatures();
            for (FaceFeature faceFeature : faceFeatures)
            {
                if (faceFeature.mFeatures == null) continue;

                float nMatch= match(orginFeature, faceFeature.mFeatures);
                match = Math.max(match, nMatch);

                if (nMatch < mFaceDetectionOption.getThresholdValue() || nMatch != match) continue;

                bestPerson = person;
            }
        }

        mBestPerson = bestPerson;

        if (bestPerson != null && match >= mFaceDetectionOption.getSaveFeatureThresholdValue())
        {
            addVaildFeature(bestPerson, orginFeature, rotation);
        }

        return match;
    }

    /** 添加已验证特征*/
    private void addVaildFeature(Person person, float[] orginFeature, FaceRotation rotation)
    {
        if (person == null || orginFeature == null || rotation == null) return;

        if (person.lastFeatureList == null){
            person.lastFeatureList = new ArrayList<FaceFeature>();
        }

        if (person.lastFeatureList.size() >= Constants.MAX_FACE_VAILD_CACHE)
        {
            person.lastFeatureList.remove(0);
        }

        FaceFeature feature = new FaceFeature(orginFeature, rotation);
        person.lastFeatureList.add(feature);
    }

    /**
     * 释放资源
     */
    public void destroy()
    {
        if (mParamStatis != null)
        {
            mParamStatis.destroy();
        }
    }

    /**
     * 上传图片类
     */
    public static class UpLoadImage
    {
        /** 上传图片的URL */
        private static final String UPLOAD_URL = "https://srv.tusdk.com/srv/image/upload";

        /** 需上传的图片文件夹名称 */
        private static final String UPLOAD_IMG_DERECTORY_NAME = "cacheBg";

        /** 上传图片成功Code */
        private static final int SUCCESS_CODE = 200;

        /** 单次上传图片数量 */
        private static final int IMAGE_LIMIT = 10;

        /**
         * 上传图片
         */
        public static void handleImageUploading()
        {
            File imgFile = FileUtils.getTempPath(UPLOAD_IMG_DERECTORY_NAME);
            final File[] files = imgFile.listFiles();

            ThreadHelper.runThread(new Runnable()
            {
                @Override
                public void run()
                {
                    int count = 0;
                    for (File file : files)
                    {
                        if (count >= IMAGE_LIMIT) break;

                        //uploadImg(file, UPLOAD_URL); gaokaidong  将图片上传到网上，不然解锁失败
                        count++;
                    }

                }
            });
        }

        /**
         * 上传图片到服务器
         *
         * @param file
         * @param requestUrl
         */
        private static void uploadImg(File file, String requestUrl)
        {
            String BOUNDARY = UUID.randomUUID().toString(); // 边界标识 随机生成
            String PREFIX = "--", LINE_END = "\r\n";
            String CONTENT_TYPE = "multipart/form-data"; // 内容类型

            try
            {
               // 初始化SSL配置
                KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
                // Create a TrustManager that trusts the CAs in our KeyStore
                String algorithm = TrustManagerFactory.getDefaultAlgorithm();
                TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(algorithm);
                trustManagerFactory.init(keystore);
                // Create an SSLContext that uses our TrustManager
                SSLContext sslContext = SSLContext.getInstance("TLS");
                sslContext.init(null, new TrustManager[]{new MyTrustManager()}, new SecureRandom());

                URL url = new URL(requestUrl);
                HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
                conn.setSSLSocketFactory(sslContext.getSocketFactory());
                conn.setHostnameVerifier(DO_NOT_VERIFY);

                conn.setReadTimeout(10 * 1000);
                conn.setConnectTimeout(5 * 1000);
                conn.setDoInput(true); // 允许输入流
                conn.setDoOutput(true); // 允许输出流
//                conn.setUseCaches(false); // 不允许使用缓存
                conn.setRequestMethod("POST"); // 请求方式
                conn.setRequestProperty("Charset", "utf-8"); // 设置编码
                conn.setRequestProperty("Content-Type", CONTENT_TYPE + ";boundary=" + BOUNDARY);
                conn.connect();

                DataOutputStream dos = new DataOutputStream(conn.getOutputStream());

                StringBuffer sb = new StringBuffer();
                sb.append(PREFIX);
                sb.append(BOUNDARY);
                sb.append(LINE_END);
                sb.append("Content-Disposition: fileData;  name=\"pic\"; filename=\""
                        +  file.getName() + "\"" + LINE_END);
                sb.append("Content-Type: application/octet-stream; charset="
                        + "utf-8" + LINE_END);
                sb.append(LINE_END);
                dos.write(sb.toString().getBytes());

                InputStream is = new FileInputStream(file);
                byte[] bytes = new byte[1024];
                int len = 0;
                int curLen = 0;
                while ((len = is.read(bytes)) != -1)
                {
                    curLen += len;
                    dos.write(bytes, 0, len);
                }

                is.close();

                dos.write(LINE_END.getBytes());
                byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINE_END).getBytes();
                dos.write(end_data);
                dos.flush();

                int responseCode = conn.getResponseCode();

                if (responseCode == SUCCESS_CODE)
                {
                    FileHelper.delete(file);
                }
            }
            catch (ProtocolException e)
            {
                e.printStackTrace();
            }
            catch (MalformedURLException e)
            {
                e.printStackTrace();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            catch (KeyStoreException e)
            {
                e.printStackTrace();
            }
            catch (NoSuchAlgorithmException e)
            {
                e.printStackTrace();
            }
            catch (KeyManagementException e)
            {
                e.printStackTrace();
            }
        }

        static HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier()
        {
            // 信任所有主机
            @Override
            public boolean verify(String hostname, SSLSession session)
            {
                return true;
            }
        };

        private static class MyTrustManager implements X509TrustManager
        {

            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType)
                    throws CertificateException
            {
            }

            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType)
                    throws CertificateException
            {
            }

            @Override
            public X509Certificate[] getAcceptedIssuers()
            {
                return null;
            }
        }
    }
}
