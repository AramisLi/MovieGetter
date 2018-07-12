# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontpreverify
-ignorewarnings
-dontoptimize
-verbose
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*
-dontwarn
-dontskipnonpubliclibraryclassmembers

-keep class com.aramis.library.http.custom.**{*;}
-keep class com.aramis.library.widget.**{*;}
-keep class com.moviegetter.bean.**{*;}
-keep class com.moviegetter.widget.**{*;}

# for DexGuard only
#-keepresourcexmlelements manifest/application/meta-data@value=GlideModule
#====================Glide end

-keep class * extends java.lang.annotation.Annotation { *; }
-keep interface * extends java.lang.annotation.Annotation { *; }
-keep class * implements java.io.Serializable { *; }
-keep interface * extends java.io.Serializable { *; }
#====================rxvolley start
-dontwarn com.kymjs.rxvolley.**
-keep class com.kymjs.rxvolley.** {*;}
#====================rxvolley end

# RxJava RxAndroid
-dontwarn sun.misc.**
-keep class rx.**
-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
    long producerIndex;
    long consumerIndex;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode producerNode;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode consumerNode;
}

-keep class com.moviegetter.utils.BottomNavigationViewHelper.**{ *; }
-keep class android.support.design.internal.**{ *; }
-keep class android.support.design.widget.**{ *; }