package com.syouth.basicrss.backend

import android.arch.persistence.room.*
import android.database.Cursor

@Entity(primaryKeys = ["url", "data"])
data class CacheEntity(
    @ColumnInfo(name = "url") var url: String,
    @ColumnInfo(name = "data") var data: String)

@Dao
interface CacheDao {
    @Query("SELECT data FROM cacheentity WHERE url = :url")
    fun getCache(url: String): Cursor

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertData(data: List<CacheEntity>)
}

@Database(entities = [CacheEntity::class], version = 1)
abstract class CacheDatabase : RoomDatabase() {
    abstract fun cacheDao(): CacheDao
}


