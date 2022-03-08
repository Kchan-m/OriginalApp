package app.sato.kchan.originalapp

import androidx.room.*
import io.reactivex.Flowable

@Dao
interface FaveDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun createFave(fave: Fave)

    @Query("SELECT * FROM Fave")
    fun findAll(): List<Fave>

    @Update
    fun updateAddress(fave: Fave)

    @Delete
    fun delete(fave: Fave)

    @Query("DELETE FROM Fave")
    fun deleteAll()
}