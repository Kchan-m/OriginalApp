package app.sato.kchan.originalapp

import android.widget.ImageView
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Expenses constructor(
    @PrimaryKey(autoGenerate = true) val id: Long,
    @ColumnInfo val year: Int,
    @ColumnInfo val month: Int,
    @ColumnInfo val day: Int,
    @ColumnInfo val faveID: Long,
    //@ColumnInfo val image: ImageView?,
    @ColumnInfo val order: String,
    @ColumnInfo val price: Int,
    @ColumnInfo val memo: String?
)