package com.moviegetter.db

import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.*
import android.arch.persistence.room.migration.Migration
import android.content.Context
import com.aramis.library.extentions.logE
import com.aramis.library.extentions.now
import com.moviegetter.bean.User
import com.moviegetter.config.DBConfig
import com.moviegetter.crawl.dyg.DygItem
import com.moviegetter.crawl.dytt.DYTTItem
import com.moviegetter.crawl.hu.HuItem
import com.moviegetter.crawl.ipz.IPZItem
import com.moviegetter.crawl.pic.PicItem
import com.moviegetter.crawl.tv.TvItem
import org.jetbrains.anko.doAsync

/**
 * Created by lizhidan on 2019/1/28.
 *
 */
object MovieDatabaseManager {
    private var database: MovieDatabase? = null

    fun create(context: Context) {
//        database = Room.databaseBuilder(context.applicationContext, MovieDatabase::class.java, "movie_getter")
//                .fallbackToDestructiveMigration()
//                .build()
        database = MovieDatabase.create(context)
    }


    fun database(): MovieDatabase {
        return database!!
    }

    fun close() {
        database?.close()
    }

}

@Database(entities = [DYTTItem::class, IPZItem::class, TvItem::class, PicItem::class, DygItem::class, User::class,HuItem::class], version = 2, exportSchema = false)
abstract class MovieDatabase : RoomDatabase() {

    abstract fun getMovieDao(): MovieDao

    abstract fun getIpzDao(): IpzDao

    abstract fun getTvDao(): TvDao

    abstract fun getPicDao(): PicDao

    abstract fun getDygDao(): DygDao

    abstract fun getUserDao(): UserDao


    companion object {
        private var instance: MovieDatabase? = null

        fun create(context: Context): MovieDatabase {
            if (instance == null) {
                synchronized(this) {
                    instance = Room.databaseBuilder(context.applicationContext, MovieDatabase::class.java, "movie_getter")
                            .fallbackToDestructiveMigration()
                            .addCallback(databaseCallback)
//                            .addMigrations(VERSION_1_2)
                            .build()
                }
            }

            return instance!!
        }

        private val  VERSION_1_2=object:Migration(1,2){
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("")
            }
        }

        private val databaseCallback = object : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)

                doAsync {
                    instance?.getUserDao()?.apply {
                        logE("创建数据库，插入用户数据")
                        val users = listOf(
                                User(1, "乐视", "868897020889812", "", DBConfig.USER_ROLE_ROOT, now()),
                                User(2, "刘大头", "861759034263086", "", DBConfig.USER_ROLE_VIP, now()),
                                User(3, "Mix2", "865736039728368", "", DBConfig.USER_ROLE_ROOT, now())
                        )
                        this.inserts(users)
                    }
                }
            }
        }
    }
}

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(user: User)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun inserts(users: List<User>)

    @Delete
    fun delete(user: User): Int

    @Query("SELECT * FROM user")
    fun getAll(): List<User>

    @Query("SELECT count(*) FROM user WHERE id=:id")
    fun count(id: Int): Int

    @Query("SELECT * FROM user WHERE imei=:imei")
    fun select(imei: String): User?
}


@Dao
interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(dyttItem: DYTTItem)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun inserts(dyttItems: List<DYTTItem>)

    @Delete
    fun delete(dyttItem: DYTTItem): Int

    @Query("SELECT * FROM movie_table ORDER BY movie_update_timestamp DESC")
    fun getAll(): List<DYTTItem>


    @Query("SELECT count(*) FROM movie_table WHERE movieId=:id")
    fun count(id: Int): Int
}

@Dao
interface DygDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(dygItem: DygItem)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun inserts(dygItems: List<DygItem>)


    @Delete
    fun delete(dygItem: DygItem): Int

    @Query("SELECT * FROM dyg_table WHERE position=:position ORDER BY update_time")
    fun getAll(position: Int=0): List<DygItem>


    @Query("SELECT count(*) FROM dyg_table WHERE movieId=:id")
    fun count(id: Int): Int
}

@Dao
interface IpzDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(ipzItem: IPZItem)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun inserts(ipzItems: List<IPZItem>)


    @Delete
    fun delete(ipzItem: IPZItem): Int

    @Query("SELECT * FROM ipz_table WHERE tag=:tag")
    fun getAll(tag: String): List<IPZItem>

    @Query("SELECT count(*) FROM ipz_table WHERE movieId=:id AND tag=:tag")
    fun count(id: Int, tag: String): Int
}

@Dao
interface TvDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(tvItem: TvItem)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun inserts(tvItems: List<TvItem>)

    @Delete
    fun delete(tvItem: TvItem): Int

    @Query("SELECT * FROM tv ORDER BY id")
    fun getAll(): List<TvItem>

    @Query("SELECT count(*) FROM tv WHERE id=:id")
    fun count(id: Int): Int
}

@Dao
interface PicDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(picItem: PicItem)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun inserts(picItems: List<PicItem>)

    @Delete
    fun delete(picItem: PicItem): Int

    @Query("SELECT * FROM pic_table")
    fun getAll(): List<PicItem>

    @Query("SELECT * FROM pic_table WHERE picId = :picId")
    fun select(picId: String): PicItem

    @Query("SELECT count(*) FROM pic_table WHERE picId=:id")
    fun count(id: Int): Int
}



