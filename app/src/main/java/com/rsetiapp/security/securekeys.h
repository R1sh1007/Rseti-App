#include <jni.h>
#include <string>

extern "C" {

JNIEXPORT jstring JNICALL
Java_com_rsetiapp_security_SecureConfig_getEncryptIvKey(JNIEnv* env, jobject /* this */) {
    return env->NewStringUTF("$10A80$10A80$10A");
}

JNIEXPORT jstring JNICALL
Java_com_rsetiapp_security_SecureConfig_getEncryptKey(JNIEnv* env, jobject /* this */) {
    return env->NewStringUTF("$10A80$10A80$10A");
}

JNIEXPORT jstring JNICALL
Java_com_rsetiapp_security_SecureConfig_getCryptLibAes(JNIEnv* env, jobject /* this */) {
    return env->NewStringUTF("AES/CBC/PKCS5PADDING");
}

JNIEXPORT jstring JNICALL
Java_com_rsetiapp_security_SecureConfig_getCryptId(JNIEnv* env, jobject /* this */) {
    return env->NewStringUTF("8080808080808080");
}

JNIEXPORT jstring JNICALL
Java_com_rsetiapp_security_SecureConfig_getCryptIv(JNIEnv* env, jobject /* this */) {
    return env->NewStringUTF("8080808080808080");
}

JNIEXPORT jstring JNICALL
Java_com_rsetiapp_security_SecureConfig_getKeyByName(JNIEnv* env, jobject /* this */, jstring keyName) {
    const char *nativeKeyName = env->GetStringUTFChars(keyName, nullptr);

    std::string result;
    std::string keyStr(nativeKeyName);

    if (keyStr == "encrypt_iv_key") {
        result = "$10A80$10A80$10A";
    } else if (keyStr == "encrypt_key") {
        result = "$10A80$10A80$10A";
    } else if (keyStr == "crypt_lib_aes") {
        result = "AES/CBC/PKCS5PADDING";
    } else if (keyStr == "crypt_id") {
        result = "8080808080808080";
    } else if (keyStr == "crypt_iv") {
        result = "8080808080808080";
    } else {
        result = "";
    }

    env->ReleaseStringUTFChars(keyName, nativeKeyName);
    return env->NewStringUTF(result.c_str());
}

}