package app.sato.kchan.originalapp

import androidx.room.*
import io.reactivex.Flowable

@Dao
interface ExpensesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun createExpenses(expenses: Expenses)

    @Query("SELECT * FROM Expenses")
    fun findAll(): List<Expenses>

    @Update
    fun updateAddress(expenses: Expenses)

    @Delete
    fun delete(expenses: Expenses)

    @Query("DELETE FROM Expenses")
    fun deleteAll()
}