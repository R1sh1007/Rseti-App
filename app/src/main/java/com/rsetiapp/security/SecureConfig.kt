package com.rsetiapp.security

import android.util.Log

/**
 * Created by Rishi Porwal
 */
object SecureConfig {
    private var isLibraryLoaded = false

    init {
        try {
            System.loadLibrary("secure-keys")
            isLibraryLoaded = true
            Log.d("SecureConfig", "✅ Native library loaded successfully")
        } catch (e: UnsatisfiedLinkError) {
            Log.e("SecureConfig", "❌ Failed to load native library: ${e.message}", e)
            isLibraryLoaded = false
        } catch (e: Exception) {
            Log.e("SecureConfig", "❌ Failed to load native library", e)
            isLibraryLoaded = false
        }
    }

    // external JNI methods
    private external fun getEncryptIvKeyNative(): String
    private external fun getEncryptKeyNative(): String
    private external fun getCryptLibAesNative(): String
    private external fun getCryptIdNative(): String
    private external fun getCryptIvNative(): String

    private external fun getKeyByNameNative(keyName: String): String

    private external fun nativeRefreshTokenUrl(): String
    private external fun nativeClientSecretKey(): String
    private external fun nativeWadhKey(): String

    // Fallback values in case native library fails to load
    private val fallbackEncryptIvKey = "$10A80$10A80$10A"
    private val fallbackEncryptKey = "$10A80$10A80$10A"
    private val fallbackCryptLibAes = "AES/CBC/PKCS5PADDING"
    private val fallbackCryptId = "8080808080808080"
    private val fallbackCryptIv = "8080808080808080"
    private val fallbackRefreshTokenUrl = "jhbheugcy2373y379y37gydygdy"
    private val fallbackClientSecretKey = "dgtbncbehkcbjebccnkec78yf37bc"
    private val fallbackWadhKey = "sgydIC09zzy6f8Lb3xaAqzKquKe9lFcNR9uTvYxFp+A="

    // Kotlin getters with fallback values
    val encryptIvKey: String by lazy { 
        try {
            if (isLibraryLoaded) getEncryptIvKeyNative() else fallbackEncryptIvKey
        } catch (e: Exception) {
            Log.e("SecureConfig", "Error getting encryptIvKey", e)
            fallbackEncryptIvKey
        }
    }

    val encryptKey: String by lazy { 
        try {
            if (isLibraryLoaded) getEncryptKeyNative() else fallbackEncryptKey
        } catch (e: Exception) {
            Log.e("SecureConfig", "Error getting encryptKey", e)
            fallbackEncryptKey
        }
    }

    val cryptLibAes: String by lazy { 
        try {
            if (isLibraryLoaded) getCryptLibAesNative() else fallbackCryptLibAes
        } catch (e: Exception) {
            Log.e("SecureConfig", "Error getting cryptLibAes", e)
            fallbackCryptLibAes
        }
    }

    val cryptId: String by lazy { 
        try {
            if (isLibraryLoaded) getCryptIdNative() else fallbackCryptId
        } catch (e: Exception) {
            Log.e("SecureConfig", "Error getting cryptId", e)
            fallbackCryptId
        }
    }

    val cryptIv: String by lazy { 
        try {
            if (isLibraryLoaded) getCryptIvNative() else fallbackCryptIv
        } catch (e: Exception) {
            Log.e("SecureConfig", "Error getting cryptIv", e)
            fallbackCryptIv
        }
    }

    fun getKey(keyName: String): String = try {
        if (isLibraryLoaded) getKeyByNameNative(keyName) else ""
    } catch (e: Exception) {
        Log.e("SecureConfig", "Error getting key: $keyName", e)
        ""
    }

    val REFRESH_TOKEN_URL: String by lazy { 
        try {
            if (isLibraryLoaded) nativeRefreshTokenUrl() else fallbackRefreshTokenUrl
        } catch (e: Exception) {
            Log.e("SecureConfig", "Error getting REFRESH_TOKEN_URL", e)
            fallbackRefreshTokenUrl
        }
    }

    val CLIENT_SECRET_KEY: String by lazy { 
        try {
            if (isLibraryLoaded) nativeClientSecretKey() else fallbackClientSecretKey
        } catch (e: Exception) {
            Log.e("SecureConfig", "Error getting CLIENT_SECRET_KEY", e)
            fallbackClientSecretKey
        }
    }

    val WADH_KEY: String by lazy { 
        try {
            if (isLibraryLoaded) nativeWadhKey() else fallbackWadhKey
        } catch (e: Exception) {
            Log.e("SecureConfig", "Error getting WADH_KEY", e)
            fallbackWadhKey
        }
    }

}
