package app.sato.kchan.originalapp

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = arrayOf(Fave::class), version = 1)
abstract class DataBase : RoomDatabase() {
    abstract fun faveDao(): FaveDao
}