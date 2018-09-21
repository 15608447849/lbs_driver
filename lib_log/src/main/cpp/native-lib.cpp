#include <jni.h>
#include <android/log.h>
#define TAG "NATIVE"
#define LOGI(...)  __android_log_print(ANDROID_LOG_INFO,TAG,__VA_ARGS__)
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG, TAG, __VA_ARGS__)
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR,TAG,__VA_ARGS__)

#include <sys/stat.h>

extern "C"
JNIEXPORT jlong JNICALL
Java_com_leezp_lib_1log_LogFileHandler_getFileCreateTime(JNIEnv *env, jobject instance, jstring filePath_) {
    const char *filePath = env->GetStringUTFChars(filePath_, 0);
    struct stat buf;
    int result =  stat(filePath,&buf);
    if(result == 0) {
        buf.st_atime;
    }
    env->ReleaseStringUTFChars(filePath_, filePath);
    return result!=0?0:buf.st_atime;
}
