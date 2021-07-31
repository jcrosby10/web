package com.huntergaming.web.http

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import okhttp3.Call
import okhttp3.Callback
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Provide HTTP/S request services.
 */
@ExperimentalCoroutinesApi
@Singleton
class HttpService @Inject constructor(
    private val httpClient: OkHttpClient
) {

    /**
     * Make an HTTP/S GET request.
     *
     * @param url the URL to make the request to.
     * @return A Flow of WebRequestStatus indication success/failure.
     */
    fun get(url: String): Flow<WebRequestStatus> = callbackFlow {
        send(WebRequestStatus.InProgress)

        val request = Request.Builder()
            .url(url)
            .build()

        httpClient.newCall(request).enqueue(object : Callback {

            override fun onFailure(call: Call, e: IOException) {
                Timber.e(e)
                trySend(WebRequestStatus.Error("Error: ${e.message}"))
            }

            override fun onResponse(call: Call, response: Response) {
                if (!response.isSuccessful) trySend(WebRequestStatus.Error("Error: ${response.message} - result code : ${response.code}"))

                else {
                    runCatching { trySend(WebRequestStatus.Success(response.body?.string())) }
                }
            }
        })

        awaitClose { cancel() }
    }

    /**
     * Make an HTTP/S POST request.
     *
     * @param url the URL to make the request to.
     * @param parameters The parameters to sent with the request.
     * @return A Flow of WebRequestStatus indication success/failure.
     */
    fun post(url: String, parameters: Map<String, String>): Flow<WebRequestStatus> = callbackFlow {
        send(WebRequestStatus.InProgress)

        val formBody = FormBody.Builder().apply {
            parameters.forEach { add(it.key, it.value) }
        }.build()

        val request = Request.Builder().apply {
            url(url)
            post(formBody)
        }.build()

        httpClient.newCall(request).enqueue(object : Callback {

            override fun onFailure(call: Call, e: IOException) {
                Timber.e(e)
                trySend(WebRequestStatus.Error("Error: ${e.message}"))
            }

            override fun onResponse(call: Call, response: Response) {
                if (!response.isSuccessful) trySend(WebRequestStatus.Error("Error: ${response.message} - result code : ${response.code}"))

                else {
                    runCatching { trySend(WebRequestStatus.Success(response.body?.string())) }
                }
            }
        })

        awaitClose { cancel() }
    }
}

/**
 * The different statuses a web request can have.
 */
sealed class WebRequestStatus {
    object NoInternet: WebRequestStatus()
    object InProgress: WebRequestStatus()
    class Success(val response: String?): WebRequestStatus()
    class Error(val message: String?): WebRequestStatus()
}