package com.nomaddeveloper.examini.network

import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody

class BaseInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val contentTypeRequest = request.newBuilder()
            .addHeader("Content-Type", "application/json")
            .build()
        val response: Response = chain.proceed(contentTypeRequest)
        val responseBody = response.body?.string()
        return response.newBuilder()
            .body(responseBody.orEmpty().toResponseBody(response.body?.contentType()))
            .build()
    }
}