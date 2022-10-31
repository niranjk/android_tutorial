package com.niranjan.androidtutorials.quotes.db

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*

/***
 * Quotes fetched from the network
 */
@Entity
data class Quote(
    @PrimaryKey
    val id: Int = 0,
    val quote: String
)

/**
 * Quotes DAO
 */
@Dao
interface QuotesDao{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertQuoteCallbacks(quote: Quote)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuote(quote: Quote)
    @get:Query("select * from Quote where id = 0")
    val quoteLiveData: LiveData<Quote?>
}

/***
 * Database
 */
@Database(entities = [Quote::class], version = 1, exportSchema = false)
abstract class QuotesDatabase : RoomDatabase(){
    abstract val quotesDao: QuotesDao
}

private lateinit var QUOTES_INSTANCE: QuotesDatabase

fun getQuotesDatabase(context: Context): QuotesDatabase {
    synchronized(QuotesDatabase::class){
        if (!::QUOTES_INSTANCE.isInitialized){
            QUOTES_INSTANCE = Room.databaseBuilder(
                context.applicationContext,
                QuotesDatabase::class.java,
                "quotes_db"
            ).fallbackToDestructiveMigration()
                .build()
        }
    }
    return QUOTES_INSTANCE
}