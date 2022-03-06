package app.sato.kchan.originalapp

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Fave constructor(
    @PrimaryKey(autoGenerate = true) val id: Long,
    @ColumnInfo val name: String
)