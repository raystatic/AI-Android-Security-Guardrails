# OkHttp ProGuard Rules
-keepattributes Signature
-keepattributes AnnotationDefault
-keep class okhttp3.** { *; }
-keep interface okhttp3.** { *; }
-dontwarn okhttp3.**
-dontwarn okio.**

# Room ProGuard Rules
-keep class * extends androidx.room.RoomDatabase
-dontwarn androidx.room.paging.**

# Retrofit ProGuard Rules
-keepattributes Signature, InnerClasses, EnclosingMethod
-keepattributes RuntimeVisibleAnnotations, RuntimeVisibleParameterAnnotations
-keepattributes RuntimeInvisibleAnnotations, RuntimeInvisibleParameterAnnotations
-keep class retrofit2.** { *; }
-keep interface retrofit2.** { *; }
-dontwarn retrofit2.**

# Gson ProGuard Rules
-keepattributes Signature, InnerClasses, EnclosingMethod
-keep class com.google.gson.** { *; }
-keep class com.example.rulesreviewer.** { *; }

# Firebase Crashlytics ProGuard Rules
-keepattributes SourceFile, LineNumberTable
-keep public class * extends com.google.firebase.crashlytics.CrashlyticsRegistrar
-keep public class * extends com.google.firebase.components.ComponentRegistrar
-keep public class com.google.firebase.crashlytics.** { *; }

# Custom ProGuard rules for reflection-based code (Rule R9)
# Replace 'com.example.rulesreviewer.models' with the actual package 
# that contains your reflection-based classes.
-keep class com.example.rulesreviewer.models.** { *; }
-keepclassmembers class com.example.rulesreviewer.models.** { *; }

# If using reflection on specific methods or fields, you can be more granular:
# -keepclassmembers class com.example.rulesreviewer.MyClass {
#     public void myReflectionMethod(java.lang.String);
#     private java.lang.String myReflectionField;
# }
