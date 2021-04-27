/*
 * Native hash functions for Java
 *
 * Copyright (c) Project Nayuki. (MIT License)
 * https://www.nayuki.io/page/native-hash-functions-for-java
 */

#include <cstdint>
#include <jni.h>


#define STATE_LEN 8

extern void sha512_compress_block(const jbyte *block, uint64_t state[STATE_LEN]);


/*
 * Class:     nayuki_nativehash_Sha512
 * Method:    compress
 * Signature: ([J[BII)Z
 */
extern "C"
JNIEXPORT jboolean JNICALL
Java_app_melon_util_encrypt_SHA512_compress(
        JNIEnv *env, jclass clazz,
        jlongArray stateArray, jbyteArray msg, jint off, jint len) {
    jboolean status = JNI_FALSE;
    if (len < 0 || (len & 127) != 0)  // Block size is 128 bytes
        return status;
    (void) clazz;

    // Get state array and convert to uint64_t
    jlong *stateJava = env->GetLongArrayElements(stateArray, nullptr);
    if (stateJava == nullptr)
        return status;
    uint64_t state[STATE_LEN];
    for (int i = 0; i < STATE_LEN; i++)
        state[i] = (uint64_t) stateJava[i];

    // Iterate over each block in msg
    auto *block = (jbyte *) env->GetPrimitiveArrayCritical(msg, nullptr);
    if (block == nullptr)
        goto cleanup;
    for (jint end = off + len; off < end; off += 128)
        sha512_compress_block(&block[off], state);
    env->ReleasePrimitiveArrayCritical(msg, block, JNI_ABORT);

    // Convert state array to jlong and clean up
    for (int i = 0; i < STATE_LEN; i++)
        stateJava[i] = (jlong) state[i];
    status = JNI_TRUE;
cleanup:
    env->ReleaseLongArrayElements(stateArray, stateJava, 0);
    return status;
}