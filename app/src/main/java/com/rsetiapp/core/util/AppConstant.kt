package com.rsetiapp.core.util
import com.rsetiapp.BuildConfig
import com.rsetiapp.security.SecurityUtils


object AppConstant {

    object StaticURL{

        // const val baseUrl= "http://10.197.183.106:8002/rsetiapp/"             //local
        //  const val baseUrl= "https://kaushal.rural.gov.in/demobackend/rsetiapp/"             //demo
        const val baseUrl= "https://kaushal.rural.gov.in/backend/rsetiapp/"   // live
       // const val FACE_AUTH_UIADI= "https://nregarep2.nic.in/Netnrega/StateServices/Uid_Face_Auth.svc/PostOnAUA_Face_auth"
        const val FACE_AUTH_UIADI= "https://nregarep2.nic.in/netnrega/stateServices/Uid_Face_Auth_DDUGKY.svc/PostOnAUA_Face_auth"

    }

    object Constants{

        var REFRESH_TOKEN_URL = "jhbheugcy2373y379y37gydygdy";
        var CLIENT_SECRET_KEY = "dgtbncbehkcbjebccnkec78yf37bc";

        const val CRYPT_ID = BuildConfig.CRYPT_ID
        const val CRYPT_IV = BuildConfig.CRYPT_IV
        const val CRYPLIBAES = BuildConfig.CRYPLIBAES
        const val CAPTURE_INTENT_RESPONSE_DATA = "response"
        const val CAPTURE_INTENT_REQUEST = "request"
        const val CAPTURE_INTENT = "in.gov.uidai.rdservice.face.CAPTURE"
        const val PRODUCTION: String = "P"
        const val ENGLISH: String = "english"
        const val PRE_PRODUCTION_CODE: String = "PP"
        const val CURRENT_ENVIRONMENT_CODE = PRE_PRODUCTION_CODE;
        const val WADH_KEY = "sgydIC09zzy6f8Lb3xaAqzKquKe9lFcNR9uTvYxFp+A="
        var LANGUAGE = "en"
        var ENVIRONMENT_TAG = "P"
       // var ENCRYPT_IV_KEY = BuildConfig.ENCRYPT_IV_KEY;
        var ENCRYPT_IV_KEY = SecurityUtils.getEncryptIvKey();

        var ENCRYPT_KEY = BuildConfig.ENCRYPT_KEY;


        const val EXTRA_CLIENT_ID = "client_id"
        const val EXTRA_FETCH_USER_EMBEDDING = "fetch_user_embeddings"
        const val EXTRA_CALL_TYPE = "call_type"
        const val EXTRA_USER_ID = "user_id"
        const val EXTRA_USER_IDS = "user_ids_json"
        const val EXTRA_USER_NAME = "user_name"
        const val CALL_TYPE_LOGIN = "LOGIN"
        const val CALL_TYPE_REGISTRATION = "REGISTRATION"
        const val RESULT_STATUS = "status"
        const val RESULT_MESSAGE = "message"
        const val YOUR_CLIENT_ID = "RD_0725"
    }
}
