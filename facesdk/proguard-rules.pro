# To enable ProGuard in your project, edit project.properties
# to define the proguard.config property as described in that file.
#
# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in ${sdk.dir}/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the ProGuard
# include property in project.properties.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}
-keepattributes Signature
-keepattributes *Annotation*
-keepattributes InnerClasses
-keepattributes EnclosingMethod

-keep class **.R
-keep class **.R$* {
    <fields>;
}

# For native methods, see http://proguard.sourceforge.net/manual/examples.html#native
-keepclasseswithmembernames class * {
    native <methods>;
}

-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

# 混淆关键代码 指定哪些方法不混淆
-keep class com.colorreco.**{ public *; protected *; }

-dontwarn java.**
-dontwarn org.**
-dontwarn android.**
# 解决can't find referenced method 'android.os.AsyncTask execute(java.lang.Object[])' in program class com.colorreco.libface.Activator
-dontwarn com.colorreco.libface.Activator

# 解决此类问题：Error: Can't find common super class of...
-dontoptimize
-dontpreverify

# 声明依赖的第三方库，混淆时不添加进Jar包
-libraryjars ./libs/TuSDKCore-2.9.0.jar
