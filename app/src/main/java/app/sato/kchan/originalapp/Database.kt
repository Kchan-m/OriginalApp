package app.sato.kchan.originalapp

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = arrayOf(Fave::class, Expenses::class), version = 3, exportSchema = false)
abstract class DataBase : RoomDatabase() {
    abstract fun faveDao(): FaveDao
    abstract fun expensesDao(): ExpensesDao
}