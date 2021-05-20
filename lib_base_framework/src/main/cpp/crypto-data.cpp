#include <jni.h>
#include <string>
#include <jni.h>

extern "C"
JNIEXPORT jstring JNICALL
Java_app_melon_base_di_NetworkModule_baseUrlFromJNI(JNIEnv *env, jclass clazz) {
    std::string baseURL = "http://10.19.131.75:3000/";
    return env->NewStringUTF(baseURL.c_str());
}