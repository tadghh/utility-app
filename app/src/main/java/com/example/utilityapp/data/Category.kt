package com.example.utilityapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Represents a category entity in the database.
 *
 * @property id The unique identifier of the category. (Auto-generated)
 * @property name The name of the category.
 */
@Entity(tableName = "categories")
data class Category(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String
)