package com.example.navigation_drawer.Classes

import android.widget.Toast
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class MyIntercepter(val token:String):Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request:Request=chain.request()
            .newBuilder()
            .addHeader("Authorization", "Bearer "+ token)
            .build()
        //Toast.makeText("$token",Toast.LENGTH_SHORT).show()
        return chain.proceed(request)
    }
}