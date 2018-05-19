package com.colorreco.libface;

public class CRFace
{
//  static
//  {
//    System.loadLibrary("crface");
//  }

  static CRFace mInstance = null;
  private static boolean isInit = false;
  private static int init_state = -1;

  public static CRFace getInstance()
  {
    if (mInstance == null) {
      mInstance = new CRFace();
    }
    return mInstance;
  }
  /**
   * 初始化模型文件接口
   * @param path 模型文件路径
   * @return 初始化状态 1：正确；-1，-2，-3：时间授权错误。
   */
  public int initSDK(String path)
  {
    if (!isInit)
    {
      init_state = mInstance.nativeInit(path);
      isInit = init_state == 1;
    }
    return init_state;
  }
  /**
   * 加载SDK动态库文件。
   * @param name 动态库名字 "crface"
   */
  public void loadLibrarySys(String name){
    System.loadLibrary(name);
  }
  /**
   * 内部初始化识别模型接口。
   * @param paramString 模型文件路径。
   * @return 初始化状态。
   */

  private native int nativeInit(String paramString);
  /**
   * 视频流人脸检测接口。
   * @param yuv 视频帧数据。格式YUV420SP.
   * @param width 图片宽，单位像素，最大支持1280。
   * @param height 图片高，单位像素，最大支持720。
   * @param isMaxface 大于0表示当检测出多张人脸时处理最大人脸。
   * @param pose 人脸角度信息，内存需要提前申请。预留接口，目前可传null。
   * @param degree 人脸在图片里的朝向。Android平台，横屏：0，竖屏：270.
   * @return 返回该帧图片里检测到的人脸信息。数组第一位（下标0）表示检测到多少个人脸。如果没有检测到人脸则这个值等于0.如果开启了最大人脸参数，即使检测到多张人脸，这个值也是1。数组第二位起（下标为1）每5个数据表示一组人脸信息，分别为：人脸框左上角坐标X，左上角坐标Y，人脸框宽度，人脸框高度，人脸区域图片质量。
   *
   * 特别注意：如果是横屏检测，人脸框的所有坐标都是相对于原图定位的；如果是竖屏检测，人脸框的数据是相对于把原图逆时针旋转270角度以后的新图来定位的（SDK检测函数并没有修改原始图像的数据）。
   */
  public native int[] detectYUV(byte[] yuv, int width, int height, int isMaxface,float[]pose,int degree);
  /**
   * 静态图人脸检测函数。
   * @param argb 传入需要处理的图片，格式ARGB8888,内存排布：bgra。
   * @param width  图片宽，单位像素。
   * @param height 图片高，单位像素。
   * @param isMaxface 大于0表示当检测出多张人脸时处理最大人脸.
   * @param pose 人脸角度信息，内存需要提前申请。预留接口，目前可传null。
   * @return 返回该帧图片里检测到的人脸信息。数组第一位（下标0）表示检测到多少个人脸。如果没有检测到人脸则这个值等于0.如果开启了最大人脸参数，即使检测到多张人脸，这个值也是1。数组第二位起（下标为1）每5个数据表示一组人脸 信息。分别为：人脸左上角X坐标，左上角Y坐标，人脸框宽，人脸框高，人脸区域图片质量。
   */
  public native int[] detectARGB(byte[] argb,int width,int height,int isMaxface,float[]pose);
  /**
   * 视频流特征抽取函数。
   * @param yuv 视频帧数据。格式YUV420SP.
   * @param width 图片宽，单位像素，最大支持1280。
   * @param height 图片高，单位像素，最大支持720。
   * @param facebox 需要处理的人脸框。长度为4的数组，分别表示：左上角X坐标，左上角Y坐标，人脸框宽，人脸框高。如果输入视频帧数据是竖屏，人脸框坐标需要用detectYUV接口获取。
   * @param feature 长度为256的数组，用于存放抽取到的人脸特征。
   * @param degree 人脸在图片里的朝向。Android平台，横屏：0，竖屏：270.
   * @return 256：正确；-2：人脸框错误；-3：抽取特征失败；-4：图片过大；-5：授权问题
   */

  public native int extractYUV(byte[] yuv, int width, int height, int[] facebox, float[] feature,int degree);
  /**
   * 静态图特征抽取函数(需要人脸框)。
   * @param argb 传入需要处理的图片，格式ARGB8888,内存排布：bgra。
   * @param width 图片宽，单位像素。
   * @param height  图片高，单位像素。
   * @param facebox 需要处理的人脸框。长度为4的数组，分别表示：左上角X坐标，左上角Y坐标，人脸框宽，人脸框高。
   * @param feature 长度为256的数组，用于存放抽取到的人脸特征。
   * @return 256：正确；-2：人脸框错误；-3：收取特征失败；-5：授权问题
   */
  public native int extractARGB(byte[] argb, int width, int height, int[] facebox, float[] feature);
  /**
   * 静态图特征抽取函数(不需要人脸框)。
   * @param argb 传入需要处理的图片，格式ARGB888,内存排布：bgra。
   * @param width 图片宽，单位像素。
   * @param height  图片高，单位像素。
   * @param feature 长度为256的数组，用于存放抽取到的人脸特征。
   * @param isMaxface 大于0表示当检测出多张人脸时处理最大人脸.
   * @param type 选择检测方法，0：速度优先；1：速度精度平衡。
   * @return 256：正确；-2：人脸检测失败-3：抽取特征失败；-5：授权问题。
   */
  public native int onepassExtractARGB(byte[] argb, int width, int height, float[] feature, int isMaxface,int type);
  /**
   * 特征比对函数。
   * @param featureA 抽取到的人脸特征，长度为256的数组。
   * @param featureB 抽取到的人脸特征，长度为256的数组。
   * @return 相似度。-1.000--1.000.值越大表示相似度越高。
   */
  public native float match(float[] featureA, float[] featureB);
  /**
   * 性别分析接口。
   * @param feature 抽取到的人脸特征，长度为256的数组。
   * @return 1：男；0：女。
   */
  public native int getSex(float[] feature);
  public native float getLive();

  /**
   * 属性计算初始化接口。
   * @param target 需要初始化的目标属性字符串。字符串的每一位，‘0’表示不初始化该属性，‘1’表示初始化该属性。<br/>
   * 目前一共支持11个人脸属性的计算。分别是:<br/>
   * 1：性别；2：年龄；3：图片清晰度；4：表情；5：颜值；6：左眼状态；7：右眼状态；8：嘴巴状态；9：是否佩戴眼镜；10：种族；11：眼睛视角。<br/>
   * 这11个属性按照从1到11的序列，由右到左地构成需要初始化的目标字符串。例如：字符串“00000000000”（11位全0）表示不初始化所有属性；<br/>
   * 字符串“11111111111”（11位全1）表示初始化全部属性；字符串“11111111110”表示初始化除性别以外的所有人脸属性；<br/>
   * 字符串“10111111101”表示初始化除种族和年龄以外的所有人脸属性。注意：此参数长度固定为11.<br/>
   * @return 返回值由各个属性的初始化状态构成的字符串表示。例如：字符串“00000000000”（11位全0）表示所有人脸属性均初始化失败；<br/>
   * 字符串“11111111111”（11位全1）表示全部人脸属性初始化成功。字符串“11111111110”表示除性别以外的人脸属性均初始化成功。<br/>
   * 注意：此返回值字符串长度固定为11.
   */
  public native String initAttributes(String target);
  /**
   * 人脸属性获取接口。注意：计算出来的人脸属性存放在长度为32的float型数组里。具体如下：<br/>
   * 性别：范围 0（男），1（女），存放在数据下标 [0]的位置。<br/>
   * 年龄：范围 [0-100]，存放在下标 [1]的位置。<br/>
   * 清晰度：范围[0-100]，数值越大，越清晰，存放在数组下标 [2]的位置。<br/>
   * 表情：范围[0-100]，有7个值，值越大置信度越高，分别是 sadness,neutral,disgust,anger,surprise,fear,happiness，依次存放在数组下标 [3-9]的位置。<br/>
   * 颜值；范围[0-100]，有2个值，分别是男性魅力度，女性魅力度，依次存放在数组下标[10-11]的位置。<br/>
   * 左眼状态：范围[0-5]，有1个值，0：佩戴普通眼镜且睁眼；1：不戴眼镜且闭眼；2：眼睛被遮挡；3：不戴眼镜且睁眼；4：佩戴普通眼镜且闭眼；5：佩戴墨镜。放在数组下标[12]的位置。<br/>
   * 右眼状态：范围[0-5]，有1个值，0：佩戴普通眼镜且睁眼；1：不戴眼镜且闭眼；2：眼睛被遮挡；3：不戴眼镜且睁眼；4：佩戴普通眼镜且闭眼；5：佩戴墨镜。放在数组下标[13]的位置。<br/>
   * 嘴巴状态；范围[0-3]，有1个值，0：嘴部没有遮挡且闭上；1：嘴部被医用口罩或呼吸面罩遮挡；2：嘴部没有遮挡且张开；3：嘴部被其他物体遮挡。放在数组下标[14]的位置。<br/>
   * 眼镜状态； 范围[0-2]，有1个值，0： 不佩戴眼镜；1：佩戴墨镜；2：佩戴普通眼镜。放在数组下标[15]的位置。<br/>
   * 种族：范围[0-2],有1个值，0：亚洲人，1：白人；2：黑人。放在数组下标[16]的位置。<br/>
   * 人脸角度：范围[-PI,PI]，弧度制，有3个值，分别是Pitch，Yaw，Roll。放在数组下标[17-19]的位置。<br/>
   * 视线方向：有10个值，放在数组下标[20-29]的位置，注意：目前该属性关闭。<br/>
   * @param yuv  视频帧数据。格式YUV420SP或者YUV420P。
   * @param width 图片宽，单位像素，最大支持1280。
   * @param height 图片高，单位像素，最大支持720。
   * @param facebox 需要处理的人脸框。长度为4的数组，分别表示：左上角X坐标，左上角Y坐标，人脸框宽，人脸框高。<br/>
   * @param attributes 长度为32的数组，用于存放人脸属性合集，内存提前申请好。
   * @param targets 需要计算的人脸属性字符串。注意：
   * @param degree 人脸在图片里的朝向。Android平台，横屏：0（前置），180（后置）；竖屏：270（前置），90（后置）。
   * @return 1：成功；0：内部错误；-1：JNI错误；-2：人脸框错误；-3：人脸属性初始化错误；-4：图片超过最大限制（1280x720）；-5：授权错误。<br/>
   */
  public native int getAttributes(byte[] yuv, int width, int height,int[]facebox,float[]attributes,String targets,int degree);

  protected native int localChecker(String str);
  protected native String getLocalMarker(String str);
  protected native int activate(String authkey,String authdata);
}
