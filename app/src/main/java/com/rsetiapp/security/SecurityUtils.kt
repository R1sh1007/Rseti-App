package com.rsetiapp.security

/**
 * Created by Rishi Porwal
 */

object SecurityUtils {

    // Key names constants
    object Keys {
        const val ENCRYPT_IV_KEY = "encrypt_iv_key"
        const val ENCRYPT_KEY = "encrypt_key"
        const val CRYPT_LIB_AES = "crypt_lib_aes"
        const val CRYPT_ID = "crypt_id"
        const val CRYPT_IV = "crypt_iv"
    }

    fun getEncryptIvKey(): String = SecureConfig.encryptIvKey
    fun getEncryptKey(): String = SecureConfig.encryptKey
    fun getCryptLibAes(): String = SecureConfig.cryptLibAes
    fun getCryptId(): String = SecureConfig.cryptId
    fun getCryptIv(): String = SecureConfig.cryptIv

    fun getSecureValue(keyName: String): String = SecureConfig.getKey(keyName)
}