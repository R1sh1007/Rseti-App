package com.rsetiapp.security

/**
 * Created by Rishi Porwal
 */
object SecureConfig {

    // Load native library
    init {
        System.loadLibrary("secure-keys")
    }

    private external fun getEncryptIvKey(): String
    private external fun getEncryptKey(): String
    private external fun getCryptLibAes(): String
    private external fun getCryptId(): String
    private external fun getCryptIv(): String

    private external fun getKeyByName(keyName: String): String

    val encryptIvKey: String by lazy { getEncryptIvKey() }
    val encryptKey: String by lazy { getEncryptKey() }
    val cryptLibAes: String by lazy { getCryptLibAes() }
    val cryptId: String by lazy { getCryptId() }
    val cryptIv: String by lazy { getCryptIv() }

    fun getKey(keyName: String): String = getKeyByName(keyName)
}
