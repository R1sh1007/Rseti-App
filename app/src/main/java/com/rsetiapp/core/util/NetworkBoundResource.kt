package com.rsetiapp.core.util

import com.google.gson.Gson
import com.rsetiapp.core.domain.model.response.BaseErrorResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import retrofit2.HttpException
import java.net.HttpURLConnection

fun <ResultType, RequestType> networkBoundResource(
    query: () -> Flow<ResultType>,
    fetch: suspend () -> RequestType,
    saveFetchResult: suspend (RequestType) -> Unit,
    shouldFetch: (ResultType) -> Boolean = { true }
) = flow {

    val data = query().first()
    val flow = if (shouldFetch(data)) {
        emit(Resource.Loading(data))
        try {
            saveFetchResult(fetch())
            query().map {
                Resource.Success(it)
            }
        } catch (t: Throwable) {
            t.printStackTrace()
            val error = if (t is HttpException)
                getErrorMessage(t)
            else
                BaseErrorResponse(0, "Something went wrong !!", false, Any())
            query().map { Resource.Error(error, it) }
        }
    } else {
        query().map { Resource.Success(it) }
    }

    emitAll(flow)

}

//fun <RequestType>  networkBoundResourceWithoutDb(
//    fetch: suspend () -> RequestType
//) = flow {
//
//    emit(Resource.Loading(null))
//    try {
//        emit(Resource.Success(fetch.invoke()))
//    } catch (t: Throwable) {
//        val error = if (t is HttpException)
//            getErrorMessage(t)
//        else{
//            if (t.message!!.contains("Unable to resolve host")){
//                 BaseErrorResponse(HttpURLConnection.HTTP_GATEWAY_TIMEOUT, "No Internet Connection", false,Any())
//            }else BaseErrorResponse(0, t.message.toString(), false, Any())
//        }
//
//        emit(Resource.Error(error, null))
//    }
//}

fun <T> networkBoundResourceWithoutDb(
    fetch: suspend () -> T
): Flow<Resource<T>> = flow {

    emit(Resource.Loading())

    try {
        val response = fetch()
        emit(Resource.Success(response))
    } catch (t: Throwable) {

        val error = if (t is HttpException)
            getErrorMessage(t)
        else {
            if (t.message?.contains("Unable to resolve host") == true) {
                BaseErrorResponse(
                    HttpURLConnection.HTTP_GATEWAY_TIMEOUT,
                    "No Internet Connection",
                    false,
                    Any()
                )
            } else {
                BaseErrorResponse(0, t.message.toString(), false, Any())
            }
        }

        emit(Resource.Error(error))
    }
}

 fun getErrorMessage(throwable: HttpException): BaseErrorResponse {

     if (throwable.message().contains("java.net.UnknownHostException")){
         return BaseErrorResponse(HttpURLConnection.HTTP_GATEWAY_TIMEOUT, "No Internet Connection", false,Any())
     }

     if (throwable.code() == HttpURLConnection.HTTP_GATEWAY_TIMEOUT)
         return BaseErrorResponse(HttpURLConnection.HTTP_GATEWAY_TIMEOUT, "No Internet Connection", false,Any())

     val errorBody = throwable.response()?.errorBody()

     val endpoint = throwable.response()?.raw()?.request?.url?.toUrl()?.path
     val host = throwable.response()?.raw()?.request?.url?.toUrl()?.host

     var baseErrorResponse: BaseErrorResponse

     baseErrorResponse = try {

         if (throwable.code() == 429){
             log("429", throwable.message())
             BaseErrorResponse(429,"Too many requests found from this IP, please try again after an hour" , false,Any())
         }

         else if (errorBody == null)
             BaseErrorResponse(0, "Server not reachable", false,Any())
         else{

             Gson().fromJson(errorBody.charStream(), BaseErrorResponse::class.java)
         }

     } catch (e: Exception) {
         e.printStackTrace()
         BaseErrorResponse(0, "Server not reachable", false,Any())
     }

    if (baseErrorResponse.code == HttpURLConnection.HTTP_INTERNAL_ERROR)
        baseErrorResponse = BaseErrorResponse(HttpURLConnection.HTTP_INTERNAL_ERROR, "Something went wrong!!", false,Any())

//     try {
//         // log Analytics event with api error and api endpoint
//         if (host?.contains("api.fitbit.com", true) == true)
//             AnalyticsHelper.logError("Fitbit API error", endpoint)
//         else
//             AnalyticsHelper.logError(baseErrorResponse.message, endpoint)
//     } catch (e: Exception) {
//         e.printStackTrace()
//     }

    return baseErrorResponse
}