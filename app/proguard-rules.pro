#your models
#if you store all model under one package then use:
# -keep class your.package.name.models.** { *; }
#if you store models under different models packages then use:
-keep class **.models.** { *; }

-keepclassmembers enum * { *; }

#butterknife
-keep class butterknife.** { *; }
-dontwarn butterknife.internal.**
-keep class **$$ViewBinder { *; }
-keepclasseswithmembernames class * {
    @butterknife.* <fields>;
}
-keepclasseswithmembernames class * {
    @butterknife.* <methods>;
}

#picasso
-dontwarn com.squareup.okhttp3.**

# OkHttp
-keepattributes Signature
-keepattributes *Annotation*
-keep class com.squareup.okhttp3.** { *; }
-keep interface com.squareup.okhttp3.** { *; }
-dontwarn com.squareup.okhttp3.**

#rxjava
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
-dontwarn sun.misc.Unsafe

#dart
-dontwarn com.f2prateek.dart.internal.**
-keep class **$$ExtraInjector { *; }
-keepclasseswithmembernames class * {
    @com.f2prateek.dart.* <fields>;
}

#start gson
-keepattributes Signature
# For using GSON @Expose annotation
-keepattributes *Annotation*

# Gson specific classes
-keep class sun.misc.Unsafe { *; }
#end of gson

#picasso
-dontwarn com.squareup.okhttp.**

#okio
-dontwarn okio.**

#retrofit2
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keepattributes Signature
-keepattributes Exceptions
-keepclasseswithmembers class * { @retrofit2.http.* <methods>; }

#retrolambda
-dontwarn java.lang.invoke.*

#greenrobot event bus
-keepattributes *Annotation*
-keepclassmembers class ** {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }

#retrolambda
-dontwarn java.lang.invoke**


-keepattributes InnerClasses
-keepattributes EnclosingMethod

#debug
-renamesourcefileattribute SourceFile
-keepattributes SourceFile,LineNumberTable

#flurry
-keep class com.flurry.** { *; }
-dontwarn com.flurry.**
-keepattributes *Annotation*,EnclosingMethod,Signature
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}



