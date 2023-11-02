package com.example.utilityapp.data

import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object (DAO) for the Category entity.
 */
@Dao
interface CategoryDao {

    /**
     * Retrieves the name of a category by its unique identifier.
     *
     * @param categoryId The unique identifier of the category.
     * @return A Flow emitting the name of the category.
     */
    @Query("SELECT name FROM categories WHERE id = :categoryId")
    fun getCategoryById(categoryId: Long?): Flow<String?>

    /**
     * Retrieves all categories from the database.
     *
     * @return LiveData containing a list of all categories.
     */
    @Query("SELECT * FROM categories")
    fun getAllCategories(): LiveData<List<Category>>

    /**
     * Inserts a new category into the database.
     *
     * @param category The category to be inserted.
     */
    @Insert
    suspend fun insert(category: Category)

    /**
     * Updates an existing category in the database.
     *
     * @param category The category to be updated.
     */
    @Update
    suspend fun update(category: Category)

    /**
     * Deletes a category from the database by its unique identifier.
     *
     * @param categoryId The unique identifier of the category to be deleted.
     */
    @Query("DELETE FROM categories WHERE id = :categoryId")
    fun delete(categoryId: Long?)
}