package com.example.utilityapp.data

import androidx.lifecycle.LiveData
import androidx.room.*

/**
 * Data Access Object (DAO) for the Note entity.
 */
@Dao
interface NoteDao {
    /**
     * Retrieves all notes from the database.
     *
     * @return LiveData containing a list of all notes.
     */
    @Query("SELECT * FROM notes")
    fun getAllNotes(): LiveData<List<Note>>

    /**
     * Inserts a new note into the database.
     *
     * @param note The note to be inserted.
     */
    @Insert
    suspend fun insert(note: Note)

    /**
     * Updates an existing note in the database.
     *
     * @param note The note to be updated (nullable).
     */
    @Update
    suspend fun update(note: Note?)

    /**
     * Deletes a note from the database.
     *
     * @param note The note to be deleted.
     */
    @Delete
    suspend fun delete(note: Note)
}