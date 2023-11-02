package com.example.utilityapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * The Room database class that defines the database configuration.
 */
@Database(entities = [Note::class, Category::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    /**
     * Provides access to the Note Data Access Object (DAO).
     *
     * @return The Note DAO.
     */
    abstract fun noteDao(): NoteDao

    /**
     * Provides access to the Category Data Access Object (DAO).
     *
     * @return The Category DAO.
     */
    abstract fun categoryDao(): CategoryDao

    companion object {
        // volatile indicates that writes to this property are immediately visible to other threads
        @Volatile
        private var instance: AppDatabase? = null

        /**
         * Gets an instance of the database or creates one if it doesn't exist.
         *
         * @param context The application context.
         * @return The AppDatabase instance.
         */
        fun getDatabase(context: Context): AppDatabase {
            // Check if an instance already exists
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): AppDatabase {
            // Create and return the database instance
            return Room.databaseBuilder(
                context.applicationContext,
                // if updating schema, create new DB or must create a migration class to version it
                AppDatabase::class.java, "app_database5"
            ).build()
        }
    }
}