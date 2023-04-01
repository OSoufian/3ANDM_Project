package com.example.a3andmproject.database

import androidx.room.*
import com.example.a3andmproject.models.Recipe

@Dao
interface RecipeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRecipes(recipes: List<Recipe>)

    @Query("SELECT * FROM Recipe")
    fun getRecipes(): List<Recipe>

    @Query("DELETE FROM Recipe")
    fun deleteRecipes()
}