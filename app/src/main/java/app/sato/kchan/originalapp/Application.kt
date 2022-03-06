package app.sato.kchan.originalapp

import android.app.Application
import androidx.room.Room

class Application : Application() {
    companion object {
        lateinit var database: DataBase
    }

    override fun onCreate() {
        super.onCreate()
        database = Room.databaseBuilder(this, objectOf<DataBase>(), "kotlin_room_sample.db").allowMainThreadQueries().build()
    }
}

internal inline fun <reified T : Any> objectOf() = T::class.java