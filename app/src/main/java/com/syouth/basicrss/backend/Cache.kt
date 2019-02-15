package com.syouth.basicrss.backend

import android.arch.persistence.room.InvalidationTracker
import android.arch.persistence.room.Room
import android.content.Context
import android.database.Cursor
import android.os.Handler
import android.os.Looper
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.schedulers.Schedulers

const val URL = "https://jsonplaceholder.typicode.com/posts"

class Cache(appContext: Context) {
    private val moshi = Moshi.Builder().build()
    private val fetcher = FeedFetcher()
    private val db = Room.databaseBuilder(
        appContext,
        CacheDatabase::class.java,
        "awesome_db"
    ).allowMainThreadQueries().build()

    fun subscribe(url: String): Flowable<Cursor> =
        Flowable.create<Cursor>({
            db.invalidationTracker.addObserver(object : InvalidationTracker.Observer("cacheentity") {

                var cursor: Cursor? = null

                override fun onInvalidated(tables: MutableSet<String>) {
                    // Only one table so no need to check tables var
                    Handler(Looper.getMainLooper()).post {
                        cursor?.close()
                        cursor = db.cacheDao().getCache(url)
                        it.onNext(cursor!!)
                    }
                }

            })

            fetcher.request(URL).observeOn(Schedulers.io()).subscribe {
                val type = Types.newParameterizedType(List::class.java, ResponseEntity::class.java)
                val adapter = moshi.adapter<List<ResponseEntity>>(type)
                val rEAdapter = moshi.adapter(ResponseEntity::class.java)
                val response = adapter.fromJson(it)
                val data = mutableListOf<CacheEntity>()
                response?.forEach {
                    data.add(CacheEntity(URL, rEAdapter.toJson(it)))
                }

                db.cacheDao().insertData(data)
            }
        }, BackpressureStrategy.LATEST).subscribeOn(Schedulers.io())

}
