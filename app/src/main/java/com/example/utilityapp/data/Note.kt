package com.example.utilityapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Represents a note entity in the database.
 *
 * @property id The unique identifier of the note. (Auto-generated)
 * @property title The title of the note.
 * @property content The content of the note.
 * @property categoryId The unique identifier of the category associated with the note (nullable).
 */
@Entity(tableName = "notes")
data class Note(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    var title: String,
    var content: String,
    var categoryId: Long?,
)