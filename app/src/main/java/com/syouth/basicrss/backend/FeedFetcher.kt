package com.syouth.basicrss.backend

import com.squareup.moshi.Json
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.Request

class FeedFetcher {

    private val client = OkHttpClient()

    fun request(url: String): Observable<String?> =
        Observable.defer {
            val request = Request.Builder().url(url).build()
            val response = client.newCall(request).execute()
            Observable.just(response.body()?.string())
        }.subscribeOn(Schedulers.io())
}

data class ResponseEntity (
    @Json(name = "userId") val userId: Int,
    @Json(name = "id") val id: Int,
    @Json(name = "title") val title: String,
    @Json(name = "body") val body: String
)
